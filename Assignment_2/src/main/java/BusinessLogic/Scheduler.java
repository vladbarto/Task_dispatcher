package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;


public class Scheduler {
    private ArrayList<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        servers = new ArrayList<>();
        while(maxNoServers > 0){
            servers.add(new Server(maxTasksPerServer));
            maxNoServers--;
        }
    }
    public ArrayList<Server> getServers() {
        return servers;
    }

    public void changeStrategy(SelectionPolicy policy)
    {
        if(policy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ConcreteStrategyQueue();
        if(policy == SelectionPolicy.SHORTEST_TIME)
            strategy = new ConcreteStrategyTime();
    }
    public void dispatchTask(Task t){
        //apel metoda addTask a lui strategy
        //strategia e deja selectata
        //ambele metode addTask din fiecare clasa care implementeaza Strategy au aceeasi semnatura, deci no cast needed
        strategy.addTask(servers, t);
    }
}
