<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title> 
    </head>
    <body>
        <h1>Login</h1> 

        <form action="Auth" method="POST">
            <fieldset>
                <legend>Login Credentials</legend>

                <label>Username:</label>
                <input type="text" name="username" required>
                <br><br>

                <label>Password:</label>
                <input type="password" name="password" required>
            </fieldset>

            <br>

            <input type="hidden" name="action" value="login">
            <input type="submit" value="Login">
        </form>

        <br>

        <form action="Auth" method="GET" style="display:inline;">
            <input type="hidden" name="action" value="register">
            <input type="submit" value="Register">
        </form>
    </body>
</html>
