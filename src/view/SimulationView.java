/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

/**
 *
 * @author ianona
 */
public class SimulationView extends JFrame{
    public SimulationView(){
        this.setTitle("Simulation");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1000,800);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
       
        this.add(new SimulationPane(),BorderLayout.NORTH);
        this.add(new ControlPane(),BorderLayout.SOUTH);
        
        this.setVisible(true);
        this.revalidate();
    }
    
}
