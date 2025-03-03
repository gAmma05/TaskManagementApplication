<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Project Members - ${project.projectName}</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4>Project: ${project.projectName}</h4>
                <div>
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Back to Dashboard</a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
                </div>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <%@ include file="/view/common/projectNavbar.jspf" %>

            <div class="mt-4">
                <h4>Members</h4>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Username</th>
                            <th>Full Name</th>
                            <th>Phone Number</th>
                            <th>Email</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${users}">
                            <tr>
                                <td>${user.username}</td>
                                <td>${user.fullName}</td>
                                <td>${user.phone}</td>
                                <td>${user.email}</td>
                                <td>
                                    <button class="btn btn-danger btn-sm" data-bs-toggle="modal" 
                                            data-bs-target="#kickMemberModal_${user.userId}">Kick</button>
                                </td>
                            </tr>

                            <!-- Kick Member Modal -->
                        <div class="modal fade" id="kickMemberModal_${user.userId}" tabindex="-1" 
                             aria-labelledby="kickMemberModalLabel_${user.userId}" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="kickMemberModalLabel_${user.userId}">Kick Member</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <p>Are you sure you want to kick "${user.username}" from the project?</p>
                                        <form action="${pageContext.request.contextPath}/kickMember" method="post">
                                            <input type="hidden" name="projectId" value="${project.projectId}">
                                            <input type="hidden" name="userId" value="${user.userId}">
                                            <input type="hidden" name="tab" value="members"> <!-- Preserve tab -->
                                            <button type="submit" class="btn btn-danger">Confirm Kick</button>
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty users}">
                        <tr><td colspan="6">No member is enrolled.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>