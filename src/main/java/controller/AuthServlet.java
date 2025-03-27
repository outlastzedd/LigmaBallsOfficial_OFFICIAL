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

import java.util.Random;

import model.Users;
import model.Cart;
import model.Cartitems;
import service.EmailService;

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

    protected void forgotPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");
        String userOTP = request.getParameter("otp"); // Mã OTP người dùng nhập
        HttpSession session = request.getSession();

        UserDAO userDAO = new UserDAO();
        EmailService emailService = new EmailService();

        // Kiểm tra xem email có tồn tại không
        boolean emailExists = userDAO.isEmailExists(email);
        if (!emailExists) {
            request.setAttribute("message", "Email không tồn tại!");
            request.getRequestDispatcher("ligmaShop/login/forgotPassword.jsp?error=wrong_info").forward(request, response);
            return;
        }

        // Nếu chưa có OTP trong session, tạo và gửi OTP
        if (session.getAttribute("otp") == null) {
            String otp = generateOTP();
            session.setAttribute("otp", otp);
            session.setAttribute("email", email);

            // Gửi email chứa OTP
            String subject = "Mã OTP để đặt lại mật khẩu";
            String messageContent = "<h3>Mã OTP của bạn là: " + otp + "</h3>"
                    + "<p>Vui lòng sử dụng mã này để đặt lại mật khẩu. Mã có hiệu lực trong 10 phút.</p>";
            boolean emailSent = emailService.send(email, subject, messageContent);

            if (emailSent) {
                request.setAttribute("message", "Mã OTP đã được gửi đến email của bạn!");
                request.getRequestDispatcher("ligmaShop/login/verifyOTP.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "Gửi email thất bại, vui lòng thử lại!");
                request.getRequestDispatcher("ligmaShop/login/forgotPassword.jsp?error=send_failed").forward(request, response);
            }
        }
        // Nếu đã có OTP và người dùng nhập OTP để xác thực
        else if (userOTP != null) {
            String storedOTP = (String) session.getAttribute("otp");

            if (userOTP.equals(storedOTP)) {
                // OTP đúng, kiểm tra mật khẩu mới
                if (!newPass.equals(confirmPass)) {
                    request.setAttribute("message", "Mật khẩu không khớp!");
                    request.getRequestDispatcher("ligmaShop/login/verifyOTP.jsp?error=wrong_info").forward(request, response);
                } else {
                    Users user = userDAO.checkLogin(email); // Giả sử điều này lấy người dùng theo email
                    user.setPassword(newPass);
                    userDAO.updateUser(user);

                    // Xóa OTP khỏi session sau khi thành công
                    session.removeAttribute("otp");
                    session.removeAttribute("email");

                    request.setAttribute("message", "Cập nhật mật khẩu thành công!");
                    response.sendRedirect(request.getContextPath() + "/ligmaShop/login/signIn.jsp?success=forgot_password");
                }
            } else {
                request.setAttribute("message", "Mã OTP không đúng!");
                request.getRequestDispatcher("ligmaShop/login/verifyOTP.jsp?error=wrong_otp").forward(request, response);
            }
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo số ngẫu nhiên 6 chữ số
        return String.valueOf(otp);
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
