
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Main Redirect Page</title>
    </head>
    
    <body>
        
        <%
            if ( request.isUserInRole("employee") ) {
                response.sendRedirect( request.getContextPath() + "/main/user-home.jsp" );
            }
        %>

    </body>
    
</html>