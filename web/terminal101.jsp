
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="bean" class="Payrollfiles.Bean" scope="application" />

<jsp:setProperty name="bean" property="*" />

<!DOCTYPE html>

<html>
    
    <head>
        
        <title>Terminals</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <script type="text/javascript" src="scripts/jquery-3.4.1.min.js"></script>
        <link rel="stylesheet" type="text/css" href="main.css">
        
        
    </head>
    
    <body>
        
        <header>
            
            <p style = "margin: 0px; text-align: center; padding: 10px;">Clock-In/Clock-Out Terminals</p>
            
        </header>
        
        <div id="sub-header">
        
            <a href="terminal.jsp">Terminal 105</a>
            <a href="terminal104.jsp">Terminal 104</a>
            <a href="terminal103.jsp">Terminal 103</a>
            <a href="terminal102.jsp">Terminal 102</a>
            <a href="terminal101.jsp" style="background-color: #f1f1f1;">Terminal 101</a>
            <a href="index.html">Home</a>
            <br></br>
        
        </div>
        
        <form name="insertform" id="insertform" action="#" method="POST">
              
            <label for="input">Badge ID:</label>
            <input type="text" name="badgeid" id="badgeid">

            <input type="Submit" value="Submit" id="Submit">        
               
        </form>
        
        <%  

                String id = bean.getBadgeid();
                if( !(id.isEmpty()) ) {
                    bean.insertPunch(101);
                }
        
        %>
        
        <div id="resultsarea">
        
            <%  
                String punchid = bean.getPunchtypeid();
                if ( punchid.equals(String.valueOf(1)) ) {
            %>

            <p>Thank you for Clocking-In</p>

            <%
                }
                else if ( punchid.equals(String.valueOf(0)) ) {
            %>

            <p>Thank you for Clocking-Out</p>

            <%
                }
                else if( punchid.equals("Error, Number of Punches exceeded!!") ) {
            %>

            <p>Error, Number of Punches exceeded!!</p>

            <%
                }
                else if( punchid.equals("Error, Please fill the BadgeID field.") ) {
            %>

             <p>Error, Please fill the BadgeID field.</p>

            <%
                }
            %>

        </div>
        
    </body>
    
</html>

