-- Fertilizer Recommendation System Database Setup Script
-- Create Database
CREATE DATABASE IF NOT EXISTS fertilizer_db;
USE fertilizer_db;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    location VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Crops Table (Agronomic reference values for crops)
CREATE TABLE IF NOT EXISTS crops (
    id INT AUTO_INCREMENT PRIMARY KEY,
    crop_name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50),
    target_n DOUBLE NOT NULL,
    target_p DOUBLE NOT NULL,
    target_k DOUBLE NOT NULL,
    min_ph DOUBLE NOT NULL,
    max_ph DOUBLE NOT NULL,
    ideal_moisture DOUBLE,
    description TEXT
);

-- 3. Soil Data Table
CREATE TABLE IF NOT EXISTS soil_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    crop_name VARCHAR(100) NOT NULL,
    soil_type VARCHAR(50),
    nitrogen DOUBLE NOT NULL,
    phosphorus DOUBLE NOT NULL,
    potassium DOUBLE NOT NULL,
    ph DOUBLE NOT NULL,
    moisture DOUBLE,
    temperature DOUBLE,
    humidity DOUBLE,
    season VARCHAR(50),
    tested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- 4. Recommendations Table
CREATE TABLE IF NOT EXISTS recommendations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    crop_name VARCHAR(100) NOT NULL,
    soil_type VARCHAR(50),
    fertilizer_name VARCHAR(150) NOT NULL,
    quantity VARCHAR(100) NOT NULL,
    application_method VARCHAR(255),
    usage_instructions TEXT,
    nitrogen_status VARCHAR(50),
    phosphorus_status VARCHAR(50),
    potassium_status VARCHAR(50),
    ph_status VARCHAR(50),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Insert Default Reference Crops Data
INSERT INTO crops (crop_name, category, target_n, target_p, target_k, min_ph, max_ph, ideal_moisture, description) VALUES
('Rice', 'Cereal', 80.0, 40.0, 40.0, 5.5, 7.0, 80.0, 'Paddy requires high nitrogen and standing water during vegetative stages.'),
('Wheat', 'Cereal', 120.0, 60.0, 40.0, 6.0, 7.5, 60.0, 'Wheat thrives in well-drained loamy soil with balanced phosphorus for root growth.'),
('Maize', 'Cereal', 120.0, 60.0, 50.0, 5.8, 7.2, 65.0, 'Corn is a heavy feeder requiring high nitrogen and balanced potash.'),
('Cotton', 'Fiber / Cash Crop', 100.0, 50.0, 50.0, 6.0, 7.5, 55.0, 'Deep-rooted crop needing phosphorus for boll formation and potassium for fiber quality.'),
('Sugarcane', 'Cash Crop', 150.0, 60.0, 90.0, 6.0, 7.5, 70.0, 'Heavy feeder requiring prolonged nutrient supply across multiple tillering phases.'),
('Potato', 'Tuber / Vegetable', 120.0, 100.0, 120.0, 5.2, 6.5, 70.0, 'Requires substantial phosphorus and high potassium for tuber development and starch.'),
('Tomato', 'Vegetable', 100.0, 60.0, 80.0, 6.0, 7.0, 65.0, 'Needs consistent potassium for fruit firmness and calcium/pH balance to prevent blossom rot.'),
('Soybean', 'Legume / Oilseed', 30.0, 60.0, 40.0, 6.0, 7.0, 60.0, 'Fixes atmospheric nitrogen; requires high phosphorus for root nodulation.'),
('Groundnut', 'Legume / Oilseed', 25.0, 50.0, 40.0, 5.8, 6.5, 50.0, 'Requires phosphorus and calcium for pegging and pod filling.'),
('Chickpea (Gram)', 'Pulse', 20.0, 40.0, 20.0, 6.0, 7.5, 45.0, 'Pulse crop requiring minimal nitrogen, relying heavily on phosphorus for root nodule bacteria.')
ON DUPLICATE KEY UPDATE target_n=VALUES(target_n);

-- Insert Default Demo User (Password: password123)
INSERT INTO users (id, name, email, password, phone, location) VALUES
(1, 'Ramesh Kumar', 'farmer@example.com', 'password123', '9876543210', 'Punjab, India')
ON DUPLICATE KEY UPDATE name=VALUES(name);
