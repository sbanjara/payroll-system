
package Payrollfiles.controller;

import Payrollfiles.model.Shift;
import Payrollfiles.model.Punch;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The Logic class contains functions like calculateTotalMinutes, calculateAbsenteeism,
 * getPunchListAsJSON, getListAsJSON, and getPayrollInfo. These functions are 
 * consumed by the Bean class.
 * @author Sabin Banjara
 */
public class Logic {
    

    /**
     * Constant variable for the punch type of a clock-in.
     */
    public static final int CLOCKIN = 1;

    /**
     * Constant variable for the punch type of a clock-out.
     */
    public static final int CLOCKOUT = 0;
    
    /**
     * Calculates the total minutes accrued by an employee on a single shift.
     * @param dailypunchlist an ArrayList of Punch objects for a shift
     * @param shift a shift object containing the shift rules
     * @return the total amount of minutes accrued in one shift as an int
     */

    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, 
            Shift shift) {

        int totalMin = 0;
        long totalMillis = 0;
        long inTime = 0;
        long outTime = 0;
        int punchCounter = 0;
        int lunchTime = shift.totalLunchTime();
    
        for(int i = 0; i < dailypunchlist.size(); i++) {
            
            dailypunchlist.get(i).adjust(shift);
            if (dailypunchlist.get(i).getPunchtypeid() == CLOCKIN) {           
                inTime = dailypunchlist.get(i).getAdjustedTimeStamp().getTime();
                punchCounter++;
                continue;        
            }

            if (dailypunchlist.get(i).getPunchtypeid() == CLOCKOUT) {
                outTime = dailypunchlist.get(i).getAdjustedTimeStamp().getTime();
                punchCounter++;          
            }

            if (inTime != 0 && outTime != 0) {         
                totalMillis += outTime - inTime;              
            }                
            inTime = 0;
            outTime = 0;
           
        }
        
        if (totalMillis != 0) {           
            totalMin = (int) (totalMillis/60000);         
        }
        
        if (totalMin > shift.getlunchDeduct() && punchCounter <= 3) {       
            totalMin -= lunchTime;           
        }
        return totalMin;
        
    }    
    
    /**
     * 
     * @param dailyPunchList an ArrayList of Punch objects for a shift
     * @return the daily punch list as a a String in JSON format
     */
    public static String getPunchListAsJSON(ArrayList<Punch> dailyPunchList){

        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        for(Punch p : dailyPunchList){
            HashMap<String, String> punchData = new HashMap<>();
            punchData.put("id", String.valueOf(p.getId()));
            punchData.put("badgeid", p.getBadgeid());
            punchData.put("terminalid", String.valueOf(p.getTerminalid()));
            punchData.put("punchtypeid", String.valueOf(p.getPunchtypeid()));
            punchData.put("punchdata", p.getAdjustMessage());
            punchData.put("originaltimestamp", Long.toString(p.getOriginaltimestamp()));
            punchData.put("adjustedtimestamp", Long.toString(p.getAdjustedTimeStamp().getTime()));
            
            jsonData.add(punchData);
            
        }
        return JSONValue.toJSONString(jsonData);
    }
    
    /**
     * 
     * @param punchlist an ArrayList of Punch objects for a shift
     * @param shift a Shift object containing the shift rules
     * @return the calculated absenteeism of a shift as a double by looking at
     * the punch list of a shift
     */
    public static double calculateAbsenteeism(ArrayList<Punch> punchlist, Shift shift) {
        
        double totalMin = 0;
        ArrayList<ArrayList<Punch>> punches = new ArrayList<ArrayList<Punch>>();
        ArrayList<Punch> tempList1 = new ArrayList<Punch>();
        ArrayList<Punch> tempList2 = new ArrayList<Punch>();
        ArrayList<Punch> tempList3 = new ArrayList<Punch>();
        ArrayList<Punch> tempList4 = new ArrayList<Punch>();
        ArrayList<Punch> tempList5 = new ArrayList<Punch>();
        ArrayList<Punch> tempList6 = new ArrayList<Punch>();
        
        for(Punch p: punchlist) {       
            Timestamp t = new Timestamp(p.getOriginaltimestamp());
            LocalDateTime t1 = t.toLocalDateTime();
            String day = t1.getDayOfWeek().toString();
            switch(day) {
                case "MONDAY":
                    tempList1.add(p);
                    break;
                case "TUESDAY":
                    tempList2.add(p);
                    break;
                case "WEDNESDAY":
                    tempList3.add(p);
                    break;
                case "THURSDAY":
                    tempList4.add(p);
                    break;
                case "FRIDAY":
                    tempList5.add(p);
                    break;
                case "SATURDAY":
                    tempList6.add(p);
                    break;
            } 
            
        }
        
        punches.add(tempList1);
        punches.add(tempList2);
        punches.add(tempList3);
        punches.add(tempList4);
        punches.add(tempList5);
        punches.add(tempList6);
        
        for(ArrayList<Punch> a: punches)
            totalMin += calculateTotalMinutes(a, shift);
        
        double absenteeism = 2400 - totalMin;
        double percentage = (absenteeism/2400 )*100;
        return percentage;
        
    }
    
    /**
     * 
     * @param punchlist an ArrayList of Punch objects for a shift
     * @param s a Shift object containing the shift rules
     * @return the daily punch list with an absenteeism percentage as a a String
     * in JSON format
     */
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift s) {
        
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        double absenteeism = calculateAbsenteeism(punchlist, s);
        String a = String.format("%.2f", absenteeism);
        
        int totalMin = 0;
        totalMin = (int) ((absenteeism/100)*2400);
        totalMin = 2400 - totalMin;
     
        for(Punch p : punchlist){
           
            p.adjust(s);
            HashMap<String, String> punchData = new HashMap<>();
            punchData.put("terminalid", String.valueOf(p.getTerminalid()));
            punchData.put("badgeid", p.getBadgeid());
            punchData.put("id", String.valueOf(p.getId()));
            punchData.put("punchtypeid", String.valueOf(p.getPunchtypeid()));
            punchData.put("punchdata", p.getAdjustMessage());
            punchData.put("originaltimestamp", Long.toString(p.getOriginaltimestamp()));
            punchData.put("adjustedtimestamp", Long.toString(p.getAdjustedTimeStamp().getTime()));
            
            jsonData.add(punchData);
            
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("absenteeism", a);
        data.put("totalminutes", String.valueOf(totalMin));
        jsonData.add(data);
        
        return JSONValue.toJSONString(jsonData);
        
    }
    
    public static String getlistAsTable(ArrayList<Punch> punches, Shift shift) throws ParseException {
        
        String data = "";
        
        if (!punches.isEmpty()) {

            String json = getPunchListPlusTotalsAsJSON(punches, shift);
            JSONParser parser = new JSONParser();
            JSONArray object = (JSONArray) parser.parse(json);
            JSONObject lastobject = (JSONObject) object.get(object.size()-1);
            String totalmins = (String) lastobject.get("totalminutes");
            String absenteeism = (String) lastobject.get("absenteeism");
            absenteeism += "%";
            
            GregorianCalendar cal = new GregorianCalendar();
            JSONObject map = (JSONObject) object.get(0);
            String stringots = (String) map.get("originaltimestamp");
            long ots = Long.parseLong(stringots);
            cal.setTimeInMillis(ots);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            String payperiod = (new SimpleDateFormat("EEE MM-dd-yyyy")).format(cal.getTime());
            
            if (object.size() > 5) {
                data += "<br/><strong> <p class=\"timeviewright\">Pay Period starting on " + 
                        payperiod.toUpperCase();
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                payperiod = (new SimpleDateFormat("EEE MM-dd-yyyy")).format(cal.getTime());
                data += " and ending on " + payperiod.toUpperCase() + ":</p> </strong>";
            }
            
            data += "<br/><table><tr><th>Original Timestamp</th><th>Adjusted Timestamp"
                + "</th><th>Punchtype</th></tr>";

            for (int i = 0; i < object.size() - 1; ++i) {

                map = (JSONObject) object.get(i);
                stringots = (String) map.get("originaltimestamp");
                ots = Long.parseLong(stringots);

                String stringats = (String) map.get("adjustedtimestamp");
                long ats = Long.parseLong(stringats);

                String id = (String) map.get("punchtypeid");
                String punchid = "Clock-In";

                if (id.equals("0")) {
                    punchid = "Clock-Out";
                }

                cal = new GregorianCalendar();
                cal.setTimeInMillis(ots);
                String pattern = "EEE MM/dd/yyyy HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                String otimestamp = sdf.format(cal.getTime()).toUpperCase();

                GregorianCalendar cal2 = new GregorianCalendar();
                cal2.setTimeInMillis(ats);
                String atimestamp = sdf.format(cal2.getTime()).toUpperCase();

                data += "<tr><td>" + otimestamp + "</td><td>" + atimestamp + "</td><td>" + punchid + "</td></tr>";

            }

            data += "</table><strong><p class=\"timeview\">Total Work "
                    + "(In Minutes): " + totalmins + "</p></strong>";
            
            if (object.size() > 5) {
                data += "<strong> <p class=\"timeview\">Your Absenteeism " + 
                        "(In Percentage): " + absenteeism + "</p> </strong>";
            }

        }

        else {
            data = "<p id=\"errormessage\">You haven't registered any time punches in the given date!!</p>";
        }
            
        return data;
        
    }
    
    public static String getPayrollInfo(ArrayList<Punch> punches, Shift shift, int rate) throws ParseException {
        
        String data = "";
        
        if (!punches.isEmpty()) {

            String json = getPunchListPlusTotalsAsJSON(punches, shift);
            JSONParser parser = new JSONParser();
            JSONArray object = (JSONArray) parser.parse(json);
            JSONObject lastobject = (JSONObject) object.get(object.size()-1);
            String totalmins = (String) lastobject.get("totalminutes");
            String absenteeism = (String) lastobject.get("absenteeism");
            double absenteeismtime = Double.parseDouble(absenteeism);
            double totaltime = Double.parseDouble(totalmins);
            double salary = (totaltime / 60) * rate;
            
            GregorianCalendar cal = new GregorianCalendar();
            JSONObject map = (JSONObject) object.get(0);
            String stringots = (String) map.get("originaltimestamp");
            long ots = Long.parseLong(stringots);
            cal.setTimeInMillis(ots);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            String payperiod = (new SimpleDateFormat("EEE MM-dd-yyyy")).format(cal.getTime());
            
           
            data += "<br/><strong> <p class=\"timeviewright\">Pay Period starting on " + payperiod.toUpperCase();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            payperiod = (new SimpleDateFormat("EEE MM-dd-yyyy")).format(cal.getTime());
            data += " and ending on " + payperiod.toUpperCase() + ":</p> </strong>";
            
            data += "<br/><table><tr><td>Total Time Worked (In Minutes)</td><td>" + totaltime + "</td></tr>";
            if(absenteeismtime > 0) {
                data += "<tr><td> Total Absenteeism (In Percentage)</td><td>" + absenteeism + "</td></tr>";
            }
            else {
                absenteeismtime = (absenteeismtime/100)*-2400;
                data += "<tr><td> Total Overtime (In Minutes)</td><td>" + absenteeismtime + "</td></tr>";
            }
            data += "<tr><td> Total Salary (In Dollars)</td><td>" + salary + "</td></tr> </table>";

        }

        else {
            data = "<p id=\"errormessage\">You haven't worked in the given payperiod!!</p>";
        }
            
        return data;
        
    }
    
}