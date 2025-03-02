<%-- 
    Document   : dashboard
    Created on : Mar 2, 2025, 2:24:25 PM
    Author     : thien
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manager Dashboard - MyApp</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container py-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="text-center">Manager Dashboard</h4>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
        </div>
        <ul class="nav nav-tabs" id="managerTabs">
            <c:forEach var="tab" items="${tabs}">
                <li class="nav-item">
                    <a class="nav-link ${tab.active ? 'active' : ''}" id="${tab.id}Tab" data-bs-toggle="tab" href="#${tab.id}">${tab.label}</a>
                </li>
            </c:forEach>
            <li class="nav-item">
                <a class="nav-link" id="myInfoTab" data-bs-toggle="tab" href="#myInfo">My Info</a>
            </li>
        </ul>
        <div class="tab-content mt-3">
            <c:forEach var="tab" items="${tabs}">
                <div class="tab-pane fade ${tab.active ? 'show active' : ''}" id="${tab.id}">
                    <p>${tab.content}</p>
                </div>
            </c:forEach>
            <div class="tab-pane fade" id="myInfo">
                <p>View and update your personal information here.</p>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

