//package controller;
//
//import jakarta.servlet.RequestDispatcher;
//import java.io.IOException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.util.List;
//import model.*;
//import productDAO.ProductDAO;
//
//@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
//public class ProductServlet extends HttpServlet {
//    ProductDAO productDAO = new ProductDAO();
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        System.out.println("ProductServlet: Received request for /products");
//
//        String query = (String) request.getAttribute("query");
//        query = (query == null ? "" : query);
//        if (query.equals("rong")) {
//            query = "";
//        }
//        String weather = request.getParameter("weather");
//        weather = (weather ==null ? "all" : weather);
//        
//        List<Products> list = productDAO.selectAllProducts();
//        List<Categories> listCategory = productDAO.selectAllCategory();
//        List<Products> listQuery=productDAO.searchProduct(query);
//        //headline for debugging in tomcat log
////        System.out.println("THE LIST OF RETRIEVED PRODUCTS: \n"); 
//        try {
//            if (listQuery != null && !listQuery.isEmpty() && listCategory != null && !listCategory.isEmpty()) {
//                // checking the list of category
//                for (Categories cate : listCategory) {
//                    System.out.println("Product name: " + cate.getCategoryName()
//                            + "Description: " + cate.getDescription());
//                }
//                request.setAttribute("weather", weather);
//                request.setAttribute("query", query);
//                request.setAttribute("products", listQuery);             
//                request.setAttribute("category", listCategory);
//            } else {
//                System.out.println("No products found!");
//            }
//        } catch (Exception e) {
//            System.out.println("ProductServlet: Error - " + e.getMessage());
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing products");
//            return;
//        }
//
//        RequestDispatcher dispatcher = request.getRequestDispatcher("ligmaShop/login/guest.jsp");
//        if (dispatcher == null) {
//            System.out.println("ProductServlet: RequestDispatcher for 'ligmaShop/login/guest.jsp' is null!");
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot forward to guest.jsp");
//            return;
//        }
//        System.out.println("ProductServlet: Forwarding to guest.jsp");
//        dispatcher.forward(request, response);
//        System.out.println("ProductServlet: Forward to guest.jsp completed"); // Won’t print if forward succeeds
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        doGet(request, response);
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
package controller;

import categoryDAO.CategoryDAO;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import model.Categories;
import model.Products;
import productDAO.ProductDAO;

@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        String weather = request.getParameter("weather");
        weather = (weather == null ? "all" : weather);

        String sortOrder = request.getParameter("sortOrder");
        sortOrder= (sortOrder==null? "nosort" : sortOrder);
        List<Products> products = productDAO.selectAllProductsActive();
        List<Categories> listCategory = productDAO.selectAllCategory();
        String query = request.getParameter("query");
        query = (query == null ? "" : query);
        if (query.equals("rong")) {
            query = "";
        }
//        List<Products> categoryProduct=categoryDAO.categorizeProducts(Integer.parseInt(categoryID), query);

//         List<Products> weatherProduct = categoryDAO.categorizeProductWithWeather(categoryProduct, weather);

//        if (categoryID != null && !categoryID.isEmpty()) {
//            try {
//                products = categoryDAO.categorizeProducts(Integer.parseInt(categoryID), query);
//                if (!weather.equalsIgnoreCase("all")) {
//                    products = categoryDAO.categorizeProductWithWeather(products, weather);
//                }
//            } catch (NumberFormatException e) {
//                products = productDAO.selectAllProducts();
//            }
//        } else if (query.isEmpty()) {
//            products = productDAO.selectAllProducts();
//        } else {
//            products = productDAO.searchProduct(query);
//        }

        System.out.println("FINAL PRODUCTS LIST");
        products.forEach(System.out::println);
//
//        if ((sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc"))) {
//            products = productDAO.sortProductsByPrice(products, sortOrder);
//        }


//        if (products != null && !products.isEmpty() && listCategory != null && !listCategory.isEmpty()) {
            request.setAttribute("weather", weather);
            request.setAttribute("query", query);
            request.setAttribute("products", products);
            request.setAttribute("sortOrder", sortOrder);

            //send the products list to weather servlet
//            session.setAttribute("categorizedProducts", categoryProduct);
            request.setAttribute("category", listCategory);
//        } else {
//            System.out.println("NO PRODUCTS FOUND");
//        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("ligmaShop/login/guest.jsp");
        if (dispatcher == null) {
            System.out.println("ProductServlet: RequestDispatcher for 'ligmaShop/login/guest.jsp' is null!");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot forward to guest.jsp");
            return;
        }
        System.out.println("ProductServlet: Forwarding to guest.jsp");
        dispatcher.forward(request, response);
        System.out.println("ProductServlet: Forward to guest.jsp completed");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
