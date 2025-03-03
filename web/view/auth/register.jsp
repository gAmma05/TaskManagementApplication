<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - MyApp</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body>
    <div class="container d-flex justify-content-center align-items-center vh-100">
        <div class="card shadow-lg p-4" style="width: 400px;">
            <h3 class="text-center mb-3">Register</h3>

            <%-- Display general error if exists --%>
            <c:if test="${not empty generalError}">
                <div class="text-danger" role="alert">
                    ${generalError}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post" onsubmit="return validateForm()">
                <div class="mb-3">
                    <label class="form-label">Username</label>
                    <input type="text" name="username" class="form-control" value="${param.username}" required>
                    <c:if test="${not empty usernameError}">
                        <small class="text-danger">${usernameError}</small>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" id="password" name="password" class="form-control" required>
                    <c:if test="${not empty passwordError}">
                        <small class="text-danger">${passwordError}</small>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label class="form-label">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
                    <small id="passwordError" class="text-danger d-none">Passwords do not match!</small>
                </div>
                <div class="mb-3">
                    <label class="form-label">Full Name</label>
                    <input type="text" name="fullname" class="form-control" value="${param.fullname}" required>
                    <c:if test="${not empty fullnameError}">
                        <small class="text-danger">${fullnameError}</small>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" class="form-control" value="${param.email}" required>
                    <c:if test="${not empty emailError}">
                        <small class="text-danger">${emailError}</small>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label class="form-label">Phone Number (Optional)</label>
                    <input type="text" name="phone_number" class="form-control" value="${param.phone_number}">
                    <c:if test="${not empty phone_numberError}">
                        <small class="text-danger">${phone_numberError}</small>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label class="form-label">Role</label>
                    <div>
                        <input type="radio" id="roleMember" name="role" value="Member" checked>
                        <label for="roleMember">Member</label>
                    </div>
                    <div>
                        <input type="radio" id="roleManager" name="role" value="Manager">
                        <label for="roleManager">Manager</label>
                    </div>
                    <c:if test="${not empty roleError}">
                        <small class="text-danger">${roleError}</small>
                    </c:if>
                </div>
                <button type="submit" class="btn btn-primary w-100">Register</button>
            </form>

            <div class="text-center mt-3">
                <a href="${pageContext.request.contextPath}/" class="btn btn-link">Already have an account? Login</a>
            </div>
        </div>
    </div>

    <script>
        function validateForm() {
            let password = document.getElementById("password").value;
            let confirmPassword = document.getElementById("confirmPassword").value;
            let errorText = document.getElementById("passwordError");

            if (password !== confirmPassword) {
                errorText.classList.remove("d-none");
                return false;
            }
            errorText.classList.add("d-none");
            return true;
        }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
