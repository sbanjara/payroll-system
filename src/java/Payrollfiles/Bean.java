
package Payrollfiles;

import java.util.ArrayList;


public class Bean {
    
    private String badgeid;
    private int terminalid;
    private String description;
    private String punchtypeid;
    
    public Bean() {
        
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
    
    public void insertPunch() {
        
        Database db = new Database();
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
            
            Punch p = new Punch(b, 105, punchtypeid);
            db.insertPunch(p);
            
        }
        
        this.setPunchtypeid(result);
        
    }
    
}
