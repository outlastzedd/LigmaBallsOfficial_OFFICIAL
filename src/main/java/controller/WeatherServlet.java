package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import model.*;
import weatherDAO.WeatherFunction;
import categoryDAO.*;
import productDAO.*;

@WebServlet(name = "WeatherServlet", urlPatterns
        = {
            "/weather"
        })
public class WeatherServlet extends HttpServlet {

    private CategoryDAO categoryDAO = new CategoryDAO();
    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
//        List<Products> products = (List<Products>) session.getAttribute("categorizedProducts");
        List<Products> products=productDAO.selectAllProducts();
        for (Products product : products) {
            System.out.println(product);
        }
        String categoryID = request.getParameter("cID");
        categoryID = (categoryID == null ? "1" : categoryID);

        String query = request.getParameter("query");
        query = (query == null ? "" : query);
        if (query.equals("rong")) {
            query = "";
        }

        String location = request.getParameter("location");
        List<Products> productsWithWeather = categorizeProductWithWeather(request, products, location);
        System.out.println("NIGGAAAAAAAAAAAAAAAAAAAAAAAAAA");
        productsWithWeather.forEach(System.out::println);

        List<Categories> listCategory = productDAO.selectAllCategory();
        request.setAttribute("query", query);
//        request.setAttribute("category", listCategory);
        request.setAttribute("products", productsWithWeather);
        request.setAttribute("category", listCategory);
        request.getRequestDispatcher("ligmaShop/login/guest.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private List<Products> categorizeProductWithWeather(HttpServletRequest request,List<Products> products, String location) {
        double temp = (double) WeatherFunction.getWeatherData(location).get("temperature");
        String condition;
        if (temp >= 25) {
            condition="hot";
            request.setAttribute("weather", condition);
            return categoryDAO.categorizeProductWithWeather(products, condition);
        } else {
            condition="cold";
            request.setAttribute("weather", condition);
            return categoryDAO.categorizeProductWithWeather(products, condition);
        }
    }
}
