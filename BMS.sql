use sql12746990;
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
  Email VARCHAR(255),
  BIN INT,
  MainCourseChoice INT,
  AppetizerChoice INT,
  DessertChoice INT,
  Remarks TEXT,
  RegistrationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (Email) REFERENCES Attendee(Email),
  FOREIGN KEY (BIN) REFERENCES Banquet(BIN),
  FOREIGN KEY (MainCourseChoice) REFERENCES Meal(MealID),
  FOREIGN KEY (AppetizerChoice) REFERENCES Meal(MealID),
  FOREIGN KEY (DessertChoice) REFERENCES Meal(MealID)
);


-- Add indexes for performance optimization
CREATE INDEX idx_banquet_datetime ON Banquet(DateTime);
CREATE INDEX idx_registration_email ON Registration(Email);
CREATE INDEX idx_registration_bin ON Registration(BIN);
CREATE INDEX idx_meal_bin ON Meal(BIN);

INSERT INTO Banquet (BanquetName, DateTime, Address, Location, Quota, AdminID, F_NameOfTheContactStaff, L_NameOfTheContactStaff, Available) VALUES
('Annual Gala', '2024-12-15 18:00:00', '123 Gala St.', 'City Hall', 200, 1, 'John', 'Doe', 'Y'),
('Charity Dinner', '2024-12-20 19:00:00', '456 Charity Ave.', 'Community Center', 150, 2, 'Jane', 'Smith', 'Y'),
('Holiday Feast', '2024-12-25 17:00:00', '789 Holiday Rd.', 'Convention Center', 300, 3, 'Tom', 'Jones', 'Y'),
('Spring Festival', '2025-03-10 12:00:00', '321 Spring Blvd.', 'Park Pavilion', 100, 4, 'Sara', 'Davis', 'Y'),
('Summer Bash', '2025-06-15 16:00:00', '654 Summer Ln.', 'Beachside Resort', 250, 5, 'Mike', 'Wilson', 'Y');

-- Meals for Annual Gala (BIN = 1)
INSERT INTO Meal (BIN, Type, DishName, Price, SpecialCuisine) VALUES
(1, 'Main Course', 'Grilled Salmon with Asparagus', 29.99, NULL),
(1, 'Appetizer', 'Stuffed Mushrooms', 12.99, NULL),
(1, 'Dessert', 'Chocolate Lava Cake', 8.99, NULL);

-- Meals for Charity Dinner (BIN = 2)
INSERT INTO Meal (BIN, Type, DishName, Price, SpecialCuisine) VALUES
(2, 'Main Course', 'Roast Chicken with Vegetables', 24.99, NULL),
(2, 'Appetizer', 'Caesar Salad', 10.99, NULL),
(2, 'Dessert', 'Cheesecake with Berries', 7.99, NULL);

-- Meals for Holiday Feast (BIN = 3)
INSERT INTO Meal (BIN, Type, DishName, Price, SpecialCuisine) VALUES
(3, 'Main Course', 'Beef Wellington with Gravy', 39.99, NULL),
(3, 'Appetizer', 'Shrimp Cocktail', 14.99, NULL),
(3, 'Dessert', 'Pumpkin Pie with Whipped Cream', 6.99, NULL);

-- Meals for Spring Festival (BIN = 4)
INSERT INTO Meal (BIN, Type, DishName, Price, SpecialCuisine) VALUES
(4, 'Main Course', 'Vegetarian Lasagna', 22.99, NULL),
(4, 'Appetizer', 'Bruschetta with Tomato and Basil', 9.99, NULL),
(4, 'Dessert', 'Fruit Tartlet', 5.99, NULL);

-- Meals for Summer Bash (BIN = 5)
INSERT INTO Meal (BIN, Type, DishName, Price, SpecialCuisine) VALUES
(5, 'Main Course', 'BBQ Ribs with Coleslaw', 34.99, NULL),
(5, 'Appetizer', 'Chicken Wings with Hot Sauce', 11.99, NULL),
(5, 'Dessert', 'Ice Cream Sundae Bar', 8.49, NULL);

INSERT INTO Attendee (Email,F_Name,L_Name ,Address ,AttendeeType ,Password ,MobileNumber ,AffiliatedOrganization) VALUES
('john.doe@example.com','John','Doe','101 Main St','student','password123','12345678','PolyU'),
('jane.doe@example.com','Jane','Doe','102 Main St','staff','password123','23456789','SPEED'),
('alice.smith@example.com','Alice','Smith','103 Main St','alumni','password123','34567890','HKCC'),
('bob.jones@example.com','Bob','Jones','104 Main St','guest','password123','45678901','Others'),
('charlie.brown@example.com','Charlie','Brown','105 Main St','student','password123','56789012','PolyU'),
('david.wilson@example.com','David','Wilson','106 Main St','staff','password123','67890123','SPEED'),
('emily.johnson@example.com','Emily','Johnson','107 Main St','alumni','password123','78901234','HKCC'),
('frank.miller@example.com','Frank','Miller','108 Main St','guest','password123','89012345','Others'),
('grace.davis@example.com','Grace','Davis','109 Main St','student','password123','90123456','PolyU'),
('henry.moore@example.com ','Henry ','Moore ','110 Main St ','staff ','password123 ','01234567 ','SPEED'),
('zoe.taylor@example.com ','Zoe ','Taylor ','150 Main St ','guest ','password123 ','87654321 ','Others');

INSERT INTO Registration (Email,BIN ,MainCourseChoice ,AppetizerChoice ,DessertChoice ,Remarks) VALUES
('john.doe@example.com ',1 ,1 ,2 ,3 ,'Looking forward to it!'),
('jane.doe@example.com ',2 ,4 ,5 ,6 ,'Excited to join!'),
('alice.smith@example.com ',3 ,7 ,8 ,9 ,'Can\'t wait!'),
('bob.jones@example.com ',4 ,10 ,11 ,12 ,'Hope it\'s fun!'),
-- Add more registrations as needed...
;





INSERT INTO Admin (Name, Email, Password) VALUES
(123,123,123);

-- Commented operations for easy testing and debugging:
  --SET FOREIGN_KEY_CHECKS = 0;
  -- Drop all tables
  --DROP TABLE IF EXISTS SeatReservation;
  --DROP TABLE IF EXISTS Registration;
  --DROP TABLE IF EXISTS Meal;
  --DROP TABLE IF EXISTS Banquet;
  --DROP TABLE IF EXISTS Admin;
  --DROP TABLE IF EXISTS Attendee;
  -- Re-enable foreign key checks
  --SET FOREIGN_KEY_CHECKS = 1;

