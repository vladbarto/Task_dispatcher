package GUI;

import BusinessLogic.SelectionPolicy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.util.Locale;

public class SimulationFrame extends JFrame {
    private boolean launched;
    private boolean stopped;
    public boolean formCorrectlyFilled;
    // panels
    private JPanel mainPanel;
    private JPanel left;
    private JPanel right;
    private JPanel panelClients;
    private JPanel panelServers;
    private JPanel panelSimTime;
    private JPanel panelMinMax;
    private JPanel panelClientsPerQ;
    private JPanel panelStrategy;
//    private JPanel leftleft;
//    private JPanel rightright;
    // labels
    private JLabel numberOfClients_label;
    private JLabel numberOfServers_label;
    private JLabel simulationTime_label;
    private JLabel minArrTime_label;
    private JLabel maxArrTime_label;
    private JLabel minProcTime_label;
    private JLabel maxProcTime_label;
    private JLabel noClientsPerQ_label;
    private JLabel chooseStrategy_label;
    // textfields
    private JTextField numberOfClients_text;
    private JTextField numberOfServers_text;
    private JTextField simulationTime_text;
    private JTextField minArrTime_text;
    private JTextField maxArrTime_text;
    private JTextField minProcTime_text;
    private JTextField maxProcTime_text;
    // slider
    private JSlider noClientsPerQ_slider;
    // combobox
    private JComboBox chooseStrategy_select;
    // button
    private JButton launch;
    // collected data (numerical data)
    private int nrClients;
    private int nrServers;
    private int simTime;
    private int minArr;
    private int maxArr;
    private int minProc;
    private int maxProc;
    private int nrClientsPerQ;
    private SelectionPolicy strateg;
    public SimulationFrame() {
        launched = false; stopped = false; formCorrectlyFilled = true;
        //here we initialise all panels
        this.mainPanel = new JPanel();
        this.left = new JPanel();
        this.right = new JPanel();
        this.panelClients = new JPanel();
        this.panelServers = new JPanel();
        this.panelSimTime = new JPanel();
        this.panelMinMax = new JPanel();
        this.panelClientsPerQ = new JPanel();
        this.panelStrategy = new JPanel();
//        this.leftleft = new JPanel();
//        this.rightright = new JPanel();

        //here we add all the panels inside the main panel
        this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        this.mainPanel.add(panelClients);
        this.mainPanel.add(panelServers);
        this.mainPanel.add(panelSimTime);
        this.mainPanel.add(panelMinMax);
        this.mainPanel.add(panelClientsPerQ);
        this.mainPanel.add(panelStrategy);
//        this.mainPanel.add(leftleft);
//        this.mainPanel.add(rightright);

        //here we initialise all labels
        numberOfClients_label = new JLabel();
        numberOfServers_label = new JLabel();
        simulationTime_label = new JLabel();
        minArrTime_label = new JLabel();
        maxArrTime_label = new JLabel();
        minProcTime_label = new JLabel();
        maxProcTime_label = new JLabel();
        noClientsPerQ_label = new JLabel();
        chooseStrategy_label = new JLabel();

        //here we initialise all text fields
        numberOfClients_text = new JTextField();
        numberOfServers_text = new JTextField();
        simulationTime_text = new JTextField();
        minArrTime_text = new JTextField();
        maxArrTime_text = new JTextField();
        minProcTime_text = new JTextField();
        maxProcTime_text = new JTextField();

        //here we initialise a slider
        noClientsPerQ_slider = new JSlider(JSlider.HORIZONTAL, 2, 10, 4);
        noClientsPerQ_slider.setMajorTickSpacing(1);
        noClientsPerQ_slider.setPaintTicks(true);
        noClientsPerQ_slider.setPaintLabels(true);

        //here we initialise a new combo box
        chooseStrategy_select = new JComboBox();

        //here we initialise a new button
        launch = new JButton("Launch execution");

        //here we set the preferred dimensions for all text fields
        numberOfClients_text.setPreferredSize(new Dimension(100, 20));
        numberOfServers_text.setPreferredSize(new Dimension(100, 20));
        simulationTime_text.setPreferredSize(new Dimension(100, 20));
        minArrTime_text.setPreferredSize(new Dimension(100, 20));
        maxArrTime_text.setPreferredSize(new Dimension(100, 20));
        minProcTime_text.setPreferredSize(new Dimension(100, 20));
        maxProcTime_text.setPreferredSize(new Dimension(100, 20));

        //here we add Client, Server and Sim time components into corresponding panels
        this.panelClients.add(numberOfClients_label);
        this.panelClients.add(numberOfClients_text);
        numberOfClients_label.setText("Number of Clients in Total");
        this.panelServers.add(numberOfServers_label);
        this.panelServers.add(numberOfServers_text);
        numberOfServers_label.setText("Number of Servers");
        this.panelSimTime.add(simulationTime_label);
        this.panelSimTime.add(simulationTime_text);
        simulationTime_label.setText("Simulation Time");

        //same goes for mins and maxes
        this.panelMinMax.add(left);
        this.panelMinMax.add(right);
        this.left.setLayout(new GridLayout(2, 2));
        this.right.setLayout(new GridLayout(2, 2));
        this.left.setBackground(new Color(70, 198, 169));
        this.right.setBackground(new Color(255, 230, 150));

        this.left.add(minArrTime_label);
        this.left.add(minArrTime_text);
        this.left.add(maxArrTime_label);
        this.left.add(maxArrTime_text);
        minArrTime_label.setText("Minimum Arrival Time");
        maxArrTime_label.setText("Maximum Arrival Time");

        this.right.add(minProcTime_label);
        this.right.add(minProcTime_text);
        this.right.add(maxProcTime_label);
        this.right.add(maxProcTime_text);
        minProcTime_label.setText("Minimum Processing Time");
        maxProcTime_label.setText("Maximum Processing Time");

        //and same goes for selecting the no. of clients per queue
        this.panelClientsPerQ.add(noClientsPerQ_label);
        noClientsPerQ_label.setText("Set how many clients can at most be at a queue");
        this.panelClientsPerQ.add(noClientsPerQ_slider);

        //and same goes for strategy
        this.panelStrategy.add(chooseStrategy_label);
        chooseStrategy_label.setText("Choose Strategy");
        this.panelStrategy.add(chooseStrategy_select);
        chooseStrategy_select.setEditable(false);
        chooseStrategy_select.addItem("Shortest Queue");
        chooseStrategy_select.addItem("Shortest Waiting Time");

        //the button is directly added to mainPanel. No extra panel really needed
        this.mainPanel.add(launch);
        this.mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // ~Listeners~
        launch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formValidation();
                readSliderAndStrategy();
                if(true == formCorrectlyFilled) {
                    launched = true;
                    setVisible(false);
                    dispose();
                }
            }
        });
        // alte date despre panel
        this.setTitle("Simulation Setup");
        this.setSize(new Dimension(600, 400));
        this.setResizable(false);
        this.setLocationRelativeTo((Component)null);
        this.setDefaultCloseOperation(3);
        // important
        this.setVisible(true);
        this.setContentPane(mainPanel);
    }

    public boolean getLaunched() {
        return launched;
    }
    public boolean getStopped() {
        return stopped;
    }

    public int getNrClients() {
        return nrClients;
    }

    public int getNrServers() {
        return nrServers;
    }

    public int getSimTime() {
        return simTime;
    }

    public int getMinArr() {
        return minArr;
    }

    public int getMaxArr() {
        return maxArr;
    }

    public int getMinProc() {
        return minProc;
    }

    public int getMaxProc() {
        return maxProc;
    }

    public int getNrClientsPerQ() {
        return nrClientsPerQ;
    }

    public SelectionPolicy getStrateg() {
        return strateg;
    }

    public void formValidation() {
        if(numberOfClients_text.getText().equals("") ||
                numberOfServers_text.getText().equals("") ||
                simulationTime_text.getText().equals("") ||
                minArrTime_text.getText().equals("") ||
                maxArrTime_text.getText().equals("") ||
                minProcTime_text.getText().equals("") ||
                maxProcTime_text.getText().equals(""))
        {
            formCorrectlyFilled = false;
            JOptionPane.showMessageDialog(mainPanel, "One or more fields are not completed");
        }
        else {
            try {
                nrClients = Integer.parseInt(numberOfClients_text.getText());
                nrServers = Integer.parseInt(numberOfServers_text.getText());
                simTime = Integer.parseInt(simulationTime_text.getText());
                minArr = Integer.parseInt(minArrTime_text.getText());
                maxArr = Integer.parseInt(maxArrTime_text.getText());
                minProc = Integer.parseInt(minProcTime_text.getText());
                maxProc = Integer.parseInt(maxProcTime_text.getText());
            } catch (NumberFormatException ex) {
                formCorrectlyFilled = false;
                JOptionPane.showMessageDialog(mainPanel, "Your input is not a positive integer");
            }
            if(minArr >= maxArr){
                formCorrectlyFilled = false;
                JOptionPane.showMessageDialog(mainPanel, "Check for min-max Arrival Time interval inconsistency (inferior bound is greater than the superior bound)");
            }
            if(minProc >= maxProc){
                formCorrectlyFilled = false;
                JOptionPane.showMessageDialog(mainPanel, "Check for min-max Processing Time interval inconsistency (inferior bound is greater than the superior bound)");
            }
            if(0 == nrClients || 0 == nrServers || 0 == minProc || 0 == simTime) {
                formCorrectlyFilled = false;
                JOptionPane.showMessageDialog(mainPanel, "Your input must ben greater than 0");
            }
            if(nrServers > 20) {
                formCorrectlyFilled = false;
                JOptionPane.showMessageDialog(mainPanel, "Number of servers cannot be greater than 20");
            }

        }
    }
    public void readSliderAndStrategy(){
        nrClientsPerQ = noClientsPerQ_slider.getValue();
        if(chooseStrategy_select.getSelectedItem().equals("Shortest Queue"))
            strateg = SelectionPolicy.SHORTEST_QUEUE;
        else strateg = SelectionPolicy.SHORTEST_TIME;
    }
    //TODO: Remove System.out.println commented lines
}
