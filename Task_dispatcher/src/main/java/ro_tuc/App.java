package ro_tuc;

import BusinessLogic.SimulationManager;
import GUI.AnimationFrame;
import GUI.SimulationFrame;

public class App {
    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager();
        Thread t = new Thread(gen);
        t.start();
        while(gen.isStopped() == false){
            ;
        }
        t.stop();
//        try {
//            t.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

    }
}