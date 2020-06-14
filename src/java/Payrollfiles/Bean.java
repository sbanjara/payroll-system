
package Payrollfiles;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;


public class Bean {
    
    private String username;
    private String badgeid;
    private int terminalid;
    private String description;
    private String punchtypeid;
    private String punchlistdate;
    private long timestamp;
    private ArrayList<Punch> dailypunchlist;
    
    public Bean() {
       
        this.badgeid = "";
        this.terminalid = 0;
        this.description = "";
        this.punchtypeid = "";
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

    public String getPunchtypeid() {
        return punchtypeid;
    }

    public void setPunchtypeid(String punchtypeid) {
        this.punchtypeid = punchtypeid;
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
        String badgeid = db.getBadgeid(username);
        Badge b = this.getBadge(badgeid);
        this.setDailypunchlist(db.getDailyPunchList(b, timestamp));
          
    }
    
    public void insertPunch(int terminalid) {
        
        Database db = new Database();
        
        if( this.getBadgeid().length() == 8 ) {
            
            Badge b = getBadge(this.getBadgeid());
            String result = String.valueOf(1);
            int punchtypeid = 1;
            ArrayList<Punch> punches = db.getDailyPunchList(b, System.currentTimeMillis());
            int numOfPunches = punches.size();

            if(numOfPunches == 0) {
                punchtypeid = Logic.CLOCKIN;
                result = String.valueOf(punchtypeid);
            }
            else if(numOfPunches <= 3) {

                Punch lastpunch = punches.get(punches.size()-1);
                int id = lastpunch.getPunchtypeid();
                if(id == Logic.CLOCKIN) {
                    punchtypeid = Logic.CLOCKOUT;
                }
                else if(id == Logic.CLOCKOUT) {
                    punchtypeid = Logic.CLOCKIN;
                }
                result = String.valueOf(punchtypeid);

            }
            else {
                result = "Error, Number of Punches exceeded!!";
            }

            if( numOfPunches <= 3 && !(badgeid.isEmpty()) ) {

                Punch p = new Punch(b, terminalid, punchtypeid);
                db.insertPunch(p);

            }

            this.setPunchtypeid(result);
        
        }
        
        else {
            this.setBadgeid("");
            this.setPunchtypeid("Error, Please fill the BadgeID field."); 
        }
        
    }
    
}
