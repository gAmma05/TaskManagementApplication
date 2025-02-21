<%-- 
    Document   : setnewpassword
    Created on : Feb 17, 2025, 7:34:06 PM
    Author     : gAmma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Set new password for the user</title>
    </head>
    <body>
        <h1>Set a new password</h1>
        <c:if test="${not empty error}">
            <p style="color:red;">${error}</p>
        </c:if>

        <c:if test="${not empty dupPass}">
            <p style="color:red;">${dupPass}</p>
        </c:if>    

        <c:if test="${not empty username}">
            <p>Welcome:</p>
            <p style="color:blue">${username}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/Auth" method="POST">

            <fieldset>
                <label>New password:</label>
                <input type="password" name="new-password" required>

                <br><br>

                <label>Confirm new password:</label>
                <input type="password" name="confirm-new-password" required>
            </fieldset>

            <input type="hidden" name="username" value="${username}">
            <input type="hidden" name="email" value="${email}">
            <input type="hidden" name="action" value="confirm-forget-password">
            <input type="submit" name="Confirm">
        </form>

        <form action="${pageContext.request.contextPath}/Auth" method="GET" style="display:inline;">
            <button type="submit">Back</button>
        </form>

    </body>
</html>
