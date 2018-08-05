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
 * @author dlsza
 */
public class Train implements Runnable{
    private int emptySeats;
    private StationMonitor station;
    private ArrayList<Passenger> passengers;
    private StationMonitor[] allStations;
    private int trainNum;
    private int curStation;
    
    public Train(int capacity, StationMonitor[] stations, int trainNum) {
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
    
    public void occupySeat(Passenger p){
        System.out.println("passenger boarded train" + trainNum);
        passengers.add(p);
        emptySeats -= 1;
    }
    
    public void freeSeat(Passenger p){
        System.out.println("passenger leaving train " + trainNum);
        passengers.remove(p);
        emptySeats += 1;
    }
    
    public void moveStation() throws InterruptedException{
        // moves and sets current station to next station
        curStation = (curStation + 1) % 8;
        while (allStations[curStation].getTrain() != null){
            System.out.println("Train #" + trainNum + " is waiting");
        }
        System.out.println("Train #"+ trainNum + " moving station to " + (curStation+1));
        station = allStations[curStation];
        
        try {
            // locks and sets parent station's current train to this
            // signals parent station that trainInStation so it can load it
            station.getStationLock().acquire();
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
            station.getStationLock().release();
        }
    }

    @Override
    public void run() {
        try {
            // initially moves station from -1 to 0
            // implies every train spawns at first station
            moveStation();
        } catch (InterruptedException ex) {
            Logger.getLogger(Train.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
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
            // Passenger leaves if current station is their destination
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
    
    // Might have to change this in the future
    public void arriveAt(Station s){
        this.currentStation = s;
        this.currentStation.trainArrives(this);
    }
    
    // Not sure if this function is necessary
    public void leaveStation(){
        this.currentStation.trainDeparts();
        this.currentStation = null;
    }
    
    public Condition getCond1(){
        return cond1;
    }
    

    public void run() throws InterruptedException{
        // Train is not ready to be boarded (in transit)
        trainReady.lock();
        cond1.await();
        try{
            // Waits for next station to be ready to receive it
            allStations.get(nextStation).getCond().signal();
            
        } catch(Exception e) {
            
        } finally {
        // Arrives to the next station
        this.arriveAt(allStations.get(nextStation));
            
        // Sets the destination for the next station
        this.nextStation = (this.nextStation+1)%8;    
        
        // Signals that the train is ready to be boarded
        trainReady.unlock();
        }
    }
    */
}
