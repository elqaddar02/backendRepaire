# Repair Tracker Application

A comprehensive repair tracking system built with Spring Boot, Spring Security, and JWT authentication.

## Features

- **User Management**: Support for different user roles (Admin, Manager, Technician, Customer)
- **Store Management**: Multi-store support with store-specific repairs
- **Repair Tracking**: Complete repair lifecycle management with status tracking
- **Timeline Tracking**: Detailed repair timeline with status changes and notes
- **Working Hours**: Track technician working hours
- **Dashboard**: Admin dashboard with statistics and analytics
- **JWT Authentication**: Secure API authentication with JWT tokens

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security**
- **Spring Data JPA**
- **H2 Database** (for development)
- **JWT (JSON Web Tokens)**
- **Lombok**
- **Maven**

## Project Structure

```
src/main/java/com/repairtracker/
├── RepairTrackerApplication.java
├── config/
│   ├── SecurityConfig.java
│   └── JwtConfig.java
├── controller/
│   ├── AuthController.java
│   ├── RepairController.java
│   ├── StoreController.java
│   └── AdminController.java
├── dto/
│   ├── request/
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── CreateRepairRequest.java
│   │   └── UpdateRepairStatusRequest.java
│   ├── response/
│   │   ├── AuthResponse.java
│   │   ├── UserDto.java
│   │   ├── RepairDto.java
│   │   ├── StoreDto.java
│   │   └── DashboardStatsDto.java
│   └── WorkingHoursDto.java
├── entity/
│   ├── User.java
│   ├── Store.java
│   ├── Repair.java
│   ├── RepairTimeline.java
│   └── WorkingHours.java
├── enums/
│   ├── UserRole.java
│   ├── UserStatus.java
│   └── RepairStatus.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── BusinessException.java
│   └── AuthenticationException.java
├── repository/
│   ├── UserRepository.java
│   ├── StoreRepository.java
│   ├── RepairRepository.java
│   └── RepairTimelineRepository.java
├── security/
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationEntryPoint.java
│   └── JwtRequestFilter.java
└── service/
    ├── AuthenticationService.java
    ├── RepairService.java
    ├── StoreService.java
    └── AdminService.java
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Database Access

- H2 Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Repairs
- `GET /api/repairs` - Get all repairs
- `POST /api/repairs` - Create new repair
- `GET /api/repairs/{id}` - Get repair by ID
- `PUT /api/repairs/{id}/status` - Update repair status
- `GET /api/repairs/store/{storeId}` - Get repairs by store
- `GET /api/repairs/technician/{technicianId}` - Get repairs by technician
- `GET /api/repairs/status/{status}` - Get repairs by status

### Stores
- `GET /api/stores` - Get all stores
- `POST /api/stores` - Create new store
- `GET /api/stores/{id}` - Get store by ID
- `PUT /api/stores/{id}` - Update store
- `DELETE /api/stores/{id}` - Delete store
- `GET /api/stores/active` - Get active stores

### Admin
- `GET /api/admin/dashboard` - Get dashboard statistics
- `GET /api/admin/users` - Get all users
- `GET /api/admin/users/role/{role}` - Get users by role
- `GET /api/admin/users/store/{storeId}` - Get users by store

## User Roles

- **ADMIN**: Full system access
- **MANAGER**: Store management and reporting
- **TECHNICIAN**: Repair management and updates
- **CUSTOMER**: View own repairs

## Repair Status Flow

1. **PENDING** - Initial status when repair is created
2. **IN_PROGRESS** - Repair work has started
3. **WAITING_FOR_PARTS** - Waiting for replacement parts
4. **ON_HOLD** - Repair temporarily paused
5. **COMPLETED** - Repair finished successfully
6. **CANCELLED** - Repair cancelled

## Security

- JWT-based authentication
- Role-based authorization
- Password encryption with BCrypt
- CORS configuration
- Global exception handling

## Development

The application uses H2 in-memory database for development. Data will be lost when the application restarts. For production, configure a persistent database in `application.yml`.

## License

This project is licensed under the MIT License.
