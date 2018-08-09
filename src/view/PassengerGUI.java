/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author ianona
 */
public class PassengerGUI extends JPanel implements ActionListener{
    private Timer t = new Timer(5, this);
    private Image passenger;
    private int X, Y, dX, dY;
    private int velX = 1, velY=1;
    private Lock gui_lock;
    private Condition gui_cond;
    private String mode;
    
    private AbsolutePositions locator = new AbsolutePositions();
    
    public PassengerGUI(Rectangle r){
        this.gui_lock = new ReentrantLock();
        this.gui_cond = gui_lock.newCondition();
        
        try {
            passenger = new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/passenger.png"))).getImage();
            prepareImage(passenger, this);  
        } catch (IOException e) {
            System.out.println("file not found");
        }
       
        X = (int) r.getX();
        Y = (int) r.getY();
        this.setDoubleBuffered(true);

        this.setOpaque(false);
    }
    
    public Lock getGui_lock() {
        return gui_lock;
    }

    public Condition getGui_cond() {
        return gui_cond;
    }
    
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(passenger, X, Y, this);
        g.dispose();
    }
    
    public void animate(Rectangle destination) {
        Random r = new Random();
        dX = (int) destination.getX();
        dY = (int) destination.getY();
        
        if (destination.getBounds().equals(locator.station4_waiting))
            dY += r.nextInt(100-0) + 0;
        else
            dX +=r.nextInt(100-0) + 0;
        
        t.start();
        mode = "animating";
    }
    
    public void board(Rectangle destination) {
        dX = (int) destination.getX();
        dY = (int) destination.getY();
        
        if (destination.getBounds().equals(locator.station1_waiting) ||
            destination.getBounds().equals(locator.station2_waiting) ||
            destination.getBounds().equals(locator.station3_waiting))
            dY -= 50;
        
        else if (destination.getBounds().equals(locator.station5_waiting) ||
                destination.getBounds().equals(locator.station6_waiting) ||
                destination.getBounds().equals(locator.station7_waiting) ||
                destination.getBounds().equals(locator.station8_waiting))
            dY += 50;
       
        else if (destination.getBounds().equals(locator.station4_waiting))
            dX += 50;
        
        t.start();
        mode = "board";
    }
    
    public void exit(Rectangle destination) {
        X = (int) destination.getX();
        Y = (int) destination.getY();
        dX = (int) destination.getX();
        dY = (int) destination.getY();
        this.setVisible(true);
        
        if (destination.getBounds().equals(locator.station1) ||
            destination.getBounds().equals(locator.station2) ||
            destination.getBounds().equals(locator.station3))
            dY -= 50;
        
        else if (destination.getBounds().equals(locator.station5) ||
                destination.getBounds().equals(locator.station6) ||
                destination.getBounds().equals(locator.station7) ||
                destination.getBounds().equals(locator.station8))
            dY += 50;
       
        else if (destination.getBounds().equals(locator.station4))
            dX += 50;
        
        t.start();
        mode = "exit";
    }
    
    public void releaselock() {
        gui_lock.lock();
        gui_cond.signal();
        gui_lock.unlock();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (X == dX && Y == dY) {
            t.stop();
            if (mode=="board" || mode=="exit")
                this.setVisible(false);
            releaselock();
        }
            
            
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
