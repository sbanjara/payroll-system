
package Payrollfiles.controller;

import Payrollfiles.model.Badge;
import Payrollfiles.model.Shift;
import Payrollfiles.model.Punch;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;

public class Bean {
    
    private String username;
    private String badgeid;
    private int terminalid;
    private String description;
    private String punchlistdate;
    private long timestamp;
    private ArrayList<Punch> dailypunchlist;
  
    public Bean() {
       
        this.badgeid = "";
        this.terminalid = 0;
        this.description = "";
        this.punchlistdate = "";
        
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBadgeid() {
        return badgeid;
    }

    public void setBadgeid(String badgeid) {
        this.badgeid = badgeid;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public void setTerminalid(int terminalid) {
        this.terminalid = terminalid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Badge getBadge(String badgeid) {
        Database db = new Database();
        Badge b = db.getBadge(badgeid);
        this.setDescription(b.getName());
        return b;
    }

    public String getPunchlistdate() {
        return punchlistdate;
    }

    public void setPunchlistdate(String punchlistdate) {
        this.punchlistdate = punchlistdate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(String timestamp) {
        
        if(!timestamp.isEmpty()) {
            
            String[] date = timestamp.split("-");
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int day = Integer.parseInt(date[2]);
            LocalDateTime ldt = LocalDateTime.of(year, month, day, 6, 30);
            Timestamp ts = Timestamp.valueOf(ldt);
            this.timestamp = ts.getTime();
            
        }
        
    }

    public ArrayList<Punch> getDailypunchlist() {
        return dailypunchlist;
    }

    public void setDailypunchlist(ArrayList<Punch> dailypunchlist) {
        this.dailypunchlist = dailypunchlist;
    }
    
    public void getDailyPunchlist(long timestamp) {  
        Database db = new Database();
        Badge b = this.getBadge(badgeid);
        this.setDailypunchlist(db.getDailyPunchList(b, timestamp));      
    }
    
    public String getEmployee() {
        Database db = new Database();
        String badgeid = db.getBadgeid(username);
        this.setBadgeid(badgeid);
        return db.getEmployee(badgeid);
    }
    
    public Shift getShift() {
        Database db = new Database();
        Badge badge = db.getBadge(badgeid);
        return db.getShift(badge);
    }
    
    public String insertPunch(int terminalid) {
        
        Database db = new Database();
        String result = "";
        
        if( this.getBadgeid().length() == 8 ) {
            
            Badge b = getBadge(this.getBadgeid());
            int punchtypeid = Logic.CLOCKIN;
            ArrayList<Punch> punches = db.getDailyPunchList(b, System.currentTimeMillis());
            int numOfPunches = punches.size();

            if(numOfPunches == 0) {
                punchtypeid = Logic.CLOCKIN;
                result = "Thank you for Clocking-In";
            }
            else if(numOfPunches <= 3) {
                
                if(numOfPunches % 2 == Logic.CLOCKIN) {
                    punchtypeid = Logic.CLOCKOUT;
                    result = "Thank you for Clocking-Out";
                }
                else if(numOfPunches % 2 == Logic.CLOCKOUT) {
                    punchtypeid = Logic.CLOCKIN;
                    result = "Thank you for Clocking-In";
                }

            }
            else {
                result = "Error, Number of Punches exceeded!!";
            }

            if( numOfPunches <= 3 && !(badgeid.isEmpty()) ) {
                Punch p = new Punch(b, terminalid, punchtypeid);
                db.insertPunch(p);
            }
        
        }
       
        this.setBadgeid("");
        return result;
        
    }
    
    public String getDailylistAsTable() throws ParseException {
        
        String data = "";
        if(!getPunchlistdate().isEmpty()) {
             
            this.setTimestamp(getPunchlistdate());
            getDailyPunchlist(getTimestamp());
            ArrayList<Punch> punches = getDailypunchlist();
            Shift shift = getShift();
            data = Logic.getlistAsTable(punches, shift);
            
        }  
        this.setPunchlistdate("");
        return data;
        
    }
    
    public String getPayperiodlistAsTable() throws ParseException {
        
        String data = "";
        if(!getPunchlistdate().isEmpty()) {
             
            this.setTimestamp(getPunchlistdate());
            Database db = new Database();
            Badge b = this.getBadge(badgeid);
            ArrayList<Punch> punches = db.getPayPeriodPunchList(b, getTimestamp());
            Shift shift = getShift();  
            data = Logic.getlistAsTable(punches, shift);
            
        }
        this.setPunchlistdate("");
        return data;
      
    }
    
    
}
