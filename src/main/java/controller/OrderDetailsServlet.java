package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Orderdetails;
import model.Orders;
import model.Users;
import orderDAO.OrderDAO;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderDetailsServlet", urlPatterns = {"/orderDetails"})
public class OrderDetailsServlet extends HttpServlet {
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("ligmaShop/login/signIn.jsp");
            return;
        }

        String orderIdStr = request.getParameter("orderID");
        boolean isAdmin = "admin".equalsIgnoreCase(user.getRole()); // Kiểm tra role từ entity Users

        if (orderIdStr != null && !orderIdStr.isEmpty()) {
            // Xử lý chi tiết một đơn hàng cụ thể
            int orderID;
            try {
                orderID = Integer.parseInt(orderIdStr);
            } catch (NumberFormatException e) {
                throw new ServletException("Invalid orderID: " + orderIdStr);
            }

            Orders order = orderDAO.getOrderDetailsById(orderID);
            if (order == null) {
                throw new ServletException("Order not found with ID: " + orderID);
            }

            // Kiểm tra quyền: Admin xem được tất cả, user chỉ xem đơn của mình
            if (!isAdmin && !order.getUserID().getUserID().equals(user.getUserID())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem chi tiết đơn hàng này.");
                return;
            }

            List<Orderdetails> orderDetailsList = (List<Orderdetails>) order.getOrderdetailsCollection();
            request.setAttribute("orderDetailsList", orderDetailsList);
            request.setAttribute("selectedOrder", order);
            request.getRequestDispatcher("/ligmaShop/user/orderHistory.jsp").forward(request, response);
        } else if (isAdmin) {
            // Admin không có orderID -> Chuyển hướng đến orderManagement
            response.sendRedirect(request.getContextPath() + "/orderManagement");
            return;
        } else {
            // User không có orderID -> Hiển thị lịch sử giao dịch của họ
            List<Orderdetails> orderDetailsList = orderDAO.getOrderDetailsByUserId(user.getUserID());
            request.setAttribute("orderDetailsList", orderDetailsList);
            request.setAttribute("selectedOrder", null);
            request.getRequestDispatcher("/ligmaShop/user/orderHistory.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}