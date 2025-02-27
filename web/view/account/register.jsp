<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <h1>Register</h1>

            <c:if test="${not empty errorList}">
                <ul>
                    <c:forEach var="error" items="${errorList}">
                        <li><font color="red">${error}</font></li>
                        </c:forEach>
                </ul>
            </c:if>

            <form action="${pageContext.request.contextPath}/Auth" method="POST">
                <fieldset>
                    <legend>Account Information</legend>
                    <label>Username:</label>
                    <input type="text" name="username" required>
                    <br><br>

                    <label>Password:</label>
                    <input type="password" name="password" required>
                    <br><br>

                    <label>Confirm Password:</label>
                    <input type="password" name="confirm-password" required>
                </fieldset>
                <br>

                <fieldset>
                    <legend>Personal Information</legend>
                    <label>First Name:</label>
                    <input type="text" name="firstName">
                    <br><br>

                    <label>Last Name:</label>
                    <input type="text" name="lastName">
                    <br><br>

                    <label>Email:</label>
                    <input type="email" name="email">
                    <br><br>

                    <label>Phone Number:</label>
                    <input type="tel" name="phone">
                </fieldset>

                <br>

                <input type="hidden" name="action" value="pre-register">
                <input type="submit" value="Register">
            </form>

            <br>

            <form action="${pageContext.request.contextPath}/Auth" method="GET" style="display:inline;">
                <button type="submit">Back</button>
            </form>

            <br><br>
        </div>
    </body>
</html>
