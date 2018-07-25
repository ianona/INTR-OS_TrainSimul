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
    ArrayList<Passenger> passengers;
    int stationNumber;
    
    Station(int num){
        this.stationNumber = num;
        this.passengers = new ArrayList<>();
    }
}
