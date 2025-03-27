# [IMPORTANT] WEBSITE ACCESS
View the final product by clicking [here](https://ligma-shop-49f1782b6042.herokuapp.com)

Pull from `postgres-test-demo-otp` branch for up-to-date code.

# LigmaBallsOfficial - eCommerce Web Application
Welcome to LigmaBallsOfficial, an eCommerce web application developed as part of the PRJ301 subject assignment. This project showcases a fully functional online shopping platform built with Java, leveraging modern web technologies and a robust database system. Designed to demonstrate core concepts of web development, this app includes features like product browsing, cart management, user authentication, and order processing.
## Project Overview
This eCommerce platform allows users to:
- Browse a catalog of products with categories, sizes, and colors.

- Add items to a shopping cart and manage their selections.

- Place orders with shipping details.

- Leave reviews and view product ratings.

- Admins can manage inventory, categories, and user accounts.

The project is built using Java with JSPs and Servlets, adhering to the MVC (Model-View-Controller) pattern, and is deployed on Heroku with a PostgreSQL backend.
## Tech Stack
### Core Technologies
- <b>Java 21</b>: The backbone of the application, leveraging the latest LTS version for performance and modern features.

- <b>JSP (JavaServer Pages)</b>: Dynamic page rendering with jakarta.servlet.jsp-api and tomcat-jasper for compilation.

- <b>Servlets</b>: Handling HTTP requests and responses using the Jakarta EE 10 API (jakarta.jakartaee-api).

- <b>PostgreSQL</b>: Our chosen database for its open-source nature, scalability, and robust features (over SQL Server—see below).

- <b>Hibernate ORM 6.4.4</b>: Object-relational mapping for seamless database interaction.

### Why PostgreSQL Over SQL Server?
We opted for PostgreSQL instead of SQL Server for several reasons:
- Open-Source: Free to use, reducing costs for educational and small-scale projects.

- Cross-Platform: Works seamlessly across environments (e.g., local dev on Windows/Linux, Heroku deployment).

- Advanced Features: Supports JSON data types, full-text search, and extensibility, which align with potential future enhancements.

- Heroku Integration: Native support on Heroku, simplifying deployment compared to SQL Server’s licensing complexities.

- Community Support: Rich documentation and community resources, ideal for a learning-focused project.

While our `pom.xml` includes the SQL Server JDBC driver (`mssql-jdbc`) for initial prototyping, we fully transitioned to PostgreSQL (`postgresql:42.7.3`) for production.
### Additional Libraries
- <b>JSTL (JavaServer Pages Standard Tag Library)</b>: Simplifies JSP development with `jakarta.servlet.jsp.jstl` and `taglibs-standard-impl`.

- <b>Gson & JSON Libraries</b>: JSON parsing and serialization with `gson:2.9.1`, `json-simple:1.1.1`, and `org.json:20230227`.

- <b>Apache HttpClient</b>: HTTP requests with `httpclient:4.5.5`, `httpcore:4.4.9`, and `fluent-hc:4.5.5`.

- <b>Jakarta Mail</b>: Email functionality (e.g., order confirmations) with `jakarta.mail-api:2.1.3` and `com.sun.mail:jakarta.mail:2.0.1`.

- <b>Dotenv</b>: Environment variable management with `dotenv-java:3.0.0` for secure configuration.

- <b>[Meteo Weather](https://open-meteo.com) API</b>: Customized product queries with weather integration.

- **[OpenRouter AI](https://openrouter.ai) API**: Engaging chatbot for website.

### Build & Deployment
- <b>Maven</b>: Dependency management and WAR packaging with plugins like `maven-war-plugin` and `maven-dependency-plugin`.

- <b>Heroku Webapp Runner</b>: Deployment using `webapp-runner:10.1.34.0` for running the WAR file on Heroku.

## Getting Started
### Prerequisites
- **Java 21**: Install the JDK (e.g., OpenJDK or Oracle JDK).

- **Maven 3.x**: For building the project.

- **PostgreSQL**: Local database setup (or use Heroku Postgres).

- **Heroku CLI**: For deployment.

- **Git**: Version control.

### Installation
1. Clone the Repository:
```bash
git clone https://github.com/<your-username>/LigmaBallsOfficial.git
cd LigmaBallsOfficial
```

2. Configure Environment:
- Create a .env file in the root directory:
```
DB_URL=jdbc:postgresql://localhost:5432/ligmashop
DB_USERNAME=your_username
DB_PASSWORD=your_password
```
- Update `persistence.xml` or application properties to match.

3. Set Up PostgreSQL:
- Create a database:
```sql
CREATE DATABASE ligmashop;
```
- Run schema scripts (if not using `ddl-auto=create`):
```bash
psql -U your_username -d ligmashop -f src/main/resources/schema.sql
```
4. Build the Project:
```bash
mvn clean package
```
5. Run Locally:
- Use an embedded server like Tomcat or Jetty, or run with `webapp-runner`:
```bash
java -jar target/dependency/webapp-runner.jar target/LigmaBallsOfficial-OFFICIAL.war
```
- Access at `http://localhost:8080`.

### Deployment to Heroku
1. Create a Heroku App:
```bash
heroku create ligma-balls-official
```
2. Add PostgreSQL:
```bash
heroku addons:create heroku-postgresql:hobby-dev
```
3. Set Environment Variables:
```bash
heroku config:set DB_URL=$(heroku config:get DATABASE_URL)
```
4. Deploy:
```bash
git push heroku main
```
5. Open the App:
```bash
heroku open
```
## Project Structure
```
LigmaBallsOfficial/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── model/          # JPA entities (e.g., Products, Users)
│   │   ├── resources/         # persistence.xml, schema.sql
│   │   └── webapp/           # JSPs, Servlets, WEB-INF
│   └── test/                 # Unit tests
├── pom.xml                   # Maven configuration
└── README.md                 # This file
```

## Contributing
This is an academic project, but feel free to fork and experiment! Submit pull requests for bug fixes or enhancements. Ensure code follows university guidelines for PRJ301.
##License
This project is for educational purposes and not licensed for commercial use.
##Acknowledgments
- PRJ301 instructors for guidance.

- University for providing this learning opportunity.

- Open-source community for tools like PostgreSQL and Hibernate.

