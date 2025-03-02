<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Requesting Enrollments</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    </head>
    <body>
        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="text-center">Manager Dashboard</h4>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
            </div>
            <%@ include file="/view/common/navbar.jspf" %>
            <div class="mt-3">
                <h3>Requesting Enrollments</h3>
                <table class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>Project ID</th>
                            <th>Project Name</th>
                            <th>Joined At</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="request" items="${requestingEnrollments}">
                            <tr>
                                <td>${request.enrollment.userId}</td>
                                <td>${request.project.projectId}</td>
                                <td>${request.project.projectName}</td>
                                <td>${request.enrollment.joinedAt}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/approveEnrollment" method="post">
                                        <input type="hidden" name="userId" value="${request.enrollment.userId}">
                                        <input type="hidden" name="projectId" value="${request.project.projectId}">
                                        <button type="submit" class="btn btn-sm btn-success">Approve</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty requestingEnrollments}">
                            <tr><td colspan="6">No requesting for enrollment.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>