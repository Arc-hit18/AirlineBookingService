# Airline Booking Service

## Prerequisites
- Java 17 or higher
- MySQL 8.0
- Gradle 7.0 or higher
- Git

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/Arc-hit18/AirlineBookingService.git
cd AirlineBookingService
```

### 2. Database Setup
1. Install MySQL 8.0 if not already installed
2. Create a new database:
   ```sql
   CREATE DATABASE airline_db;
   ```
3. Update the database and application configuration in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/airline_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update

   # --- Application Settings ---
   # Cache refresh interval (in seconds)
   cache.refresh.interval=60
   # Maximum retries for booking in case of concurrent update (optimistic locking)
   booking.retry.max=3
   ```

**cache.refresh.interval**: How often (in seconds) the application refreshes its in-memory cache from the database.

**booking.retry.max**: Controls how many times the booking logic will retry if a concurrent update/optimistic locking conflict occurs. Increase for high contention, decrease for fast failure.


### 3. Build and Run the Application
```bash
# Build the application
./gradlew build

# Run the application
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## Project Scope

### Flight Operations
- **Domestic Flights Only**
- **Same Day Travel**
- **Search Capabilities**
  - Direct flights
  - Indirect flights (with connections)
  - Filter by date, source, and destination

### Booking System
- **Direct Flights Only** for booking
- **Seat Reservation**
  - No specific seat selection
  - Only number of seats to be reserved

## API Documentation

### Swagger UI
Access the interactive API documentation at:
- http://localhost:8080/swagger-ui.html

### Postman Collections
1. **Main API Collection**: Contains all the API endpoints for the application
2. **Search Collection**: For testing search functionality