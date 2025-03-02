CREATE DATABASE TMA;
USE TMA;

-- User Table
CREATE TABLE User (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100),
    phone_number VARCHAR(20),
    role ENUM('NONE', 'MEMBER', 'MANAGER') DEFAULT 'NONE'
);

-- Project Table
CREATE TABLE Project (
    project_id VARCHAR(50) PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL,
    description TEXT,
    manager_id VARCHAR(50),
    budget DECIMAL(15, 2),
    start_date DATETIME,
    end_date DATETIME,
    status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED') DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (manager_id) REFERENCES User(user_id)
);

-- Task Table
CREATE TABLE Task (
    task_id VARCHAR(50) PRIMARY KEY,
    assigner_id VARCHAR(50), -- New field for who assigned the task
    task_name VARCHAR(100) NOT NULL,
    description TEXT,
    project_id VARCHAR(50),
    priority ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'MEDIUM',
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING',
    deadline DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES Project(project_id),
    FOREIGN KEY (assigner_id) REFERENCES User(user_id) -- New foreign key
);

-- Enroll Table
CREATE TABLE Enroll (
    user_id VARCHAR(50),
    project_id VARCHAR(50),
    joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'ACTIVE', 'REMOVED') DEFAULT 'PENDING',
    PRIMARY KEY (user_id, project_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (project_id) REFERENCES Project(project_id)
);

-- Insert Fake Data into User
INSERT INTO User (user_id, username, password, full_name, email, phone_number, role) VALUES
('U001', 'admin', '123123', 'John Doe', 'john.doe@example.com', '123-456-7890', 'MANAGER'),
('U002', 'janedoe', 'hashedpassword2', 'Jane Doe', 'jane.doe@example.com', '234-567-8901', 'MEMBER'),
('U003', 'bobsmith', 'hashedpassword3', 'Bob Smith', 'bob.smith@example.com', '345-678-9012', 'MEMBER'),
('U004', 'alicejones', 'hashedpassword4', 'Alice Jones', 'alice.jones@example.com', '456-789-0123', 'NONE'),
('U005', 'maryjohnson', 'hashedpassword5', 'Mary Johnson', 'mary.j@example.com', '567-890-1234', 'MANAGER');

-- Insert Fake Data into Project
INSERT INTO Project (project_id, project_name, description, manager_id, budget, start_date, end_date, status) VALUES
('P001', 'Website Redesign', 'Redesign company website', 'U001', 50000.00, '2025-01-01 09:00:00', '2025-06-30 17:00:00', 'ACTIVE'),
('P002', 'Mobile App Dev', 'Develop a mobile app for clients', 'U005', 75000.00, '2025-02-01 09:00:00', '2025-12-31 17:00:00', 'ACTIVE'),
('P003', 'Database Migration', 'Migrate legacy DB to new system', 'U001', 30000.00, '2024-11-01 09:00:00', '2025-03-31 17:00:00', 'COMPLETED');

-- Insert Fake Data into Task (with assigner_id)
INSERT INTO Task (task_id, assigner_id, task_name, description, project_id, priority, status, deadline) VALUES
('T001', 'U001', 'Design Homepage', 'Create UI for homepage', 'P001', 'HIGH', 'IN_PROGRESS', '2025-03-15 17:00:00'),
('T002', 'U001', 'Setup Backend', 'Configure server and APIs', 'P001', 'MEDIUM', 'PENDING', '2025-04-01 17:00:00'),
('T003', 'U005', 'App Login Module', 'Implement login functionality', 'P002', 'HIGH', 'PENDING', '2025-04-30 17:00:00'),
('T004', 'U001', 'Database Backup', 'Backup existing data', 'P003', 'LOW', 'COMPLETED', '2024-12-01 17:00:00'),
('T005', 'U001', 'Schema Design', 'Design new DB schema', 'P003', 'MEDIUM', 'COMPLETED', '2025-01-15 17:00:00');

-- Insert Fake Data into Enroll
INSERT INTO Enroll (user_id, project_id, joined_at, status) VALUES
('U001', 'P001', '2025-01-05 09:00:00', 'ACTIVE'),
('U002', 'P002', '2025-01-06 09:00:00', 'ACTIVE'),
('U001', 'P002', '2025-02-05 09:00:00', 'PENDING'),
('U001', 'P003', '2024-11-05 09:00:00', 'ACTIVE'),
('U002', 'P001', '2025-01-10 09:00:00', 'REMOVED');