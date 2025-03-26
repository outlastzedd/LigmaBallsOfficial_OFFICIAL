package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Orders;
import orderDAO.OrderDAO;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderManagementServlet", urlPatterns = {"/orderManagement"})
public class OrderManagementServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String type = request.getParameter("type");
            if (type == null || type.isEmpty()) {
                type = "all";
            }

            String search = request.getParameter("search");
            List<Orders> allOrders;
            if (search != null && !search.trim().isEmpty()) {
                try {
                    // Nếu search là số, tìm theo orderID hoặc userID
                    int searchId = Integer.parseInt(search);
                    allOrders = orderDAO.searchOrdersById(searchId);
                } catch (NumberFormatException e) {
                    // Nếu không phải số, tìm theo fullName
                    allOrders = orderDAO.searchOrdersByName(search);
                }
            } else {
                allOrders = orderDAO.getAllOrders();
            }

            List<Orders> codOrders = orderDAO.getOrdersByPaymentMethod(1);
            List<Orders> vnpayOrders = orderDAO.getOrdersByPaymentMethod(2);

            request.setAttribute("allOrders", allOrders);
            request.setAttribute("vnpayOrders", vnpayOrders);
            request.setAttribute("codOrders", codOrders);
            request.setAttribute("activeTab", type);

            request.getRequestDispatcher("/ligmaShop/admin/orderManagement.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error retrieving order data: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}