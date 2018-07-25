/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author dlsza
 */
public class Passenger {
    private Station originStation;
    private Station destinationStation;
    
    Passenger(Station start, Station end){
        this.originStation = start;
        this.destinationStation = end;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }
    
    /*Should we add a board function here? Or should we keep it as the load passengers in station*/
}
