\# 🛒 E-Commerce Management System (Spring Boot REST API)



A full-featured backend \*\*E-Commerce application\*\* built using \*\*Spring Boot\*\*, \*\*Spring Security\*\*, \*\*JWT Authentication\*\*, and \*\*MySQL\*\*.  

It provides secure and scalable REST APIs for managing users, products, categories, cart, and orders.



\---



\## 🚀 API Documentation



\### Swagger UI

http://localhost:8080/swagger-ui/index.html



\---



\## ⚙️ Base URL

http://localhost:8080



\---



\## 🔐 Authentication APIs



\### Auth Controller

\- POST `/auth/register` → Register new user  

\- POST `/auth/login` → Login \& receive JWT token  



\---



\## 📦 Product APIs



\### Product Controller

\- GET `/products` → Get all products  

\- GET `/products/{id}` → Get product by ID  

\- POST `/products` → Create product  

\- PUT `/products/{id}` → Update product  

\- DELETE `/products/{id}` → Delete product  

\- GET `/products/search` → Search products  

\- GET `/products/page` → Paginated products  

\- GET `/products/category/{categoryId}` → Products by category  



\---



\## 🗂 Category APIs



\### Category Controller

\- GET `/categories` → Get all categories  

\- GET `/categories/{id}` → Get category by ID  

\- POST `/categories` → Create category  

\- PUT `/categories/{id}` → Update category  

\- DELETE `/categories/{id}` → Delete category  



\---



\## 🛒 Cart APIs



\### Cart Controller

\- POST `/cart/{userId}/items` → Add item to cart  

\- GET `/cart/{userId}` → Get user cart  

\- PUT `/cart/{userId}/items/{productId}` → Update item quantity  

\- DELETE `/cart/{userId}/items/{productId}` → Remove item  

\- DELETE `/cart/{userId}/clear` → Clear cart  



\---



\## 📑 Order APIs



\### Order Controller

\- POST `/orders/{userId}` → Place order  

\- GET `/orders/{orderId}` → Get order by ID  

\- GET `/orders/user/{userId}` → Get user orders  

\- PUT `/orders/{orderId}/cancel` → Cancel order  



\---



\## 📊 Data Models (DTOs)



\- ProductDTO  

\- CategoryDTO  

\- CartDTO  

\- CartItemDTO  

\- OrderDTO  

\- OrderItemDTO  

\- RegisterRequest  

\- LoginRequest  

\- AuthResponse  



\---



\## 🔒 Security Features



\- JWT Authentication  

\- Role-based access control (Admin / User)  

\- BCrypt password encryption  

\- Stateless session management  

\- Secure REST APIs  



\---



\## 🛠 Tech Stack



\- Java  

\- Spring Boot  

\- Spring Security  

\- JWT  

\- Spring Data JPA  

\- MySQL  

\- Maven  

\- Swagger (OpenAPI 3.1)  



\---



\## 🏗 Architecture

Controller → Service → Repository → Database

DTO Layer → Service Logic → Entity Mapping 



\---



\## 📈 Features



\- Full e-commerce backend system  

\- Authentication \& Authorization  

\- Product \& Category management  

\- Cart \& Order workflow  

\- Search \& Pagination  

\- Exception handling  

\- Swagger documentation  



\---



\## 🧪 Testing



```bash

mvn test



\---



```md

\## 👨‍💻 Author



\*\*Khushi\*\*  

Java Backend Developer  

Spring Boot | MySQL | REST APIs

