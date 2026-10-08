# ShopSphere – E-Commerce Web Application

ShopSphere is a full-stack e-commerce web application built using Java, Spring Boot, Spring Security, Spring Data JPA, MySQL, HTML, CSS, and JavaScript.

The application provides secure user authentication, product management, shopping cart functionality, order processing, and role-based access control for USER and ADMIN.

## Technologies Used

**Backend:** Java, Spring Boot, Spring Security, Spring Data JPA, Hibernate, REST APIs, JWT Authentication

**Frontend:** HTML, CSS, JavaScript

**Database:** MySQL

**Tools:** Eclipse IDE, Maven

## Key Features

### User Features
- User registration and login
- JWT-based authentication
- Browse and search products
- Add products to shopping cart
- Update cart quantities and remove products
- Place orders
- View order history and order details
- Access control to protect user-specific data

### Admin Features
- Admin authentication and authorization
- Add, update, and delete products
- View all orders
- View registered users
- Role-based API security

### Backend Features
- RESTful APIs
- Spring Data JPA and Hibernate ORM
- Entity relationships
- Input validation
- Global exception handling
- Product stock validation and reduction
- Automatic order total calculation
- Product pagination and search
- BCrypt password hashing
- JWT authentication with stateless sessions

## Project Structure

```text
src/main/java/shopsphere/
├── config/
├── controller/
├── dto/
├── entity/
├── exception/
├── repository/
└── service/

src/main/resources/
├── static/
│   ├── css/
│   ├── index.html
│   ├── login.html
│   ├── register.html
│   ├── products.html
│   ├── cart.html
│   ├── checkout.html
│   ├── orders.html
│   ├── order-details.html
│   └── admin.html
└── application.properties
```

## Configuration

Create a MySQL database named `shopsphere`.

Configure these environment variables:

- `DB_USERNAME` – MySQL username
- `DB_PASSWORD` – MySQL password
- `JWT_SECRET` – Base64-encoded secret for JWT signing

The application reads these variables from `application.properties`.

## How to Run

1. Clone the repository.
2. Import the project into Eclipse as a Maven project.
3. Create the `shopsphere` MySQL database.
4. Configure the required environment variables.
5. Run the Spring Boot application.
6. Open `http://localhost:8080/` in your browser.

## Testing

The application has been manually tested for user registration, login, product browsing, shopping cart operations, checkout, order history, order details, and admin management.

## Author

Gurana Sai Ram

Java Full Stack Developer