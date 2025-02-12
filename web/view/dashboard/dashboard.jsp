<%-- 
    Document   : dashboard
    Created on : Jan 9, 2025, 5:12:31 PM
    Author     : gAmma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${sessionScope.isLoggedIn}">
                <h1>Welcome, ${sessionScope.username}!</h1>
                <p>Your role: ${sessionScope.role}</p>
                <a href="Project">Return to main menu</a>
                <a href="Auth?action=logout">Logout</a>
            </c:when>
            <c:otherwise>
                <h1>You are not logged in.</h1>
            </c:otherwise>
        </c:choose>
    </body>
</html>
