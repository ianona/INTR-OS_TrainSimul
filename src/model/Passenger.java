/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dlsza
 */
public class Passenger implements Runnable {
    private StationMonitor originStation;
    private StationMonitor currentStation;
    private StationMonitor destinationStation;
    private Lock stationLock;
    private Condition trainArrived;
    private Condition allSeated;
    
    public Passenger(StationMonitor start, StationMonitor end){
        this.originStation = start;
        this.currentStation = start;
        this.destinationStation = end;
        this.stationLock = start.getStationLock();
        this.trainArrived = start.getTrainArrived();
        this.allSeated = start.getAllSeated();
    }

    public StationMonitor getDestinationStation() {
        return destinationStation;
    }
    
    public StationMonitor getOriginStation() {
        return originStation;
    }
    
    public void station_wait_for_train(){
        // locks and adds itself to its station's waiting array
        stationLock.lock();
        originStation.addWaiting(this);
        
        // wait for trainArrived signal
        // after receiving signal, remove from waiting, unlock, proceed to board
        try {
            System.out.println("passenger at station " + originStation.getStationNumber()+" waiting for train...");
            trainArrived.await();
            System.out.println("passenger is signalled by train");
            originStation.removeWaiting(this);
        } catch (InterruptedException ex) {
            Logger.getLogger(Passenger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            stationLock.unlock();
            station_on_board();
        }
    }
    
    public void station_on_board(){
        // locks and boards train
        // signals allSeated if no more waiting or train is full
        // signal allSeated is sign for train to move on to next station
        stationLock.lock();
        try {
            System.out.println("bording train...");
            originStation.boardPassenger(this);
            if (originStation.getFreeTrainSeats() == 0 || originStation.getWaitingPassengers() == 0) {
                allSeated.signal();
            }
        } finally {
            stationLock.unlock();
        }
    }
    
    public void moveCurStation(StationMonitor s) {
        this.currentStation = s;
    }

    @Override
    public void run() {
        // waits for train
        station_wait_for_train();
        // after in train, passenger is practically dead
        // train is responsible for releasing passenger
        
        // OLD CODE
        // while (currentStation.getStationNumber() != destinationStation.getStationNumber());
        /*
        stationLock.lock();
        try {
            System.out.println("I have arrived at my destination and leaving train...[" + originStation.getStationNumber() + " -> " + destinationStation.getStationNumber() + "]");
            originStation.releasePassenger(this);
        } finally {
            stationLock.unlock();
        }
        */
    }
}