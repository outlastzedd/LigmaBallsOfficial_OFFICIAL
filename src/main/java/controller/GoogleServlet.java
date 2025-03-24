package controller;

import service.GoogleLogin;
import entity.GoogleAccount;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;

import userDAO.UserDAO;

@WebServlet(name = "LoginServlet", urlPatterns = {"/logingg"})
public class GoogleServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();

        String error = request.getParameter("error");
        if (error != null) {
            if (error.equalsIgnoreCase("access_denied")) {
                request.setAttribute("message", "Login was cancelled");
                request.setAttribute("messageType", "warning");
            } else {
                request.setAttribute("message", "Login failed");
                request.setAttribute("messageType", "danger");
            }
            request.getRequestDispatcher("ligmaShop/login/register.jsp").forward(request, response);
            return;
        }

        try {
            String code = request.getParameter("code");
            if (code != null) {
                GoogleLogin gg = new GoogleLogin();
                String accessToken = gg.getToken(code);
                GoogleAccount acc = gg.getUserInfo(accessToken);
                if (acc != null) {
                    //1 line to store user infor in session
                    //redirect to homepage
                    Users user = userDAO.checkLogin(acc.getEmail());
                    if (user != null) {
                        session.setAttribute("user", user);
                        request.getRequestDispatcher("/test").forward(request, response);
                    } else {
                        // checkRegister(name, email, phone, password);
                        userDAO.checkRegister(acc.getName(), acc.getEmail(), "", "abc@123");
                        // request.setAttribute("registered", "Registered successfully!");
                        session.setAttribute("user", user);
                        request.getRequestDispatcher("/test").forward(request, response);
                    }
                } else {
                    request.setAttribute("message", "Failed to get user information");
                    request.setAttribute("messageType", "danger");
                    request.getRequestDispatcher("ligmaShop/login/register.jsp").forward(request, response);
                }
            }
        } catch (ServletException | IOException e) {
            request.setAttribute("message", "An error has occured" + e.getMessage());
            request.getRequestDispatcher("ligmaShop/login/register.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
