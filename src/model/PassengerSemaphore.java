/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kyles
 */
public class PassengerSemaphore implements Runnable{
    private StationMonitorSemaphore originStation;
    private StationMonitorSemaphore currentStation;
    private StationMonitorSemaphore destinationStation;
    private Semaphore stationLock;
    private Semaphore trainArrived;
    private Semaphore allSeated;
    private Semaphore trainReadyToBoard;
    
    public PassengerSemaphore(StationMonitorSemaphore start, StationMonitorSemaphore end){
        this.originStation = start;
        this.currentStation = start;
        this.destinationStation = end;
        this.stationLock = start.getStationLock();
        this.trainArrived = start.getTrainArrived();
        this.allSeated = start.getAllSeated();
        this.trainReadyToBoard = start.getTrainReadyToBoard();
    }

    public StationMonitorSemaphore getDestinationStation() {
        return destinationStation;
    }
    
    public StationMonitorSemaphore getOriginStation() {
        return originStation;
    }
    
    public void station_wait_for_train() throws InterruptedException{
        
        // wait for trainArrived signal
        // after receiving signal, remove from waiting, unlock, proceed to board
        try {
            // locks and adds itself to its station's waiting array
            //stationLock.acquire();
            originStation.addWaiting(this);
            System.out.println("passenger at station #" + originStation.getStationNumber()+" waiting for train...");
            //trainArrived.acquire();
            System.out.println("passenger is signalled by train");
            
        } catch (Exception ex) {
            Logger.getLogger(Passenger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //stationLock.release();
            station_on_board();
        }
    }
    
    public void station_on_board() throws InterruptedException{
        // locks and boards train
        // signals allSeated if no more waiting or train is full
        // signal allSeated is sign for train to move on to next station
        try {
            trainReadyToBoard.acquire();
            System.out.println("bording train..." + this);
            originStation.boardPassenger(this);
            originStation.removeWaiting(this);
            if (originStation.getFreeTrainSeats() == 0 || originStation.getWaitingPassengers() == 0) {
                System.out.println("Sir the train is full sir");
                allSeated.release();
            }
        } finally {
            trainReadyToBoard.release();
        }
    }
    
    public void moveCurStation(StationMonitorSemaphore s) {
        this.currentStation = s;
    }

    @Override
    public void run() {
        try {
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
        } catch (InterruptedException ex) {
            Logger.getLogger(Passenger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
