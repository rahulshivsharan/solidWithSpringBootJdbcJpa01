# SOLID Principles with Spring Boot, JDBC & JPA

## 📚 Overview

This project demonstrates the implementation of **SOLID principles** in a **Spring Boot** application with both **JDBC** and **JPA** data access patterns. It provides a practical, production-ready example of applying object-oriented design principles in a modern Java framework.

**Current SOLID Score: 8.0/10** ✅

---

## 🎯 Project Highlights

- ✅ Spring Boot 3.4.x with Jakarta Persistence API
- ✅ Dual data access implementations (JDBC & JPA)
- ✅ Dependency Injection & Component Scanning
- ✅ Custom Exception Handling
- ✅ Repository Pattern with Strategy Design Pattern
- ✅ Clean Architecture & Separation of Concerns
- ✅ Transaction Management with `@Transactional`

---

## 📁 Project Structure

```
src/main/java/com/sol/
├── MyDataApplication.java           ← @SpringBootApplication (Root Package)
│
├── entity/
│   └── UserEntity.java              ← JPA Entity (Database Mapping)
│
├── exception/
│   └── UserRepositoryException.java  ← Custom Exception
│
├── mapper/
│   └── UserRowMapper.java            ← JDBC Row Mapping
│
├── repo/
│   ├── UserRepository.java           ← Repository Interface
│   ├── JDBCUserRepository.java       ← JDBC Implementation (@Repository("jdbcRepo"))
│   └── JPAUserRepository.java        ← JPA Implementation (@Repository("jpaRepo"))
│
├── service/
│   └── UserService.java              ← Business Logic Layer (@Service)
│
└── vo/
    └── UserVO.java                   ← Value Object (Data Transfer)

src/main/resources/
└── application.properties             ← Spring Boot Configuration
```

---

## 🎓 SOLID Principles Implementation

### **1. S - Single Responsibility Principle (SRP)** ✅ 9/10

**Definition:** Each class should have only one reason to change.

#### How It's Applied:

| Class | Single Responsibility |
|-------|------|
| `UserEntity` | JPA entity mapping to database table |
| `UserVO` | Data transfer between layers |
| `UserRowMapper` | Maps JDBC ResultSet to UserVO |
| `UserRepositoryException` | Custom exception handling |
| `JDBCUserRepository` | JDBC-specific data access |
| `JPAUserRepository` | JPA-specific data access |
| `UserService` | Business logic & orchestration |

#### Code Example:
```java
// ✓ Good: UserEntity only handles database mapping
@Entity
@Table(name = "userstbl")
public class UserEntity {
    @Id
    @Column(name = "id")
    private Integer id;
    
    // Only getters/setters and JPA annotations
}

// ✓ Good: UserVO only handles data transfer
public class UserVO {
    private Integer id;
    private String name;
    private String password;
    
    // Only data transfer logic
}

// ✓ Good: UserRowMapper only handles JDBC mapping
public class UserRowMapper implements RowMapper<UserVO> {
    @Override
    public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserVO(rs.getInt("id"), rs.getString("username"), 
                         rs.getString("password"));
    }
}
```

**Benefit:** Easy to test, modify, and maintain each component independently.

---

### **2. O - Open/Closed Principle (OCP)** ✅ 7/10

**Definition:** Classes should be open for extension, closed for modification.

#### How It's Applied:

**Repository Pattern with Multiple Implementations:**
```java
// ✓ Interface is closed for modification
public interface UserRepository {
    void saveUser();
    List<UserVO> getUsers();
}

// ✓ Can extend with new implementations without modifying existing code
@Repository("jdbcRepo")
public class JDBCUserRepository implements UserRepository { }

@Repository("jpaRepo")
public class JPAUserRepository implements UserRepository { }
```

#### Adding New Implementation (e.g., MongoDB):
```java
// Just create a new implementation - no changes to existing code
@Repository("mongoRepo")
public class MongoUserRepository implements UserRepository {
    // MongoDB implementation
}

// Update application.properties or use @Qualifier
@Qualifier("mongoRepo")
```

**Benefit:** Extend functionality without risking existing code.

---

### **3. L - Liskov Substitution Principle (LSP)** ✅ 9/10

**Definition:** Derived classes must be substitutable for their base types without breaking behavior.

#### How It's Applied:

```java
// Both implementations are seamlessly interchangeable
public class UserService {
    private final UserRepository repo;
    
    // Switch between JDBC and JPA by just changing the qualifier
    public UserService(@Qualifier("jpaRepo") UserRepository repo) {
        this.repo = repo;
    }
    
    // Same behavior regardless of implementation
    public List<UserVO> getUsers() {
        return repo.getUsers();
    }
}

// Usage - works identically with both implementations
// @Qualifier("jdbcRepo") → Uses JDBC
// @Qualifier("jpaRepo")  → Uses JPA
```

**Benefit:** Seamless switching between implementations without code changes.

---

### **4. I - Interface Segregation Principle (ISP)** ✅ 7/10

**Definition:** Clients should not be forced to depend on interfaces they don't use.

#### How It's Applied:

```java
// ✓ Lean, focused interface
public interface UserRepository {
    void saveUser();
    List<UserVO> getUsers();
}

// ✓ Service only depends on what it needs
@Service
public class UserService {
    private final UserRepository repo;
    
    public UserService(@Qualifier("jpaRepo") UserRepository repo) {
        this.repo = repo;  // Only depends on these two methods
    }
}
```

**Benefit:** Minimal interface contracts, easier mocking and testing.

---

### **5. D - Dependency Inversion Principle (DIP)** ✅ 9/10

**Definition:** High-level modules should not depend on low-level modules. Both should depend on abstractions.

#### How It's Applied:

```java
// ✓ High-level: UserService depends on abstraction
@Service
public class UserService {
    private final UserRepository repo;  // Interface, not implementation
    
    // ✓ Constructor Injection
    public UserService(@Qualifier("jpaRepo") UserRepository repo) {
        this.repo = repo;
    }
}

// ✓ Low-level: Repository depends on abstraction
@Repository("jpaRepo")
public class JPAUserRepository implements UserRepository {
    @PersistenceContext
    private EntityManager em;  // Injected, not instantiated
    
    public JPAUserRepository() {}  // Managed by Spring
}

// ✓ Dependency Flow:
// Spring instantiates JPAUserRepository
// ↓
// Injects it into UserService
// ↓
// UserService uses interface, not implementation
```

**Benefit:** Loose coupling, easy testing with mock implementations.

---

## 🏗️ Architecture Layers

```
┌─────────────────────────────────────┐
│   Application Layer                 │
│   MyDataApplication.java            │
│   (CommandLineRunner)               │
└──────────────┬──────────────────────┘
               │ depends on
┌──────────────▼──────────────────────┐
│   Service Layer                     │
│   UserService.java                  │
│   (Business Logic)                  │
└──────────────┬──────────────────────┘
               │ depends on
┌──────────────▼──────────────────────┐
│   Repository Layer (Interface)      │
│   UserRepository.java               │
│   (Data Access Abstraction)         │
└──────────────┬──────────────────────┘
               │ implemented by
        ┌──────┴──────┐
        │             │
┌───────▼────┐  ┌────▼──────┐
│  JDBC Impl │  │  JPA Impl │
└───────┬────┘  └────┬──────┘
        │            │
┌───────▼────┐  ┌────▼──────┐
│  JDBC Core │  │ Hibernate │
└───────┬────┘  └────┬──────┘
        │            │
└───────┴────────────┴──────┐
                             │
                   PostgreSQL DB
```

---

## 🔌 Getting Started

### **Prerequisites**

- Java 17+
- Maven 3.8+
- PostgreSQL 12+
- Spring Boot 3.4.12

### **Installation**

1. **Clone the repository:**
```bash
git clone https://github.com/rahulshivsharan/solidWithSpringBootJdbcJpa01.git
cd solidWithSpringBootJdbcJpa01
```

2. **Update database configuration** (src/main/resources/application.properties):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=your_password
```

3. **Create database table:**
```sql
CREATE TABLE userstbl (
    id INTEGER PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100)
);

INSERT INTO userstbl VALUES 
(1, 'rahul', 'password1'),
(2, 'john', 'password2'),
(3, 'jane', 'password3');
```

4. **Build and run:**
```bash
mvn clean install
mvn spring-boot:run
```

### **Output:**
```
User Id '1', User name 'rahul', user password 'password1'
User Id '2', User name 'john', user password 'password2'
User Id '3', User name 'jane', user password 'password3'
```

---

## 📊 Configuration

### **Switch Between JDBC and JPA**

The application is currently configured to use **JPA**. To switch:

**Current (JPA):**
```java
// UserService.java
public UserService(@Qualifier("jpaRepo") UserRepository repo) {
    this.repo = repo;
}
```

**Switch to JDBC:**
```java
// UserService.java
public UserService(@Qualifier("jdbcRepo") UserRepository repo) {
    this.repo = repo;
}
```

Both implementations provide identical results.

---

## 🎨 Design Patterns Used

| Pattern | Class | Purpose |
|---------|-------|---------|
| **Strategy** | `UserRepository` + implementations | Multiple data access strategies |
| **Dependency Injection** | Throughout | Loose coupling via Spring |
| **Data Transfer Object** | `UserVO` | Transfer data between layers |
| **Row Mapper** | `UserRowMapper` | JDBC result mapping |
| **Exception Wrapping** | `UserRepositoryException` | Custom exceptions |
| **Repository** | `*Repository` | Data access abstraction |
| **Service** | `UserService` | Business logic layer |

---

## 📝 Key Code Examples

### **JDBC Implementation**
```java
@Repository("jdbcRepo")
public class JDBCUserRepository implements UserRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public JDBCUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<UserVO> getUsers() {
        try {
            String sql = "select id, username, password from userstbl";
            return jdbcTemplate.query(sql, new UserRowMapper());
        } catch(Exception e) {
            throw new UserRepositoryException("Failed to fetch users", e);
        }
    }
}
```

### **JPA Implementation**
```java
@Repository("jpaRepo")
public class JPAUserRepository implements UserRepository {
    
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<UserVO> getUsers() {
        TypedQuery<UserEntity> query = em.createQuery(
            "select u from UserEntity u", 
            UserEntity.class
        );
        List<UserEntity> userList = query.getResultList();
        
        return userList.stream()
            .map(UserVO::new)
            .collect(Collectors.toList());
    }
}
```

### **Service Layer**
```java
@Service
public class UserService {
    
    private final UserRepository repo;
    
    public UserService(@Qualifier("jpaRepo") UserRepository repo) {
        this.repo = repo;
    }
    
    @Transactional
    public List<UserVO> getUsers() {
        return repo.getUsers();
    }
}
```

---

## ✅ SOLID Compliance Scorecard

```
┌──────────────────────────────────────────┐
│ SOLID Principles Assessment              │
├──────────────────────────────────────────┤
│ S - Single Responsibility      ████████████░░ 9/10
│ O - Open/Closed                ████████░░░░░ 7/10
│ L - Liskov Substitution        ████████████░░ 9/10
│ I - Interface Segregation      ████████░░░░░ 7/10
│ D - Dependency Inversion       ████████████░░ 9/10
├──────────────────────────────────────────┤
│ Overall SOLID Score            ████████░░░░░ 8.0/10
└──────────────────────────────────────────┘
```

---

## 🔍 Class Responsibilities Matrix

| Class | SRP | DIP | OCP | ISP | LSP |
|-------|-----|-----|-----|-----|-----|
| UserEntity | ✅ | ✅ | ✅ | ✅ | ✅ |
| UserVO | ✅ | ✅ | ✅ | ✅ | ✅ |
| UserRepository (interface) | ✅ | ✅ | ✅ | ✅ | ✅ |
| JDBCUserRepository | ✅ | ✅ | ✅ | ✅ | ✅ |
| JPAUserRepository | ✅ | ✅ | ✅ | ✅ | ✅ |
| UserRowMapper | ✅ | ✅ | ✅ | ✅ | ✅ |
| UserService | ✅ | ✅ | ✅ | ✅ | ✅ |
| UserRepositoryException | ✅ | ✅ | ✅ | ✅ | ✅ |

---

## 🚀 Advanced Topics

### **Adding a New Data Source (e.g., MongoDB)**

1. **Create new repository implementation:**
```java
@Repository("mongoRepo")
public class MongoUserRepository implements UserRepository {
    private final MongoTemplate mongoTemplate;
    
    public MongoUserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    @Override
    public List<UserVO> getUsers() {
        // MongoDB implementation
    }
}
```

2. **Switch in UserService:**
```java
public UserService(@Qualifier("mongoRepo") UserRepository repo) {
    this.repo = repo;
}
```

**No other changes needed!** This demonstrates the **Open/Closed Principle** in action.

---

## 🧪 Testing

### **Unit Test Example:**
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository repository;
    
    @InjectMocks
    private UserService service;
    
    @Test
    void testGetUsers() {
        List<UserVO> expected = Arrays.asList(
            new UserVO(1, "rahul", "password1")
        );
        
        when(repository.getUsers()).thenReturn(expected);
        
        List<UserVO> actual = service.getUsers();
        
        assertEquals(expected, actual);
        verify(repository, times(1)).getUsers();
    }
}
```

---

## 📚 Dependencies

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>3.4.12</version>
</dependency>

<!-- Spring Boot JDBC -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
    <version>3.4.11</version>
</dependency>

<!-- Spring Boot JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
    <version>3.4.11</version>
</dependency>

<!-- PostgreSQL -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.9</version>
</dependency>
```

---

## 💡 Key Takeaways

### **Why SOLID Principles Matter:**

1. **Maintainability** - Easy to understand, modify, and fix code
2. **Testability** - Loose coupling makes testing straightforward
3. **Scalability** - New features can be added without breaking existing code
4. **Reusability** - Components can be reused in different contexts
5. **Flexibility** - Easy to swap implementations without side effects

### **Practical Benefits in This Project:**

✅ Switch between JDBC/JPA with one line change  
✅ Easy to add new data sources (MongoDB, Cassandra, etc.)  
✅ Simple to mock repositories for unit testing  
✅ Clear separation of concerns makes code readable  
✅ Each class has single, well-defined responsibility  

---

## 🔗 Project References

- [SOLID Principles Overview](https://en.wikipedia.org/wiki/SOLID)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JPA/Hibernate Guide](https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa)
- [JDBC Best Practices](https://www.baeldung.com/spring-jdbc)

---

## 🛠️ Troubleshooting

### **Issue: "Bean of type 'UserService' not found"**
- Ensure `MyDataApplication` is in root package (`com.sol`)
- Spring scans current package and all sub-packages

### **Issue: Database connection error**
- Verify PostgreSQL is running
- Check credentials in `application.properties`
- Ensure `userstbl` table exists

### **Issue: Entity not found in JPA**
- Check `@Entity` annotation on UserEntity
- Verify `@Table(name = "userstbl")` matches actual table name
- Ensure entity is in scanned package

---

## 📄 License

This project is open source and available for educational purposes.

---

## 👨‍💻 Author

**Rahul Shivsharan**

---

## 🙏 Contributing

Contributions are welcome! Please feel free to:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

---

## 📞 Support

For questions or issues, please open an issue on GitHub or contact the author.

---

**Last Updated:** 2026-05-19  
**SOLID Score:** 8.0/10 ✅  
**Spring Boot Version:** 3.4.12  
**Java Version:** 17+
