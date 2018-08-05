/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author kyles
 */
public class StationMonitorSemaphore implements Runnable{
    private int stationNumber;
    private Lock stationLock;
    private Condition trainArrived;
    private Condition allSeated;
    private Condition trainInStation;
    private Condition waitForFree;
    private Train train;
    private ArrayList<Passenger> waitingPassengers;
    
    public StationMonitorSemaphore(int num){
        this.stationNumber = num;
        this.stationLock = new ReentrantLock();
        this.trainArrived = stationLock.newCondition();
        this.allSeated = stationLock.newCondition();
        this.trainInStation = stationLock.newCondition();
        this.waitForFree = stationLock.newCondition();
        this.train = null;
        this.waitingPassengers = new ArrayList<>();
    }
    
    public void station_load_train() {
        stationLock.lock();
        
        // loads train by signalling all waiting passengers that trainArrived
        // waits for an allSeated signal to know when to release train to next station
        // once signal is received, moves train to next station and sets current train to null
        try{
            if (getWaitingPassengers() > 0 && getFreeTrainSeats() > 0) {
                System.out.println("STATION "+stationNumber +" FILLING TRAIN " + train.getTrainNumber());
                trainArrived.signalAll();
                
                try {
                    allSeated.await();
                } catch (InterruptedException ex) {
                    System.out.println("ERROR THINGO " + ex);
                }
            }
            
            System.out.println("bye bye train");
            this.train.moveStation();
            this.train = null;
            this.waitForFree.signal();
        } finally {
            stationLock.unlock();
        }
    }
    
    public void boardPassenger(Passenger p){
        this.train.occupySeat(p);
    }
    
    public void releasePassenger(Passenger p){
        this.train.freeSeat(p);
    }
    
    public int getFreeTrainSeats(){
        return this.train.getEmptySeats();
    }
    
    public int getWaitingPassengers(){
        return this.waitingPassengers.size();
    }
    
    public void addWaiting(Passenger p){
        waitingPassengers.add(p);
    }
    
    public void removeWaiting(Passenger p){
        //System.out.println("before removing waiting... " + waitingPassengers.size());
        waitingPassengers.remove(p);
        //System.out.println("after removing waiting... " + waitingPassengers.size());
    }
    
    public Train getTrain(){
        return train;
    }
    
    public Lock getStationLock() {
        return stationLock;
    }
    
    public Condition getWaitForFree(){
        return waitForFree;
    }
    
    public Condition getTrainArrived() {
        return trainArrived;
    }

    public Condition getAllSeated() {
        return allSeated;
    }
    
    public Condition getTrainInStation() {
        return trainInStation;
    }
    
    public void setTrain(Train t){
        this.train = t;
    }
    
    public int getStationNumber() {
        return stationNumber;
    }

    @Override
    public void run() {
        System.out.println("station " + stationNumber + " is running...");
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
                stationLock.lock();
                trainInStation.await();
                System.out.println("train "+ train.getTrainNumber() + " arrived at station" + this.getStationNumber());
                station_load_train();
            } catch (InterruptedException ex) {
                System.out.println("no train yet :(");
            } finally {
                stationLock.unlock();
            }
        }
    }
}
