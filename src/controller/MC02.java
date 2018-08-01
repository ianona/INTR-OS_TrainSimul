/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.Feed;
import view.SimulationView;

/**
 *
 * @author ianona
 */
public class MC02 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //SimulationView sv = new SimulationView();
        //Feed f = new Feed();
        SimulationController sc = new SimulationController();
        sc.initialize();
    }
}