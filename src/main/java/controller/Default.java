package controller;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name="Default", urlPatterns={"/"})
public class Default extends HttpServlet {
    static {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.out.println("ProductServlet: Loaded environment variables from .env");
        String URL = dotenv.get("JDBC_DATABASE_URL");
        System.out.println("ProductServlet: JDBC_DATABASE_URL = " + URL);
        if (URL != null) {
            System.setProperty("JDBC_DATABASE_URL", dotenv.get("JDBC_DATABASE_URL"));
        }
        System.out.println("ProductServlet: SystemProperty JDBC_DATABASE_URL = " + System.getProperty("JDBC_DATABASE_URL"));
        System.out.println("ProductServlet: EnvProperty JDBC_DATABASE_URL = " + System.getenv("JDBC_DATABASE_URL"));
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            request.getRequestDispatcher("products").forward(request, response);
        }
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
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
