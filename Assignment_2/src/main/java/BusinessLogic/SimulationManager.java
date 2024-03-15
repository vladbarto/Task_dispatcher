package BusinessLogic;

import GUI.AnimationFrame;
import GUI.SimulationFrame;
import Model.Server;
import Model.Task;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SimulationManager implements Runnable {
    private Scheduler scheduler;
    private SimulationFrame frame;//simulationframe va fi gui-ul nostru
    private AnimationFrame anim;
    private ArrayList<Task> tasks;
    //data read from UI:
    private SelectionPolicy selectionPolicy;
    private int timeLimit; //maximum processing time - read from UI
    private int maxProcessingTime; //maxim atata ia ca un client sa fie aranjat
    private int minProcessingTime; //minim atata ia ca un client sa fie aranjat
    private int minArrivalTime;
    private int maxArrivalTime;
    private int numberOfServers;
    private int numberOfClients;
    private double awt; //average waiting time
    private double ast; //average service/processing time
    private int peakHour;

    private boolean stopped;

    public SimulationManager() {
        this.tasks = new ArrayList<>();
        this.awt = 0.0;
        this.peakHour = 0;
        this.stopped = false;
    }

    public boolean isStopped() {
        return stopped;
    }

    public SimulationFrame getFrame() {
        return frame;
    }

    public void generateNRandomTasks(){
        int randID;
        int randArrival;
        int randProcessing;
        Random rand = new Random();

        for(int i = 0; i < numberOfClients; i++) {
            randID = i+1;
            randArrival = rand.nextInt(minArrivalTime, maxArrivalTime);
            randProcessing = rand.nextInt(minProcessingTime, maxProcessingTime);

            Task t = new Task(randID, randArrival, randProcessing);
            tasks.add(t);
            ast += randProcessing;
        }
    }
    private void createLogOfEvents(int currentTime, BufferedWriter out) {
        try {
            out.write("Time: " + currentTime + "\n");
            out.write("Waiting clients: ");
            for (Task t : tasks)
                out.write(t.toString()+"\n");
            int idcoada = 1;
            out.write("___\n");
            for (Server q: scheduler.getServers()) {
                out.write("Q"+idcoada+": ");
                if(q.getSize() == 0)
                    out.write("closed\n");
                else{
                    out.write(q.toString());
                }
                out.write("^^^\n");
                idcoada++;
            }
            out.write("-----------------------------------------------------------------------------------------------------------------\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialiseSetupNumbers(){
        do {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }while(frame.getLaunched()==false);

        System.out.println("Main thread a fost lansat");
        this.scheduler = new Scheduler(frame.getNrServers(), frame.getNrClientsPerQ());
        this.selectionPolicy = frame.getStrateg();
        this.timeLimit = frame.getSimTime();
        this.maxProcessingTime = frame.getMaxProc();
        this.minProcessingTime = frame.getMinProc();
        this.minArrivalTime = frame.getMinArr();
        this.maxArrivalTime = frame.getMaxArr();
        this.numberOfServers = frame.getNrServers();
        this.numberOfClients = frame.getNrClients();
    }
    @Override
    public void run() {
        synchronized (this) {
            this.frame = new SimulationFrame();
            initialiseSetupNumbers();
            this.anim = new AnimationFrame(frame.getNrServers(), frame.getSimTime());
            //anim.makeVisible();
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter("Log.txt"));
            out.write("Log of events\n");
            out.write("-------------------------------------------------------------------\n");
            out.write("Number of Clients: " + numberOfClients + "\n");
            out.write("Number of Queues: " + numberOfServers + "\n");
            out.write("Time of simulation: " + timeLimit + " seconds\n");
            out.write("[tArrivalMin, tArrivalMax] = [" + minArrivalTime + ", " + maxArrivalTime + "]\n");
            out.write("[tServiceMin, tServiceMax] = [" + minProcessingTime + ", " + maxProcessingTime + "]\n");
            out.write("Selected strategy: Shortest");
            if(selectionPolicy == SelectionPolicy.SHORTEST_QUEUE) out.write(" Queue\n");
            else out.write(" Waiting Time\n");
            out.write("-------------------------------------------------------------------\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        int currentTime = 0;
        generateNRandomTasks();
        tasks.sort(new TaskComparator());
        Task newTask;
        scheduler.changeStrategy(selectionPolicy);

        // we create an array of threads, having the size of initially given number of queues
        ArrayList<Thread> t = new ArrayList<>(); // t = lista de servere
        for (int i = 0; i < numberOfServers; i++) {
            t.add(new Thread(scheduler.getServers().get(i)));
            t.get(i).start();
        }

        int nrTotalClienti = 0, auxNTC = 0;
        while(currentTime < timeLimit){
            anim.updateNord(tasks);
            anim.updateSud(currentTime);
            anim.updateCentru(scheduler.getServers());
            synchronized (this) {
                try {
                    if(Thread.currentThread().isAlive())
                        Thread.sleep(1000);
                } catch (InterruptedException e) { }

                System.out.println("Current Time: "+currentTime);
            }

            //here, with this boolean and this while loop, we dispatch tasks to servers
            boolean condition = true;
            //condition meaning: atâta vreme cât există taskuri la grămadă care au arrivalTime == currentTime, ..
            //..condition rămâne true și tot scoatem acele taskuri
            //Deoarece tasks este sortat crescător după arrival time, este suficient să scoatem elementele din vârf
            while(true == condition && tasks.size() > 0) {
                if(currentTime == tasks.get(0).getArrivalTime()) {
                    newTask = tasks.remove(0);
                    scheduler.dispatchTask(newTask);
                }
                else condition = false;
            }

            // b) We compute Peak Hour, but we have to do this in real time
            auxNTC = 0;
            for(int i = 0; i < numberOfServers; i++) {
                auxNTC += scheduler.getServers().get(i).getSize();
            }
            if(nrTotalClienti < auxNTC)
            {
                nrTotalClienti = auxNTC;
                peakHour = currentTime;
            }

            createLogOfEvents(currentTime, out);
            currentTime++;
            if(currentTime == timeLimit) {
                anim.showGenerateResults();
                stopped = true;
            }
        }
        //la final we compute Simulation Results:
        // a) AWT
        for(int i = 0; i < numberOfServers; i++){
            awt += scheduler.getServers().get(i).getNeededRunningTime().get();
        }
        awt /= numberOfServers;
        // b) Peak Hour
        // see in while(currentTime < timeLimit)...

        // c) Average Service Time
        // computed as average of every single client's given processing time, regarding of the Server it's been dispatched
        // see in generateNRandomTasks() method
        // and also here
        ast /= numberOfClients;

        //la finalul buclei While, currentTime va fi ajuns la final, iar magazinul se va inchide
        //este treaba noastra sa notificam fiecare server sa isi inceteze activitatea
        //stopAllServerThreads(t);

        try {
            DecimalFormat df = new DecimalFormat("0.00");
            out.write("Final stats:\n");
            out.write("~ ~ ~ ~ ~ ~\n");
            out.write("Average waiting time was " + df.format(awt) + "s\n");
            out.write("Average processing time was " + df.format(ast) + "s\n");
            out.write("The Peak \"Second\" was " + peakHour + "\n");
            out.write("The shop is closed now. Go home!\n");
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void stopAllServerThreads(ArrayList<Thread> threads) {
        Iterator<Thread> it = threads.iterator();
        while(it.hasNext()){
            Thread curr = it.next();
            curr.interrupt();
        }
    }
}
