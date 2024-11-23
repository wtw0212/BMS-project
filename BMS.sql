create database BMS;
USE BMS;

-- Create Admin table
CREATE TABLE Admin (
    AdminID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL
);

-- Create Banquet table
CREATE TABLE Banquet (
    BIN INT PRIMARY KEY AUTO_INCREMENT,
    BanquetName VARCHAR(100) NOT NULL,
    DateTime DATETIME NOT NULL,
    Address VARCHAR(255) NOT NULL,
    Location VARCHAR(100) NOT NULL,
    Quota INT NOT NULL,
    AdminID INT,
    F_NameOfTheContactStaff VARCHAR(100) NOT NULL,
    L_NameOfTheContactStaff VARCHAR(100) NOT NULL,
    Available CHAR(1) NOT NULL CHECK (Available IN ('Y', 'N')),
    FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)
);

-- Create Meal table
CREATE TABLE Meal (
    MealID INT PRIMARY KEY AUTO_INCREMENT,
    BIN INT,
    Type VARCHAR(50) NOT NULL,
    DishName VARCHAR(100) NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    SpecialCuisine VARCHAR(100),
    FOREIGN KEY (BIN) REFERENCES Banquet(BIN)
);

-- Create Attendee table
CREATE TABLE Attendee (
    Email VARCHAR(100) PRIMARY KEY,
    F_Name VARCHAR(100) NOT NULL,
    L_Name VARCHAR(100) NOT NULL,
    Address VARCHAR(255) NOT NULL,
    AttendeeType ENUM('staff', 'student', 'alumni', 'guest') NOT NULL,
    Password VARCHAR(255) NOT NULL,
    MobileNumber CHAR(8) NOT NULL,
    AffiliatedOrganization ENUM('PolyU', 'SPEED', 'HKCC', 'Others') NOT NULL,
    CHECK (Email LIKE '%@%'),
    CHECK (MobileNumber REGEXP '^[0-9]{8}$')
);

-- Create Registration table
CREATE TABLE Registration (
    RegistrationID INT PRIMARY KEY AUTO_INCREMENT,
    Email VARCHAR(100),
    BIN INT,
    MealChoice INT,
    Remarks TEXT,
    RegistrationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (Email) REFERENCES Attendee(Email),
    FOREIGN KEY (BIN) REFERENCES Banquet(BIN),
    FOREIGN KEY (MealChoice) REFERENCES Meal(MealID)
);

-- Create SeatReservation table
CREATE TABLE SeatReservation (
    ReservationID INT PRIMARY KEY AUTO_INCREMENT,
    Email VARCHAR(100),
    BIN INT,
    SeatNumber VARCHAR(10) NOT NULL,
    FOREIGN KEY (Email) REFERENCES Attendee(Email),
    FOREIGN KEY (BIN) REFERENCES Banquet(BIN)
);

-- Add indexes for performance optimization
CREATE INDEX idx_banquet_datetime ON Banquet(DateTime);
CREATE INDEX idx_registration_email ON Registration(Email);
CREATE INDEX idx_registration_bin ON Registration(BIN);
CREATE INDEX idx_meal_bin ON Meal(BIN);

INSERT INTO Admin (Name, Email, Password) VALUES
('Alice Johnson', 'alice.johnson@example.com', 'password123'),
('Bob Smith', 'bob.smith@example.com', 'securepass456');

INSERT INTO Banquet (BanquetName, DateTime, Address, Location, Quota, AdminID, F_NameOfTheContactStaff, L_NameOfTheContactStaff, Available) VALUES
('Annual Charity Gala', '2024-12-15 18:00:00', '123 Charity St, Cityville', 'Grand Hall', 100, 1, 'Jane', 'Doe', 'Y'),
('Tech Conference 2024', '2024-11-10 09:00:00', '456 Tech Ave, Cityville', 'Convention Center', 200, 2, 'John', 'Smith', 'Y'),
('Holiday Dinner Party', '2024-12-20 19:00:00', '789 Festive Rd, Cityville', 'Banquet Room A', 150, 1, 'Emily', 'Clark', 'N');

INSERT INTO Meal (BIN, Type, DishName, Price, SpecialCuisine) VALUES
(1, 'Main Course', 'Grilled Salmon', 25.00, 'Mediterranean'),
(1, 'Main Course', 'Roast Chicken', 20.00, 'American'),
(1, 'Dessert', 'Chocolate Cake', 8.00, 'French'),
(1, 'Appetizer', 'Caesar Salad', 10.00, 'Italian'),
(2, 'Main Course', 'Vegetarian Lasagna', 18.00, 'Italian'),
(2, 'Main Course', 'Beef Wellington', 30.00, 'British'),
(2, 'Dessert', 'Tiramisu', 9.00, 'Italian'),
(3, 'Main Course', 'Stuffed Peppers', 22.00, 'Mexican'),
(3, 'Appetizer', 'Bruschetta', 7.50, 'Italian'),
(3, 'Dessert', 'Pavlova', 12.00, 'Australian');

INSERT INTO Attendee (Email, F_Name, L_Name, Address, AttendeeType, Password, MobileNumber, AffiliatedOrganization) VALUES
('student1@example.com', 'Emily', 'Davis', '789 Student Rd, Cityville', 'student', 'studentpass123', '12345678', 'PolyU'),
('guest1@example.com', 'Michael', 'Brown', '321 Guest Blvd, Cityville', 'guest', 'guestpass456', '87654321', 'SPEED'),
('staff1@example.com', 'Sarah', 'Wilson', '654 Staff St, Cityville', 'staff', 'staffpass789', '23456789', 'Others'),
('alumni1@example.com', 'David', 'Lee', '159 Alumni Ave, Cityville', 'alumni', 'alumnipass321','34567890','HKCC');

INSERT INTO Registration (Email, BIN, MealChoice, Remarks) VALUES
('student1@example.com', 1, 1, ''),
('guest1@example.com', 2, 5,'Window seat preference'),
('staff1@example.com','3','7','Allergic to nuts'),
('alumni1@example.com','2','6','No special requests');

INSERT INTO SeatReservation (Email, BIN, SeatNumber) VALUES
('student1@example.com','1','A1'),
('guest1@example.com','2','B2'),
('staff1@example.com','3','C3'),
('alumni1@example.com','2','D4');


INSERT INTO Admin (Name, Email, Password) VALUES
(123,123,123);
