package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.Iterator;

public class ConcreteStrategyQueue implements Strategy{
    private Server shortestQueue(ArrayList<Server> servers) {
        Iterator<Server> s = servers.iterator();
        Server shortest = s.next();
        while(s.hasNext()) {
            Server curr = s.next();
            if(shortest.getSize() > curr.getSize())
                shortest = curr;
        }
        return shortest;
    }
    @Override
    public void addTask(ArrayList<Server> servers, Task t) {
        if(servers.size() > 0) {
            Server theChosenOne = shortestQueue(servers);
            theChosenOne.addTask(t);
        }
    }
}
