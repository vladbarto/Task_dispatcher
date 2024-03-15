package GUI;

import Model.Server;
import Model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class AnimationFrame extends JFrame {
    //here are resting our panels
    private JPanel mainPanel;
    private JPanel centru;
    private JPanel centru2;
    private JPanel centruWrap;
    private JPanel sud;
    private JPanel nord;
    private JPanel vest; //iesirea magazinului
    private ArrayList<JPanel> serverPanel;
    private ArrayList<JPanel> clientiCase;
    private ArrayList<JPanel> caseMarcat;
    private ArrayList<JTextField> serverTextFields;
    //some data we need
    private int nrServers;
    //here we place our labels
    private ArrayList<JLabel> tasks;
    private JLabel timpCurent;
    //here we place our progress bar
    private JProgressBar progres;
    public AnimationFrame(int nrServers, int timeLimit){
        // here we get our numeric data
        this.nrServers = nrServers;
        // here we initialise our panels
        this.mainPanel = new JPanel();
        this.centru = new JPanel();
        this.centru2 = new JPanel();
        this.centruWrap = new JPanel();
        this.sud = new JPanel();
        this.nord = new JPanel();
        this.vest = new JPanel();
        // work about main panel
        this.mainPanel.setLayout(new BorderLayout());
        this.mainPanel.add(centruWrap, BorderLayout.CENTER);
        this.mainPanel.add(sud, BorderLayout.SOUTH);
        this.mainPanel.add(nord, BorderLayout.NORTH);
        this.mainPanel.add(vest, BorderLayout.WEST);

        relatedToCentru();
        relatedToSud(timeLimit);
        relatedToNord();
        relatedToVest();
        // alte date despre panel
        this.setTitle("Animation");
        this.setSize(new Dimension(1000, 720));
        this.setResizable(false);
        this.setLocationRelativeTo((Component)null);
        this.setDefaultCloseOperation(3);
        // important
        this.setVisible(true);
        this.setContentPane(mainPanel);
    }
    public void relatedToCentru(){
        // work about drawing Servers ( central Panel )
        this.centruWrap.setLayout(new BoxLayout(centruWrap, BoxLayout.Y_AXIS));
        this.centru.setLayout(new BoxLayout(centru, BoxLayout.X_AXIS));
        this.centru2.setLayout(new BoxLayout(centru2, BoxLayout.X_AXIS));
        clientiCase = new ArrayList<JPanel>();
        caseMarcat = new ArrayList<JPanel>();
        centruWrap.add(centru);
        centruWrap.add(centru2);
        serverPanel = new ArrayList<JPanel>();
        serverTextFields = new ArrayList<JTextField>();
        Random rand = new Random();
        for(int i = 0; i < nrServers; i++){
            serverPanel.add(new JPanel());
            clientiCase.add(new JPanel());
            caseMarcat.add(new JPanel());
            serverPanel.get(i).setLayout(new BoxLayout(serverPanel.get(i), BoxLayout.X_AXIS));
            clientiCase.get(i).setLayout(new BoxLayout(clientiCase.get(i), BoxLayout.Y_AXIS));
            caseMarcat.get(i).setLayout(new BoxLayout(caseMarcat.get(i), BoxLayout.X_AXIS));
            serverTextFields.add(new JTextField());
            serverTextFields.get(i).setEditable(false);
            serverTextFields.get(i).setText(""+(i+1));
            if(i > 9)
                centru2.add(serverPanel.get(i));
            else
                centru.add(serverPanel.get(i));
            caseMarcat.get(i).add(serverTextFields.get(i));
            serverPanel.get(i).add(caseMarcat.get(i));
            serverPanel.get(i).add(clientiCase.get(i));
            clientiCase.get(i).setOpaque(false);
            serverPanel.get(i).setBackground(Color.white);
            serverTextFields.get(i).setPreferredSize(new Dimension(22, 200));
        }
    }
    public void relatedToSud(int timeLimit){
        //work related to south panel
        this.timpCurent = new JLabel("Timp curent: 0");
        progres = new JProgressBar(0, timeLimit);
        this.sud.add(progres);
        progres.setPreferredSize(new Dimension(300, 20));
        progres.setBorderPainted(true);
        progres.setMaximum(timeLimit);
        progres.setMinimum(0);
        progres.setValue(0);
        progres.setForeground(Color.decode("#74B72E"));
        this.sud.add(timpCurent);
    }
    public void relatedToNord() {
        this.tasks = new ArrayList<>();
        this.nord.setBackground(Color.decode("#F2D2BD"));
        this.nord.setPreferredSize(new Dimension(1000, 200));
    }
    public void relatedToVest() {
        this.vest.setLayout(new FlowLayout());
        this.vest.setBackground(Color.decode("#AEF359"));
        this.vest.add(new JLabel("Shop Exit"));
    }

    // Update Methods
    public void updateNord(ArrayList<Task> taskList) {
        if(tasks.size() > 0)
            tasks.removeAll(tasks);
        nord.removeAll();
        nord.revalidate();
        nord.repaint();
        nord.add(new JLabel("Waiting Area: "));
        for(int i = 0; i < taskList.size(); ++i) {
            tasks.add(new JLabel("(" + taskList.get(i).getID() + ", " + taskList.get(i).getArrivalTime() + ", " + taskList.get(i).getServiceTime() + ")"));
            nord.add(tasks.get(i));
        }
    }
    public void updateSud(int currentTime) {
        timpCurent.setText("Timp curent: " + currentTime);
        progres.setValue(currentTime%progres.getMaximum() + 1);
    }
    public void updateCentru(ArrayList<Server> servers) {
        for(int i = 0; i < nrServers; i++) {
            clientiCase.get(i).removeAll();
            clientiCase.get(i).revalidate();
            clientiCase.get(i).repaint();
            Iterator it = servers.get(i).getTasks().iterator();
            while(it.hasNext()){
                Task _curr = (Task) it.next();
                clientiCase.get(i).add(new JLabel("(" + _curr.getID() + ", " + _curr.getArrivalTime() + ", " + _curr.getServiceTime() + ")"));
            }
        }
    }

    public void showGenerateResults() {
        JButton btn = new JButton("Generate log of events");
        mainPanel.add(btn, BorderLayout.EAST);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
    }
}
