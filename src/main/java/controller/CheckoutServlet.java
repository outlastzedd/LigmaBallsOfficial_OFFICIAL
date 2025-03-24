package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;
import service.Config;

import static service.Config.hmacSHA512;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private static final String vnp_TmnCode = "X3CYJAHU";
    private static final String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String vnp_ReturnUrl = "http://localhost:8080/LigmaBallsOfficial/vnpayreturn";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try {
            // Lấy thông tin từ request
            String paymentMethod = request.getParameter("paymentMethod");
            String amount = request.getParameter("amount");
            String orderInfo = request.getParameter("orderInfo");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String products = request.getParameter("products");

            // In chi tiết tham số nhận được
            System.out.println("All parameters received:");
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (parameterMap.isEmpty()) {
                System.out.println("ParameterMap is empty!");
            } else {
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    System.out.println(entry.getKey() + ": " + String.join(",", entry.getValue()));
                }
            }

            // Kiểm tra dữ liệu đầu vào
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                jsonResponse.put("code", "01");
                jsonResponse.put("message", "Payment method is required");
                out.print(jsonResponse.toString());
                return;
            }

            if (amount == null || amount.trim().isEmpty() || orderInfo == null || orderInfo.trim().isEmpty()
                    || fullName == null || email == null || phone == null || address == null || products == null) {
                jsonResponse.put("code", "01");
                jsonResponse.put("message", "All fields (amount, orderInfo, fullName, email, phone, address, products) are required");
                out.print(jsonResponse.toString());
                return;
            }

            int amountValue;
            try {
                amount = amount.replaceAll("[^0-9]", "");
                amountValue = Integer.parseInt(amount);
                if (amountValue <= 0) {
                    throw new IllegalArgumentException("Amount must be greater than 0");
                }
            } catch (NumberFormatException e) {
                jsonResponse.put("code", "02");
                jsonResponse.put("message", "Invalid amount format");
                out.print(jsonResponse.toString());
                return;
            }

            // Lưu thông tin vào session
            HttpSession session = request.getSession();
            Map<String, String> pendingTransaction = new HashMap<>();
            pendingTransaction.put("vnp_Amount", String.valueOf(amountValue * 100));
            pendingTransaction.put("fullName", fullName);
            pendingTransaction.put("email", email);
            pendingTransaction.put("phone", phone);
            pendingTransaction.put("address", address);
            pendingTransaction.put("products", products);
            pendingTransaction.put("totalAmount", String.valueOf(amountValue));
            pendingTransaction.put("paymentMethod", paymentMethod); // Đảm bảo lưu paymentMethod

            // Xử lý theo phương thức thanh toán
            if ("vnpay".equals(paymentMethod)) {
                // Xử lý thanh toán VNPay
                String orderId = String.valueOf(System.currentTimeMillis());
                pendingTransaction.put("vnp_TxnRef", orderId);
                session.setAttribute("pendingTransaction", pendingTransaction);

                // Tạo tham số thanh toán
                Map<String, String> vnp_Params = new TreeMap<>();
                vnp_Params.put("vnp_Version", "2.1.0");
                vnp_Params.put("vnp_Command", "pay");
                vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
                vnp_Params.put("vnp_Amount", String.valueOf(amountValue * 100));
                vnp_Params.put("vnp_CurrCode", "VND");
                vnp_Params.put("vnp_TxnRef", orderId);
                vnp_Params.put("vnp_OrderInfo", orderInfo);
                vnp_Params.put("vnp_OrderType", "other");
                vnp_Params.put("vnp_Locale", "vn");
                vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
                vnp_Params.put("vnp_IpAddr", request.getRemoteAddr());
                vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

                StringBuilder hashData = new StringBuilder();
                for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                    if (hashData.length() > 0) {
                        hashData.append("&");
                    }
                    hashData.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
                }
                String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
                vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

                StringBuilder paymentUrl = new StringBuilder(vnp_Url);
                paymentUrl.append("?");
                for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                    if (paymentUrl.indexOf("?") != paymentUrl.length() - 1) {
                        paymentUrl.append("&");
                    }
                    paymentUrl.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
                }

                jsonResponse.put("code", "00");
                jsonResponse.put("message", "Success");
                jsonResponse.put("data", paymentUrl.toString());
                out.print(jsonResponse.toString());
                request.setAttribute("paymentUrl", paymentUrl.toString());
                request.getRequestDispatcher("ligmaShop/payment/redirect.jsp").forward(request, response);

            } else if ("cod".equals(paymentMethod)) {
                // Lưu paymentMethod vào pendingTransaction
                session.setAttribute("pendingTransaction", pendingTransaction);

                // Sử dụng forward thay vì redirect để giữ POST request
                request.getRequestDispatcher("/vnpayreturn").forward(request, response);
            } else {
                jsonResponse.put("code", "03");
                jsonResponse.put("message", "Invalid payment method");
                out.print(jsonResponse.toString());
            }

        } catch (Exception e) {
            jsonResponse.put("code", "99");
            jsonResponse.put("message", "Server error: " + e.getMessage());
            out.print(jsonResponse.toString());
        } finally {
            out.flush();
            out.close();
        }
    }
}