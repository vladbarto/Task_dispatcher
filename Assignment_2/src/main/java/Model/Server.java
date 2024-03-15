package Model;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

//aceasta clasa va fi casa de marcat
public class Server implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private AtomicInteger neededRunningTime;
    private AtomicInteger currentClientProcessingTime;
    private int maxSize;
    private AtomicBoolean busy;
    public Server(int maxSize) {
        this.tasks = new ArrayBlockingQueue<>(maxSize);
        this.waitingPeriod = new AtomicInteger(0);
        this.neededRunningTime = new AtomicInteger(0);
        this.currentClientProcessingTime = new AtomicInteger(0);
        this.maxSize = maxSize;
        this.busy = new AtomicBoolean(false);
    }

    public AtomicBoolean getBusy() {
        return busy;
    }

    public int getSize() {
        return this.tasks.size();
    }
    public void setBusy(AtomicBoolean busy) {
        this.busy = busy;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public AtomicInteger getNeededRunningTime() { return neededRunningTime; }

    public void addTask(Task newTask){
        try {
            tasks.put(newTask);
            waitingPeriod.addAndGet(newTask.getServiceTime());
            neededRunningTime.addAndGet(newTask.getServiceTime());
        }
        catch (InterruptedException e){
            System.out.println("Eroare: client vid");
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                if(null != tasks.peek()) {
                    //va trebui sa memoram timpul initial de procesare al clientului
                    if(0 == currentClientProcessingTime.get())
                        currentClientProcessingTime.set(tasks.peek().getServiceTime());
                    Thread.sleep(1000); //orice task va fi procesat o secunda
                    tasks.peek().decreaseServiceTime();
                    //scadem acea secunda in care sta
                    // *1000 e ca sa transform acest numar in milisecs
                    //daca am ajuns cu timpul de servire la 0 => clientul a fost procesat
                    if(-1 == tasks.peek().getServiceTime()) {//daca clientul pleaca de la coada, scadem timpul de asteptare
                        waitingPeriod.addAndGet(-currentClientProcessingTime.get());
                        tasks.poll();
                        //resetam pe 0 currentClientProcessingTime
                        currentClientProcessingTime.set(0);
                    }
                }
            } catch (InterruptedException e) { }
        }
    }


    //public Task[] getTasks() {
    public BlockingQueue<Task> getTasks(){
        return tasks;
    }

    @Override
    public String toString() {
        String clienti = "";
        for(Task i : tasks)
            clienti += i.toString()+"\n";
//        clienti += "timp de asteptare="+getWaitingPeriod().get();
        return clienti;
    }
}
