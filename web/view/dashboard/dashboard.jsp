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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <c:choose>
                <c:when test="${sessionScope.isLoggedIn}">
                    <h1>Welcome, ${sessionScope.username}!</h1>

                    <div class="mt-4">
                        <a href="/projects" class="btn btn-primary me-2">Go to Projects</a>
                        <a href="Auth?action=logout" class="btn btn-danger">Logout</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <h1>You are not logged in.</h1>
                    <a href="${pageContext.request.contextPath}/Auth" class="btn btn-primary">Return to login</a>
                </c:otherwise>
            </c:choose>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>