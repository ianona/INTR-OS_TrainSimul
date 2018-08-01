 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author ianona
 */
public class TrainGUI extends JPanel implements ActionListener{
    private Timer t = new Timer(5, this);
    private Image train;
    private int X, Y, dX, dY;
    private int velX = 2, velY=1;
    
    private static final Rectangle station1 = new Rectangle(0,0, 100, 100);
    private static final Rectangle station2 = new Rectangle(310, 215, 100, 100);
    
    public TrainGUI(Rectangle r){
        try {
            train = new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/train.png"))).getImage();
            prepareImage(train, this);  
            //train = new JLabel(new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/train.png"))));
        } catch (IOException e) {
            System.out.println("file not found");
        }
       
        X = (int) r.getX();
        Y = (int) r.getY();
        this.setDoubleBuffered(true);

        this.setOpaque(false);
    }
    
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(train, X, Y, this); //Draws the ball Image at the correct X and Y co-ordinates
        //Toolkit.getDefaultToolkit().sync(); // necessary for linux users to draw  and animate image correctly
        g.dispose();
        System.out.println(X+" "+Y);
        //train.setBounds(0,200,100,100);
    }
    
    public void animate(Rectangle curStation, Rectangle destination) {
        dX = (int) destination.getX();
        dY = (int) destination.getY();
        t.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (X == dX && Y == dY)
            t.stop();
            
        if (X > dX)
            X -= velX;
        if (X < dX)
            X += velX;
        if (Y < dY)
            Y += velY;
        if (Y > dY)
            Y -= velY;
        
        repaint();
    }
}
