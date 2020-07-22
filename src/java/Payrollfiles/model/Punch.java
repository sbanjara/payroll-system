
package Payrollfiles.model;

import java.sql.Timestamp;
import java.util.*;
import java.text.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * The Punch class is an abstraction of an employee's time punch. This class is used
 * to store the information regarding the employee's time punches and to adjust 
 * those punches according to the employee's shift and the company rules.
 * @author Sabin Banjara
 */

public class Punch {
    
    private String badgeID;
    private int terminalID;
    private int punchTypeID;
    private int id;
    private Timestamp originalTimeStamp;
    private Timestamp adjustedTimeStamp;
    private String adjustMessage;
    
    public Punch (int id, int terminalID, String badgeID, Timestamp originalTimeStamp, int punchTypeID) {
        
        if(id >= 0){this.id = id;}
        this.terminalID = terminalID;
        this.badgeID = badgeID;
        this.originalTimeStamp = originalTimeStamp;
        this.punchTypeID = punchTypeID;
        
    }

    public Punch(Badge b, int terminalID, int punchTypeID){
        this(-1, terminalID, b.getBadgeid(), new Timestamp(new GregorianCalendar().getTimeInMillis()), punchTypeID);
    }
    
    public String printOriginalTimestamp() {
        
        String punchResults = "";
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(originalTimeStamp.getTime());
        
        switch(this.punchTypeID){
            case 0:
                punchResults = "CLOCKED OUT:";
                break;
            case 1:
                punchResults = "CLOCKED IN:";
                break;
            case 2:
                punchResults = "TIMED OUT:";
                break;
            default:
                System.out.println("ERROR");
        }   
        
        /*
        A pattern is created for the format according to the documentation on SimpleDateFormat. 
        Then an output string is constructed using the cal.getTime(). Then the output string is built,
        .toUppercase() is used to ensure the day of the week is capitalized.
        */
        
        String pattern = "EEE MM/dd/yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String formattedDate = sdf.format(cal.getTime()).toUpperCase();
        
        String originalTimestamptoString = "#" + getBadgeid() + " " + punchResults + " " + formattedDate;
            
        return originalTimestamptoString;
         
    }
    
    public String printAdjustedTimestamp() {
        String punchResults = "";
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(adjustedTimeStamp.getTime());
        
        switch(this.punchTypeID){
            case 0:
                punchResults = "CLOCKED OUT:";
                break;
            case 1:
                punchResults = "CLOCKED IN:";
                break;
            case 2:
                punchResults = "TIMED OUT:";
                break;
          
        }   
        
        String pattern = "EEE MM/dd/yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String formattedDate = sdf.format(cal.getTime()).toUpperCase();

        String originalTimestamptoString = "#" + getBadgeid() + " " + punchResults + " " + formattedDate + " (" + adjustMessage + ")";
            
        return originalTimestamptoString;
    }
        
    public String getBadgeid() {
        return badgeID;
    }

    public int getTerminalid() {
        return terminalID;
    }

    public int getPunchtypeid() {
        return punchTypeID;
    }

    public int getId() {
        return id;
    }

    public long getOriginaltimestamp() {
        return originalTimeStamp.getTime();

    }
    
    public String getAdjustMessage(){
        return adjustMessage;

    }
    
    public Timestamp getOriginaltimestamp2() {
        return originalTimeStamp;
    }
    
    public Timestamp getAdjustedTimeStamp() {
        return adjustedTimeStamp;
    }
    
    public void setBadgeID(String badgeID) {
        this.badgeID = badgeID;
    }

    public void setTerminalID(int terminalID) {
        this.terminalID = terminalID;
    }

    public void setPunchTypeID(int punchTypeID) {
        this.punchTypeID = punchTypeID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOriginalTimeStamp(Timestamp originalTimeStamp) {
        this.originalTimeStamp = originalTimeStamp;
    }
    
    private Timestamp helperMethod1(LocalDateTime punch, int totalminutes) {
        punch = punch.withHour(totalminutes/60);
        punch = punch.withMinute(totalminutes%60);
        punch = punch.withSecond(0);
        Timestamp t = Timestamp.valueOf(punch);
        
        return t;
    }
    
    private Timestamp helperMethod2(LocalDateTime punch, LocalTime time) {
        punch = punch.withHour(time.getHour());
        punch = punch.withMinute(time.getMinute());
        punch = punch.withSecond(0);
        Timestamp t = Timestamp.valueOf(punch);
        
        return t;
    }
    

    /**
     *
     * @param s a Shift that represents the shift that should be adjusted
     */
    public void adjust(Shift s){
        
        LocalTime shiftStart = s.getStartingTime();
        LocalTime shiftStop = s.getStoppingTime();
        LocalTime lunchStart = s.getLunchStart();
        LocalTime lunchStop = s.getLunchStop();
        
        int gracePeriod = s.getGracePeriod();
        int interval = s.getInterval();
        int dock = s.getDock();
        int lunchDeduct = s.getlunchDeduct();
        
        LocalDateTime punchTimeStamp = originalTimeStamp.toLocalDateTime();
        LocalTime punchTime = punchTimeStamp.toLocalTime();
        int punchTimeSeconds =  punchTime.getSecond();
        int totalpunchTimeMinutes = punchTime.getMinute() + (punchTime.getHour()*60);
        int totalshiftStopMinutes = shiftStop.getMinute() + (shiftStop.getHour()*60);
        int totalshiftStartMinutes = shiftStart.getMinute() + (shiftStart.getHour()*60);
        boolean weekend = (punchTimeStamp.getDayOfWeek().toString().equals("SATURDAY") || 
                punchTimeStamp.getDayOfWeek().toString().equals("SUNDAY") );
        
        adjustedTimeStamp = Timestamp.valueOf(punchTimeStamp.withSecond(0));
        
        switch(this.punchTypeID){
            
            case 0:
                
                // CHECKS IF THE PUNCH IS CLOCKOUT FOR THE LUNCH BREAK
           
                if( punchTime.isAfter(shiftStart) && punchTime.isBefore(lunchStop) && (!weekend) ) {
                  
                    if( punchTime.isAfter(lunchStart) && punchTime.isBefore(lunchStop)) {
                        adjustMessage = "Lunch Start";
                        adjustedTimeStamp = helperMethod2(punchTimeStamp, lunchStart);                       
                    }
                    
                }
                
                else if(weekend) {
                    
                    if(punchTime.isBefore(shiftStop) ) {
                        
                        if( (totalshiftStopMinutes - totalpunchTimeMinutes) <= gracePeriod ) {
                            adjustMessage = "Shift Stop";
                            adjustedTimeStamp = helperMethod2(punchTimeStamp, shiftStop);
                        }
                        
                        else if ( (totalshiftStopMinutes - totalpunchTimeMinutes) > gracePeriod && 
                                (totalshiftStopMinutes - totalpunchTimeMinutes) <= dock ) {
                            adjustMessage = "Shift Dock";
                            totalshiftStopMinutes = totalshiftStopMinutes - dock;
                            adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStopMinutes);  
                        }
                        
                        else {
                            adjustMessage = "Interval Round";
                            int timeInterval = totalshiftStopMinutes - totalpunchTimeMinutes;
                            int quotient = timeInterval/interval;
                            int remainder = timeInterval%interval;
                            if(remainder <= 8) {
                                totalshiftStopMinutes = totalshiftStopMinutes - (quotient*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStopMinutes);
                            }
                            else {
                                totalshiftStopMinutes = totalshiftStopMinutes - ((quotient+1)*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStopMinutes);
                            }
                        }
                    }
                    
                    else if(punchTime.isAfter(shiftStop) ) { 
                        
                        int timeInterval = totalpunchTimeMinutes - totalshiftStopMinutes;
                        int quotient = timeInterval/interval;
                        int remainder = timeInterval%interval;
                        
                        if(timeInterval <= interval) {
                            
                            if( (remainder == 0) && punchTime.getSecond() < 60) {
                                adjustMessage = "None";
                            }
                            else {
                                adjustMessage = "Shift Stop";
                            }
                            totalpunchTimeMinutes = totalpunchTimeMinutes - (quotient*interval);
                            adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStopMinutes);
                        }
                        else {
                            adjustMessage = "Interval Round";
                            totalpunchTimeMinutes = totalpunchTimeMinutes - ((quotient+1)*interval);
                            adjustedTimeStamp = helperMethod1(punchTimeStamp, totalpunchTimeMinutes);
                        }
                        
                    }

                }
                
                
                //CHECKS IF THE PUNCH IS CLOCKOUT FOR THE SHIFT END
                
                else if ( punchTime.isAfter(lunchStop) && (!weekend) ) {
                    
                    //CHECKS IF THE PUNCH IS BEFORE THE SHIFT STOP
                    
                    if( punchTime.isBefore(shiftStop) ) {
                        
                        if( (totalshiftStopMinutes - totalpunchTimeMinutes) <= gracePeriod ) {
                            adjustMessage = "Shift Stop";
                            adjustedTimeStamp = helperMethod2(punchTimeStamp, shiftStop);
                        }
                        
                        else if ( (totalshiftStopMinutes - totalpunchTimeMinutes) > gracePeriod && 
                                (totalshiftStopMinutes - totalpunchTimeMinutes) <= dock ) {
                            adjustMessage = "Shift Dock";
                            totalshiftStopMinutes = totalshiftStopMinutes - dock;
                            adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStopMinutes);
                        }
                        
                        else {
                            adjustMessage = "Interval Round";
                            int timeInterval = totalshiftStopMinutes - totalpunchTimeMinutes;
                            int quotient = timeInterval/interval;
                            int remainder = timeInterval%interval;
                            if(remainder <= 8) {
                                totalshiftStopMinutes = totalshiftStopMinutes - (quotient*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStopMinutes);
                            }
                            else {
                                totalshiftStopMinutes = totalshiftStopMinutes - ((quotient+1)*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStopMinutes);
                            }
               
                        }
                        
                    }
                    
                    //CHECKS IF THE PUNCH IS AFTER THE SHIFT STOP
                    
                    else if( punchTime.isAfter(shiftStop) && (!weekend) ) {
                        
                        int timeInterval = totalpunchTimeMinutes - totalshiftStopMinutes;
                        int quotient = timeInterval/interval;
                        int remainder = timeInterval%interval;
                        
                        if(timeInterval <= interval) {
                            
                            if( (remainder == 0) && punchTime.getSecond() < 60) {
                                adjustMessage = "Shift Stop";
                            }
                            else {
                                adjustMessage = "Shift Stop";
                            }
                            totalpunchTimeMinutes = totalpunchTimeMinutes - (quotient*interval);
                            adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStopMinutes);
                        }
                        else {
                            if(remainder == 0 && punchTime.getSecond() < 60) {
                                adjustMessage = "None";
                            }
                            else {
                                adjustMessage = "Interval Round";
                                if(remainder <= 7)
                                    totalpunchTimeMinutes = totalpunchTimeMinutes - remainder;
                                else
                                    totalpunchTimeMinutes = totalpunchTimeMinutes + (interval-remainder);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalpunchTimeMinutes);
                            }
                            
                        }
                        
                    }
                                       
                }
                
                break;
                
                
            case 1:
                
                // CHECKS IF THE PUNCH IS CLOCKIN FOR THE LUNCH END
                
                if( punchTime.isAfter(lunchStart) && punchTime.isBefore(shiftStop) && (!weekend) ) {
                  
                    if( punchTime.isAfter(lunchStart) && punchTime.isBefore(lunchStop)) {
                        adjustMessage = "Lunch Stop";
                        adjustedTimeStamp = helperMethod2(punchTimeStamp, lunchStop);    
                    }
                    
                }
                
                // CHECKS IF THE GIVEN PUNCH IS CLOCK IN FOR THE SHIFT START
                
                else if( punchTime.isBefore(lunchStart) && (!weekend) ) {
                    
                    // CHECKS IF THE PUNCH IS BEFORE THE SHIFT START
                    
                    if( punchTime.isBefore(shiftStart) ) {
                        
                        int timeInterval = totalshiftStartMinutes - totalpunchTimeMinutes;
                        int quotient = timeInterval/interval;
                        int remainder = timeInterval%interval;
                                           
                        if(timeInterval <= interval) {
                            adjustMessage = "Shift Start";
                            adjustedTimeStamp = helperMethod2(punchTimeStamp, shiftStart);
                        }
                        else {
                           if(remainder == 0 && punchTime.getSecond() < 60) {
                                adjustMessage = "None";
                            }
                           else if(remainder <= 7) {
                                adjustMessage = "Interval Round";
                                totalpunchTimeMinutes = totalshiftStartMinutes - ((quotient)*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalpunchTimeMinutes);
                            }
                            else {
                                adjustMessage = "Interval Round";
                                totalpunchTimeMinutes = totalshiftStartMinutes - ((quotient+1)*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalpunchTimeMinutes);
                            }
                        }    

                    }
                    
                    // CHECKS IF THE PUNCH IS AFTER THE SHIFT START
                    
                    else if( punchTime.isAfter(shiftStart) ) {
                    
                        if( (totalpunchTimeMinutes - totalshiftStartMinutes) <= gracePeriod ) {
                            adjustMessage = "Shift Start";
                            adjustedTimeStamp = helperMethod2(punchTimeStamp, shiftStart);
                        }

                        else if ( (totalpunchTimeMinutes - totalshiftStartMinutes) <= dock ) {
                            adjustMessage = "Shift Dock";
                            totalshiftStartMinutes = totalshiftStartMinutes + dock;
                            adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStartMinutes);
                        }

                        else {
                            adjustMessage = "Interval Round";
                            int timeInterval = totalpunchTimeMinutes - totalshiftStartMinutes;
                            int quotient = timeInterval/interval;
                            int remainder = timeInterval%interval;
                            if(remainder <= 7) {
                                totalshiftStartMinutes = totalshiftStartMinutes + (quotient*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStartMinutes);
                            }
                            else {
                                totalshiftStartMinutes = totalshiftStartMinutes + ((quotient+1)*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStartMinutes);
                            }                        
                        }
                        
                    }
                    
                }
            
                else if(weekend) {
                    
                    if( punchTime.isBefore(shiftStart) ) {
                        
                        int timeInterval = totalshiftStartMinutes - totalpunchTimeMinutes;
                        int quotient = timeInterval/interval;
                        int remainder = timeInterval%interval;
                                           
                        if(timeInterval <= interval) {
                            adjustMessage = "Shift Start";
                            adjustedTimeStamp = helperMethod2(punchTimeStamp, shiftStart);
                        }
                        else {
                            adjustMessage = "Interval Round";
                            if(remainder <= 7) {
                                totalpunchTimeMinutes = totalshiftStartMinutes - ((quotient)*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalpunchTimeMinutes);
                            }
                            else {
                                totalpunchTimeMinutes = totalshiftStartMinutes - ((quotient+1)*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalpunchTimeMinutes);
                            }
                        }    
                        
                    }
                    
                    else if( punchTime.isAfter(shiftStart) ) {
                        
                        if( (totalpunchTimeMinutes - totalshiftStartMinutes) <= gracePeriod ) {
                            adjustMessage = "Shift Start";
                            adjustedTimeStamp = helperMethod2(punchTimeStamp, shiftStart);
                        }

                        else if ( (totalpunchTimeMinutes - totalshiftStartMinutes) <= dock ) {
                            adjustMessage = "Shift Dock";
                            totalshiftStartMinutes = totalshiftStartMinutes + dock;
                            adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStartMinutes);
                        }

                        else {
                            adjustMessage = "Interval Round";
                            int timeInterval = totalpunchTimeMinutes - totalshiftStartMinutes;
                            int quotient = timeInterval/interval;
                            int remainder = timeInterval%interval;
                            if(remainder <= 7) {
                                totalshiftStartMinutes = totalshiftStartMinutes + (quotient*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStartMinutes);
                            }
                            else {
                                totalshiftStartMinutes = totalshiftStartMinutes + ((quotient+1)*interval);
                                adjustedTimeStamp = helperMethod1(punchTimeStamp, totalshiftStartMinutes);
                            }                        
                        }
                        
                    }
                    
                }

                break;
            
            default:
                break;
                               
        }
        
    }
      
}