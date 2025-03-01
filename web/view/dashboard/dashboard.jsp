<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .gradient-custom {
                background: linear-gradient(to right, #ff7e5f, #feb47b);
            }
        </style>
    </head>
    <body class="gradient-custom">

        <section class="vh-100">
            <div class="container py-5 h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                        <div class="card bg-dark text-white" style="border-radius: 1rem;">
                            <div class="card-body p-5 text-center">

                                <c:choose>
                                    <c:when test="${sessionScope.isLoggedIn}">
                                        <h1 class="fw-bold mb-4 text-uppercase">Welcome, ${sessionScope.fullName}!</h1>

                                        <div class="mt-4">
                                            <a href="/projects" class="btn btn-outline-light btn-lg px-5 me-2">Go to Projects</a>
                                            <a href="Auth?action=logout" class="btn btn-outline-light btn-lg px-5">Logout</a>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <h1 class="fw-bold mb-4 text-uppercase">You are not logged in.</h1>
                                        <a href="${pageContext.request.contextPath}/Auth" class="btn btn-outline-light btn-lg px-5">Return to login</a>
                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
