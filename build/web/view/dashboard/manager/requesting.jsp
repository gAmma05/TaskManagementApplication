<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Requesting Enrollments</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
                            <th>Username</th>
                            <th>Project Name</th>
                            <th>Request Time</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="request" items="${requestingEnrollments}">
                            <tr>
                                <td>${enrollmentUsers[request.enrollment.userId].username}</td>
                                <td>${request.project.projectName}</td>
                                <td><fmt:formatDate value="${request.enrollment.joinedAt}" pattern="MM/dd/yyyy HH:mm:ss"/></td>
                                <td>
                                    <div class="d-flex gap-2">
                                        <form action="${pageContext.request.contextPath}/approveEnrollment" method="post">
                                            <input type="hidden" name="userId" value="${request.enrollment.userId}">
                                            <input type="hidden" name="projectId" value="${request.project.projectId}">
                                            <input type="hidden" name="tab" value="requesting">
                                            <button type="submit" class="btn btn-sm btn-success">Approve</button>
                                        </form>
                                        <button type="button" class="btn btn-sm btn-info" data-bs-toggle="modal" 
                                            data-bs-target="#userDetailsModal${request.enrollment.userId}">
                                            User Details
                                        </button>
                                    </div>
                                </td>
                            </tr>
                            <!-- User Details Modal -->
                            <div class="modal fade" id="userDetailsModal${request.enrollment.userId}" tabindex="-1" 
                                aria-labelledby="userDetailsModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="userDetailsModalLabel">User Details</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <dl class="row">
                                                <dt class="col-sm-4">Username:</dt>
                                                <dd class="col-sm-8">${enrollmentUsers[request.enrollment.userId].username}</dd>
                                                
                                                <dt class="col-sm-4">Full Name:</dt>
                                                <dd class="col-sm-8">${enrollmentUsers[request.enrollment.userId].fullName}</dd>
                                                
                                                <dt class="col-sm-4">Email:</dt>
                                                <dd class="col-sm-8">${enrollmentUsers[request.enrollment.userId].email}</dd>
                                                
                                                <dt class="col-sm-4">Phone:</dt>
                                                <dd class="col-sm-8">${enrollmentUsers[request.enrollment.userId].phone}</dd>
                                            </dl>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty requestingEnrollments}">
                            <tr><td colspan="4">No requesting for enrollment.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>