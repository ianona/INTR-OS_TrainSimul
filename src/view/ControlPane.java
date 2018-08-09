/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.SimulationController;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Passenger;
import model.PassengerSemaphore;
import model.Train;
import model.TrainSemaphore;

/**
 *
 * @author ianona
 */
public class ControlPane extends JLayeredPane {

    private JLabel waitingLbl, passLbl, waitingCount, passCount;
    private JLabel passOnLbl, passOffLbl, capacityLbl;
    private JTextField capacity;
    private JComboBox passOn, passOff;
    private JButton trainBtn, passBtn;
    private JButton startBtn, exitBtn;
    private JTable trainTable;
    private DefaultTableModel tableModel;

    private final static Integer[] stationNos = {1, 2, 3, 4, 5, 6, 7, 8};

    private SimulationController controller;
    int curTrains = 1;

    public ControlPane(SimulationController sc) {
        this.setPreferredSize(new Dimension(1115, 200));
        this.controller = sc;

        waitingLbl = new JLabel("<html>WAITING<br>PASSENGERS</html>");
        passLbl = new JLabel("<html>IN-TRAIN<br>PASSENGERS</html>");
        waitingCount = new JLabel("0");
        passCount = new JLabel("0");

        waitingLbl.setBounds(20, 40, 100, 50);
        passLbl.setBounds(120, 40, 100, 50);
        waitingCount.setBounds(20, 55, 100, 100);
        passCount.setBounds(120, 55, 100, 100);

        waitingCount.setFont(waitingCount.getFont().deriveFont(Font.PLAIN, 50));
        passCount.setFont(passCount.getFont().deriveFont(Font.PLAIN, 50));

        this.add(waitingLbl);
        this.add(passLbl);
        this.add(waitingCount);
        this.add(passCount);

        capacityLbl = new JLabel("Capacity:");
        capacity = new JTextField(3);
        capacityLbl.setBounds(230, 50, 80, 30);
        capacity.setBounds(230, 80, 80, 30);

        this.add(capacityLbl);
        this.add(capacity);

        trainBtn = new JButton("Add Train");
        trainBtn.setBounds(220, 120, 100, 30);
        trainBtn.addActionListener(new addTrain());

        passOnLbl = new JLabel("Get on at:");
        passOnLbl.setBounds(320, 50, 100, 30);
        passOn = new JComboBox(stationNos);
        passOn.setBounds(420, 50, 80, 30);
        passOffLbl = new JLabel("Get off at:");
        passOffLbl.setBounds(320, 80, 100, 30);
        passOff = new JComboBox(stationNos);
        passOff.setBounds(420, 80, 80, 30);
        passBtn = new JButton("Add Passenger");
        passBtn.setBounds(320, 120, 180, 30);
        passBtn.addActionListener(new addPassenger());

        this.add(passOnLbl);
        this.add(passOn);
        this.add(passOffLbl);
        this.add(passOff);
        this.add(trainBtn);
        this.add(passBtn);

        startBtn = new JButton("START");
        startBtn.setBounds(220, 150, 100, 30);
        exitBtn = new JButton("EXIT");
        exitBtn.setBounds(320, 150, 100, 30);
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        //this.add(startBtn);
        this.add(exitBtn);

        Object rowData[][] = {{"Train #1", "0", "Train #5", "0", "Train #9", "0", "Train #13", "0"},
        {"Train #2", "0", "Train #6", "0", "Train #10", "0", "Train #14", "0"},
        {"Train #3", "0", "Train #7", "0", "Train #11", "0", "Train #15", "0"},
        {"Train #4", "0", "Train #8", "0", "Train #12", "0", "Train #16", "0"}};
        Object columnNames[] = {"TRAIN #", "PASSENGERS", "TRAIN #", "PASSENGERS", "TRAIN #", "PASSENGERS", "TRAIN #", "PASSENGERS"};

        tableModel = new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        trainTable = new JTable();
        trainTable.setModel(tableModel);
        trainTable.setBounds(550, 50, 500, 100);
        this.add(trainTable);
    }

    public void boardTrain(int trainNum) {
        waitingCount.setText(String.valueOf(Integer.valueOf(waitingCount.getText()) - 1));
        passCount.setText(String.valueOf(Integer.valueOf(passCount.getText()) + 1));

        int row = trainNum % 4;
        if (row == 0) {
            row = 4;
        }
        row -= 1;

        int column = ((trainNum - 1) / 4) + 1;
        tableModel.setValueAt(Integer.valueOf(tableModel.getValueAt(row, column).toString()) + 1, row, column);
        tableModel.fireTableDataChanged();
    }

    public void exitTrain(int trainNum) {
        passCount.setText(String.valueOf(Integer.valueOf(passCount.getText()) - 1));

        int row = trainNum % 4;
        if (row == 0) {
            row = 4;
        }
        row -= 1;

        int column = ((((trainNum - 1) / 4) + 1) * 2) - 1;
        tableModel.setValueAt(Integer.valueOf(tableModel.getValueAt(row, column).toString()) - 1, row, column);
        tableModel.fireTableDataChanged();
    }

    class addPassenger implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (controller.getMode() == 'l') {
                int off = (Integer) passOff.getSelectedItem();
                int on = (Integer) passOn.getSelectedItem();
                Passenger p = new Passenger(controller.getStation(on), controller.getStation(off), controller.getFeed(), controller);
                new Thread(p).start();
            } else {
                int off = (Integer) passOff.getSelectedItem();
                int on = (Integer) passOn.getSelectedItem();
                PassengerSemaphore p = new PassengerSemaphore(controller.getStation_Semaphore(on), controller.getStation_Semaphore(off), controller.getFeed(), controller);
                new Thread(p).start();
            }

            waitingCount.setText(String.valueOf(Integer.valueOf(waitingCount.getText()) + 1));
        }
    }

    class addTrain implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (controller.getMode() == 'l') {
                Train tr = new Train(Integer.valueOf(capacity.getText()), controller.getStations(), curTrains, controller.getFeed(), controller);
                curTrains += 1;
                new Thread(tr).start();
            } else {
                TrainSemaphore tr = new TrainSemaphore(Integer.valueOf(capacity.getText()), controller.getStations_Semaphore(), curTrains, controller.getFeed(), controller);
                curTrains += 1;
                new Thread(tr).start();
            }
        }
    }
}
