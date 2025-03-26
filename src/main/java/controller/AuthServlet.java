package controller;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Users;
import model.Cart;
import model.Cartitems;

import userDAO.*;
import cartDAO.*;

@WebServlet(name = "AuthServlet", urlPatterns = {"/authservlet"})

public class AuthServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private CartDAO cartDAO = new CartDAO();
    
    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";
        return password.matches(regex);
    }

    protected void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        Users user = userDAO.checkLogin(email, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            session.setAttribute("user", user);

            // Create cart for user
            Cart cart = cartDAO.getCartByUser(user);
            if (cart == null) {
                cart = new Cart(new Date(), new ArrayList<>(), user);
                cartDAO.saveCart(cart);
            }
            session.setAttribute("cart", cart);
            // Set cartItems to session
            Collection<Cartitems> cartItems = cart.getCartitemsCollection();
            if (cartItems == null) {
                cartItems = new ArrayList<>();
                cart.setCartitemsCollection(cartItems);
            }
            session.setAttribute("cartItems", cartItems);

            Cookie cEmail, cPassword, cRememberMe;
            
            if (rememberMe != null && rememberMe.equals("true")) {
                cEmail = new Cookie("email", email);
                cPassword = new Cookie("password", password);
                cRememberMe = new Cookie("rememberMe", rememberMe);
            } else {
                cEmail = new Cookie("email", "");
                cPassword = new Cookie("password", "");
                cRememberMe = new Cookie("rememberMe", "");
            }
            
            // chỉnh thời gian sống của cookie tại đây (đơn vị: giây)
            cEmail.setMaxAge(10);
            cPassword.setMaxAge(10);
            cRememberMe.setMaxAge(10);
            
            response.addCookie(cRememberMe);
            response.addCookie(cPassword);
            response.addCookie(cEmail);
            
            // Check if response is committed
            if (response.isCommitted()) {
                System.out.println("Response already committed, cannot forward!");
                response.sendRedirect(request.getContextPath() + "/test"); // Fallback
                return;
            }
            
            // Debug the RequestDispatcher
            RequestDispatcher dispatcher = request.getRequestDispatcher("/test");
            if (dispatcher == null) {
                System.out.println("RequestDispatcher for '/test' is null!");
                response.sendRedirect(request.getContextPath() + "/test"); // Fallback to redirect
            } else {
                System.out.println("Forwarding to /test");
                dispatcher.forward(request, response);
            }
        } else {
            request.setAttribute("wrong_login_message", "Wrong username or password!");
            request.getRequestDispatcher("ligmaShop/login/signIn.jsp?error=wrong_login").forward(request, response);
        }
    }

    protected void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        if (!isValidEmail(email)) {
            response.sendRedirect("ligmaShop/login/register.jsp?error=invalid_email");
            return;
        }
        Users user = userDAO.checkRegister(fullname, email, phone, password);
        if (user == null) {
            request.setAttribute("user_existed_message", "Email hoặc số điện thoại dã tồn tại trong hệ thống!");
            request.getRequestDispatcher("ligmaShop/login/register.jsp?error=user_existed").forward(request, response);
        } else {
            response.sendRedirect("ligmaShop/login/signIn.jsp?success=registered");
        }
    }

    protected void forgotPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");
        boolean check = userDAO.isEmailExists(email);
        if (!check) {
            request.setAttribute("message", "Email không tồn tại!");
            request.getRequestDispatcher("ligmaShop/login/forgotPassword.jsp?error=wrong_info").forward(request, response);
        } else {
            if (!newPass.equals(confirmPass)) {
                request.setAttribute("message", "Mật khẩu không khớp!");
                request.getRequestDispatcher("ligmaShop/login/forgotPassword.jsp?error=wrong_info").forward(request, response);
            } else {
                Users user = userDAO.checkLogin(email);
                user.setPassword(newPass);
                userDAO.updateUser(user);

                request.setAttribute("message", "Cập nhật thành công!");
                response.sendRedirect("ligmaShop/login/signIn.jsp?success=forgot_password");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // handle logout
        HttpSession session = request.getSession();
        session.invalidate();
        request.getRequestDispatcher("/test").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
            System.out.println("action empty");
        } else {
            System.out.println(action);
        }
        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "register":
                handleRegister(request, response);
                break;
            case "forgotPassword":
                forgotPassword(request, response);
                break;
        }
    }
}
