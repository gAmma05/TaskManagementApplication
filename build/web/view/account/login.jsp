<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title> 
    </head>
    <body>
        <h1>Login</h1> 

        <c:if test="${not empty loginGood}">
            <p style="color: greenyellow;">${loginGood}</p>
        </c:if>

        <c:if test="${not empty loginBad}">
            <p style="color: red;">${loginBad}</p>
        </c:if>

        <c:if test="${not empty error}">
            <p style="color: red;">${error}</p>
        </c:if>

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
        
        <form action="Auth" method="GET" style="display:inline;">
            <input type="hidden" name="action" value="forget-password">
            <input type="submit" value="Forget password">
        </form>

    </body>
</html>
