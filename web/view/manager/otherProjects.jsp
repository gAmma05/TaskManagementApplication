<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Manager Dashboard - Other Projects</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    </head>
    <body>
        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="text-center">Manager Dashboard</h4>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
            </div>

            <!-- Error Message Display -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <%@ include file="/view/common/navbar.jspf" %>

            <!-- Content -->
            <div class="mt-3">
                <h5>List of other projects in the company</h5>
                <table class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>Project ID</th>
                            <th>Project Name</th>
                            <th>Manager</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="project" items="${unenrolledProjects}">
                            <tr>
                                <td>${projectWithEnrollment.project.projectId}</td>
                                <td>${projectWithEnrollment.project.projectName}</td>
                                <td>${projectWithEnrollment.project.managerId}</td>
                                <td>${projectWithEnrollment.project.status}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/enroll?projectId=${projectWithEnrollment.project.projectId}" 
                                               class="btn btn-sm btn-primary">Enroll</a>
                                </td>
                            </tr>
                            <!-- Enroll Confirmation Modal -->
                        <div class="modal fade" id="enrollProjectModal_${project.projectId}" tabindex="-1" aria-labelledby="enrollProjectModalLabel_${project.projectId}" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="enrollProjectModalLabel_${project.projectId}">Enroll in Project</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <p>Are you sure you want to enroll in "${project.projectName}"? Your enrollment will be pending approval.</p>
                                        <form action="${pageContext.request.contextPath}/enrollProject" method="post">
                                            <input type="hidden" name="projectId" value="${project.projectId}">
                                            <button type="submit" class="btn btn-success">Enroll</button>
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty unenrolledProjects}">
                        <tr><td colspan="5">No available projects found.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
        <script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
    </body>
</html>