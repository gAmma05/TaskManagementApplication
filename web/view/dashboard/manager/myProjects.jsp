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
                <h3>List of your projects</h3>
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
                            <!-- Update Project Modal -->
                        <div class="modal fade" id="updateProjectModal_${project.projectId}" tabindex="-1" aria-labelledby="updateProjectModalLabel_${project.projectId}" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="updateProjectModalLabel_${project.projectId}">Update Project</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <form action="${pageContext.request.contextPath}/updateProject" method="post">
                                            <input type="hidden" name="tab" value="myProjects">
                                            <input type="hidden" name="projectId" value="${project.projectId}">
                                            <div class="mb-3">
                                                <label for="projectName_${project.projectId}" class="form-label">Project Name</label>
                                                <input type="text" class="form-control" id="projectName_${project.projectId}" name="projectName" value="${project.projectName}" required>
                                            </div>
                                            <div class="mb-3">
                                                <label for="description_${project.projectId}" class="form-label">Description</label>
                                                <input type="text" class="form-control" id="description_${project.projectId}" name="description" value="${project.description}">
                                            </div>
                                            <div class="mb-3">
                                                <label for="budget_${project.projectId}" class="form-label">Budget</label>
                                                <input type="number" step="0.01" class="form-control" id="budget_${project.projectId}" name="budget" value="${project.budget}">
                                            </div>
                                            <div class="mb-3">
                                                <label for="startDate_${project.projectId}" class="form-label">Start Date</label>
                                                <input type="date" class="form-control" id="startDate_${project.projectId}" name="startDate" value="<fmt:formatDate value='${project.startDate}' pattern='yyyy-MM-dd' />" required>
                                            </div>
                                            <div class="mb-3">
                                                <label for="endDate_${project.projectId}" class="form-label">End Date</label>
                                                <input type="date" class="form-control" id="endDate_${project.projectId}" name="endDate" value="<fmt:formatDate value='${project.endDate}' pattern='yyyy-MM-dd' />" required>
                                            </div>
                                            <button type="submit" class="btn btn-primary">Update Project</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Delete Project Modal -->
                        <div class="modal fade" id="deleteProjectModal_${project.projectId}" tabindex="-1" 
                             aria-labelledby="deleteProjectModalLabel_${project.projectId}" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="deleteProjectModalLabel_${project.projectId}">Delete Project</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <p>Are you sure you want to delete project "${project.projectName}"?</p>
                                        <form action="${pageContext.request.contextPath}/deleteProject" method="post">
                                            <input type="hidden" name="tab" value="myProjects">
                                            <input type="hidden" name="projectId" value="${project.projectId}">
                                            <button type="submit" class="btn btn-danger">Delete</button>
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty myProjects}">
                        <tr><td colspan="6">No projects available.</td></tr>
                    </c:if>
                    </tbody>
                </table>

                <!-- Add Project Modal -->
                <div class="modal fade" id="addProjectModal" tabindex="-1" aria-labelledby="addProjectModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="addProjectModalLabel">Create New Project</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form action="${pageContext.request.contextPath}/addProject" method="post">
                                    <input type="hidden" name="tab" value="myProjects">
                                    <div class="mb-3">
                                        <label for="projectName" class="form-label">Project Name</label>
                                        <input type="text" class="form-control" id="projectName" name="projectName" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="description" class="form-label">Description</label>
                                        <input type="text" class="form-control" id="description" name="description">
                                    </div>
                                    <div class="mb-3">
                                        <label for="budget" class="form-label">Budget</label>
                                        <input type="number" step="0.01" class="form-control" id="budget" name="budget">
                                    </div>
                                    <div class="mb-3">
                                        <label for="startDate" class="form-label">Start Date</label>
                                        <input type="date" class="form-control" id="startDate" name="startDate" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="endDate" class="form-label">End Date</label>
                                        <input type="date" class="form-control" id="endDate" name="endDate" required>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Create Project</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
        <script src="${pageContext.request.contextPath}/js/script.js"></script>
    </body>
</html>