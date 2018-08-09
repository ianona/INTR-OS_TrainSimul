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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import model.Train;

/**
 *
 * @author ianona
 */
public class TrainGUI extends JPanel implements ActionListener {

    private Timer t = new Timer(5, this);
    private Image train, trainDown, trainLeft;
    private int X, Y, dX, dY;
    private int velX = 1, velY = 1;
    private Lock gui_lock;
    private Condition gui_cond;
    private String mode;

    private AbsolutePositions locator = new AbsolutePositions();

    public TrainGUI(Rectangle r) {
        this.gui_lock = new ReentrantLock();
        this.gui_cond = gui_lock.newCondition();

        try {
            train = new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/train.png"))).getImage();
            prepareImage(train, this);
            trainDown = new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/trainDown.png"))).getImage();
            prepareImage(trainDown, this);
            trainLeft = new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/trainLeft.png"))).getImage();
            prepareImage(trainLeft, this);
        } catch (IOException e) {
            System.out.println("file not found");
        }
        mode = "right";
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

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (mode == "right") {
            g2d.drawImage(train, X, Y, this);
        } else if (mode == "down") {
            g2d.drawImage(trainDown, X, Y, this);
        } else if (mode == "left") {
            g2d.drawImage(trainLeft, X, Y, this);
        }
        g.dispose();
    }

    public void animate(Rectangle destination) {
        dX = (int) destination.getX();
        dY = (int) destination.getY();
        if (destination.getBounds().equals(locator.station1)) {
            mode = "right";
            Y = (int) locator.station1.getY();
        } else if (destination.getBounds().equals(locator.station4)) {
            mode = "down";
            X = (int) locator.station4.getX();
        } else if (destination.getBounds().equals(locator.station5)) {
            mode = "left";
            Y = (int) locator.station5.getY();
        }
        t.start();
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
            releaselock();
        }

        if (X > dX) {
            X -= velX;
        }
        if (X < dX) {
            X += velX;
        }
        if (Y < dY) {
            Y += velY;
        }
        if (Y > dY) {
            Y -= velY;
        }

        repaint();
    }
}
