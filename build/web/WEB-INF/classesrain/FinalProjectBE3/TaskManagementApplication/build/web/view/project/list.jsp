<%-- 
    Document   : list.jsp
    Created on : Feb 11, 2025, 4:36:48 PM
    Author     : asus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <a href="${pageContext.request.contextPath}/projects/new" class="btn btn-primary mb-3">Create New Project</a>
        
        <div class="row">
            <c:forEach items="${projects}" var="project">
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">${project.project_name}</h5>
                            <p class="card-text">${project.description}</p>
                            <p>Status: ${project.status}</p>
                            <p>Priority: ${project.priority}</p>
                            <p>Start Date: <fmt:formatDate value="${project.start_date}" pattern="dd/MM/yyyy"/></p>
                            <p>End Date: <fmt:formatDate value="${project.end_date}" pattern="dd/MM/yyyy"/></p>
                            <p>Budget: $${project.budget}</p>
                            <a href="${pageContext.request.contextPath}/projects/tasks/${project.project_id}" class="btn btn-info">View Tasks</a>
                            <a href="${pageContext.request.contextPath}/projects/new" class="btn btn-primary mb-3">Create New Project</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>
