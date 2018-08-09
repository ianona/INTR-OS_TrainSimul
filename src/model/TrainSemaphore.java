/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.SimulationController;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.AbsolutePositions;
import view.Feed;
import view.TrainGUI;

/**
 *
 * @author kyles
 */
public class TrainSemaphore implements Runnable{
    private int emptySeats;
    private StationMonitorSemaphore station;
    private ArrayList<PassengerSemaphore> passengers;
    private StationMonitorSemaphore[] allStations;
    private int trainNum;
    private int curStation;
    private Feed feed;
    private SimulationController controller;

    private TrainGUI trainGUI;
    private AbsolutePositions locator = new AbsolutePositions();
    
    public TrainSemaphore(int capacity, StationMonitorSemaphore[] stations, int trainNum, Feed feed, SimulationController sc) {
        this.emptySeats = capacity;
        this.station = null;
        this.curStation = -1;
        
        this.passengers = new ArrayList<>();
        this.allStations = stations;
        this.trainNum = trainNum;
        
        this.feed = feed;
        this.controller = sc;
        this.trainGUI = controller.addTrainGUI();
    }
    
    public int getEmptySeats(){
        return emptySeats;
    }
    
    public int getTrainNumber(){
        return trainNum;
    }
    
    public void occupySeat(PassengerSemaphore p){
        // System.out.println("passenger boarded train #" + trainNum);
        passengers.add(p);
        emptySeats -= 1;
        //System.out.println(passengers);
    }
    
    /*
    public void freeSeat(PassengerSemaphore p){
        // System.out.println("passenger leaving train #" + trainNum);
        passengers.remove(p);
        emptySeats += 1;
    }
*/
    
    public void moveStation() throws InterruptedException{
        // moves and sets current station to next station
        curStation = (curStation + 1) % 8;
        while (allStations[curStation].getTrain() != null){
            feed.update("[TRAIN] Train #" + trainNum + " is waiting");
        }
        feed.update("[TRAIN] Train #" + trainNum + " moving to station " + (curStation + 1));
        switch (curStation + 1) {
            case 1:
                trainGUI.animate(locator.station8_1);
                Thread.sleep(2000);
                trainGUI.animate(locator.station1);
                break;
            case 2:
                trainGUI.animate(locator.station2);
                break;
            case 3:
                trainGUI.animate(locator.station3);
                break;
            case 4:
                trainGUI.animate(locator.station3_1);
                Thread.sleep(500);
                trainGUI.animate(locator.station4);
                break;
            case 5:
                trainGUI.animate(locator.station4_1);
                Thread.sleep(1000);
                trainGUI.animate(locator.station5);
                break;
            case 6:
                trainGUI.animate(locator.station6);
                break;
            case 7:
                trainGUI.animate(locator.station7);
                break;
            case 8:
                trainGUI.animate(locator.station8);
                break;
        }
        trainGUI.getGui_lock().lock();
        trainGUI.getGui_cond().await();
        trainGUI.getGui_lock().unlock();
        
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
                    passengers.get(i).disembark();
                    passengers.remove(i);
                    emptySeats += 1;
                }

            }
        
            station.setTrain(this);
            station.getTrainInStation().release();
        } finally {
            //station.getStationLock().release();
            station.getDoneUsingTrain().acquire();
        }
    }

    @Override
    public void run() {
        try {
            // initially moves station from -1 to 0
            // implies every train spawns at first station
            while(true) {
                Thread.sleep(1000);
                moveStation();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Train.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
