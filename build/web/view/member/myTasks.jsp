<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manager Dashboard - My Tasks</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
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
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>