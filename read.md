Spring Boot REST API Template
Một template Spring Boot REST API với đầy đủ tính năng authentication, authorization, và quản lý người dùng, được thiết kế để giúp bạn bắt đầu các dự án mới một cách nhanh chóng.

Tính năng
Authentication & Authorization
JWT-based authentication
Role-based access control
Refresh token support
Secure password encoding
User Management
Đăng ký, đăng nhập người dùng
CRUD cho tài khoản người dùng
Phân quyền Admin/User
API Features
RESTful API design
Pagination, sorting, filtering
Comprehensive error handling
Standardized response format
Documentation
OpenAPI 3.0 (Swagger) integration
Detailed API documentation
Example request/response
Data Management
Spring Data JPA integration
Auditing (creation/modification tracking)
MySQL database support
Security
Spring Security integration
CORS configuration
Input validation
Exception handling
Các công nghệ sử dụng
Core: Java 17, Spring Boot 3.2.0
Security: Spring Security, JWT
Database: MySQL, Spring Data JPA, Hibernate
Documentation: SpringDoc OpenAPI
Others: Lombok, SLF4J, Jackson
Getting Started
Yêu cầu
JDK 17+
Maven 3.6+
MySQL 8.0+
Cài đặt và chạy
Clone repository
bash
git clone https://github.com/yourusername/spring-boot-template.git
cd spring-boot-template
Đổi tên dự án (xem phần "Refactoring tên dự án" bên dưới)
Cấu hình database
Tạo database MySQL
Cập nhật thông tin database trong src/main/resources/application.properties
Build và chạy ứng dụng
bash
mvn clean install
mvn spring-boot:run
Truy cập Swagger UI
http://localhost:8080/swagger-ui.html
Tài khoản mặc định
Khi khởi động ứng dụng lần đầu, một tài khoản admin mặc định sẽ được tạo:

Username: admin
Password: admin@123
Cấu trúc dự án
src/main/java/com/quang/template/
├── config/               # Cấu hình Spring và các thành phần
│   ├── auditor/          # Cấu hình JPA auditing
│   ├── exception/        # Xử lý exception
│   ├── jwt/              # JWT configuration và filter
│   └── swagger/          # OpenAPI configuration
├── controller/           # REST controllers
├── dto/                  # Data Transfer Objects
│   ├── request/          # Request DTOs
│   └── response/         # Response DTOs
├── exception/            # Custom exceptions
├── model/                # JPA entities
│   └── Enum/             # Enumerations
├── repository/           # Spring Data repositories
├── service/              # Business logic services
└── utils/                # Utility classes
Refactoring tên dự án
Để đổi tên dự án và package từ template sang dự án của bạn, hãy thực hiện các bước sau:

Đổi tên package
Sử dụng IDE (như IntelliJ IDEA hoặc Eclipse) để refactor package từ com.quang.template sang com.yourcompany.yourproject
Trong IntelliJ: Chuột phải vào package -> Refactor -> Rename
Cập nhật pom.xml
xml
<groupId>com.yourcompany</groupId>
<artifactId>yourproject</artifactId>
<version>0.0.1-SNAPSHOT</version>
<name>yourproject</name>
<description>Your Project Description</description>
Đổi tên lớp application chính
Đổi tên TemplateApplication.java thành YourProjectApplication.java
Cập nhật tên lớp bên trong file
Cập nhật application.properties
properties
spring.application.name=yourproject
Cập nhật các tham chiếu khác
Tìm kiếm trong project để tìm các tham chiếu đến "template" và thay thế chúng
Mở rộng và phát triển
Thêm Entity mới
Tạo Entity class
java
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
@Column(nullable = false)
private String name;

    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    // Getters, setters, constructors
}
Tạo Repository
java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
// Custom query methods
}
Tạo DTOs
Tạo Request DTOs (CreateProductRequest, UpdateProductRequest)
Tạo Response DTOs (ProductResponse)
Tạo Service
java
@Service
@RequiredArgsConstructor
public class ProductService {
private final ProductRepository productRepository;

    // CRUD methods
}
Tạo Controller
java
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ProductController {
private final ProductService productService;

    // CRUD endpoints
}
Thêm tính năng mới
Thiết lập Email Service
Thêm dependencies vào pom.xml
xml
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-mail</artifactId>
</dependency>
Cấu hình trong application.properties
properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
Tạo Email Service
java
@Service
@RequiredArgsConstructor
public class EmailService {
private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String content) {
        // Implementation
    }
}
Implement Scheduled Tasks
Enable scheduling trong main application class
java
@SpringBootApplication
@EnableScheduling
public class YourApplication {
// ...
}
Tạo Scheduled Task Service
java
@Service
public class ScheduledTasksService {
@Scheduled(cron = "0 0 0 * * *") // Midnight every day
public void performDailyTask() {
// Implementation
}
}
Best Practices
Coding Standards
Sử dụng camelCase cho tên biến và phương thức
Sử dụng PascalCase cho tên lớp
Tất cả các entity đều kế thừa từ BaseEntity
Đảm bảo comment đầy đủ cho code phức tạp
Sử dụng DTO để truyền dữ liệu giữa layers
API Design
Sử dụng các HTTP method phù hợp (GET, POST, PUT, DELETE)
Phân trang cho các danh sách lớn
Đảm bảo đầu ra API nhất quán với ResponseFactory
Xác thực đầu vào với Bean Validation
Sử dụng versioning cho API (/api/v1/...)
Bảo mật
Không lưu trữ thông tin nhạy cảm trong code
Sử dụng environment variables cho các thông tin nhạy cảm
Đảm bảo mã hóa mật khẩu với BCrypt
Thiết lập quyền truy cập phù hợp với @PreAuthorize
Kiểm tra và cân nhắc OWASP Top 10 vulnerabilities
Testing
Unit Testing
java
@SpringBootTest
class UserServiceTest {
@MockBean
private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    @Test
    void testGetUserById() {
        // Implementation
    }
}
Integration Testing
java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
@Autowired
private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testCreateUser() {
        // Implementation
    }
}
Deployment
Docker
Tạo Dockerfile
dockerfile
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
Build và run
bash
mvn clean package
docker build -t yourproject .
docker run -p 8080:8080 yourproject
Docker Compose
yaml
version: '3'
services:
db:
image: mysql:8.0
environment:
MYSQL_ROOT_PASSWORD: 1234
MYSQL_DATABASE: yourproject
ports:
- "3306:3306"
volumes:
- mysql-data:/var/lib/mysql

app:
build: .
ports:
- "8080:8080"
depends_on:
- db
environment:
- SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/yourproject
- SPRING_DATASOURCE_USERNAME=root
- SPRING_DATASOURCE_PASSWORD=1234

volumes:
mysql-data:
Contributing
Fork repository
Tạo feature branch (git checkout -b feature/amazing-feature)
Commit changes (git commit -m 'Add some amazing feature')
Push to branch (git push origin feature/amazing-feature)
Open Pull Request
Các vấn đề thường gặp và cách giải quyết
Database connection issues
Kiểm tra thông tin kết nối trong application.properties
Đảm bảo MySQL đang chạy và accessible
Kiểm tra firewall settings
JWT token không hoạt động
Kiểm tra secret key trong application.properties
Xác minh thời gian hết hạn token
Đảm bảo token hợp lệ và không bị sửa đổi
Swagger không hiển thị
Kiểm tra URL: http://localhost:8080/swagger-ui.html hoặc http://localhost:8080/swagger-ui/index.html
Kiểm tra cấu hình Swagger trong SwaggerConfig.java
Đảm bảo các annotations @Operation, @Tag đã được sử dụng đúng cách
License
MIT License

Contact
Bùi Minh Quang - https://bminhquang.name.vn


