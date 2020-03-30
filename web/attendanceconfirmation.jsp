
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="bean" class="Payrollfiles.Bean" scope="application" />

<jsp:setProperty name="bean" property="*" />

<%  
    
    String id = bean.getBadgeid();
    if( !(id.isEmpty()) ) {
        bean.insertPunch(105);
    }
  
%>

<!DOCTYPE html>

<html>
    
    <head>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Clock-in/Clock-out Confirmation</title>
        
    </head>
    
    <body>
        
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
            else {
        %>
        
         <p>Error, Please fill the BadgeID field.</p>

        <%
            }
        %>
        
    </body>
    
</html>
