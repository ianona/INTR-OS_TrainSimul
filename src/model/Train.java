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
 * @author dlsza
 */
public class Train {
    private int freeSeats;
    private Station currentStation;
    private ArrayList<Passenger> passengers;
    private ArrayList<Station> allStations;
    private int nextStation;
    private final Lock trainReady = new ReentrantLock();
    private final Condition cond1 = trainReady.newCondition();
    private final Condition cond2 = trainReady.newCondition();
    
    public Train(int capacity, ArrayList<Station> stations) {
        this.freeSeats = capacity;
        this.currentStation = null;
        this.passengers = new ArrayList<>();
        this.allStations = stations;
        this.nextStation = 0;
    }
            
    public void boardPassenger(Passenger p){
        passengers.add(p);
        freeSeats--;
    }
    
    public void dropOffPassengers(){
        for(Passenger p: passengers){
            /*Passenger leaves if current station is their destination*/
            if(p.getDestinationStation().equals(currentStation)){
                passengers.remove(p);
                freeSeats++;
            }
        }
    }
    
    public Boolean isFull(){
        if(freeSeats == 0){
            return true;
        } else {
            return false;
        }
    }
    
    /*Might have to change this in the future*/
    public void arriveAt(Station s){
        this.currentStation = s;
        this.currentStation.trainArrives(this);
    }
    
    /*Not sure if this function is necessary*/
    public void leaveStation(){
        this.currentStation.trainDeparts();
        this.currentStation = null;
    }
    
    public Condition getCond1(){
        return cond1;
    }
    

    public void run() throws InterruptedException{
        //Train is not ready to be boarded (in transit)
        trainReady.lock();
        cond1.await();
        try{
            //Waits for next station to be ready to receive it
            allStations.get(nextStation).getCond().signal();
            
        } catch(Exception e) {
            
        } finally {
        //Arrives to the next station
        this.arriveAt(allStations.get(nextStation));
            
        //Sets the destination for the next station
        this.nextStation = (this.nextStation+1)%8;    
        
        //Signals that the train is ready to be boarded
        trainReady.unlock();
        }
    }
}
