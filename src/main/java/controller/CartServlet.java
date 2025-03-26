package controller;

import cartDAO.CartDAO;
import dao.DBConnection;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.Cartitems;
import model.Products;
import model.Productsizecolor;
import model.Users;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/ligmaShop/login/signIn.jsp");
            return;
        }

        if (session.getAttribute("csrfToken") == null) {
            String csrfToken = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrfToken);
        }

        try {
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = cartDAO.getCartByUser(user);
                if (cart == null) {
                    cart = new Cart();
                    cart.setUserID(user);
                    cart.setCreatedDate(new Date());
                    cart.setCartitemsCollection(new ArrayList<>());
                    cartDAO.saveCart(cart);
                }
                session.setAttribute("cart", cart);
            }

            Collection<Cartitems> cartItems = (Collection<Cartitems>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = cart.getCartitemsCollection();
                if (cartItems == null) {
                    cartItems = new ArrayList<>();
                    cart.setCartitemsCollection(cartItems);
                }
                session.setAttribute("cartItems", cartItems);
            }

            double totalAmount = 0;
            for (Cartitems item : cartItems) {
                Productsizecolor productSizeColor = item.getProductSizeColorID();
                if (productSizeColor == null) {
                    continue;
                }

                Products product = productSizeColor.getProductID();
                if (product == null) {
                    continue;
                }

                BigDecimal basePrice = product.getPrice();
                if (basePrice == null) {
                    continue;
                }

                BigDecimal adjustedPrice = getBigDecimal(productSizeColor, basePrice);
                double discount = product.getDiscount() != null ? product.getDiscount() : 0.0;
                BigDecimal discountedPrice = adjustedPrice.subtract(
                        adjustedPrice.multiply(BigDecimal.valueOf(discount)).divide(BigDecimal.valueOf(100))
                );

                totalAmount += discountedPrice.doubleValue() * item.getQuantity();
            }

            request.setAttribute("totalAmount", totalAmount);
            request.getRequestDispatcher("/ligmaShop/cart/cart.jsp").forward(request, response);
        } catch (Exception e) {
            session.setAttribute("error", "Không thể tải giỏ hàng: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/ligmaShop/cart/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/ligmaShop/login/signIn.jsp");
            return;
        }

        try {

            String action = request.getParameter("action");
            if (action == null) {
                action = "add";
            }

            if (action.equalsIgnoreCase("buynow")) {
                int productSizeColorID = Integer.parseInt(request.getParameter("productSizeColorID"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                EntityManager em = DBConnection.getEntityManager();
                Productsizecolor productsizecolor = em.find(Productsizecolor.class, productSizeColorID);

                //temp cart for checkout jsp
                //productSizeColor table contains everything needed
                Cartitems buyNowItem = new Cartitems();
                buyNowItem.setProductSizeColorID(productsizecolor);
                buyNowItem.setQuantity(quantity);

                Products product = productsizecolor.getProductID();
                BigDecimal basePrice = product.getPrice();
                double buyNowTotalAmount = 0;
                BigDecimal adjustedPrice = getBigDecimal(productsizecolor, basePrice);
                double discount = product.getDiscount() != null ? product.getDiscount() : 0.0;
                BigDecimal discountedPrice = adjustedPrice.subtract(
                        adjustedPrice.multiply(BigDecimal.valueOf(discount)).divide(BigDecimal.valueOf(100))
                );

                buyNowTotalAmount += discountedPrice.doubleValue() * quantity;

                request.setAttribute("buyNowItem", buyNowItem);
                request.setAttribute("buyNowAdjustedPrice", adjustedPrice);
                request.setAttribute("totalAmount", buyNowTotalAmount);
                request.setAttribute("isBuyNow", true);

                request.getRequestDispatcher("ligmaShop/payment/checkout.jsp").forward(request, response);
                return;
            }

            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = cartDAO.getCartByUser(user);
                if (cart == null) {
                    cart = new Cart();
                    cart.setUserID(user);
                    cart.setCreatedDate(new Date());
                    cart.setCartitemsCollection(new ArrayList<>());
                    cartDAO.saveCart(cart);
                }
                session.setAttribute("cart", cart);
            }

            switch (action) {
                case "add":
                    int productSizeColorID = Integer.parseInt(request.getParameter("productSizeColorID"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    cartDAO.addToCart(cart, productSizeColorID, quantity);
                    break;
                case "update":
                    int cartItemId = Integer.parseInt(request.getParameter("cartItemID"));
                    int newQuantity = Integer.parseInt(request.getParameter("quantity"));
                    cartDAO.updateCartItem(cart, cartItemId, newQuantity);
                    break;
                case "remove":
                    int cartItemIdToRemove = Integer.parseInt(request.getParameter("cartItemID"));
                    cartDAO.clearCartItems(cart, cartItemIdToRemove);
                    break;
                default:
                    break;
            }

            // Refresh cartItems in session after modification
            Collection<Cartitems> cartItems = cart.getCartitemsCollection();
            session.setAttribute("cartItems", cartItems);

            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            response.sendRedirect("cart");
        } catch (Exception e) {
            session.setAttribute("error", "Có lỗi xảy ra khi xử lý giỏ hàng: " + e.getMessage());
            response.sendRedirect("cart");
        }
    }

    private static BigDecimal getBigDecimal(Productsizecolor productSizeColor, BigDecimal basePrice) {
        BigDecimal priceAdjustment = BigDecimal.ZERO;
        String sizeName = productSizeColor.getSizeID() != null ? productSizeColor.getSizeID().getSizeName() : null;
        if ("XL".equals(sizeName)) {
            priceAdjustment = new BigDecimal("50000");
        } else if ("XXL".equals(sizeName)) {
            priceAdjustment = new BigDecimal("100000");
        }
        return basePrice.add(priceAdjustment);
    }
}