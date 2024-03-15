package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.Iterator;

public class ConcreteStrategyTime implements Strategy{
    private Server queueWithShortestTime(ArrayList<Server> servers) {
        Iterator<Server> s = servers.iterator();
        Server shortest = s.next();
        while(s.hasNext()) {
            Server curr = s.next();
            if(shortest.getWaitingPeriod().get() > curr.getWaitingPeriod().get())
                shortest = curr;
        }
        return shortest;
    }
    @Override
    public void addTask(ArrayList<Server> servers, Task t) {
        if(servers.size() > 0) {
            Server theChosenOne = queueWithShortestTime(servers);
            theChosenOne.addTask(t);
        }
    }
}
