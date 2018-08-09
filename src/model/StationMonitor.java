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
import view.Feed;

/**
 *
 * @author ianona
 */
public class StationMonitor implements Runnable {

    private int stationNumber;
    private Lock stationLock;
    private Condition trainArrived;
    private Condition allSeated;
    private Condition trainInStation;
    private Condition doneUsingTrain;
    private Train train;
    private ArrayList<Passenger> waitingPassengers;
    private Feed feed;

    public StationMonitor(int num, Feed f) {
        this.stationNumber = num;
        this.stationLock = new ReentrantLock();
        this.trainArrived = stationLock.newCondition();
        this.allSeated = stationLock.newCondition();
        this.trainInStation = stationLock.newCondition();
        this.doneUsingTrain = stationLock.newCondition();
        this.train = null;
        this.waitingPassengers = new ArrayList<>();
        this.feed = f;
    }

    public void station_load_train() {
        stationLock.lock();

        // loads train by signalling all waiting passengers that trainArrived
        // waits for an allSeated signal to know when to release train to next station
        // once signal is received, moves train to next station and sets current train to null
        try {
            if (getWaitingPassengers() > 0 && getFreeTrainSeats() > 0) {
                feed.update("[STATION] Station #" + stationNumber + " filling train #" + train.getTrainNumber());
                trainArrived.signalAll();

                try {
                    allSeated.await();
                } catch (InterruptedException ex) {
                    feed.update("ERROR THINGO " + ex);
                }
            }

            doneUsingTrain.signal();
            this.train = null;
            feed.update("[STATION] Station #" + stationNumber + " freed it's train");

        } finally {
            stationLock.unlock();
        }
    }

    public void boardPassenger(Passenger p) {
        this.train.occupySeat(p);
    }

    /*
    public void releasePassenger(Passenger p){
        this.train.freeSeat(p);
    }
     */
    
    public int getFreeTrainSeats() {
        return this.train.getEmptySeats();
    }

    public int getWaitingPassengers() {
        return this.waitingPassengers.size();
    }

    public void addWaiting(Passenger p) {
        waitingPassengers.add(p);
    }

    public void removeWaiting(Passenger p) {
        waitingPassengers.remove(p);
    }

    public Train getTrain() {
        return train;
    }

    public Lock getStationLock() {
        return stationLock;
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

    public Condition getDoneUsingTrain() {
        return doneUsingTrain;
    }

    public void setTrain(Train t) {
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
                // locks and waits for train to arrive in station
                // after receiving trainInStation signal, proceeds to load train
                stationLock.lock();
                trainInStation.await();
                feed.update("[STATION] Train #" + train.getTrainNumber() + " arrived at station " + this.getStationNumber());
                station_load_train();
            } catch (InterruptedException ex) {
                feed.update("no train yet :(");
            } finally {
                feed.update("[STATION] Station #" + stationNumber + " is free. NEXT!");
                stationLock.unlock();
            }
        }
    }
}
