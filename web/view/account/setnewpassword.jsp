<%-- 
    Document   : setnewpassword
    Created on : Feb 17, 2025, 7:34:06 PM
    Author     : gAmma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Set new password for the user</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>

        <c:if test="${not empty errorList}">
            <ul>
                <c:forEach var="error" items="${errorList}">
                    <li><font color="red">${error}</font></li>
                    </c:forEach>
            </ul>
        </c:if>


        <div class="container mt-5">

            <c:if test="${not empty username}">
                <p>Welcome:</p>
                <p style="color:blue">${username}</p>
            </c:if>

            <c:if test="${not empty errorList}">
                <ul>
                    <c:forEach var="error" items="${errorList}">
                        <li><font color="red">${error}</font></li>
                        </c:forEach>
                </ul>
            </c:if>

            <c:choose>
                <c:when test="${empty email}">
                    <h1>You haven't entered the required input into field. Try again.</h1>
                    <a href="${pageContext.request.contextPath}/Auth" class="btn btn-primary">Return to login</a>
                </c:when>
                <c:otherwise>
                    <h1>Set a new password</h1>
                    <form action="${pageContext.request.contextPath}/ForgetPassword" method="POST">

                        <fieldset>
                            <label>New password:</label>
                            <input type="password" name="new-password" required>

                            <br><br>

                            <label>Confirm new password:</label>
                            <input type="password" name="confirm-new-password" required>
                        </fieldset>

                        <input type="hidden" name="username" value="${username}">
                        <input type="hidden" name="email" value="${email}">
                        <input type="hidden" name="action" value="confirm-forget-password">
                        <input type="submit" name="Confirm">
                    </form>

                    <form action="${pageContext.request.contextPath}/Auth" method="GET" style="display:inline;">
                        <button type="submit">Back</button>
                    </form>

                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>
