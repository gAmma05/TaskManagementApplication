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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <style>
            .offcanvas-wide {
                width: 50% !important; /* Custom width */
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
                <h4>Tasks</h4>

                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Priority</th>
                            <th>Status</th>
                            <th>Assignee</th>
                            <th>Deadline</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="task" items="${tasks}">
                            <tr>
                                <td>${task.taskName}</td>
                                <td>${task.priority}</td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/updateTaskStatus" method="post" class="status-form">
                                        <input type="hidden" name="taskId" value="${task.taskId}">
                                        <input type="hidden" name="tab" value="tasks">
                                        <input type="hidden" name="source" value="projectDetails">
                                        <select name="status" class="form-select status-dropdown" onchange="this.form.submit()">
                                            <option value="PENDING" ${task.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                            <option value="IN_PROGRESS" ${task.status == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                            <option value="COMPLETED" ${task.status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                                        </select>
                                    </form>
                                </td>
                                <td>
                                    <c:forEach var="user" items="${users}">
                                        <c:if test="${user.userId == task.memberId}">
                                            ${user.username}
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${empty task.memberId || task.memberId == null}">
                                        Unassigned
                                    </c:if>
                                </td>
                                <td><fmt:formatDate value="${task.deadline}" pattern="yyyy-MM-dd"/></td>
                                <td>
                                    <button class="btn btn-info btn-sm" data-bs-toggle="offcanvas" 
                                            data-bs-target="#taskDetailsOffcanvas_${task.taskId}">Details</button>
                                </td>
                            </tr>

                            <!-- Task Details Offcanvas -->
                        <div class="offcanvas offcanvas-end offcanvas-wide" tabindex="-1" 
                             id="taskDetailsOffcanvas_${task.taskId}" 
                             aria-labelledby="taskDetailsOffcanvasLabel_${task.taskId}">
                            <div class="offcanvas-header">
                                <h5 class="offcanvas-title" id="taskDetailsOffcanvasLabel_${task.taskId}">
                                    Task Details - ${task.taskName}
                                </h5>
                                <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                            </div>
                            <div class="offcanvas-body">
                                <form action="${pageContext.request.contextPath}/updateTaskStatus" method="post" 
                                      id="statusForm_${task.taskId}">
                                    <input type="hidden" name="taskId" value="${task.taskId}">
                                    <input type="hidden" name="tab" value="tasks">
                                    <input type="hidden" name="source" value="projectDetails">
                                    <div class="row mb-3">
                                        <label for="taskName_${task.taskId}" class="col-sm-4 col-form-label">Name</label>
                                        <div class="col-sm-8">
                                            <input type="text" class="form-control" id="taskName_${task.taskId}" 
                                                   name="taskName" value="${task.taskName}" disabled>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="description_${task.taskId}" class="col-sm-4 col-form-label">Description</label>
                                        <div class="col-sm-8">
                                            <textarea class="form-control" id="description_${task.taskId}" 
                                                      name="description" disabled>${task.description}</textarea>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="memberId_${task.taskId}" class="col-sm-4 col-form-label">Assignee</label>
                                        <div class="col-sm-8">
                                            <select class="form-control" id="memberId_${task.taskId}" 
                                                    name="memberId" disabled>
                                                <c:forEach var="user" items="${users}">
                                                    <option value="${user.userId}" 
                                                            ${user.userId eq task.memberId ? 'selected' : ''}>
                                                        ${user.username}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="priority_${task.taskId}" class="col-sm-4 col-form-label">Priority</label>
                                        <div class="col-sm-8">
                                            <select class="form-control" id="priority_${task.taskId}" 
                                                    name="priority" disabled>
                                                <option value="LOW" ${task.priority.name() == 'LOW' ? 'selected' : ''}>Low</option>
                                                <option value="MEDIUM" ${task.priority.name() == 'MEDIUM' ? 'selected' : ''}>Medium</option>
                                                <option value="HIGH" ${task.priority.name() == 'HIGH' ? 'selected' : ''}>High</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="status_${task.taskId}" class="col-sm-4 col-form-label">Status</label>
                                        <div class="col-sm-8">
                                            <select class="form-control" id="status_${task.taskId}" 
                                                    name="status" onchange="this.form.submit()">
                                                <option value="PENDING" ${task.status.name() == 'PENDING' ? 'selected' : ''}>Pending</option>
                                                <option value="IN_PROGRESS" ${task.status.name() == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                                <option value="COMPLETED" ${task.status.name() == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="deadline_${task.taskId}" class="col-sm-4 col-form-label">Deadline</label>
                                        <div class="col-sm-8">
                                            <input type="date" class="form-control" id="deadline_${task.taskId}" 
                                                   name="deadline" value="<fmt:formatDate value='${task.deadline}' pattern='yyyy-MM-dd'/>" disabled>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="createdAt_${task.taskId}" class="col-sm-4 col-form-label">Created At</label>
                                        <div class="col-sm-8">
                                            <input type="datetime-local" class="form-control" id="createdAt_${task.taskId}" 
                                                   name="createdAt" value="<fmt:formatDate value='${task.createdAt}' pattern='yyyy-MM-dd\'T\'HH:mm'/>" disabled>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label for="updatedAt_${task.taskId}" class="col-sm-4 col-form-label">Updated At</label>
                                        <div class="col-sm-8">
                                            <input type="datetime-local" class="form-control" id="updatedAt_${task.taskId}" 
                                                   name="updatedAt" value="<fmt:formatDate value='${task.updatedAt}' pattern='yyyy-MM-dd\'T\'HH:mm'/>" disabled>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty tasks}">
                        <tr><td colspan="6">No task is assigned.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>