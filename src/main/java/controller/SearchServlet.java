package controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.List;

import productDAO.*;
import model.*;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    protected void searchProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("query");
        String sortOrder = request.getParameter("sortOrder");
        sortOrder = (sortOrder == null ? "nosort" : sortOrder);
        HttpSession session = request.getSession();
        ProductDAO productDAO = new ProductDAO();
        List<Products> productList = productDAO.searchProduct(keyword);
        request.setAttribute("products", productList);
        request.setAttribute("query", keyword);
        request.setAttribute("sortOrder", sortOrder);
        System.out.println("Search results: " + productList.size() + " query = " + keyword);
        for (Products p : productList) {
            System.out.println(p.getProductName()); // Ensure products are being retrieved
        }
        request.getRequestDispatcher("ligmaShop/login/guest.jsp").forward(request, response);
//        response.sendRedirect("test");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        searchProduct(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        searchProduct(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}