<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Manager Dashboard - MyApp</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
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
            <c:if test="${not empty generalError}">
                <div class="alert alert-warning">${generalError}</div>
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
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>Project ID</th>
                                <th>Project Name</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="project" items="${enrolledProjects}">
                                <tr>
                                    <td>${project.projectId}</td>
                                    <td>${project.projectName}</td>
                                    <td>${project.startDate}</td>
                                    <td>${project.endDate}</td>
                                    <td>${project.status}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty enrolledProjects}">
                                <tr><td colspan="5">No enrolled projects found.</td></tr>
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
                                    <td>${task.status}</td>
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
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="project" items="${unenrolledProjects}">
                                <tr>
                                    <td>${project.projectId}</td>
                                    <td>${project.projectName}</td>
                                    <td>${project.manager}</td> <!-- Ensure Project has this field -->
                                    <td>${project.status}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty unenrolledProjects}">
                                <tr><td colspan="4">No unenrolled projects found.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <!-- My Info Tab -->
                <div class="tab-pane fade" id="myInfo">
                    <h5>My Information</h5>
                    <c:choose>
                        <c:when test="${not empty myInfo}">
                            <table class="table table-bordered">
                                <tr>
                                    <th>Name</th>
                                    <td>${myInfo.fullName}</td>
                                </tr>
                                <tr>
                                    <th>Email</th>
                                    <td>${myInfo.email}</td>
                                </tr>
                                <tr>
                                    <th>Role</th>
                                    <td>${myInfo.role}</td>
                                </tr>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <p>No user information available.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>