<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Member Dashboard - Other Projects</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
            <h3>List of other projects in the company</h3>
            <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <th>Project Name</th>
                        <th>Manager</th>
                        <th>Status</th>
                        <th>Budget</th>
                        <th>Created At</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="project" items="${unenrolledProjects}">
                        <tr>
                            <td>${project.projectName}</td>
                            <td>${managerUsernames[project.projectId]}</td>
                            <td>${project.status}</td>
                            <td><fmt:formatNumber value="${project.budget}" type="currency"/></td>
                            <td><fmt:formatDate value="${project.createdAt}" pattern="MM/dd/yyyy"/></td>
                            <td>
                                <button class="btn btn-success btn-sm enroll-btn" data-bs-toggle="modal" data-bs-target="#enrollProjectModal_${project.projectId}">Enroll</button>
                                <button class="btn btn-info btn-sm details-btn" data-bs-toggle="modal" data-bs-target="#detailsProjectModal_${project.projectId}">Details</button>
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
                                            <input type="hidden" name="tab" value="otherProjects">
                                            <button type="submit" class="btn btn-success">Enroll</button>
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Project Details Modal -->
                        <div class="modal fade" id="detailsProjectModal_${project.projectId}" tabindex="-1" aria-labelledby="detailsProjectModalLabel_${project.projectId}" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="detailsProjectModalLabel_${project.projectId}">Project Details - ${project.projectName}</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <p class="fw-bold">Project ID</p>
                                                    <p>${project.projectId}</p>
                                                </div>
                                                <div class="mb-3">
                                                    <p class="fw-bold">Project Name</p>
                                                    <p>${project.projectName}</p>
                                                </div>
                                                <div class="mb-3">
                                                    <p class="fw-bold">Description</p>
                                                    <p>${project.description}</p>
                                                </div>
                                                <div class="mb-3">
                                                    <p class="fw-bold">Manager Name</p>
                                                    <p>${managerUsernames[project.projectId]}</p>
                                                </div>
                                                <div class="mb-3">
                                                    <p class="fw-bold">Budget</p>
                                                    <p><fmt:formatNumber value="${project.budget}" type="currency"/></p>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="mb-3">
                                                    <p class="fw-bold">Status</p>
                                                    <p>${project.status}</p>
                                                </div>
                                                <div class="mb-3">
                                                    <p class="fw-bold">Start Date</p>
                                                    <p><fmt:formatDate value="${project.startDate}" pattern="MM/dd/yyyy"/></p>
                                                </div>
                                                <div class="mb-3">
                                                    <p class="fw-bold">End Date</p>
                                                    <p><fmt:formatDate value="${project.endDate}" pattern="MM/dd/yyyy"/></p>
                                                </div>
                                                <div class="mb-3">
                                                    <p class="fw-bold">Created At</p>
                                                    <p><fmt:formatDate value="${project.createdAt}" pattern="MM/dd/yyyy"/></p>
                                                </div>
                                                <div class="mb-3">
                                                    <p class="fw-bold">Updated At</p>
                                                    <p><fmt:formatDate value="${project.updatedAt}" pattern="MM/dd/yyyy"/></p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty unenrolledProjects}">
                        <tr><td colspan="7">No projects available.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
</body>
</html>