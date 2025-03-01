-- Create the database
CREATE DATABASE TMA;
USE TMA;

-- Create UserProfile table
CREATE TABLE UserProfile (
    user_id VARCHAR(50) PRIMARY KEY,
    full_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    phone_number VARCHAR(20)
);

-- Create User table
CREATE TABLE User (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create Event table
CREATE TABLE Event (
    event_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50),
    joined_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- Create Project table
CREATE TABLE Project (
    project_id VARCHAR(50) PRIMARY KEY,
    manager VARCHAR(50),
    status VARCHAR(20)
);

-- Create ProjectInfo table
CREATE TABLE ProjectInfo (
    project_id VARCHAR(50) PRIMARY KEY,
    project_name VARCHAR(100),
    description TEXT,
    start_date DATE,
    end_date DATE,
    budget DECIMAL(15,2),
    FOREIGN KEY (project_id) REFERENCES Project(project_id)
);

-- Create Task table
CREATE TABLE Task (
    task_id VARCHAR(50) PRIMARY KEY,
    project_id VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    status VARCHAR(20),
    FOREIGN KEY (project_id) REFERENCES Project(project_id)
);

-- Create TaskInfo table
CREATE TABLE TaskInfo (
    task_id VARCHAR(50) PRIMARY KEY,
    task_name VARCHAR(100),
    description TEXT,
    deadline DATE,
    priority INT,
    FOREIGN KEY (task_id) REFERENCES Task(task_id)
);

-- Create UserTask table (for many-to-many relationship between User and Task)
CREATE TABLE UserTask (
    user_id VARCHAR(50),
    task_id VARCHAR(50),
    begin_at TIMESTAMP,
    done_at TIMESTAMP,
    PRIMARY KEY (user_id, task_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (task_id) REFERENCES Task(task_id)
);