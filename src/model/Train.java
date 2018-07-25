/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author dlsza
 */
public class Train {
    private int freeSeats;
    private Station currentStation;
    private ArrayList<Passenger> passengers;

    public Train(int capacity) {
        this.freeSeats = capacity;
        this.currentStation = null;
        this.passengers = new ArrayList<>();
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
}
