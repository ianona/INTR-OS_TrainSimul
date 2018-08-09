/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author ianona
 */
public class SimulationPane extends JLayeredPane{
    private JLabel bgImage;
    private AbsolutePositions locator = new AbsolutePositions();
    
    public SimulationPane(){
        try{
            bgImage = new JLabel(new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/bgImage.png"))));
        }
        catch(IOException e) {
            System.out.println("file not found");
        }
        
        bgImage.setBounds(0, 0, 1115, 670);
        this.setPreferredSize(new Dimension(1115,670));
        this.add(bgImage, 1);
    }
    
    public PassengerGUI addPassenger(){
        PassengerGUI samplePass = new PassengerGUI(locator.passengerSpawn);
        samplePass.setBounds(0, 0, 1115, 670);
        this.add(samplePass, 0);
        return samplePass;
    }
    
    public TrainGUI addTrain(){
        TrainGUI sampleTrain = new TrainGUI(locator.station0);
        sampleTrain.setBounds(0, 0, 1115, 670);
        this.add(sampleTrain, 1);
        return sampleTrain;
    }
}
