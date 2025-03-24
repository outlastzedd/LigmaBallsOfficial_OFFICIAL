package controller;

import categoryDAO.CategoryDAO;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import model.Categories;
import model.ProductCategories;
import model.Products;
import productDAO.ProductDAO;
import weatherDAO.*;

@WebServlet(name = "CategoryServlet", urlPatterns = {"/category"})
public class CategoryServlet extends HttpServlet {

    ProductDAO productDAO = new ProductDAO();
    CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String categoryID = request.getParameter("cID");
        categoryID = (categoryID == null ? "1" : categoryID);
        String query = request.getParameter("query");
        query = (query == null ? "" : query);
        if (query.equals("rong")) {
            query = "";
        }
        String weather = request.getParameter("weather");
        weather = (weather == null ? "all" : weather);
//        if (categoryID == null) {
//            response.getWriter().println("Invalid category ID");
//            return;
//        }
        List<Categories> listCategory = productDAO.selectAllCategory();
        List<Products> products = categoryDAO.categorizeProducts(Integer.parseInt(categoryID), query);

        String sortOrder = request.getParameter("sortOrder");
        sortOrder = (sortOrder == null ? "nosort" : sortOrder);

        if (weather != null && !weather.equals("all")) {
            products = categoryDAO.categorizeProductWithWeather(products, weather);
        }
        if ((sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc"))) {
            products = productDAO.sortProductsByPrice(products, sortOrder);
        }

        HttpSession session = request.getSession();
        session.setAttribute("categorizedProducts", products);
        request.setAttribute("weather", weather);
        request.setAttribute("query", query);
        request.setAttribute("sortOrder", sortOrder);
        request.setAttribute("products", products);
        request.setAttribute("category", listCategory);
        request.getRequestDispatcher("ligmaShop/login/guest.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
