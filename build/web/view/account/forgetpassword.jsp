<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forget Password</title>
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .card {
                box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.5);
                border-radius: 1rem;
                backdrop-filter: blur(10px);
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
                                    <h2 class="fw-bold mb-2 text-uppercase">Forget Password</h2>
                                    <form action="${pageContext.request.contextPath}/ForgetPassword" method="POST">
                                        <fieldset>
                                            <p class="text-white-50 mb-5">Insert your email to set a new password</p>
                                            <c:if test="${not empty errorList}">
                                                <c:forEach var="error" items="${errorList}">
                                                    <p style="color: red;">${error}</p>
                                                    <br>
                                                </c:forEach>
                                            </c:if>

                                            <div class="form-outline form-white mb-4">
                                                <!--<label class="form-label">Email</label>-->
                                                <input type="email" name="email" class="form-control form-control-lg" required>
                                            </div>
                                        </fieldset>
                                        <input type="hidden" name="action" value="fp-confirm-email">
                                        <button class="btn btn-aqua btn-lg px-5" type="submit">Confirm</button>
                                    </form>
                                    <div class="mt-4">
                                        <form action="${pageContext.request.contextPath}/Auth" method="GET">
                                            <button class="btn btn-light" type="submit">Back</button>
                                        </form>
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
