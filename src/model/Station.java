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
public class Station {
    private ArrayList<Passenger> passengers;
    private Train currentTrain;
    private int stationNumber;
    
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
        for(Passenger p: passengers){
            /*Put a break here for when the train is full*/
            this.currentTrain.boardPassenger(p);
            this.passengers.remove(p);
        }
    }
    
    public void trainArrives(Train t){
        this.currentTrain = t;
    }
    
    public void trainDeparts(){
        this.currentTrain = null;
    }
}
