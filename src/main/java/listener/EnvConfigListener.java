package listener;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class EnvConfigListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Load from Heroku env first
        String url = System.getenv("JDBC_DATABASE_URL");
        String CHATBOT_API = System.getenv("CHATBOT_API");
        if (url == null) {
            // Fallback to .env for local dev
            Dotenv dotenv = Dotenv.configure().directory("./").ignoreIfMissing().load();
            url = dotenv.get("JDBC_DATABASE_URL");
            CHATBOT_API = dotenv.get("CHATBOT_API");
        }
        if (url != null) {
            System.setProperty("JDBC_DATABASE_URL", url);
            System.setProperty("CHATBOT_API", CHATBOT_API);
        }
        System.out.println("EnvConfigListener: JDBC_DATABASE_URL from env = " + System.getenv("JDBC_DATABASE_URL"));
        System.out.println("EnvConfigListener: JDBC_DATABASE_URL from prop = " + System.getProperty("JDBC_DATABASE_URL"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}