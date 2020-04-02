
package Payrollfiles;

import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * The Database class is used by the Server to connect to the database
 * and to insert or retrieve information from the database.
 * @author Sabin Banjara
 */

public class Database {
    
    private Connection conn;
    
    public Database() {
        
        try {
            
            Context envContext = new InitialContext();
            Context initContext  = (Context)envContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)initContext.lookup("jdbc/db_pool");
            conn = ds.getConnection();
            
        }        
        catch (Exception e) { e.printStackTrace(); }

    }
    
    public String getBadgeid(String username) {
        
        String id = null;
        String query = null;
        boolean hasresults;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;  
        
        try {
            
            query = "SELECT * FROM employee WHERE firstname = ?";

            pstatement = conn.prepareStatement(query);
                
            if(!username.isEmpty())
                pstatement.setString(1, username);
                
            hasresults = pstatement.execute();                
 
            if ( hasresults ) {
                
                resultset = pstatement.getResultSet();

                while(resultset.next()) {

                    id = resultset.getString("badgeid");

                }

            }

        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        return id;
        
    }
    
    public Badge getBadge(String badgeid) {
        
        String id = null;
        String description = null;
        
        String query = null;
        boolean hasresults;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;  
        
        try {
            
            query = "SELECT * FROM badge WHERE id = ?";

            pstatement = conn.prepareStatement(query);
                
            if(badgeid.length() == 8)
                pstatement.setString(1, badgeid);
                
            hasresults = pstatement.execute();                
 
            if ( hasresults ) {
                
                resultset = pstatement.getResultSet();

                while(resultset.next()) {

                    id = resultset.getString("id");
                    description = resultset.getString("description");

                }

            }

        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        Badge b = new Badge(id, description);    
        return b;
        
    }
    
    public Punch getPunch(int punchID) {
       
        String description = null;
        
        String query = null;
        boolean hasresults;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        int id = 0, terminalID = 0, punchTypeID = 0;
        String badgeID = "";
        Timestamp originalTimestamp = null;
        
        try {
 
            query = "SELECT * FROM punch WHERE id = ?";
            
            pstatement = conn.prepareStatement(query);
                
            if(punchID > 0)
                pstatement.setInt(1, punchID);
                
            hasresults = pstatement.execute();                  

            if ( hasresults ) {

                resultset = pstatement.getResultSet();

                while(resultset.next()) {

                    id = resultset.getInt("id");
                    terminalID = resultset.getInt("terminalid");
                    badgeID = resultset.getString("badgeid");
                    originalTimestamp = resultset.getTimestamp("originaltimestamp");
                    punchTypeID = resultset.getInt("punchtypeid");

                }

            }
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        //finally { closeConnection(conn, resultset, pstatement); }
 
        Punch p = new Punch(id, terminalID, badgeID, originalTimestamp, punchTypeID);
        
        return p;
        
    }
    
   
    public Shift getShift(int shiftID) {
        
        String query = null;
        boolean hasresults;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        String description = null; 
        String[] startingTime = null;
        String[] stoppingTime = null;
        String[] lunchStart = null;
        String[] lunchStop = null;

        int lunchDeduct = 0;
        int interval = 0;
        int gracePeriod = 0;
        int dock = 0;
        
        try {
            
            query = "SELECT * FROM shift WHERE id =  ?";

            pstatement = conn.prepareStatement(query);
            
            if(shiftID > 0)
                pstatement.setInt(1, shiftID);
                
            hasresults = pstatement.execute(); 

            if ( hasresults ) {

                resultset = pstatement.getResultSet();

                while(resultset.next()) {

                   description = resultset.getString("description");
                   startingTime = resultset.getTime("start").toString().split(":");
                   stoppingTime = resultset.getTime("stop").toString().split(":");
                   interval = resultset.getInt("interval");
                   gracePeriod = resultset.getInt("graceperiod");  
                   dock = resultset.getInt("dock");
                   lunchStart = resultset.getTime("lunchstart").toString().split(":");
                   lunchStop = resultset.getTime("lunchstop").toString().split(":");
                   lunchDeduct = resultset.getInt("lunchdeduct");

                }
            }
                       
        }
        
        catch (Exception e) { e.printStackTrace();  }
        
        //finally { closeConnection(conn, resultset, pstatement); }
        
        int shiftStartHour = Integer.parseInt(startingTime[0]);
        int shiftStartMinute = Integer.parseInt(startingTime[1]);
        int shiftStopHour = Integer.parseInt(stoppingTime[0]);
        int shiftStopMinute = Integer.parseInt(stoppingTime[1]);
        int lunchStartHour = Integer.parseInt(lunchStart[0]);
        int lunchStartMinute = Integer.parseInt(lunchStart[1]);
        int lunchStopHour = Integer.parseInt(lunchStop[0]);
        int lunchStopMinute = Integer.parseInt(lunchStop[1]);
        
        Shift s = new Shift(description, shiftStartHour, shiftStartMinute, 
                interval, gracePeriod, dock, shiftStopHour, shiftStopMinute,
                lunchStartHour, lunchStartMinute, lunchStopHour,
                lunchStopMinute, lunchDeduct);
        
        return s;
        
    }
      
    
    public Shift getShift(Badge badge) {
        
        Shift s = null;
        
        String query = null;
        boolean hasresults;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        try {
       
            query = "SELECT * FROM employee WHERE badgeid = ?";

            pstatement = conn.prepareStatement(query);
            pstatement.setString(1, badge.getBadgeid());
                              
            hasresults = pstatement.execute();
            
            if ( hasresults ) {

                resultset = pstatement.getResultSet();
                    
                while(resultset.next()) {
   
                    s = getShift(resultset.getInt("shiftid")); 
                    
                }

            }
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        //finally { closeConnection(conn, resultset, pstatement); }
         
        return s;
        
    }
    
   
    public void insertPunch(Punch p) {
        
        String query = null;
        int result = 0;
        int id = 0;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        String badgeID = p.getBadgeid();
        int terminalID = p.getTerminalid();
        int punchTypeID = p.getPunchtypeid();
        int newPunchID = p.getId();
        long originalTimeStampLong = p.getOriginaltimestamp();
        
        Timestamp originalTimeStamp = new Timestamp(originalTimeStampLong);
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String formatteddots  = sdf.format(originalTimeStamp);
        
        try {

            query = "INSERT INTO punch(terminalid, badgeid, originaltimestamp, punchtypeid)"
                    + " VALUES(?,?,?,?)";

            pstatement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            
            pstatement.setInt(1, terminalID);
            pstatement.setString(2, badgeID);
            pstatement.setString(3, formatteddots);
            pstatement.setInt(4, p.getPunchtypeid());
                
            result = pstatement.executeUpdate();
            
            if(result == 1) {
                
                resultset = pstatement.getGeneratedKeys();
                if(resultset.next()) {
                    id = resultset.getInt(1);
                }
                 
            }
       
        }
      
        catch (Exception e) {  e.printStackTrace(); }
        
        //finally { closeConnection(conn, resultset, pstatement); }
        
    }
    
    
    public ArrayList getDailyPunchList(Badge b, long ts) {
        
        String query = null;
        boolean hasresults = false;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        ArrayList<Punch> punchlist = new ArrayList();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(ts);
        SimpleDateFormat formatstring = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatstring.format(cal.getTime());
        cal.add( Calendar.DATE, 1 );
        String datePlus1 = formatstring.format(cal.getTime());
        int lastPunchType = 0;
        
        try {
        
            /* Prepare Select Query */
            
            query = "SELECT * FROM punch WHERE badgeid = ? AND DATE(originaltimestamp) = ?";

            pstatement = conn.prepareStatement(query);
            
            pstatement.setString(1, b.getBadgeid());
            pstatement.setString(2, date);
                
            hasresults = pstatement.execute();                
            
            if(hasresults) {
                         
                resultset = pstatement.getResultSet();                    

                while(resultset.next()) {

                    punchlist.add(new Punch(resultset.getInt("id"), resultset.getInt("terminalid"),
                            resultset.getString("badgeid"), resultset.getTimestamp("originaltimestamp"),
                            resultset.getInt("punchtypeid")));

                }

                if (resultset.last()) {

                    lastPunchType = resultset.getInt("punchtypeid");

                }
            
            }
            
        }        
        
        catch (Exception e) { e.printStackTrace(); }
        
        try {
            
            query = "SELECT * FROM punch WHERE badgeid = ? AND DATE(originaltimestamp) = ?";
  
            pstatement = conn.prepareStatement(query);
            
            pstatement.setString(1, b.getBadgeid());
            pstatement.setString(2, datePlus1); 
            
            hasresults = pstatement.execute();                
       
            if(hasresults) {  
                
                resultset = pstatement.getResultSet(); 
                if(resultset.first()) {
                
                    if ( (resultset.getInt("punchtypeid") == Logic.CLOCKOUT) && (lastPunchType == Logic.CLOCKIN) ) {

                        punchlist.add( new Punch( resultset.getInt("id"), resultset.getInt("terminalid"),
                                resultset.getString("badgeid"), resultset.getTimestamp("originaltimestamp"), 
                                resultset.getInt("punchtypeid") ) );

                    }
                
                }
            }
                        
        }       
        
        catch (Exception e) { e.printStackTrace(); }
        
        //finally { closeConnection(conn, resultset, pstatement); }
        
        return punchlist;
        
    }
    
   
    private long adjust(long timestamp) {
        
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(timestamp);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        
        return cal.getTimeInMillis();
        
    }
    
    
    public ArrayList getPayPeriodPunchList(Badge b, long timestamp) {
        
        String query = null;
        boolean hasresults;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;
        
        ArrayList<Punch> punchlist = new ArrayList<Punch>();
        GregorianCalendar cal1 = new GregorianCalendar();
        cal1.setTimeInMillis(adjust(timestamp));
        
        GregorianCalendar cal2 = new GregorianCalendar();
        cal2.setTimeInMillis(adjust(timestamp));
        int dayofMonth = cal2.get(Calendar.DATE);
        cal2.set(Calendar.DATE, dayofMonth+7);
        
        try {
        
            /* Prepare Select Query */
            
            query = "SELECT * FROM punch WHERE badgeid = ?";
            
            pstatement = conn.prepareStatement(query);
            pstatement.setString(1, b.getBadgeid());
                
            /* Execute Select Query */

            hasresults = pstatement.execute();                
            
            if(hasresults) {

                resultset = pstatement.getResultSet();                    

                while(resultset.next()) {

                    Timestamp ts = resultset.getTimestamp("originaltimestamp");
                    GregorianCalendar cal3 = new GregorianCalendar();
                    cal3.setTimeInMillis(ts.getTime());

                    if(cal3.after(cal1) && cal3.before(cal2)) {
                        
                        punchlist.add( new Punch( resultset.getInt("id"),resultset.getInt("terminalid"), 
                            resultset.getString("badgeid"), resultset.getTimestamp("originaltimestamp"),
                            resultset.getInt("punchtypeid") ) );
                        
                    }

                }
            
            }
        }        
        
        catch (Exception e) { e.printStackTrace(); }
        
        //finally { closeConnection(conn, resultset, pstatement); }
        
        return punchlist;
        
    }
    
    
    public Absenteeism getAbsenteeism(String badgeid, long timestamp) {
        
        String query = null;
        boolean hasresults;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;
        
        String badgeID = "";
        long ts = 0;
        double percentage = 0;
        Absenteeism a = null;
        
        Timestamp payperiod = new Timestamp(adjust(timestamp));
        String s = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(payperiod);
        
        try {
                
            query = "SELECT * FROM absenteeism WHERE badgeid = ? AND payperiod = ?"; 
            
            pstatement = conn.prepareStatement(query);
            pstatement.setString(1, badgeid);
            pstatement.setString(2, s);
            
            hasresults = pstatement.execute();         
            
            if ( hasresults ) {

                resultset = pstatement.getResultSet();  

                while(resultset.next()) {
                    
                    badgeID = resultset.getString(1);
                    Timestamp t = resultset.getTimestamp(2);
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTimeInMillis(t.getTime());
                    ts = cal.getTimeInMillis();
                    percentage = resultset.getDouble(3);

                    a = new Absenteeism(badgeid, ts, percentage);
                    
                }

            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        //finally { closeConnection(conn, resultset, pstatement); }
       
        return a;
        
    }
    
   
    public void insertAbsenteeism(Absenteeism a) {
        
        String query = null;
        boolean hasresults;
        PreparedStatement pstatement = null;
        ResultSet resultset = null;
        
        String badgeID = a.getBadgeid();
        long ts = a.getPayperiod();
        double percentage = a.getPercentage();
        
        Timestamp payperiod = new Timestamp(ts);
        String s = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(payperiod);
        String pay = (new SimpleDateFormat("MM-dd-yyyy")).format(adjust(ts));
        String st = "#" + badgeID + " (Pay Period Starting " + pay + "): " + percentage;
        
        try {
            
            if(getAbsenteeism(badgeID, ts) == null) {
                query = "INSERT INTO absenteeism (badgeid, payperiod, percentage) VALUES(?, ?, ?)";
            }
            else {
                query = "UPDATE absenteeism SET percentage = ? WHERE badgeid = ? AND payperiod = ?";
            }

            pstatement = conn.prepareStatement(query);
            
            pstatement.setString(1, badgeID);
            pstatement.setString(2, s);
            pstatement.setDouble(3, percentage);
                
            pstatement.execute();
       
        }
        
        catch (Exception e) { e.printStackTrace(); }
            
        //finally { closeConnection(conn, resultset, pstatement); }
        
    }
    
    public void closeConnection(Connection conn, ResultSet resultset, PreparedStatement ps) {
        
        if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }
            
        if (ps != null) { try { ps.close(); ps = null; } catch (Exception e) {} }
            
        if (conn != null) { try { conn.close(); conn = null;  } catch (Exception e) {} }
    }
    
        
}