/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author ianona
 */
public class SimulationPane extends JLayeredPane{
    private JLabel bgImage;
    private JLabel sampleTrain;
    private static final Rectangle station1 = new Rectangle(130, 280, 100, 100);
    
    public SimulationPane(){
        try{
            bgImage = new JLabel(new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/bgImage.png"))));
            sampleTrain = new JLabel(new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/train.png"))));
        }
        catch(IOException e) {
            System.out.println("file not found");
        }
        
        bgImage.setBounds(0, 0, 1000, 600);
        sampleTrain.setBounds(station1);
        this.setPreferredSize(new Dimension(1000,600));
        this.add(bgImage, 1);
        this.add(sampleTrain, 0);

    }
}
