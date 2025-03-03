<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>My Projects</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="text-center">Member Dashboard</h4>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
            </div>

            <!-- Error Message Display -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <%@ include file="/view/common/navbar.jspf" %>

            <!-- Content -->
            <div class="mt-3">
                <h3>List of projects assigned to you</h3>
                <table class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>Project Name</th>
                            <th>Manager</th>
                            <th>Start Date</th>
                            <th>End Date</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="project" items="${enrolledProjects}">
                            <tr class="project-row" data-project-id="${project.projectId}">
                                <td>${project.projectName}</td>
                                <td>${managerUsernames[project.projectId]}</td>
                                <td><fmt:formatDate value="${project.startDate}" pattern="yyyy-MM-dd" /></td>
                                <td><fmt:formatDate value="${project.endDate}" pattern="yyyy-MM-dd" /></td>
                                <td>${project.status}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/projectDetails?projectId=${project.projectId}" class="btn btn-info btn-sm">
                                        Details
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty enrolledProjects}">
                            <tr><td colspan="6">No projects assigned to you.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
        <script src="${pageContext.request.contextPath}/js/script.js"></script>
    </body>
</html>