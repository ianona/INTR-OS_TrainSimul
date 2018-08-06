/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kyles
 */
public class TrainSemaphore implements Runnable{
    private int emptySeats;
    private StationMonitorSemaphore station;
    private ArrayList<PassengerSemaphore> passengers;
    private StationMonitorSemaphore[] allStations;
    private int trainNum;
    private int curStation;
    
    public TrainSemaphore(int capacity, StationMonitorSemaphore[] stations, int trainNum) {
        this.emptySeats = capacity;
        this.station = null;
        this.curStation = -1;
        
        this.passengers = new ArrayList<>();
        this.allStations = stations;
        this.trainNum = trainNum;
    }
    
    public int getEmptySeats(){
        return emptySeats;
    }
    
    public int getTrainNumber(){
        return trainNum;
    }
    
    public void occupySeat(PassengerSemaphore p){
        System.out.println("passenger boarded train #" + trainNum);
        passengers.add(p);
        emptySeats -= 1;
        System.out.println(passengers);
    }
    
    public void freeSeat(PassengerSemaphore p){
        System.out.println("passenger leaving train #" + trainNum);
        passengers.remove(p);
        emptySeats += 1;
    }
    
    public void moveStation() throws InterruptedException{
        // moves and sets current station to next station
        curStation = (curStation + 1) % 8;
        while (allStations[curStation].getTrain() != null){
            System.out.println("Train #" + trainNum + " is waiting");
        }
        System.out.println("Train #"+ trainNum + " moving station to station #" + (curStation+1));
        station = allStations[curStation];
        
        try {
            // locks and sets parent station's current train to this
            // signals parent station that trainInStation so it can load it
            //station.getStationLock().acquire();
            // checks if new station is dropoff for passengers
            // if so, removes passenger from array
            for (int i = passengers.size() - 1; i >= 0; i--) {
                // p.moveCurStation(station);
                if (passengers.get(i).getDestinationStation().getStationNumber() == station.getStationNumber()) {
                    System.out.println("I have arrived at my destination and leaving train...[" + 
                            passengers.get(i).getOriginStation().getStationNumber() + " -> " + 
                            passengers.get(i).getDestinationStation().getStationNumber() + "]");
                    passengers.remove(i);
                }

            }
        
            station.setTrain(this);
            station.getTrainInStation().release();
        } finally {
            //station.getStationLock().release();
            station.getDoneUsingTrain().acquire();
        }
    }

    @Override
    public void run() {
        try {
            // initially moves station from -1 to 0
            // implies every train spawns at first station
            while(true)
                moveStation();
        } catch (InterruptedException ex) {
            Logger.getLogger(Train.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
