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
public class Station {
    private ArrayList<Passenger> passengers;
    private Train currentTrain;
    private int stationNumber;
    
    private final Lock stationLock = new ReentrantLock();
    private final Condition cond = stationLock.newCondition();
    
    Station(int num){
        this.stationNumber = num;
        this.passengers = new ArrayList<>();
        this.currentTrain = null;
    }
    
    /*Passenger arrives at the station*/
    public void addPassenger(Station destinationStation){
        this.passengers.add(new Passenger(this, destinationStation));
    }
    
    /*Load the train full of passengers*/
    public void loadTrain(){
//        stationLock.lock();
//        try{
            for(Passenger p: passengers){
                /*Put a break here for when the train is full*/
                if(this.currentTrain.isFull())
                    break;
                this.currentTrain.boardPassenger(p);
                this.passengers.remove(p);
            }
//        }catch(Exception e)
//        {
//         e.printStackTrace();
//        } finally{
//          stationLock.unlock();
//        }
     }
    
    
    public void trainArrives(Train t){
        this.currentTrain = t;
    }
    
    public void trainDeparts(){
        this.currentTrain = null;
    }
    
    public Lock getLock(){
        return stationLock;
    }
    
    public Condition getCond(){
        return cond;
    }
    
    public void run() throws InterruptedException{
        while(true){
            
            if(currentTrain == null){
                ;//do nothing
            } else {
                stationLock.lock();
                //Signal that you are using the train
                currentTrain.getCond1().signal();
                
                cond.await();
                
                try{
                    //When a train arrives at the station, drop off passengers
                    this.currentTrain.dropOffPassengers();

                    //Afterwards, load the train
                    loadTrain();

                    //When it's done loading, signal it is done using the train
//                    currentTrain.getLock().unlock();
                    
                    //Set currentTrain to null
                    this.trainDeparts();
                    currentTrain.getCond2().signal();
                }catch(Exception e){
                    e.printStackTrace();
                } finally{
                    //Station is free to be boarded
                    stationLock.unlock();
                }
            }
        }
    }
}
