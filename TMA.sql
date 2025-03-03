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
    FOREIGN KEY (manager_id) REFERENCES User(user_id) ON DELETE SET NULL
);

-- Task Table
CREATE TABLE Task (
    task_id VARCHAR(50) PRIMARY KEY,
    member_id VARCHAR(50),
    task_name VARCHAR(100) NOT NULL,
    description TEXT,
    project_id VARCHAR(50),
    priority ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'MEDIUM',
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING',
    deadline DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES User(user_id) ON DELETE SET NULL,
    FOREIGN KEY (project_id) REFERENCES Project(project_id) ON DELETE CASCADE
);

-- Enroll Table
CREATE TABLE Enroll (
    user_id VARCHAR(50),
    project_id VARCHAR(50),
    joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'ACTIVE', 'REMOVED') DEFAULT 'PENDING',
    PRIMARY KEY (user_id, project_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES Project(project_id) ON DELETE CASCADE
);