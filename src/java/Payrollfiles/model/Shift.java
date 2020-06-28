
package Payrollfiles.model;

import java.time.LocalTime;

/**
 * The Shift class is an abstraction of a shift. This class is used to determine 
 * how the starting and stopping time for a particular shift and the rules that
 * determine how to adjust the punchs' timestamps.
 * @author Sabin Banjara
 */

public class Shift {
    
    private String description; 
    private LocalTime startingTime;
    private LocalTime stoppingTime;
    private LocalTime lunchStart;
    private LocalTime lunchStop;
    
    private int lunchDeduct;
    private int interval;
    private int gracePeriod;
    private int dock;
    
    
    public Shift(String description, int startHour, int startMin,int interval, int gracePeriod,
            int dock, int stopHour, int stopMin, int lunchStartHour, int lunchStartMin,
            int lunchStopHour, int lunchStopMin, int lunchDeduct) {
        
        this.description = description;
        this.startingTime = LocalTime.of(startHour, startMin);
        this.stoppingTime = LocalTime.of(stopHour, stopMin);
        this.lunchStart = LocalTime.of(lunchStartHour, lunchStartMin);
        this.lunchStop = LocalTime.of(lunchStopHour, lunchStopMin);
        this.lunchDeduct = lunchDeduct;
        this.interval = interval;
        this.gracePeriod = gracePeriod;
        this.dock = dock;
        
    }
    
    public LocalTime getStartingTime() {
        return startingTime;
    }
    
    public int getStartingTimeHour() {
        return getStartingTime().getHour();
    }

    public int getStartingTimeMinutes() {
        return getStartingTime().getMinute();
    }
    
    public LocalTime getStoppingTime() {
        return stoppingTime;
    }
    
    public int getStoppingTimeHour() {
        return getStoppingTime().getHour();
    }
    
    public int getStoppingTimeMinutes() {
        return getStoppingTime().getMinute();
    }
    
    public LocalTime getLunchStart() {
        return lunchStart;
    }
    
    public LocalTime getLunchStop() {
        return lunchStop;
    }
    
    public int getInterval() {
        return interval;
    }
    
    /**
     * Fetches the time that should be deducted for a lunch break for a specific
     * shift
     * @return the deduct time for lunch break as an int
     */
    public int getlunchDeduct() {
        return lunchDeduct;
    }
    

    /**
     * Fetches the grace period for a specific shift which will determine if you
     * will be too late or early when clocking in or clocking out
     * @return the grace period for checking in late or checking out early as an
     * int
     */

    public int getGracePeriod() {
        return gracePeriod;
    }
    

    /**
     * Fetches the time after grace period known as the dock which will push you
     * forwards or backwards in the interval round
     * @return the time after grace period that will push you forward or
     * backwards in the interval round as an int
     */

    public int getDock() {
        return dock;
    }
    
    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }

    public void setStoppingTime(LocalTime stoppingTime) {
        this.stoppingTime = stoppingTime;
    }

    public void setLunchStart(LocalTime lunchStart) {
        this.lunchStart = lunchStart;
    }

    public void setLunchStop(LocalTime lunchStop) {
        this.lunchStop = lunchStop;
    }

    public void setLunchDeduct(int lunchDeduct) {
        this.lunchDeduct = lunchDeduct;
    }


    /**
     * Sets the interval of time you have before and after a specific shift
     * to determine the time that should be pushed to the beginning or end of a 
     * shift
     * @param interval an int that represents the interval time of a shift
     */

    public void setInterval(int interval) {
        this.interval = interval;
    }


    /**
     * Sets the grace period for a specific shift which will determine if you
     * will be too late or early when clocking in or clocking out
     * @param gracePeriod an int that represents the grace period of a shift
     */

    public void setGracePeriod(int gracePeriod) {
        this.gracePeriod = gracePeriod;
    }


    public void setDock(int dock) {
        this.dock = dock;
    }
    
    public int totalShiftHours() {
        
       int totalStartTimeMinutes = (getStartingTime().getHour()*60) + getStartingTime().getMinute();
       int totalStopTimeMinutes = (getStoppingTime().getHour()*60) + getStoppingTime().getMinute();
       return (totalStopTimeMinutes - totalStartTimeMinutes);
       
    }
    
    public int totalLunchTime() {
        
        int totalLunchStartMinutes = (getLunchStart().getHour()*60) + getLunchStart().getMinute();
        int totalLunchStopMinutes = (getLunchStop().getHour()*60) + getLunchStop().getMinute();
        return (totalLunchStopMinutes - totalLunchStartMinutes);
        
    }
    
    @Override
    public String toString() {
        
       StringBuilder s = new StringBuilder();
       s.append(description).append(": ").append(startingTime.toString()).append(" - ");
       s.append(stoppingTime.toString()).append(" (").append(totalShiftHours()).append(" minutes); Lunch: ");
       s.append(lunchStart.toString()).append(" - ").append(lunchStop.toString()).append(" (");
       s.append(totalLunchTime()).append(" minutes)");
       
       return s.toString();
        
    }   
    
}