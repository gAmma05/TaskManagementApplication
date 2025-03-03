<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Tasks</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .offcanvas-wide {
            width: 50% !important; /* Custom width for offcanvas */
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="text-center">Member Dashboard</h4>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
        </div>

        <!-- Error Message Display -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>

        <%@ include file="/view/common/navbar.jspf" %>

        <!-- Content -->
        <div class="mt-3">
            <h3>List of tasks assigned to you</h3>
            <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>Task Name</th>
                        <th>Project Name</th> <!-- Added Project Name column -->
                        <th>Deadline</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="task" items="${myTasks}">
                        <tr>
                            <td>${task.taskName}</td>
                            <td>${taskProjectNames[task.taskId]}</td> <!-- Display Project Name -->
                            <td><fmt:formatDate value="${task.deadline}" pattern="yyyy-MM-dd"/></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/updateTaskStatus" method="post" class="status-form">
                                    <input type="hidden" name="taskId" value="${task.taskId}">
                                    <input type="hidden" name="tab" value="myTasks">
                                    <input type="hidden" name="source" value="dashboard">
                                    <select name="status" class="form-select status-dropdown" onchange="this.form.submit()">
                                        <option value="PENDING" ${task.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                        <option value="IN_PROGRESS" ${task.status == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                        <option value="COMPLETED" ${task.status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                                    </select>
                                </form>
                            </td>
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
                                <dl class="row">
                                    <dt class="col-sm-4">Task Name:</dt>
                                    <dd class="col-sm-8">${task.taskName}</dd>

                                    <dt class="col-sm-4">Project Name:</dt>
                                    <dd class="col-sm-8">${taskProjectNames[task.taskId]}</dd>

                                    <dt class="col-sm-4">Priority:</dt>
                                    <dd class="col-sm-8">${task.priority}</dd>

                                    <dt class="col-sm-4">Status:</dt>
                                    <dd class="col-sm-8">${task.status}</dd>

                                    <dt class="col-sm-4">Assignee:</dt>
                                    <dd class="col-sm-8">
                                        <c:choose>
                                            <c:when test="${not empty task.memberId}">
                                                <c:if test="${myInfo.userId == task.memberId}">
                                                    ${myInfo.username}
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                Unassigned
                                            </c:otherwise>
                                        </c:choose>
                                    </dd>

                                    <dt class="col-sm-4">Deadline:</dt>
                                    <dd class="col-sm-8"><fmt:formatDate value="${task.deadline}" pattern="yyyy-MM-dd"/></dd>

                                    <dt class="col-sm-4">Created At:</dt>
                                    <dd class="col-sm-8"><fmt:formatDate value="${task.createdAt}" pattern="yyyy-MM-dd HH:mm"/></dd>

                                    <dt class="col-sm-4">Updated At:</dt>
                                    <dd class="col-sm-8"><fmt:formatDate value="${task.updatedAt}" pattern="yyyy-MM-dd HH:mm"/></dd>

                                    <!-- Description at bottom with full width textarea -->
                                    <dt class="col-sm-4">Description:</dt>
                                    <dd class="col-sm-8">
                                        <textarea class="form-control w-100" rows="4" disabled>${task.description}</textarea>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty myTasks}">
                        <tr><td colspan="5">No tasks assigned.</td></tr> <!-- Updated colspan to 5 -->
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>