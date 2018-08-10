/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.SimulationController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author ianona
 */
public class MenuView extends JFrame {

    private JButton locks, semaphore;
    private JLabel bgImage;
    private JPanel mainPanel;

    public MenuView() {
        this.setTitle("Menu");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500, 320);
        this.setResizable(false);
        this.setLayout(null);

        try {
            ImageIcon io = new ImageIcon(new ImageIcon(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/small.png"))).getImage().getScaledInstance(500, 300, Image.SCALE_DEFAULT));
            bgImage = new JLabel(io);
        } catch (IOException e) {
            System.out.println("file not found");
        }

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(500, 320));
        mainPanel.setLayout(null);
        bgImage.setBounds(0, 0, 500, 300);
        mainPanel.add(bgImage, 0);

        locks = new JButton("Locks & Monitors");
        locks.setBounds(180, 20, 150, 100);
        mainPanel.add(locks, 0);
        semaphore = new JButton("Semaphores");
        semaphore.setBounds(180, 120, 150, 100);
        mainPanel.add(semaphore, 0);

        locks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Feed f = new Feed();
                SimulationController sc = new SimulationController(f);
                sc.setMode('l');
                SimulationView sv = new SimulationView(sc);
                sc.attach(sv.getSimulation());
                sc.attach(sv.getControl());
                sc.initialize();
                dispose();
            }
        });
        
        semaphore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Feed f = new Feed();
                SimulationController sc = new SimulationController(f);
                sc.setMode('s');
                SimulationView sv = new SimulationView(sc);
                sc.attach(sv.getSimulation());
                sc.attach(sv.getControl());
                sc.initialize();
                dispose();
            }
        });

        mainPanel.setBounds(0, 0, 500, 300);
        this.add(mainPanel);
        this.setVisible(true);
        this.revalidate();
    }
}
