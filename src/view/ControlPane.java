/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

/**
 *
 * @author ianona
 */
public class ControlPane extends JLayeredPane{
    private JLabel trainLbl, passLbl, trainCount, passCount;
    private JLabel passOnLbl, passOffLbl;
    private JTextField passOn, passOff;
    private JButton trainBtn, passBtn;
    private JButton startBtn, exitBtn;
    
    public ControlPane(){
        this.setPreferredSize(new Dimension(1000,200));
        
        trainLbl = new JLabel("TRAINS");
        passLbl = new JLabel("PASSENGERS");
        trainCount = new JLabel("0");
        passCount = new JLabel("0");
        
        trainLbl.setBounds(20, 50, 100, 20);
        passLbl.setBounds(120, 50, 100, 20);
        trainCount.setBounds(20, 55, 100, 100);
        passCount.setBounds(120, 55, 100, 100);
        
        trainCount.setFont(trainCount.getFont().deriveFont(Font.PLAIN,50));
        passCount.setFont(passCount.getFont().deriveFont(Font.PLAIN,50));
        
        this.add(trainLbl);
        this.add(passLbl);
        this.add(trainCount);
        this.add(passCount);
        
        trainBtn = new JButton("Add Train");
        trainBtn.setBounds(250, 120, 100, 30);
        
        passOnLbl = new JLabel("Get on at:");
        passOnLbl.setBounds(350,50,100,30);
        passOn = new JTextField(3);
        passOn.setBounds(450, 50, 80, 30);
        passOffLbl = new JLabel("Get off at:");
        passOffLbl.setBounds(350,80,100,30);
        passOff = new JTextField(3);
        passOff.setBounds(450, 80, 80, 30);
        passBtn = new JButton("Add Passenger");
        passBtn.setBounds(350, 120, 180, 30);
        
        this.add(passOnLbl);
        this.add(passOn);
        this.add(passOffLbl);
        this.add(passOff);
        this.add(trainBtn);
        this.add(passBtn);
        
        startBtn = new JButton("START");
        startBtn.setBounds(250, 150, 100, 30);
        exitBtn = new JButton("EXIT");
        exitBtn.setBounds(350, 150, 100, 30);
        this.add(startBtn);
        this.add(exitBtn);
    }
}
