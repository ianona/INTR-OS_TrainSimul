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

/**
 *
 * @author ianona
 */
public class SimulationPane extends JLayeredPane{
    private JLabel bgImage;
    private TrainGUI sampleTrain;
    
    private static final Rectangle station1 = new Rectangle(130, 280, 100, 100);
    private static final Rectangle station2 = new Rectangle(310, 215, 100, 100);
    
    public SimulationPane(){
        try{
            bgImage = new JLabel(new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/bgImage.png"))));
        }
        catch(IOException e) {
            System.out.println("file not found");
        }
        
        bgImage.setBounds(0, 0, 1000, 600);
        sampleTrain = new TrainGUI(station1);
        sampleTrain.setBounds(0, 0, 1000, 600);
        this.setPreferredSize(new Dimension(1000,600));
        this.add(bgImage, 1);
        this.add(sampleTrain, 0);
        
        
        JButton samp = new JButton("TEST");
        samp.setBounds(0, 0, 100, 50);
        this.add(samp,0);
        samp.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                sampleTrain.animate(station1, station2);
            } 
        });
    }
}
