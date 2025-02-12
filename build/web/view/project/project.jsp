<%-- 
    Document   : project
    Created on : Feb 12, 2025, 12:58:20 PM
    Author     : asus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Project</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
<body>
    <div class="container mt-5">
        <h2>Create New Project</h2>
        
<c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
    ${error}
            </div>
</c:if>
        
        <form action="${pageContext.request.contextPath}/projects/create" method="post" onsubmit="return validateForm()">
            <div class="mb-3">
                <label for="projectName" class="form-label">Project Name</label>
                <input type="text" class="form-control" id="projectName" name="projectName" required>
            </div>
            <div class="mb-3">
                <label for="description" class="form-label">Description</label>
                <textarea class="form-control" id="description" name="description" rows="3"></textarea>
            </div>
            <div class="mb-3">
                <label for="startDate" class="form-label">Start Date</label>
                <input type="date" class="form-control" id="startDate" name="startDate" required>
            </div>
            <div class="mb-3">
                <label for="endDate" class="form-label">End Date</label>
                <input type="date" class="form-control" id="endDate" name="endDate" required>
            </div>
            <div class="mb-3">
                <label for="priority" class="form-label">Priority</label>
                <select class="form-control" id="priority" name="priority">
                    <option value="LOW">Low</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="HIGH">High</option>
                </select>
            </div>
            <div class="mb-3">
                <label for="budget" class="form-label">Budget</label>
                <input type="number" class="form-control" id="budget" name="budget" step="0.01" required min="0">
            </div>
            <button type="submit" class="btn btn-primary">Create Project</button>
            <a href="${pageContext.request.contextPath}/projects" class="btn btn-secondary">Cancel</a>
        </form>
    </div>

    <script>
        function validateForm() {
            var startDate = new Date(document.getElementById('startDate').value);
            var endDate = new Date(document.getElementById('endDate').value);
            
            if (endDate < startDate) {
                alert('End date must be after start date');
                return false;
            }
            
            var budget = document.getElementById('budget').value;
            if (budget < 0) {
                alert('Budget must be a positive number');
                return false;
            }
            
            return true;
        }
    </script>
</body>
</html>