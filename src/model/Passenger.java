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
    Station originStation;
    Station destinationStation;
    
    Passenger(Station start, Station end){
        this.originStation = start;
        this.destinationStation = end;
    }
}
