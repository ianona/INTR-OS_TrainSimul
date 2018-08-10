/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.SimulationController;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

/**
 *
 * @author ianona
 */
public class SimulationView extends JFrame{
    private SimulationPane sp;
    private ControlPane cp;
    
    public SimulationView(SimulationController sc){
        System.out.println(sc.getMode());
        if (sc.getMode() == 'l')
            this.setTitle("Locks & Monitors Simulation");
        else
            this.setTitle("Semaphore Simulation");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1115,870);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
       
        sp = new SimulationPane();
        this.add(sp,BorderLayout.NORTH);
        cp = new ControlPane(sc);
        this.add(cp,BorderLayout.SOUTH);
        
        this.setVisible(true);
        this.revalidate();
    }
    
    public SimulationPane getSimulation(){
        return sp;
    }
    
    public ControlPane getControl(){
        return cp;
    }
}
