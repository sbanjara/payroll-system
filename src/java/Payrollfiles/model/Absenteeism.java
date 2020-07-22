
package Payrollfiles.model;

import java.text.*;
import java.util.*;

/**
 * The Absenteeism class is an abstraction of absenteeism percentage of an
 * employee for a pay period. This class is used to calculate the absenteeism of
 * an employee.
 * @author Sabin Banjara
 */
public class Absenteeism {
    
    private String badgeid;
    private long payperiod;
    private double percentage;
   
    public Absenteeism(String badgeid, long payperiod, double percentage) {
        
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(payperiod);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        
        this.badgeid = badgeid;
        this.payperiod = cal.getTimeInMillis();
        this.percentage = percentage;
        
    }

    public String getBadgeid() {
        return badgeid;
    }

    public void setBadgeid(String badgeid) {
        this.badgeid = badgeid;
    }

    public long getPayperiod() {
        return payperiod;
    }

    public void setPayperiod(long payperiod) {
        this.payperiod = payperiod;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    
    @Override
    public String toString() {
        
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(getPayperiod());
        String payperiod = (new SimpleDateFormat("MM-dd-yyyy")).format(cal.getTime());
        String a = String.format("%.2f", getPercentage());
      
        StringBuilder s = new StringBuilder();
        s.append("#").append(getBadgeid()).append(" (Pay Period Starting ").append(payperiod);
        s.append("): ").append(a).append("%");
        return s.toString();
        
    }
     
}
