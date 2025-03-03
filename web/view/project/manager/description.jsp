<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Project Description - ${project.projectName}</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <style>
            #updateButton {
                display: none;
            }
        </style>
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
                <h4>Project Description</h4>
                <form action="${pageContext.request.contextPath}/updateProject" method="post">
                    <input type="hidden" name="projectId" value="${project.projectId}">
                    <input type="hidden" name="tab" value="description"> <!-- Preserve tab -->
                    <div class="mb-3">
                        <label><strong>ID:</strong></label>
                        <input type="text" class="form-control" value="${project.projectId}" disabled>
                    </div>
                    <div class="mb-3">
                        <label><strong>Name:</strong></label>
                        <input type="text" class="form-control editable" name="projectName" 
                               value="${project.projectName}" required>
                    </div>
                    <div class="mb-3">
                        <label><strong>Description:</strong></label>
                        <input type="text" class="form-control editable" name="description" 
                               value="${project.description}">
                    </div>
                    <div class="mb-3">
                        <label><strong>Budget:</strong></label>
                        <input type="number" step="0.01" class="form-control editable" name="budget" 
                               value="${project.budget}" required>
                    </div>
                    <div class="mb-3">
                        <label><strong>Start Date:</strong></label>
                        <input type="date" class="form-control editable" name="startDate" 
                               value="<fmt:formatDate value='${project.startDate}' pattern='yyyy-MM-dd'/>" required>
                    </div>
                    <div class="mb-3">
                        <label><strong>End Date:</strong></label>
                        <input type="date" class="form-control editable" name="endDate" 
                               value="<fmt:formatDate value='${project.endDate}' pattern='yyyy-MM-dd'/>" required>
                    </div>
                    <div class="mb-3">
                        <label><strong>Status:</strong></label>
                        <input type="text" class="form-control editable" name="status" 
                               value="${project.status}" required>
                    </div>
                    <button type="submit" class="btn btn-primary" id="updateButton">Update</button>
                </form>

                <!-- Delete Project Button and Modal -->
                <button class="btn btn-danger mt-3" data-bs-toggle="modal" data-bs-target="#deleteProjectModal">Delete Project</button>
                <div class="modal fade" id="deleteProjectModal" tabindex="-1" aria-labelledby="deleteProjectModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteProjectModalLabel">Delete Project</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <p>Are you sure you want to delete project "${project.projectName}"?</p>
                                <form action="${pageContext.request.contextPath}/deleteProject" method="post">
                                    <input type="hidden" name="projectId" value="${project.projectId}">
                                    <input type="hidden" name="tab" value="description"> <!-- Preserve tab -->
                                    <button type="submit" class="btn btn-danger">Delete</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const inputs = document.querySelectorAll('.editable');
                const updateButton = document.getElementById('updateButton');
                inputs.forEach(input => {
                    input.addEventListener('change', function () {
                        updateButton.style.display = 'block';
                    });
                });
            });
        </script>
    </body>
</html>