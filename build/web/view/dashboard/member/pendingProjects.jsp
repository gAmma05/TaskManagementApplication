<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Pending Projects</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="text-center">Member Dashboard</h4>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
            </div>
            <%@ include file="/view/common/navbar.jspf" %>

            <div class="mt-3">
                <h3>Pending Projects</h3>
                <table class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>Project Name</th>
                            <th>Manager</th>
                            <th>Status</th>
                            <th>Requesting Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="pending" items="${pendingProjects}">
                            <tr>
                                <td>${pending.project.projectName}</td>
                                <td>${managerUsernames[pending.project.projectId]}</td> <!-- Fixed reference -->
                                <td>${pending.project.status}</td>
                                <td>${pending.enrollmentStatus}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty pendingProjects}">
                            <tr><td colspan="4">No project is requesting.</td></tr> <!-- Corrected colspan to 4 -->
                        </c:if>  
                    </tbody>
                </table>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>