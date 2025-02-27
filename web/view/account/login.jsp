<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title> 
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <h1>Login</h1> 

            <c:if test="${not empty loginGood}">
                <p style="color: greenyellow;">${loginGood}</p>
            </c:if>

            <c:if test="${not empty loginBad}">
                <p style="color: red;">${loginBad}</p>
            </c:if>

            <c:if test="${not empty errorList}">
                <ul>
                    <c:forEach var="error" items="${errorList}">
                        <li><font color="red">${error}</font></li>
                        </c:forEach>
                </ul>
            </c:if>

            <form action="Auth" method="POST">
                <fieldset>
                    <legend>Login Credentials</legend>

                    <label>Username:</label>
                    <input type="text" name="username" value="${param.username}"required>
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
        </div>
    </body>
</html>
