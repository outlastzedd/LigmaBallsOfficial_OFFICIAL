package controller;

import inventoryDAO.InventoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import model.ProductStockInfo;
import model.Products;
import model.Reviews;
import productDAO.ProductDAO;
import reviewDAO.ReviewDAO;

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/productDetail"})
public class ProductDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productIDStr = request.getParameter("pID");

        if (productIDStr != null && !productIDStr.isEmpty()) {
            try {
                int productID = Integer.parseInt(productIDStr);
                ProductDAO productDAO = new ProductDAO();
                ReviewDAO reviewDAO = new ReviewDAO();
                InventoryDAO inven = new InventoryDAO();
                Products product = productDAO.selectProduct(productID);
                List<Reviews> reviews = reviewDAO.getReviewsByProductId(productID);

                // Thêm thông tin kho vào request
                Map<Integer, ProductStockInfo> stockMap = inven.getProductStockMap();
                request.setAttribute("stockMap", stockMap);

                System.out.println("Product: " + product.getProductName()); // Debug

                request.setAttribute("singleProduct", product);
                request.setAttribute("reviews", reviews);
                request.getRequestDispatcher("ligmaShop/product/ProductDetail.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is missing.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
