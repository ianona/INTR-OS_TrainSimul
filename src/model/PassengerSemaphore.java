/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.SimulationController;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.AbsolutePositions;
import view.Feed;
import view.PassengerGUI;

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
    
    private Feed feed;
    private int trainNum;

    private SimulationController controller;
    private PassengerGUI sprite;
    private AbsolutePositions locator = new AbsolutePositions();
    
    public PassengerSemaphore(StationMonitorSemaphore start, StationMonitorSemaphore end, Feed feed, SimulationController sc){
        this.originStation = start;
        this.currentStation = start;
        this.destinationStation = end;
        this.stationLock = start.getStationLock();
        this.trainArrived = start.getTrainArrived();
        this.allSeated = start.getAllSeated();
        this.trainReadyToBoard = start.getTrainReadyToBoard();
        
        this.feed = feed;
        this.controller = sc;
        
        this.sprite = controller.addPassengerGUI();
        switch (start.getStationNumber()) {
            case 1:
                sprite.animate(locator.station1_waiting);
                break;
            case 2:
                sprite.animate(locator.station2_waiting);
                break;
            case 3:
                sprite.animate(locator.station3_waiting);
                break;
            case 4:
                sprite.animate(locator.station4_waiting);
                break;
            case 5:
                sprite.animate(locator.station5_waiting);
                break;
            case 6:
                sprite.animate(locator.station6_waiting);
                break;
            case 7:
                sprite.animate(locator.station7_waiting);
                break;
            case 8:
                sprite.animate(locator.station8_waiting);
                break;
        }
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
            feed.update("[PASSENGER] At station " + originStation.getStationNumber() + ", waiting for train...");
            //trainArrived.acquire();
            //System.out.println("passenger is signalled by train");
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
            
            feed.update("[PASSENGER] Boarding train #" + originStation.getStationNumber());
            switch (originStation.getStationNumber()) {
                case 1:
                    sprite.board(locator.station1_waiting);
                    break;
                case 2:
                    sprite.board(locator.station2_waiting);
                    break;
                case 3:
                    sprite.board(locator.station3_waiting);
                    break;
                case 4:
                    sprite.board(locator.station4_waiting);
                    break;
                case 5:
                    sprite.board(locator.station5_waiting);
                    break;
                case 6:
                    sprite.board(locator.station6_waiting);
                    break;
                case 7:
                    sprite.board(locator.station7_waiting);
                    break;
                case 8:
                    sprite.board(locator.station8_waiting);
                    break;
            }
            sprite.getGui_lock().lock();
            sprite.getGui_cond().await();
            sprite.getGui_lock().unlock();
            trainNum = originStation.getTrain().getTrainNumber();
            controller.boardTrain(trainNum);
            
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
    
    public void disembark() {
        try {
            switch (destinationStation.getStationNumber()) {
                case 1:
                    sprite.exit(locator.station1);
                    break;
                case 2:
                    sprite.exit(locator.station2);
                    break;
                case 3:
                    sprite.exit(locator.station3);
                    break;
                case 4:
                    sprite.exit(locator.station4);
                    break;
                case 5:
                    sprite.exit(locator.station5);
                    break;
                case 6:
                    sprite.exit(locator.station6);
                    break;
                case 7:
                    sprite.exit(locator.station7);
                    break;
                case 8:
                    sprite.exit(locator.station8);
                    break;
            }
            sprite.getGui_lock().lock();
            sprite.getGui_cond().await();
            sprite.getGui_lock().unlock();
            controller.exitTrain(trainNum);
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Passenger.class.getName()).log(Level.SEVERE, null, ex);
        }
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
