/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import model.Passenger;
import model.StationMonitor;
import model.StationMonitorSemaphore;
import model.Train;
import sun.applet.Main;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;
import view.ControlPane;
import view.Feed;
import view.PassengerGUI;
import view.SimulationPane;
import view.TrainGUI;

/**
 *
 * @author ianona
 */
public class SimulationController {

    private StationMonitor[] stations;
    private StationMonitorSemaphore[] stations_semaphore;
    private Feed feed;
    private SimulationPane view;
    private ControlPane control_view;
    private char mode;

    public SimulationController(Feed f) {
        stations = new StationMonitor[8];
        stations_semaphore = new StationMonitorSemaphore[8];
        this.feed = f;
    }

    public void attach(SimulationPane v) {
        view = v;
    }

    public void attach(ControlPane c) {
        control_view = c;
    }

    public void setMode(char c) {
        this.mode = c;
    }

    public char getMode() {
        return mode;
    }

    public void initialize() {
        if (mode == 'l') {
            for (int i = 0; i < 8; i++) {
                StationMonitor sm = new StationMonitor(i + 1, feed);
                Thread t = new Thread(sm);
                stations[i] = sm;
                t.start();
            }
        } else if (mode == 's') {
            for (int i = 0; i < 8; i++) {
                StationMonitorSemaphore sm = new StationMonitorSemaphore(i + 1, feed);
                Thread t = new Thread(sm);
                stations_semaphore[i] = sm;
                t.start();
            }
        }
    }

    public StationMonitor getStation(int n) {
        return stations[n - 1];
    }

    public StationMonitorSemaphore getStation_Semaphore(int n) {
        return stations_semaphore[n - 1];
    }

    public StationMonitor[] getStations() {
        return stations;
    }

    public StationMonitorSemaphore[] getStations_Semaphore() {
        return stations_semaphore;
    }

    public Feed getFeed() {
        return feed;
    }

    public PassengerGUI addPassengerGUI() {
        return view.addPassenger();
    }

    public TrainGUI addTrainGUI() {
        return view.addTrain();
    }

    public void boardTrain(int trainNum) {
        control_view.boardTrain(trainNum);
    }

    public void exitTrain(int trainNum) {
        control_view.exitTrain(trainNum);
    }
}
