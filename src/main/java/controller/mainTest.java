package controller;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name="mainTest", urlPatterns={"/test"})
public class mainTest extends HttpServlet {
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        System.out.println("mainTest: Received request for /test");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/products");
        if (dispatcher == null) {
            System.out.println("mainTest: RequestDispatcher for '/products' is null!");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot forward to /products");
            return;
        }
        System.out.println("mainTest: Forwarding to /products");
        dispatcher.forward(request, response);
        System.out.println("mainTest: Forward to /products completed"); // Won’t print if forward succeeds

    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        System.out.println("mainTest: POST received, forwarding to /products");
        doGet(request, response);
    }
}
