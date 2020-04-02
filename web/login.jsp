
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>

<html>
    
    <head>
        
        <title>Employee Login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/login.css">
        
    </head>
    
    <body>
        
        <header>
            
            <p style = "margin: 0px; text-align: center; padding: 10px;">Employee Login Portal</p>
            
        </header>
        
        <div id="sub-header"> 
            
            <a href="login.jsp">Login Setup</a>
            <a href="index.html">Home Page</a>
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
                            <label for="j_password"> Password:</label>
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

                <div id="loginerror" style="color: red;">

                    <b>There was a problem processing your login request.</b>

                    <ul>
                        <li>Please try entering your Username and Password again.</li>
                    </ul>

                </div>

                <%
                    }
                %>
                
            </div>
        
        </div>
        
    </body>
    
</html>

