<%-- 
    Document   : list.jsp
    Created on : Feb 11, 2025, 4:36:48 PM
    Author     : asus
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Projects</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
<body>
<div class="container mt-5">
    <h2>My Projects</h2>
    
    <!-- Search and filter form -->
    <form action="${pageContext.request.contextPath}/projects" method="get" class="mb-4">
        <div class="row g-3">
            <div class="col-md-3">
                <input type="text" name="search" class="form-control" placeholder="Search by name" value="${param.search}">
            </div>
            <div class="col-md-2">
                <select name="status" class="form-select">
                    <option value="">All Status</option>
                    <option value="NEW" ${param.status == 'NEW' ? 'selected' : ''}>New</option>
                    <option value="IN_PROGRESS" ${param.status == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                    <option value="COMPLETED" ${param.status == 'NOT START' ? 'selected' : ''}>Not Start</option>
                    <option value="COMPLETED" ${param.status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                </select>
            </div>
            <div class="col-md-2">
                <select name="priority" class="form-select">
                    <option value="">All Priorities</option>
                    <option value="HIGH" ${param.priority == 'HIGH' ? 'selected' : ''}>High</option>
                    <option value="MEDIUM" ${param.priority == 'MEDIUM' ? 'selected' : ''}>Medium</option>
                    <option value="LOW" ${param.priority == 'LOW' ? 'selected' : ''}>Low</option>
                </select>
            </div>
            <div class="col-md-2">
                <select name="budget" class="form-select">
                    <option value="">All Budgets</option>
                    <option value="0-1000" ${param.budget == '0-1000' ? 'selected' : ''}>$0 - $1,000</option>
                    <option value="1000-5000" ${param.budget == '1000-5000' ? 'selected' : ''}>$1,000 - $5,000</option>
                    <option value="5000-10000" ${param.budget == '5000-10000' ? 'selected' : ''}>$5,000 - $10,000</option>
                    <option value="10000+" ${param.budget == '10000+' ? 'selected' : ''}>$10,000+</option>
                </select>
            </div>
            <div class="col-md-3">
                <button type="submit" class="btn btn-primary">Filter</button>
                <a href="${pageContext.request.contextPath}/projects" class="btn btn-outline-secondary ms-2">Reset</a>
            </div>
        </div>
    </form>
    
    <!-- Rest of your code -->
<c:if test="${not empty sessionScope.userId || sessionScope.isLoggedIn}">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <a href="${pageContext.request.contextPath}/projects/new" class="btn btn-primary">Create New Project</a>
                <a href="${pageContext.request.contextPath}/Auth?action=logout" class="btn btn-outline-danger">Logout</a>
            </div>
</c:if>
        <div class="row">
<c:choose>
    <c:when test="${empty projects}">
            <div class="col-12 text-center mt-4">
                <div class="alert alert-info">
                    No projects found matching your criteria. <a href="${pageContext.request.contextPath}/projects">Reset filters</a> to see all projects.
                </div>
            </div>
    </c:when>
    <c:otherwise>
        <c:forEach items="${projects}" var="project">
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">${project.project_name}</h5>
                            <p class="card-text">${project.description}</p>
                            <p>Status: ${project.status.displayName}</p>
                            <p>Priority: ${project.priority.displayName}</p>
                            <p>Start Date: <fmt:formatDate value="${project.start_date}" pattern="dd/MM/yyyy"/></p>
                            <p>End Date: <fmt:formatDate value="${project.end_date}" pattern="dd/MM/yyyy"/></p>
                            <p>Budget: $${project.budget}</p>
                            <a href="${pageContext.request.contextPath}/projects/tasks/${project.project_id}" class="btn btn-info">View Tasks</a>
                             <a href="${pageContext.request.contextPath}/projects/remove/${project.project_id}" class="btn btn-danger">Close</a>
                        </div>
                    </div>
                </div>
        </c:forEach>
    </c:otherwise>
</c:choose>
        </div>
    </div>
        
    <script>
        document.querySelectorAll('.btn-danger').forEach(function(button) {
            button.addEventListener('click', function(e) {
                if (!confirm('Are you sure you want to close this project?')) {
                    e.preventDefault();
                }
            });
        });
    </script>
</body>
</html>
