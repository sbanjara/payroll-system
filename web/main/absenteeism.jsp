<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="bean" class="Payrollfiles.Bean" scope="application" />

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
        
        <div id="myProgress">
        <div id="myBar"></div>
        </div>

        <br>
        <button onclick="move()">Click Me</button> 

        <script>
            
            var i = 0;
            
            function move() {
                
                if (i == 0) {
                    
                    i = 1;
                    var elem = document.getElementById("myBar");
                    var width = 10;
                    var id = setInterval(frame, 10);

                    function frame() {
                      if (width >= 100) {
                        clearInterval(id);
                        i = 0;
                      }
                       else {
                        width += 1;
                        elem.style.width = width + "%";
                      }

                    }
                  
                }
            }
            
        </script>
    
    </body>
    
</html>

