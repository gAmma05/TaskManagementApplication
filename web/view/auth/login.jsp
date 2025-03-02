<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login - MyApp</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    </head>
    <body>
        <div class="container d-flex justify-content-center align-items-center vh-100">
            <div class="card shadow-lg p-4" style="width: 350px;">
                <h3 class="text-center mb-3">Login</h3>

                <%-- Display general error if exists --%>
                <c:if test="${not empty generalError}">
                    <div class="alert alert-danger p-2 text-center">
                        <c:out value="${generalError}"/>
                    </div>
                </c:if>

                <%-- Display the notification if success or fail --%>
                <c:if test="${not empty success}">
                    <div class="alert alert-danger p-2 text-center">
                        <c:out value="${success}"/>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-3">
                        <label class="form-label">Username</label>
                        <input type="text" name="username" class="form-control" value="${param.username}" required>
                        <c:if test="${not empty usernameError}">
                            <small class="text-danger">${usernameError}</small>
                        </c:if>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Password</label>
                        <input type="password" name="password" class="form-control" required>
                        <c:if test="${not empty passwordError}">
                            <small class="text-danger">${passwordError}</small>
                        </c:if>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Login</button>
                </form>

                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/view/auth/register.jsp" class="btn btn-outline-success w-100 mb-2">Register</a>
                    <a href="${pageContext.request.contextPath}/view/auth/reset-password.jsp" class="btn btn-link">Forgot Password?</a>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>