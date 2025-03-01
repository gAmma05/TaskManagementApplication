<%-- 
    Document   : addtask
    Created on : Feb 27, 2025, 9:14:46 PM
    Author     : thien
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add task</title>
    </head>
    <body>
        <h2>Add New Task</h2>
        <form action="TaskServlet" method="post">
            <label for="name">Task Name:</label>
            <input type="text" id="name" name="name" required><br><br>

            <label for="description">Description:</label><br>
            <textarea id="description" name="description" rows="4" cols="50" required></textarea><br><br>

            <label for="deadline">Deadline:</label>
            <input type="date" id="deadline" name="deadline" required><br><br>

            <label>Priority:</label><br>
            <input type="radio" name="priority" value="LOW" checked> Low <br>
            <input type="radio" name="priority" value="MEDIUM"> Medium <br>
            <input type="radio" name="priority" value="HIGH"> High <br><br>

            <label for="member">Assign to Member:</label>
            <select id="member" name="member" required>
                <option value="">Select a member</option>
                <option value="member1">Member 1</option>
                <option value="member2">Member 2</option>
                <option value="member3">Member 3</option>
            </select><br><br>

            <input type="submit" value="Add Task">
        </form>
    </body>
</html>
