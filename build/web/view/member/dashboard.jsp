<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manager Dashboard - MyApp</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
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

        <ul class="nav nav-tabs" id="managerTabs">
            <li class="nav-item">
                <a class="nav-link active" id="myProjectsTab" data-bs-toggle="tab" href="#myProjects">My Projects</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="myTasksTab" data-bs-toggle="tab" href="#myTasks">My Tasks</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="otherProjectsTab" data-bs-toggle="tab" href="#otherProjects">Other Projects</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" id="myInfoTab" data-bs-toggle="tab" href="#myInfo">My Info</a>
            </li>
        </ul>

        <div class="tab-content mt-3">
            <!-- My Projects Tab -->
            <div class="tab-pane fade show active" id="myProjects">
                <h5>List of projects assigned to you</h5>
                <button class="btn btn-primary mb-2" data-bs-toggle="modal" data-bs-target="#addProjectModal">Add New Project</button>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Project ID</th>
                            <th>Project Name</th>
                            <th>Start Date</th>
                            <th>End Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="project" items="${enrolledProjects}">
                            <tr>
                                <td>${project.projectId}</td>
                                <td>${project.projectName}</td>
                                <td><fmt:formatDate value="${project.startDate}" pattern="yyyy-MM-dd" /></td>
                                <td><fmt:formatDate value="${project.endDate}" pattern="yyyy-MM-dd" /></td>
                                <td>${project.status}</td>
                                <td>
                                    <button class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#updateProjectModal_${project.projectId}">Update</button>
                                    <button class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteProjectModal_${project.projectId}">Delete</button>
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
                            <div class="modal fade" id="deleteProjectModal_${project.projectId}" tabindex="-1" aria-labelledby="deleteProjectModalLabel_${project.projectId}" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="deleteProjectModalLabel_${project.projectId}">Delete Project</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <p>Are you sure you want to delete project "${project.projectName}"?</p>
                                            <form action="${pageContext.request.contextPath}/deleteProject" method="post">
                                                <input type="hidden" name="projectId" value="${project.projectId}">
                                                <button type="submit" class="btn btn-danger">Delete</button>
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <c:if test="${empty enrolledProjects}">
                            <tr><td colspan="6">No projects assigned to you.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- My Tasks Tab -->
            <div class="tab-pane fade" id="myTasks">
                <h5>List of tasks assigned to you</h5>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Task ID</th>
                            <th>Task Name</th>
                            <th>Due Date</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="task" items="${myTasks}">
                            <tr>
                                <td>${task.taskId}</td>
                                <td>${task.taskName}</td>
                                <td>${task.deadline}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/updateTaskStatus" method="post" class="status-form">
                                        <input type="hidden" name="taskId" value="${task.taskId}">
                                        <select name="status" class="form-select status-dropdown" onchange="this.form.submit()">
                                            <option value="PENDING" ${task.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                            <option value="IN_PROGRESS" ${task.status == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                            <option value="COMPLETED" ${task.status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                                        </select>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty myTasks}">
                            <tr><td colspan="4">No tasks assigned.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- Other Projects Tab -->
            <div class="tab-pane fade" id="otherProjects">
                <h5>List of other projects in the company</h5>
                <table class="table table-bordered">
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
                                <td>${project.projectId}</td>
                                <td>${project.projectName}</td>
                                <td>${project.manager}</td>
                                <td>${project.status}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${project.enrollmentStatus == 'PENDING'}">
                                            <button class="btn btn-secondary btn-sm pending-btn" disabled>Pending</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn btn-success btn-sm enroll-btn" data-bs-toggle="modal" data-bs-target="#enrollProjectModal_${project.projectId}">Enroll</button>
                                        </c:otherwise>
                                    </c:choose>
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
                            <tr><td colspan="5">No other projects found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- My Info Tab -->
            <div class="tab-pane fade" id="myInfo">
                <h5>My Information</h5>
                <c:choose>
                    <c:when test="${not empty myInfo}">
                        <form action="${pageContext.request.contextPath}/updateUser" method="post" id="userInfoForm">
                            <table class="table table-bordered" id="userInfoTable">
                                <tr>
                                    <th>Name</th>
                                    <td contenteditable="true" class="editable-cell" data-field="fullName">${myInfo.fullName}</td>
                                </tr>
                                <tr>
                                    <th>Email</th>
                                    <td contenteditable="true" class="editable-cell" data-field="email">${myInfo.email}</td>
                                </tr>
                                <tr>
                                    <th>Role</th>
                                    <td>${myInfo.role}</td> <!-- Not editable -->
                                </tr>
                            </table>
                            <input type="hidden" name="userId" value="${myInfo.userId}">
                            <input type="hidden" name="fullName" id="fullNameInput" value="${myInfo.fullName}">
                            <input type="hidden" name="email" id="emailInput" value="${myInfo.email}">
                            <button type="submit" class="btn btn-primary update-btn" id="updateUserBtn">Update</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p>No user information available.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>