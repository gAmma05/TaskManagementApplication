/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.task;

import dao.implementations.TaskDAO;
import enums.TaskPriority;
import enums.TaskStatus;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Task;
import utils.DBConnection;

/**
 *
 * @author thien
 */
public class UpdateTaskServlet extends HttpServlet {
    private TaskDAO taskDAO;

    @Override
    public void init() throws ServletException {
        try {
            taskDAO = new TaskDAO(DBConnection.getConnection());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UpdateTaskServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UpdateTaskServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateTaskServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Task task = new Task();
            task.setTaskId(request.getParameter("taskId"));
            task.setProjectId(request.getParameter("projectId"));
            task.setTaskName(request.getParameter("taskName"));
            task.setDescription(request.getParameter("description"));
            task.setMemberId(request.getParameter("memberId")); // Changed from assignerId
            task.setPriority(TaskPriority.valueOf(request.getParameter("priority")));
            task.setStatus(TaskStatus.valueOf(request.getParameter("status")));
            task.setDeadline((java.sql.Date) new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("deadline")));
            task.setUpdatedAt((java.sql.Date) new Date());
            
            taskDAO.updateTask(task);
            String tab = request.getParameter("tab");
            response.sendRedirect(request.getContextPath() + "/projectDetails?projectId=" + task.getProjectId() + "&tab=" + tab);
        } catch (ParseException ex) {
            Logger.getLogger(UpdateTaskServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
