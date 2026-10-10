# 🌱 Precision Fertilizer Recommendation System

A comprehensive, production-ready Full-Stack Agricultural Decision Support System built with **Java Spring Boot**, **REST APIs**, **MySQL Database**, and a responsive **Web Frontend**.

The system analyzes farmer-provided soil laboratory test metrics (Nitrogen, Phosphorus, Potassium, pH, Moisture, Temperature, Humidity) and determines the exact fertilizer formula, application dosage, split scheduling, and soil chemistry adjustments tailored for specific crops.

---

## 🏛️ System Architecture

```text
                    FERTILIZER RECOMMENDATION SYSTEM
                              │
              ┌───────────────┴───────────────┐
              │                               │
          FRONTEND                         BACKEND
              │                               │
      ┌───────┴───────┐               ┌───────┴────────┐
      │               │               │                │
   Login/Register   Dashboard     REST APIs        Database
      │               │               │                │
      │          Farmer Input         │          MySQL / H2
      │               │               │
      │       ┌───────┴────────┐      │
      │       │                │      │
      │     Crop            Soil      │
      │     Type            Data      │
      │       │                │      │
      │       └───────┬────────┘      │
      │               │               │
      └───────────────┼───────────────┘
                      │
              Recommendation Engine
                      │
              ┌───────┴────────┐
              │                │
        Fertilizer Rules    Agronomy Logic
              │                │
              └───────┬────────┘
                      │
              Recommended Fertilizer
                      │
              ┌───────┴────────┐
              │                │
          Fertilizer         Quantity
             Name          / Application
                      │
                      ▼
               Farmer Dashboard
```

---

## 📁 Project Structure

```text
Fertilizer Recommendation System/
├── backend/
│   ├── pom.xml                                      # Maven Configuration (Java 21, Spring Boot 3.3.4)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/fertilizer/recommendation/
│   │   │   │   ├── FertilizerRecommendationApplication.java # Spring Boot Main & Seeder
│   │   │   │   ├── config/
│   │   │   │   │   ├── CorsConfig.java              # CORS configuration for Frontend
│   │   │   │   │   └── OpenApiConfig.java           # Swagger UI documentation config
│   │   │   │   ├── controller/
│   │   │   │   │   ├── AuthController.java          # User Registration & Login endpoints
│   │   │   │   │   ├── CropController.java          # Crop catalog & requirements
│   │   │   │   │   ├── SoilController.java          # Soil diagnostic telemetry endpoints
│   │   │   │   │   └── RecommendationController.java# Engine & History endpoints
│   │   │   │   ├── dto/
│   │   │   │   │   ├── AuthRequest.java / AuthResponse.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   ├── RecommendationRequest.java / RecommendationResponse.java
│   │   │   │   │   └── NutrientAnalysisDto.java
│   │   │   │   ├── entity/
│   │   │   │   │   ├── User.java                    # 'users' table
│   │   │   │   │   ├── Crop.java                    # 'crops' table
│   │   │   │   │   ├── SoilData.java                # 'soil_data' table
│   │   │   │   │   └── Recommendation.java          # 'recommendations' table
│   │   │   │   ├── repository/
│   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   ├── CropRepository.java
│   │   │   │   │   ├── SoilDataRepository.java
│   │   │   │   │   └── RecommendationRepository.java
│   │   │   │   └── service/
│   │   │   │       ├── UserService.java
│   │   │   │       ├── CropService.java
│   │   │   │       ├── SoilService.java
│   │   │   │       ├── RecommendationEngineService.java # Expert Agronomy Engine
│   │   │   │       └── HistoryService.java
│   │   │   └── resources/
│   │   │       ├── application.properties           # General settings & Swagger
│   │   │       ├── application-dev.properties       # Zero-config in-memory testing
│   │   │       ├── application-mysql.properties     # Production MySQL database
│   │   │       └── static/                          # Bundled Web Frontend
│   └── target/
│       └── fertilizer-recommendation-system-1.0.0.jar # Executable Fat JAR
├── Frontend/
│   ├── index.html                                   # Responsive Single-Page Application
│   ├── css/
│   │   └── styles.css                               # Modern Agricultural UI Styling
│   └── js/
│       ├── api.js                                   # REST API connector & offline cache
│       └── app.js                                   # State, tabs, charts, validation
├── database_setup.sql                               # MySQL DDL & Seed Script
└── README.md
```

---

## 🗄️ Database Schema (`MySQL`)

The database script is located at [database_setup.sql](file:///c:/Users/IIISI/Desktop/Fertilizer%20Recommendation%20System/Fertilizer%20Recommendation%20System/database_setup.sql).

### 1. `users` Table
Stores farmer profile information and login credentials.
- `id` (INT, Primary Key, Auto Increment)
- `name` (VARCHAR(100))
- `email` (VARCHAR(120), Unique)
- `password` (VARCHAR(255))
- `phone` (VARCHAR(20))
- `location` (VARCHAR(100))
- `created_at` (TIMESTAMP)

### 2. `crops` Table
Stores standard agronomic benchmark requirements for each crop.
- `id` (INT, Primary Key, Auto Increment)
- `crop_name` (VARCHAR(100), Unique)
- `category` (VARCHAR(50))
- `target_n`, `target_p`, `target_k` (DOUBLE) - Optimal nutrient levels in kg/hectare
- `min_ph`, `max_ph` (DOUBLE) - Suitable soil pH range
- `ideal_moisture` (DOUBLE) - Ideal percentage
- `description` (TEXT)

*Preloaded crops include:* Rice, Wheat, Maize, Cotton, Sugarcane, Potato, Tomato, Soybean, Groundnut, and Chickpea.

### 3. `soil_data` Table
Captures farmer-submitted soil test parameters.
- `id` (INT, Primary Key, Auto Increment)
- `user_id` (INT, Foreign Key referencing `users(id)`)
- `crop_name` (VARCHAR(100))
- `soil_type` (VARCHAR(50))
- `nitrogen`, `phosphorus`, `potassium` (DOUBLE)
- `ph`, `moisture`, `temperature`, `humidity` (DOUBLE)
- `season` (VARCHAR(50))
- `tested_at` (TIMESTAMP)

### 4. `recommendations` Table
Persists generated advisory plans for historical review.
- `id` (INT, Primary Key, Auto Increment)
- `user_id` (INT, Foreign Key referencing `users(id)`)
- `crop_name` (VARCHAR(100))
- `soil_type` (VARCHAR(50))
- `fertilizer_name` (VARCHAR(150))
- `quantity` (VARCHAR(100))
- `application_method` (VARCHAR(255))
- `usage_instructions` (TEXT)
- `organic_advice` (TEXT)
- `nitrogen_status`, `phosphorus_status`, `potassium_status`, `ph_status` (VARCHAR(50))
- `created_date` (TIMESTAMP)

---

## ⚡ Recommendation Engine Logic

The engine follows scientific crop nutrition principles:

1. **Deficit Calculation:**
   $$\text{Deficit}_N = \max(0, \text{Target}_N - \text{Soil}_N)$$
   $$\text{Deficit}_P = \max(0, \text{Target}_P - \text{Soil}_P)$$
   $$\text{Deficit}_K = \max(0, \text{Target}_K - \text{Soil}_K)$$

2. **Nutrient Status Classification:**
   - $\text{Soil} / \text{Target} < 0.75$: **Low / Deficient**
   - $0.75 \le \text{Soil} / \text{Target} \le 1.25$: **Optimal / Balanced**
   - $\text{Soil} / \text{Target} > 1.25$: **High / Excess**

3. **Fertilizer Selection Matrix:**
   - **N + P + K Deficient:** NPK Complex (10:26:26 or 12:32:16) basal + Neem-Coated Urea split.
   - **N + P Deficient:** DAP (18-46-0) basal placement + Urea top-dressing.
   - **N + K Deficient:** Urea + MOP (Muriate of Potash 60% K₂O) split.
   - **P + K Deficient:** SSP (Single Super Phosphate 16% P + 11% S) + MOP basal.
   - **Only Nitrogen Low:** Neem-Coated Urea (46% N) in 2 to 3 split vegetative doses.
   - **Only Phosphorus Low:** Single Super Phosphate (SSP) or DAP placed 5 cm beneath seed furrow.
   - **Only Potassium Low:** Muriate of Potash (MOP) or SOP (Sulfate of Potash for sensitive crops).
   - **All Nutrients Balanced/High:** Bio-NPK Consortium (Azotobacter, PSB, KMB) + Well-rotted Farm Yard Manure / Vermicompost.

4. **Soil pH & Environmental Corrections:**
   - **Acidic ($\text{pH} < 6.0$):** Agricultural Lime ($\text{CaCO}_3$) or Dolomite at 250-350 kg/acre 2-3 weeks prior to sowing.
   - **Alkaline ($\text{pH} > 8.0$):** Agricultural Gypsum ($\text{CaSO}_4\cdot 2\text{H}_2\text{O}$) at 200 kg/acre + Green Manuring (Dhaincha/Sesbania).
   - **High Temperature ($> 34^\circ\text{C}$):** Ammonia volatilization warning; morning/evening broadcasting.
   - **Low Moisture ($< 35\%$):** Osmotic root burn alert; light irrigation prior to application.

---

## 🚀 Running the Project

### Prerequisites
- **Java 21 LTS**
- **Apache Maven 3.9+**
- **MySQL 8.0+** (or use the built-in instant Dev profile)

### 1. Launching the Backend

#### Option A: Instant Zero-Config Launch (Default Dev Profile)
```bash
cd backend
mvn spring-boot:run
```
*(Or execute the packaged JAR:)*
```bash
java -jar backend/target/fertilizer-recommendation-system-1.0.0.jar
```

#### Option B: Connecting to MySQL
1. Open MySQL Workbench or CLI and execute:
   ```bash
   mysql -u root -p < database_setup.sql
   ```
2. Run backend with the MySQL profile:
   ```bash
   java -jar backend/target/fertilizer-recommendation-system-1.0.0.jar --spring.profiles.active=mysql --spring.datasource.username=root --spring.datasource.password=YOUR_PASSWORD
   ```

### 2. Accessing the Applications
- **Web Application:** [http://localhost:8080](http://localhost:8080)
- **Interactive Swagger REST API Docs:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON Schema:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **Standalone Frontend:** You can also open [Frontend/index.html](file:///c:/Users/IIISI/Desktop/Fertilizer%20Recommendation%20System/Fertilizer%20Recommendation%20System/Frontend/index.html) directly in any web browser.

### 3. Default Demo Credentials
- **Email:** `farmer@example.com`
- **Password:** `password123`
*(A 1-click "Use Demo Farmer" button is available in the login dialog).*

---

## 📡 REST API Reference

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register new farmer account |
| `POST` | `/api/auth/login` | Login and retrieve farmer profile |
| `GET` | `/api/auth/farmer/{id}` | Get farmer profile by ID |
| `PUT` | `/api/auth/farmer/{id}` | Update farmer information |
| `GET` | `/api/crops` | Retrieve all 10+ agronomic crops with nutrient ranges |
| `GET` | `/api/crops/{name}` | Get crop specifics by name |
| `POST` | `/api/crops` | Add custom crop with target N-P-K |
| `POST` | `/api/soil` | Store soil test record |
| `GET` | `/api/soil/farmer/{userId}` | Get historical soil tests for a farmer |
| `POST` | `/api/recommendations/generate` | Run engine on soil data & get recommendation |
| `GET` | `/api/recommendations/history` | List recommendation history (supports `?userId=X`) |
| `GET` | `/api/recommendations/{id}` | Get single recommendation details |
| `DELETE` | `/api/recommendations/{id}` | Remove history entry |
