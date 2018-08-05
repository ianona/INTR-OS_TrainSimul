/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Passenger;
import model.StationMonitor;
import model.Train;

/**
 *
 * @author ianona
 */
public class SimulationController {
    private StationMonitor[] stations;
    
    public SimulationController(){
        stations = new StationMonitor[8];
    }
    
    public void initialize(){
        // initialize 8 stations and store in stations array
        for (int i=0; i<8; i++){
            StationMonitor sm = new StationMonitor(i+1);
            Thread t = new Thread(sm);
            stations[i] = sm;
            t.start();
        }
//        
//        Passenger p1 = new Passenger(stations[0], stations[4]);
//        new Thread(p1).start();
//        
//        Passenger p2 = new Passenger(stations[2], stations[7]);
//        new Thread(p2).start();
//        
//        Passenger p3 = new Passenger(stations[5], stations[0]);
//        new Thread(p3).start();
//        
        Train tr1 = new Train(3, stations, 1);
        new Thread(tr1).start();
        
        Train tr2 = new Train(3, stations, 2);
        new Thread(tr2).start();
        
        Train tr3 = new Train(3, stations, 3);
        new Thread(tr3).start();
    }
}
