
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="bean" class="Payrollfiles.controller.Bean" scope="application" />
<jsp:setProperty name="bean" property="*" />
<jsp:setProperty name="bean" property="username" value="<%= request.getRemoteUser() %>" />


<!DOCTYPE html>

<html>
    
    <head>
        
        <title>Employee Home</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <script type="text/javascript" src="scripts/jquery-3.4.1.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/public/cssfiles/main.css">
        <link href="https://fonts.googleapis.com/css2?family=Oswald&family=Work+Sans&display=swap" rel="stylesheet">
         
    </head>
    
    <body>
        
        <header>
            
            <div id="employee_description">
                <p> Employee: <%= bean.getEmployee() %> </p>
                <div id="line"> </div>
                <p> Badgeid: <%= bean.getBadgeid() %> </p>
            </div>
            
        </header>
        
        <div id="sub-header">
        
            <a href="user-home.jsp">Daily PunchList</a>
            <a href="payperiod_punchlist.jsp" style="background-color: #f1f1f1;">Pay period PunchList</a>
            <a href="payroll.jsp">Payroll Information</a>
            <a href="logout.jsp">Logout</a>
        
        </div>
        
        <form name="insertform" id="insertform" action="#" method="POST">
              
            <label for="input">Punchlist Date:</label>
            <input type="DATE" name="punchlistdate" id="punchlistdate">
            <input type="Submit" value="Submit" id="Submit">        
               
        </form>
            
        <%= bean.getPayperiodlistAsTable() %>
       
    </body>
    
</html>
