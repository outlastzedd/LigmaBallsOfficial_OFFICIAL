package controller;

import jakarta.servlet.RequestDispatcher;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import model.Categories;
import model.Products;
import productDAO.ProductDAO;

@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
public class ProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String weather = request.getParameter("weather");
        weather = (weather == null ? "all" : weather);

        String sortOrder = request.getParameter("sortOrder");
        sortOrder = (sortOrder == null ? "nosort" : sortOrder);
        List<Products> products = productDAO.selectAllProductsActive();
        List<Categories> listCategory = productDAO.selectAllCategory();
        String query = request.getParameter("query");
        query = (query == null ? "" : query);
        if (query.equals("rong")) {
            query = "";
        }
        System.out.println("FINAL PRODUCTS LIST");
        products.forEach(System.out::println);
        request.setAttribute("weather", weather);
        request.setAttribute("query", query);
        request.setAttribute("products", products);
        request.setAttribute("sortOrder", sortOrder);

        //send the products list to weather servlet
        request.setAttribute("category", listCategory);

        RequestDispatcher dispatcher = request.getRequestDispatcher("ligmaShop/login/guest.jsp");
        if (dispatcher == null) {
            System.out.println("ProductServlet: RequestDispatcher for 'ligmaShop/login/guest.jsp' is null!");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot forward to guest.jsp");
            return;
        }
        System.out.println("ProductServlet: Forwarding to guest.jsp");
        try {
            dispatcher.forward(request, response);
            System.out.println("ProductServlet: Forward to guest.jsp completed");
        } catch (ServletException | IOException e) {
            System.out.println("ProductServlet: Error forwarding to guest.jsp: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error forwarding to guest.jsp");
        }
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
