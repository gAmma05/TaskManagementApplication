<%-- 
    Document   : forgetpassword
    Created on : Feb 17, 2025, 6:10:48 PM
    Author     : gAmma
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forget Password</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <h1>Forget Password</h1>
            <c:if test="${not empty emailDup}">
                <p style="color:red;">${emailDup}</p>
            </c:if>
            <form action="${pageContext.request.contextPath}/Auth" method="POST">
                <fieldset>
                    <legend>Insert your email to set a new password</legend>

                    <label>Email:</label>
                    <input type="email" name="email" required>
                    <br>

                </fieldset>

                <br>
                <input type="hidden" name="action" value="fp-confirm-email">
                <input type="submit" name="Confirm">


            </form>

            <br>
            <form action="${pageContext.request.contextPath}/Auth" method="GET" style="display:inline;">
                <button type="submit">Back</button>
            </form>
        </div>
    </body>
</html>
