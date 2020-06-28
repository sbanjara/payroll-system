
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="bean" class="Payrollfiles.controller.Bean" scope="application" />
<jsp:setProperty name="bean" property="*" />
<jsp:setProperty name="bean" property="terminalid" value="104" />

<!DOCTYPE html>

<html>
    
    <head>
        
        <title>Terminals</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <script type="text/javascript" src="scripts/jquery-3.4.1.min.js"></script>
        <link rel="stylesheet" type="text/css" href="cssfiles/main.css">
        <link href="https://fonts.googleapis.com/css2?family=Oswald&family=Work+Sans&display=swap" rel="stylesheet">
        
    </head>
    
    <body>
        
        <header>
            
            <p style = "margin: 0px; text-align: center; padding: 10px;">Clock-In/Clock-Out Terminals</p>
            
        </header>
        
        <div id="sub-header">
        
            <a href="terminal.jsp">Terminal 105</a>
            <a href="terminal104.jsp" style="background-color: #f1f1f1;">Terminal 104</a>
            <a href="terminal103.jsp">Terminal 103</a>
            <a href="terminal102.jsp">Terminal 102</a>
            <a href="terminal101.jsp">Terminal 101</a>
            <a href="index.html">Home</a>
            <br></br>
        
        </div>
        
        <form name="insertform" id="insertform" action="#" method="POST">
              
            <label for="input">Badge ID:</label>
            <input type="text" name="badgeid" id="badgeid">

            <input type="Submit" value="Submit" id="Submit">        
               
        </form>

        <div id="resultsarea">
        
            <p> <%= bean.insertPunch(bean.getTerminalid()) %> </p>

        </div>
        
    </body>
    
</html>
