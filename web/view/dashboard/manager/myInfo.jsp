<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Info</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container py-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="text-center">Manager Dashboard</h4>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
        </div>

        <!-- Error Message Display -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>

        <%@ include file="/view/common/navbar.jspf" %>

        <!-- Content -->
        <div class="mt-3">
            <h3>My Information</h3>
            <c:choose>
                <c:when test="${not empty myInfo}">
                    <form action="${pageContext.request.contextPath}/updateUser" method="post" id="userInfoForm">
                        <table class="table table-bordered table-striped" id="userInfoTable">
                            <tr>
                                <th>Username</th>
                                <td>${myInfo.username}</td> <!-- Not editable -->
                            </tr>
                            <tr>
                                <th>Full Name</th>
                                <td contenteditable="true" class="editable-cell" data-field="fullName">${myInfo.fullName}</td>
                            </tr>
                            <tr>
                                <th>Email</th>
                                <td contenteditable="true" class="editable-cell" data-field="email">${myInfo.email}</td>
                            </tr>
                            <tr>
                                <th>Phone</th>
                                <td contenteditable="true" class="editable-cell" data-field="phone">${myInfo.phone}</td>
                            </tr>
                            <tr>
                                <th>Role</th>
                                <td>${myInfo.role}</td> <!-- Not editable -->
                            </tr>
                        </table>
                        <input type="hidden" name="userId" value="${myInfo.userId}">
                        <input type="hidden" name="fullName" id="fullNameInput" value="${myInfo.fullName}">
                        <input type="hidden" name="email" id="emailInput" value="${myInfo.email}">
                        <input type="hidden" name="phone" id="phoneInput" value="${myInfo.phone}">
                        <input type="hidden" name="tab" value="myInfo">
                        <button type="submit" class="btn btn-primary update-btn" id="updateUserBtn">Update</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <p>No user information available.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>