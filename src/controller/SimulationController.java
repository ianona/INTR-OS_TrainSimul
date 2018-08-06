/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Passenger;
import model.PassengerSemaphore;
import model.StationMonitor;
import model.StationMonitorSemaphore;
import model.Train;
import model.TrainSemaphore;

/**
 *
 * @author ianona
 */
public class SimulationController {
    private StationMonitor[] stations;
    private StationMonitorSemaphore[] semaStations;
    
    public SimulationController(){
        stations = new StationMonitor[8];
        semaStations = new StationMonitorSemaphore[8];
    }
    
    public void initialize(){
        // initialize 8 stations and store in stations array
        
        // to decide which solution to use, semaphore or locks. true = semaphore and false = locks
        boolean semaphore = true;
        
        // set number of trains before hand
        int numberOfTrains = 1; 
        
        if (semaphore){
            System.out.println("Semaphore solution");
            for (int i=0; i<8; i++){
                StationMonitorSemaphore sm = new StationMonitorSemaphore(i+1);
                Thread t = new Thread(sm);
                semaStations[i] = sm;
                t.start();
            }
            
            new Thread(new PassengerSemaphore(semaStations[0], semaStations[4])).start();
            new Thread(new PassengerSemaphore(semaStations[0], semaStations[4])).start();
            
            new Thread(new PassengerSemaphore(semaStations[2], semaStations[7])).start();
            
            new Thread(new PassengerSemaphore(semaStations[5], semaStations[0])).start();
            
            for (int x=0; x < numberOfTrains; x++){
                new Thread(new TrainSemaphore(3, semaStations, x+1)).start();
            }
        }
        else{
            System.out.println("Lock solution");
            for (int i=0; i<8; i++){
                StationMonitor sm = new StationMonitor(i+1);
                Thread t = new Thread(sm);
                stations[i] = sm;
                t.start();
            }
            
            Passenger p1 = new Passenger(stations[0], stations[4]);
            new Thread(p1).start();
            Passenger p4 = new Passenger(stations[0], stations[4]);
            new Thread(p4).start();
            
            Passenger p2 = new Passenger(stations[2], stations[7]);
            new Thread(p2).start();

            Passenger p3 = new Passenger(stations[5], stations[0]);
            new Thread(p3).start();
            
            for (int x=0; x < numberOfTrains; x++){
                new Thread(new Train(3, stations, x+1)).start();
            }
        }
//        
        
//        
//        Train tr1 = new Train(3, stations, 1);
//        new Thread(tr1).start();
//        
//        Train tr2 = new Train(3, stations, 2);
//        new Thread(tr2).start();
//        
//        Train tr3 = new Train(3, stations, 3);
//        new Thread(tr3).start();
//        
//        Train tr3 = new Train(3, stations, 3);
//        new Thread(tr3).start();
//        
//        Train tr3 = new Train(3, stations, 3);
//        new Thread(tr3).start();
    }
}
