
package Payrollfiles;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet(name = "TimeClock", urlPatterns = {"/timeclock"})
public class TimeClock extends HttpServlet {

 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Punch p = null;
        int id = 0;
        int punchid = 0;
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String badgeid = request.getParameter("badgeid");
        int terminalid = Integer.parseInt(request.getParameter("terminalid"));
       
        Database db = new Database();
        Badge badge = db.getBadge(badgeid);
        ArrayList<Punch> punchlist = db.getDailyPunchList(badge, System.currentTimeMillis());
        
        if(!punchlist.isEmpty()) {
            
            p = punchlist.get(punchlist.size()-1);
            id = p.getPunchtypeid();
            
            if(id != punchid) {
                punchid = 1;
            }
        }
        
        Punch punch = new Punch(badge, terminalid, punchid);
        out.println(db.insertPunch(punch));
        
    }

    @Override
    public String getServletInfo() {
        return "Time-In and Time-Out Servlet";
    }

}
