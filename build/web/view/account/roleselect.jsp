<%-- 
    Document   : roleselect
    Created on : Feb 8, 2025, 11:00:33 AM
    Author     : gAmma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Choose a role</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <c:choose>
                <c:when test="${empty username || empty password || (empty firstName && empty lastName) || empty email || empty phone}">
                    <h1>You haven't entered the required input into field. Try again.</h1>
                    <a href="${pageContext.request.contextPath}/Auth" class="btn btn-primary">Return to login</a>
                </c:when>
                <c:otherwise>

                    <h1>Before getting to main page!</h1>

                    <form action="${pageContext.request.contextPath}/Auth" method="POST">
                        <fieldset>
                            <legend>Choose a role to get access to projects list</legend>

                            <label>Project Manager</label>
                            <input type="radio" name="selected" value="manager"required>

                            <br><br>

                            <label>Team Member</label>
                            <input type="radio" name="selected" value="member" required>
                        </fieldset>

                        <br>

                        <input type="hidden" name="username" value="${username}">
                        <input type="hidden" name="password" value="${password}">
                        <input type="hidden" name="firstName" value="${firstName}">
                        <input type="hidden" name="lastName" value="${lastName}">
                        <input type="hidden" name="email" value="${email}">
                        <input type="hidden" name="phone" value="${phone}">

                        <input type="hidden" name="action" value="confirm-register">
                        <input type="submit" value="Confirm">
                    </form>

                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>
