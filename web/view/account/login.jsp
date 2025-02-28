<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title> 
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .card {
                box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.5);
                border-radius: 1rem;
                backdrop-filter: blur(30px);
            }
            .btn-aqua {
                background: linear-gradient(to right, #00c6ff, #0072ff);
                color: white;
                border: none;
                transition: 0.3s;
            }
            .btn-aqua:hover {
                background: linear-gradient(to right, #0072ff, #00c6ff);
            }
        </style>
    </head>
    <body>
        <section class="vh-100 gradient-custom">
            <div class="container py-5 h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                        <div class="card bg-dark text-white">
                            <div class="card-body p-5 text-center">
                                <div class="mb-md-5 mt-md-4 pb-5">
                                    <h2 class="fw-bold mb-2 text-uppercase">Login</h2>
                                    <p class="text-white-50 mb-5">Please enter your login and password!</p>

                                    <c:if test="${not empty loginGood}">
                                        <p style="color: greenyellow;">${loginGood}</p>
                                    </c:if>
                                    <c:if test="${not empty loginBad}">
                                        <p style="color: red;">${loginBad}</p>
                                    </c:if>
                                    <c:if test="${not empty errorList}">
                                        <c:forEach var="error" items="${errorList}">
                                            <p style="color: red;">${error}</p>
                                            <br>
                                        </c:forEach>
                                    </c:if>

                                    <form action="Auth" method="POST">
                                        <div class="form-outline form-white mb-4">
                                            <label class="form-label">Username</label>
                                            <input type="text" name="username" class="form-control form-control-lg" value="${param.username}" required>
                                        </div>
                                        <div class="form-outline form-white mb-4">
                                            <label class="form-label">Password</label>
                                            <input type="password" name="password" class="form-control form-control-lg" required>
                                        </div>

                                        <p class="small mb-5 pb-lg-2"><a class="text-white-50" href="Auth?action=forget-password">Forgot password?</a></p>
                                        <input type="hidden" name="action" value="login">
                                        <button class="btn btn-aqua btn-lg px-5" type="submit">Login</button>
                                    </form>

                                    <div class="mt-4">
                                        <p class="mb-0">Don't have an account? <a href="Auth?action=register" class="text-white-50 fw-bold">Sign Up</a></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </body>
</html>
