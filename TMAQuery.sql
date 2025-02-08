CREATE DATABASE TMA;

-- Department Table
CREATE TABLE Department (
    department_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    department_name NVARCHAR(100) NOT NULL,
    manager_id BIGINT NULL,
    created_at DATETIME2 DEFAULT SYSUTCDATETIME()
);

-- User Table
CREATE TABLE [User] (
    user_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    first_name NVARCHAR(50) NOT NULL,
    last_name NVARCHAR(50) NOT NULL,
    username NVARCHAR(50) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL, -- Hashed password
    email NVARCHAR(100) UNIQUE NOT NULL,
    phone NVARCHAR(20) NOT NULL,
    role NVARCHAR(50) NOT NULL,
    department_id BIGINT NULL,
    created_at DATETIME2 DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2 DEFAULT SYSUTCDATETIME()
);

-- Add Foreign Key Constraint for Department (User -> Department)
ALTER TABLE [User] 
ADD CONSTRAINT FK_User_Department FOREIGN KEY (department_id) 
REFERENCES Department(department_id) ON DELETE SET NULL;

-- Update Department Table to Reference User
ALTER TABLE Department 
ADD CONSTRAINT FK_Department_Manager FOREIGN KEY (manager_id) 
REFERENCES [User](user_id) ON DELETE SET NULL;

-- Project Table
CREATE TABLE Project (
    project_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    project_name NVARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    description NVARCHAR(MAX),
    budget DECIMAL(15,2) NULL,
    manager_id BIGINT NOT NULL,
    created_at DATETIME2 DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2 DEFAULT SYSUTCDATETIME(),
    FOREIGN KEY (manager_id) REFERENCES [User](user_id) ON DELETE NO ACTION
);

-- Enrollment Table (User-Project Relationship)
CREATE TABLE Enroll (
    enroll_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NULL,
    project_id BIGINT NULL,
    role NVARCHAR(50) NULL,
    joined_at DATETIME2 DEFAULT SYSUTCDATETIME()
);

-- Fix cascading delete by using ON DELETE SET NULL
ALTER TABLE Enroll 
ADD CONSTRAINT FK_Enroll_User FOREIGN KEY (user_id) 
REFERENCES [User](user_id) ON DELETE SET NULL;

ALTER TABLE Enroll 
ADD CONSTRAINT FK_Enroll_Project FOREIGN KEY (project_id) 
REFERENCES Project(project_id) ON DELETE SET NULL;

-- Task Table
CREATE TABLE Task (
    task_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    task_name NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX) NULL,
    priority NVARCHAR(10) CHECK (priority IN ('Low', 'Medium', 'High')) NOT NULL,
    status NVARCHAR(15) CHECK (status IN ('To Do', 'In Progress', 'Completed')) NOT NULL,
    estimate_hours INT NULL,
    actual_hours INT NULL,
    deadline DATE NOT NULL,
    project_id BIGINT NOT NULL,
    assignee_id BIGINT NULL,
    parent_task_id BIGINT NULL,
    created_at DATETIME2 DEFAULT SYSUTCDATETIME(),
    updated_at DATETIME2 DEFAULT SYSUTCDATETIME(),
    FOREIGN KEY (project_id) REFERENCES Project(project_id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES [User](user_id) ON DELETE SET NULL,
    FOREIGN KEY (parent_task_id) REFERENCES Task(task_id) ON DELETE SET NULL
);

-- Assignee Table (User-Task Relationship)
CREATE TABLE Assignee (
    assignee_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    assigned_at DATETIME2 DEFAULT SYSUTCDATETIME(),
    FOREIGN KEY (user_id) REFERENCES [User](user_id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES Task(task_id) ON DELETE CASCADE
);

-- Comment Table
CREATE TABLE Comment (
    comment_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    created_at DATETIME2 DEFAULT SYSUTCDATETIME(),
    FOREIGN KEY (task_id) REFERENCES Task(task_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES [User](user_id) ON DELETE CASCADE
);

-- Notification Table
CREATE TABLE Notification (
    notification_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    is_read BIT DEFAULT 0,
    created_at DATETIME2 DEFAULT SYSUTCDATETIME(),
    FOREIGN KEY (user_id) REFERENCES [User](user_id) ON DELETE CASCADE
);

-- Attachment Table
CREATE TABLE Attachment (
    attachment_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    task_id BIGINT NOT NULL,
    uploaded_by BIGINT NOT NULL,
    file_name NVARCHAR(255) NOT NULL,
    file_path NVARCHAR(255) NOT NULL,
    uploaded_at DATETIME2 DEFAULT SYSUTCDATETIME(),
    FOREIGN KEY (task_id) REFERENCES Task(task_id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES [User](user_id) ON DELETE CASCADE
)

-- Add Project_Label
CREATE TABLE Project_Label (
	project_label_id BIGINT IDENTITY(1,1) PRIMARY KEY,
	project_id BIGINT NOT NULL,
	label_name NVARCHAR(255) NOT NULL,
	FOREIGN KEY (project_id) REFERENCES Project(project_id) ON DELETE CASCADE
)

-- Add Task_Label
CREATE TABLE Task_Label (
	task_label_id BIGINT IDENTITY(1,1) PRIMARY KEY,
	task_id BIGINT NOT NULL,
	label_name NVARCHAR(255) NOT NULL,
	FOREIGN KEY (task_id) REFERENCES Task(task_id) ON DELETE CASCADE
);
