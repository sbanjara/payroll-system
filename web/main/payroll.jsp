<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="bean" class="Payrollfiles.controller.Bean" scope="application" />
<jsp:setProperty name="bean" property="*" />

<!DOCTYPE html>

<html>
    
    <style>
        
        #myProgress {
          width: 100%;
          background-color: #ddd;
        }

        #myBar {
          width: 0%;
          height: 30px;
          background-color: #4CAF50;
        }
        
    </style>
    
    <head>
        
        <title>Employee Home/Payroll</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <script type="text/javascript" src="scripts/jquery-3.4.1.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/public/cssfiles/main.css">
        
        
    </head>
    
    <body>
        
        <header>
            
            <p style = "margin: 0px; text-align: center; padding: 10px;">Payroll Information</p>
            
        </header>
        
        <div id="sub-header">
        
            <a href="user-home.jsp">Daily PunchList</a>
            <a href="payperiod_punchlist.jsp">Pay period PunchList</a>
            <a href="payroll.jsp" style="background-color: #f1f1f1;">Payroll Information</a>
            <a href="logout.jsp">Logout</a>
        
        </div>
    
    </body>
    
</html>

