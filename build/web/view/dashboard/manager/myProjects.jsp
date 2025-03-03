<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>My Projects</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="text-center">Manager Dashboard</h4>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <%@ include file="/view/common/navbar.jspf" %>

            <!-- Filter Form -->
            <div class="mt-3">
                <h3>List of your projects</h3>
                <form method="get" action="${pageContext.request.contextPath}/dashboard" class="mb-3">
                    <input type="hidden" name="tab" value="myProjects">
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label for="projectName" class="form-label">Project Name</label>
                            <input type="text" class="form-control" id="projectName" name="projectName" value="${param.projectName}">
                        </div>
                        <div class="col-md-4">
                            <label for="budget" class="form-label">Budget (Max)</label>
                            <input type="number" step="0.01" class="form-control" id="budget" name="budget" value="${param.budget}">
                        </div>
                        <div class="col-md-4">
                            <label for="status" class="form-label">Status</label>
                            <select class="form-select" id="status" name="status">
                                <option value="">All</option>
                                <option value="PLANNING" <c:if test="${param.status == 'PLANNING'}">selected</c:if>>Planning</option>
                                <option value="IN_PROGRESS" <c:if test="${param.status == 'IN_PROGRESS'}">selected</c:if>>In Progress</option>
                                <option value="COMPLETED" <c:if test="${param.status == 'COMPLETED'}">selected</c:if>>Completed</option>
                            </select>
                        </div>
                    </div>
                    <div class="mt-2">
                        <button type="submit" class="btn btn-primary">Filter</button>
                        <a href="${pageContext.request.contextPath}/dashboard?tab=myProjects" class="btn btn-secondary">Reset</a>
                    </div>
                </form>

                <button class="btn btn-primary mb-2" data-bs-toggle="modal" data-bs-target="#addProjectModal">Add</button>
                <table class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>Project Name</th>
                            <th>Start Date</th>
                            <th>End Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="project" items="${myProjects}">
                            <tr class="project-row" data-project-id="${project.projectId}">
                                <td>${project.projectName}</td>
                                <td><fmt:formatDate value="${project.startDate}" pattern="yyyy-MM-dd" /></td>
                                <td><fmt:formatDate value="${project.endDate}" pattern="yyyy-MM-dd" /></td>
                                <td>${project.status}</td>
                                <td>
                                    <button class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#updateProjectModal_${project.projectId}">Update</button>
                                    <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteProjectModal_${project.projectId}">Delete</button>
                                    <a href="${pageContext.request.contextPath}/projectDetails?projectId=${project.projectId}" class="btn btn-info btn-sm">Details</a>
                                </td>
                            </tr>
                            <!-- Update và Delete Modal giữ nguyên như cũ -->
                        </c:forEach>
                        <c:if test="${empty myProjects}">
                            <tr><td colspan="5">No projects available.</td></tr>
                        </c:if>
                    </tbody>
                </table>
                <!-- Add Project Modal giữ nguyên như cũ -->
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/script.js"></script>
    </body>
</html>