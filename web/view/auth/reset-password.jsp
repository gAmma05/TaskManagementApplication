<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reset Password - MyApp</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
    <body>
        <div class="container d-flex justify-content-center align-items-center vh-100">
            <div class="card shadow-lg p-4" style="width: 400px;">
                <h3 class="text-center mb-3">Reset Password</h3>

                <%-- Display success message --%>
                <c:if test="${not empty param.success}">
                    <div class="alert alert-success text-center">Password reset successfully!</div>
                </c:if>

                <%-- Display general error if exists --%>
                <c:if test="${not empty generalError}">
                    <div class="alert alert-danger text-center">${generalError}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/reset-password" method="post">
                    <div class="mb-3">
                        <label class="form-label">Enter Your Username</label>
                        <input type="text" name="username" class="form-control" value="${param.username}" required>
                        <c:if test="${not empty usernameError}">
                            <small class="text-danger">${usernameError}</small>
                        </c:if>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">New Password</label>
                        <input type="password" name="newPassword" class="form-control" required>
                        <c:if test="${not empty newPasswordError}">
                            <small class="text-danger">${newPasswordError}</small>
                        </c:if>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Confirm Password</label>
                        <input type="password" name="confirmPassword" class="form-control" required>
                        <c:if test="${not empty confirmPasswordError}">
                            <small class="text-danger">${confirmPasswordError}</small>
                        </c:if>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Reset Password</button>
                </form>

                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/" class="btn btn-link">Back to Login</a>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>