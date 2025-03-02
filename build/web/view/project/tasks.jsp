<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Project Tasks - ${project.projectName}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
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
            <h4>Tasks</h4>
            <c:choose>
                <c:when test="${empty tasks}">
                    <p>No tasks assigned to this project yet.</p>
                </c:when>
                <c:otherwise>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>Task ID</th>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Priority</th>
                                <th>Status</th>
                                <th>Assigned To</th> <!-- Changed from "Assigned By" -->
                                <th>Deadline</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="task" items="${tasks}">
                                <tr>
                                    <td>${task.taskId}</td>
                                    <td>${task.taskName}</td>
                                    <td>${task.description}</td>
                                    <td>${task.priority}</td>
                                    <td>${task.status}</td>
                                    <td>${task.memberId}</td> <!-- Changed from assignerId -->
                                    <td><fmt:formatDate value="${task.deadline}" pattern="yyyy-MM-dd"/></td>
                                    <td>
                                        <button class="btn btn-warning btn-sm" data-bs-toggle="modal" 
                                                data-bs-target="#updateTaskModal_${task.taskId}">Update</button>
                                        <button class="btn btn-danger btn-sm" data-bs-toggle="modal" 
                                                data-bs-target="#deleteTaskModal_${task.taskId}">Delete</button>
                                    </td>
                                </tr>

                                <!-- Update Task Modal -->
                                <div class="modal fade" id="updateTaskModal_${task.taskId}" tabindex="-1" 
                                     aria-labelledby="updateTaskModalLabel_${task.taskId}" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="updateTaskModalLabel_${task.taskId}">Update Task</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                <form action="${pageContext.request.contextPath}/updateTask" method="post">
                                                    <input type="hidden" name="taskId" value="${task.taskId}">
                                                    <input type="hidden" name="projectId" value="${project.projectId}">
                                                    <input type="hidden" name="tab" value="tasks">
                                                    <div class="mb-3">
                                                        <label for="taskName_${task.taskId}" class="form-label">Task Name</label>
                                                        <input type="text" class="form-control" id="taskName_${task.taskId}" 
                                                               name="taskName" value="${task.taskName}" required>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="description_${task.taskId}" class="form-label">Description</label>
                                                        <textarea class="form-control" id="description_${task.taskId}" 
                                                                  name="description">${task.description}</textarea>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="memberId_${task.taskId}" class="form-label">Assigned To</label> <!-- Changed from "Assigned For" -->
                                                        <select class="form-control" id="memberId_${task.taskId}" 
                                                                name="memberId" required> <!-- Changed from assignerId -->
                                                            <c:forEach var="user" items="${users}">
                                                                <option value="${user.userId}" 
                                                                        ${user.userId eq task.memberId ? 'selected' : ''}> <!-- Changed from assignerId -->
                                                                    ${user.username}
                                                                </option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="priority_${task.taskId}" class="form-label">Priority</label>
                                                        <select class="form-control" id="priority_${task.taskId}" 
                                                                name="priority" required>
                                                            <option value="LOW" ${task.priority.name() == 'LOW' ? 'selected' : ''}>Low</option>
                                                            <option value="MEDIUM" ${task.priority.name() == 'MEDIUM' ? 'selected' : ''}>Medium</option>
                                                            <option value="HIGH" ${task.priority.name() == 'HIGH' ? 'selected' : ''}>High</option>
                                                        </select>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="status_${task.taskId}" class="form-label">Status</label>
                                                        <select class="form-control" id="status_${task.taskId}" 
                                                                name="status" required>
                                                            <option value="TODO" ${task.status.name() == 'TODO' ? 'selected' : ''}>To Do</option>
                                                            <option value="IN_PROGRESS" ${task.status.name() == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                                            <option value="DONE" ${task.status.name() == 'DONE' ? 'selected' : ''}>Done</option>
                                                        </select>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="deadline_${task.taskId}" class="form-label">Deadline</label>
                                                        <input type="date" class="form-control" id="deadline_${task.taskId}" 
                                                               name="deadline" value="<fmt:formatDate value='${task.deadline}' pattern='yyyy-MM-dd'/>" required>
                                                    </div>
                                                    <button type="submit" class="btn btn-primary">Update Task</button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Delete Task Modal -->
                                <div class="modal fade" id="deleteTaskModal_${task.taskId}" tabindex="-1" 
                                     aria-labelledby="deleteTaskModalLabel_${task.taskId}" aria-hidden="true">
                                    <div class="modal-dialog modal-dialog-centered">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="deleteTaskModalLabel_${task.taskId}">Delete Task</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                <p>Are you sure you want to delete task "${task.taskName}"?</p>
                                                <form action="${pageContext.request.contextPath}/deleteTask" method="post">
                                                    <input type="hidden" name="taskId" value="${task.taskId}">
                                                    <input type="hidden" name="projectId" value="${project.projectId}">
                                                    <input type="hidden" name="tab" value="tasks">
                                                    <button type="submit" class="btn btn-danger">Delete</button>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
            <button class="btn btn-primary mt-3" data-bs-toggle="modal" data-bs-target="#createTaskModal">Create Task</button>
        </div>

        <!-- Create Task Modal -->
        <div class="modal fade" id="createTaskModal" tabindex="-1" aria-labelledby="createTaskModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createTaskModalLabel">Create New Task</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="${pageContext.request.contextPath}/createTask" method="post">
                            <input type="hidden" name="projectId" value="${project.projectId}">
                            <input type="hidden" name="tab" value="tasks">
                            <div class="mb-3">
                                <label for="taskName" class="form-label">Task Name</label>
                                <input type="text" class="form-control" id="taskName" name="taskName" required>
                            </div>
                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control" id="description" name="description"></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="memberId" class="form-label">Assigned To</label> <!-- Changed from "Assigned For" -->
                                <select class="form-control" id="memberId" name="memberId" required> <!-- Changed from assignerId -->
                                    <c:forEach var="user" items="${users}">
                                        <option value="${user.userId}">${user.username}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="priority" class="form-label">Priority</label>
                                <select class="form-control" id="priority" name="priority" required>
                                    <option value="LOW">Low</option>
                                    <option value="MEDIUM">Medium</option>
                                    <option value="HIGH">High</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="deadline" class="form-label">Deadline</label>
                                <input type="date" class="form-control" id="deadline" name="deadline" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Create Task</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>