/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author ianona
 */
public class Feed extends JFrame{
    private JTextArea feed;
    private JScrollPane feedscroll;
    private JButton clear;
    
    public Feed(){
        this.setTitle("Simulation Feed");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(300,800);
        this.setResizable(false);
        this.setLayout(null);
        
        feed = new JTextArea(5,10);
        feed.setBounds(0, 0, 300, 750);
        feed.setEditable(false);
        feedscroll = new JScrollPane(feed);
        feedscroll.setBounds(0, 50, 300, 750);
        clear = new JButton("Clear");
        clear.setBounds(100, 0, 100, 50);
        clear.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        
        this.add(clear);
        this.add(feedscroll);
        
        clear();
        
        this.setVisible(true);
        this.revalidate();
    }
    
    public void update(String s){
        feed.append(s+"\n");
    }
    
    public void clear(){
        feed.setText("");
        feed.append("CalTrain II Simulation INTR-OS S18\n");
        feed.append("by Marasigan, Ona, Santiago\n");
        feed.append("\n");
    }
}
