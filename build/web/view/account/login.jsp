<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title> 
    </head>
    <body>
        <h1>Login Page</h1> 
        <form action="Auth" method="POST">

            <label>Username: </label>
            <input type="text" name="username">

            <br>

            <label>Password: </label>
            <input type="password" name="password">

            <br>

            <input type="hidden" name="action" value="login">
            <input type="submit" value="Login">
        </form>
        
        <form action="Register" method="GET">
            <input type="submit" value="Register">
        </form>
    </body>
</html>
