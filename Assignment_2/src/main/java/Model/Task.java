package Model;

import java.util.Comparator;

//aceasta clasa va fi clientul
public class Task {
    private final int ID;
    private final int arrivalTime;
    private int serviceTime; // this is the processing time
//variabile long si double>>var. volatile
    //read si write sunt atomice (se fac intr-un singur pas)
    //metoda sincronizata pt scrire
    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getID() {
        return ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }
    public void decreaseServiceTime(){
        serviceTime--;
    }

    @Override
    public String toString() {
        return  "(" + ID +
                ", " + arrivalTime +
                ", " + serviceTime +
                ')';
    }
}
