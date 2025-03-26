package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.EmailService;
import model.Cart;
import paymentDAO.PaymentDAO;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "VNPayReturnServlet", urlPatterns = {"/vnpayreturn"})
public class VNPayReturnServlet extends HttpServlet {

    private EmailService emailService;
    private PaymentDAO paymentDAO;

    @Override
    public void init() throws ServletException {
        emailService = new EmailService();
        paymentDAO = new PaymentDAO();
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, String> pendingTransaction = (Map<String, String>) session.getAttribute("pendingTransaction");

        if (pendingTransaction == null) {
            request.setAttribute("message", "Không tìm thấy thông tin giao dịch");
            request.getRequestDispatcher("ligmaShop/payment/result.jsp").forward(request, response);
            return;
        }

        String paymentMethod = pendingTransaction.get("paymentMethod");
        String fullName = pendingTransaction.get("fullName");
        String email = pendingTransaction.get("email");
        String phone = pendingTransaction.get("phone");
        String address = pendingTransaction.get("address");
        String products = pendingTransaction.get("products");

        if ("cod".equals(paymentMethod)) {
            processCodPayment(request, response, pendingTransaction, session);
        } else {
            processVnpayPayment(request, response, pendingTransaction, session);
        }
    }

    private void processCodPayment(HttpServletRequest request, HttpServletResponse response,
                                   Map<String, String> pendingTransaction, HttpSession session)
            throws ServletException, IOException {
        try {
            String transactionId = "COD" + System.currentTimeMillis();
            String amount = pendingTransaction.get("totalAmount");
            String fullName = pendingTransaction.get("fullName");
            String email = pendingTransaction.get("email");
            String phone = pendingTransaction.get("phone");
            String address = pendingTransaction.get("address");
            String products = pendingTransaction.get("products");

            // Process payment in database
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart != null) {
                BigDecimal totalAmount = new BigDecimal(amount);
                paymentDAO.processPayment(pendingTransaction, transactionId, totalAmount, cart, 1);
                session.removeAttribute("cart");
                session.removeAttribute("cartItems");
            } else {
                System.out.println("Cart is null - cannot process order in database");
            }

            // Send confirmation email
            boolean emailSent = sendOrderConfirmationEmail(email, fullName, transactionId, products, amount, address, "COD");

            // Set attributes for result page
            request.setAttribute("message", "Đặt hàng thành công! Bạn sẽ thanh toán khi nhận hàng.");
            request.setAttribute("transactionId", transactionId);
            request.setAttribute("amount", formatCurrency(amount));
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("address", address);
            request.setAttribute("products", products);

            // Clean up session
            session.removeAttribute("pendingTransaction");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Đặt hàng thất bại: " + e.getMessage());
        }

        request.getRequestDispatcher("ligmaShop/payment/result.jsp").forward(request, response);
    }

    private void processVnpayPayment(HttpServletRequest request, HttpServletResponse response,
                                     Map<String, String> pendingTransaction, HttpSession session)
            throws ServletException, IOException {
        try {
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");

            String fullName = pendingTransaction.get("fullName");
            String email = pendingTransaction.get("email");
            String phone = pendingTransaction.get("phone");
            String address = pendingTransaction.get("address");
            String products = pendingTransaction.get("products");
            String amount = pendingTransaction.get("totalAmount");
            String transactionId = request.getParameter("vnp_TransactionNo");

            if ("00".equals(vnp_ResponseCode)) {
                // Payment successful
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart != null) {
                    BigDecimal totalAmount = new BigDecimal(amount);
                    paymentDAO.processPayment(pendingTransaction, transactionId, totalAmount, cart, 2);
                    session.removeAttribute("cart");
                    session.removeAttribute("cartItems");
                } else {
                    System.out.println("Cart is null - cannot process order in database");
                }

                // Send confirmation email
                boolean emailSent = sendOrderConfirmationEmail(email, fullName, transactionId, products, amount, address, "VNPay");

                // Set attributes for result page
                request.setAttribute("message", "Thanh toán thành công!");
            } else {
                // Payment failed
                request.setAttribute("message", "Thanh toán thất bại! Mã lỗi: " + vnp_ResponseCode);
                request.setAttribute("errorCode", vnp_ResponseCode);
            }

            // Set common attributes
            request.setAttribute("transactionId", transactionId);
            request.setAttribute("amount", formatCurrency(amount));
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("address", address);
            request.setAttribute("products", products);

            // Clean up session
            session.removeAttribute("pendingTransaction");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Xử lý thanh toán thất bại: " + e.getMessage());
        }

        request.getRequestDispatcher("ligmaShop/payment/result.jsp").forward(request, response);
    }

    /**
     * Sends an order confirmation email with HTML formatting
     */
    private boolean sendOrderConfirmationEmail(String email, String fullName, String transactionId,
                                               String products, String amount, String address, String paymentMethod) {
        try {
            String subject = "Xác nhận đơn hàng #" + transactionId + " từ LigmaShop";

            // Create HTML email content
            String htmlContent = createOrderConfirmationHtml(fullName, transactionId, products, amount, address, paymentMethod);

            // Send email
            if (emailService != null) {
                return emailService.send(email, subject, htmlContent);
            } else {
                System.err.println("Error sending email: emailService is null");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates HTML content for order confirmation email
     */
    private String createOrderConfirmationHtml(String fullName, String transactionId,
                                               String products, String amount, String address, String paymentMethod) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "    <meta charset='UTF-8'>"
                + "    <style>"
                + "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 20px; }"
                + "        .container { max-width: 600px; margin: 0 auto; border: 1px solid #ddd; border-radius: 5px; padding: 20px; }"
                + "        .header { text-align: center; padding-bottom: 15px; border-bottom: 1px solid #eee; }"
                + "        .logo { max-width: 150px; }"
                + "        h1 { color: #444; margin-top: 20px; }"
                + "        .details { margin: 20px 0; }"
                + "        .details table { width: 100%; border-collapse: collapse; }"
                + "        .details th, .details td { padding: 10px; text-align: left; border-bottom: 1px solid #eee; }"
                + "        .details th { width: 30%; }"
                + "        .footer { margin-top: 20px; text-align: center; color: #777; font-size: 12px; border-top: 1px solid #eee; padding-top: 20px; }"
                + "        .thank-you { font-weight: bold; font-size: 18px; margin: 20px 0; text-align: center; }"
                + "        .order-number { font-weight: bold; color: #0066cc; }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div class='container'>"
                + "        <div class='header'>"
                + "            <h1>LigmaShop</h1>"
                + "            <p>Xác nhận đơn hàng của bạn</p>"
                + "        </div>"
                + "        <p>Xin chào <strong>" + fullName + "</strong>,</p>"
                + "        <p>Cảm ơn bạn đã đặt hàng tại LigmaShop. Đơn hàng của bạn đã được xác nhận và đang được xử lý.</p>"
                + "        <div class='details'>"
                + "            <h2>Chi tiết đơn hàng:</h2>"
                + "            <table>"
                + "                <tr><th>Mã đơn hàng:</th><td class='order-number'>" + transactionId + "</td></tr>"
                + "                <tr><th>Phương thức thanh toán:</th><td>" + paymentMethod + "</td></tr>"
                + "                <tr><th>Tổng tiền:</th><td>" + formatCurrency(amount) + " VNĐ</td></tr>"
                + "                <tr><th>Địa chỉ giao hàng:</th><td>" + address + "</td></tr>"
                + "            </table>"
                + "            <h2>Sản phẩm:</h2>"
                + "            <p>" + formatProductsList(products) + "</p>"
                + "        </div>"
                + "        <p class='thank-you'>Cảm ơn bạn đã mua hàng tại LigmaShop!</p>"
                + "        <div class='footer'>"
                + "            <p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi tại <a href='mailto:support@ligmashop.com'>support@ligmashop.com</a></p>"
                + "            <p>© 2024 LigmaShop. Tất cả các quyền được bảo lưu.</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";
    }

    /**
     * Format product list for better display in email
     */
    private String formatProductsList(String products) {
        // Replace comma separators with HTML break tags
        return products.replace("), ", ")<br>");
    }

    /**
     * Format currency values
     */
    private String formatCurrency(String amount) {
        try {
            double amountValue = Double.parseDouble(amount);
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            return formatter.format(amountValue);
        } catch (NumberFormatException e) {
            return amount;
        }
    }
}
