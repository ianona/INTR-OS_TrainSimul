/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import view.Feed;

/**
 *
 * @author kyles
 */
public class StationMonitorSemaphore implements Runnable{
    private int stationNumber;
    private Semaphore stationLock;
    private Semaphore trainArrived;
    private Semaphore allSeated;
    private Semaphore trainInStation;
    private Semaphore trainReadyToBoard;
    private Semaphore doneUsingTrain;   
    private TrainSemaphore train;
    private ArrayList<PassengerSemaphore> waitingPassengers;
    private Feed feed;
    
    public StationMonitorSemaphore(int num, Feed f){
        this.stationNumber = num;
        this.stationLock = new Semaphore(1,true);
        this.trainArrived = new Semaphore(0,true);
        this.allSeated = new Semaphore(0,true);
        this.trainInStation = new Semaphore(0,true);
        this.doneUsingTrain = new Semaphore(0, true);
        this.trainReadyToBoard = new Semaphore(0, true);
        this.train = null;
        this.waitingPassengers = new ArrayList<>();
        this.feed = f;
    }
    
    public void station_load_train() throws InterruptedException {
        // loads train by signalling all waiting passengers that trainArrived
        // waits for an allSeated signal to know when to release train to next station
        // once signal is received, moves train to next station and sets current train to null
        try{
            
            if (getWaitingPassengers() > 0 && getFreeTrainSeats() > 0) {
                System.out.println("Station #"+stationNumber +" FILLING TRAIN #" + train.getTrainNumber());
                trainReadyToBoard.release();
                System.out.println("All aboard the train @" + stationNumber);
                allSeated.acquire();
            }
            
            System.out.println("bye bye train");
            doneUsingTrain.release();
            this.train = null;
            System.out.println("Station #" + stationNumber +" freed it's train");
        } finally {
            //stationLock.release();
        }
    }

    public Semaphore getTrainReadyToBoard() {
        return trainReadyToBoard;
    }
    
    public void boardPassenger(PassengerSemaphore p){
        this.train.occupySeat(p);
    }
    
    /*
    public void releasePassenger(PassengerSemaphore p){
        this.train.freeSeat(p);
    }
    */
    
    public int getFreeTrainSeats(){
        return this.train.getEmptySeats();
    }
    
    public int getWaitingPassengers(){
        return this.waitingPassengers.size();
    }
    
    public void addWaiting(PassengerSemaphore p){
        waitingPassengers.add(p);
    }
    
    public void removeWaiting(PassengerSemaphore p){
        waitingPassengers.remove(p);
    }
    
    public TrainSemaphore getTrain(){
        return train;
    }
    
    public Semaphore getStationLock() {
        return stationLock;
    }
    
    public Semaphore getTrainArrived() {
        return trainArrived;
    }

    public Semaphore getAllSeated() {
        return allSeated;
    }
    
    public Semaphore getTrainInStation() {
        return trainInStation;
    }
    
    public Semaphore getDoneUsingTrain() {
        return doneUsingTrain;
    }
    
    public void setTrain(TrainSemaphore t){
        this.train = t;
    }
    
    public int getStationNumber() {
        return stationNumber;
    }

    @Override
    public void run() {
        feed.update("[STATION] Station #" + stationNumber + " is running...");
        while (true) {
            try {
                /*
                if (this.train != null) {
                System.out.println("train "+ train.getTrainNumber() + " arrived at station" + this.getStationNumber());
                station_load_train();
                }
                */
                
                // locks and waits for train to arrive in station
                // after receiving trainInStation signal, proceeds to load train
                //stationLock.acquire();
                trainInStation.acquire();
                feed.update("[STATION] Train #" + train.getTrainNumber() + " arrived at station " + this.getStationNumber());
                station_load_train();
            } catch (InterruptedException ex) {
                System.out.println("no train yet :(");
            } finally {
                feed.update("[STATION] Station #" + stationNumber + " is free. NEXT!");
                stationLock.release();
            }
        }
    }
}
