package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;

public interface Strategy {
    public void addTask(ArrayList<Server> servers, Task t);
}
