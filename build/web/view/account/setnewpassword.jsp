<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Set new password for the user</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            /*            .gradient-custom {
                            background: linear-gradient(to right, #ff7e5f, #feb47b);
                        }*/
        </style>
    </head>
    <body class="gradient-custom">

        <section class="vh-100">
            <div class="container py-5 h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                        <div class="card bg-dark text-white" style="border-radius: 1rem;">
                            <div class="card-body p-5 text-center">

                                <c:if test="${not empty username}">
                                    <p>Welcome:</p>
                                    <p style="color:white">${username}</p>
                                </c:if>

                                <c:if test="${not empty errorList}">
                                    <c:forEach var="error" items="${errorList}">
                                        <p style="color: red;">${error}</p>
                                        <br>
                                    </c:forEach>
                                </c:if>

                                <c:choose>
                                    <c:when test="${empty email}">
                                        <h1>You haven't entered the required input into the field. Try again.</h1>
                                        <a href="${pageContext.request.contextPath}/Auth" class="btn btn-primary">Return to login</a>
                                    </c:when>
                                    <c:otherwise>
                                        <h1 class="fw-bold mb-4 text-uppercase">Set a new password</h1>
                                        <form action="${pageContext.request.contextPath}/ForgetPassword" method="POST">

                                            <fieldset>
                                                <div class="form-outline form-white mb-4">
                                                    <label for="new-password" class="form-label">New password:</label>
                                                    <input type="password" name="new-password" id="new-password" class="form-control form-control-lg" required>
                                                </div>

                                                <div class="form-outline form-white mb-4">
                                                    <label for="confirm-new-password" class="form-label">Confirm new password:</label>
                                                    <input type="password" name="confirm-new-password" id="confirm-new-password" class="form-control form-control-lg" required>
                                                </div>
                                            </fieldset>

                                            <input type="hidden" name="username" value="${username}">
                                            <input type="hidden" name="email" value="${email}">
                                            <input type="hidden" name="action" value="confirm-forget-password">
                                            <button type="submit" class="btn btn-outline-light btn-lg px-5">Confirm</button>
                                        </form>

                                        <br>

                                        <form action="${pageContext.request.contextPath}/Auth" method="GET" style="display:inline;">
                                            <button type="submit" class="btn btn-outline-light btn-lg px-5">Back</button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </body>
</html>
