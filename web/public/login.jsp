
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="bean" class="Payrollfiles.controller.Bean" scope="application" />
<jsp:setProperty name="bean" property="badgeid" value="" />


<!DOCTYPE html>

<html>
    
    <head>
        
        <title>Employee Login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <link href="https://fonts.googleapis.com/css2?family=Oswald&family=Work+Sans&display=swap" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/public/cssfiles/login.css">
        
    </head>
    
    <body>
        
        <header>
            
            <p style = "margin: 0px; text-align: center; padding: 10px;">Employee Login Portal</p>
            
        </header>
        
        <div id="sub-header"> 
            
            <a href="<%= request.getContextPath() %>/public/login.jsp">Login Setup</a>
            <a href="<%= request.getContextPath() %>/public/index.html">Home Page</a>
            <br></br>
               
        </div>
        
        <div id=container> 
            
            <div id="loginarea">
            
                <form id="loginform" name="loginform" method="POST" action="j_security_check" accept-charset="UTF-8">

                    <fieldset>

                        <legend>Log In</legend>

                        <p>
                            <label for="j_username">Username:</label>
                            <input type="text" name="j_username" id="j_username">
                        </p>

                        <p>
                            <label for="j_password">Password:</label>
                            <input type="password" name="j_password" id="j_password">
                        </p>

                        <p>
                            <input type="Submit" value="Submit" id="Submit">
                        </p>

                    </fieldset>

                </form>
                
                <%
                    String result = request.getParameter("error");
                    if (result != null) {
                %>

                <div id="loginerror" style="color: #BC0000; font-size: 20px; padding-top: 10px;">

                    <b>There was a problem processing your login request. Invalid Username/Password.</b>

                </div>

                <%
                    }
                %>
                
            </div>
        
        </div>
        
    </body>
    
</html>

