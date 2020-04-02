
<%@page import="java.util.ArrayList"%>
<%@page import="Payrollfiles.Punch"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="bean" class="Payrollfiles.Bean" scope="application" />

<jsp:setProperty name="bean" property="*" />

<!DOCTYPE html>

<html>
    
    <head>
        
        <title>Employee Home</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <script type="text/javascript" src="scripts/jquery-3.4.1.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/main.css">
        
        
    </head>
    
    <body>
        
        <header>
            
            <p style = "margin: 0px; text-align: center; padding: 10px;">Employee Home</p>
            
        </header>
        
        <div id="sub-header">
        
            <a>Daily PunchList</a>
            <a>Pay period PunchList</a>
            <a>View Absenteeism</a>
            <a href="logout.jsp">Logout</a>
        
        </div>
        
        <form name="insertform" id="insertform" action="#" method="POST">
              
            <label for="input">Punchlist Date:</label>
            <input type="DATE" name="punchlistdate" id="punchlistdate">

            <input type="Submit" value="Submit" id="Submit">        
               
        </form>
        
        <%  
            bean.setTimestamp(bean.getPunchlistdate());
            bean.getDailyPunchlist(bean.getTimestamp());
        
        %>
        
        <div id="resultsarea">
        
            <%  
                ArrayList<Punch> punches = bean.getDailypunchlist();
                if ( punches.isEmpty() ) {
            %>

            <p>No punches were registered for the given date.</p>

            <%
                }
                else {
                    for(Punch p: punches) {
            %>

            <p> <%= p.toString() %></p>

            <%
                }
                }
            %>

        </div>
       
    </body>
    
</html>
