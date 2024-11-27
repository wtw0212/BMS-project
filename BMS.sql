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

-- Meals
INSERT INTO Meal (BIN, Type, DishName, Price, SpecialCuisine) VALUES
-- BIN 1
(1, 'Main Course', 'Grilled Salmon with Asparagus', 29.99, NULL),
(1, 'Main Course', 'Chicken Parmesan', 24.99, 'Italian'),
(1, 'Main Course', 'Vegetable Lasagna', 19.99, 'Italian'),
(1, 'Main Course', 'Beef Stroganoff', 26.99, 'Russian'),
(1, 'Main Course', 'Shrimp Scampi', 27.99, 'Italian'),

(1, 'Appetizer', 'Stuffed Mushrooms', 12.99, NULL),
(1, 'Appetizer', 'Caprese Salad', 10.99, 'Italian'),
(1, 'Appetizer', 'Bruschetta', 9.99, 'Italian'),
(1, 'Appetizer', 'Garlic Bread', 5.99, 'Italian'),
(1, 'Appetizer', 'Antipasto Platter', 14.99, 'Italian'),

(1, 'Dessert', 'Chocolate Lava Cake', 8.99, NULL),
(1, 'Dessert', 'Tiramisu', 7.99, 'Italian'),
(1, 'Dessert', 'Panna Cotta', 6.99, 'Italian'),
(1, 'Dessert', 'Cheesecake', 7.49, 'American'),
(1, 'Dessert', 'Fruit Tart', 5.99, 'French'),

-- BIN 2
(2, 'Main Course', 'Chicken Alfredo Pasta', 24.99, 'Italian'),
(2, 'Main Course', 'Beef Tacos', 19.99, 'Mexican'),
(2, 'Main Course', 'Vegetable Stir Fry', 18.99, 'Asian'),
(2, 'Main Course', 'Lamb Curry', 27.99, 'Indian'),
(2, 'Main Course', 'BBQ Ribs', 32.99, 'American'),

(2, 'Appetizer', 'Bruschetta', 9.99, 'Italian'),
(2, 'Appetizer', 'Guacamole and Chips', 8.99, 'Mexican'),
(2, 'Appetizer', 'Spring Rolls', 6.99, 'Asian'),
(2, 'Appetizer', 'Samosas', 5.99, 'Indian'),
(2, 'Appetizer', 'Buffalo Wings', 10.99, 'American'),

(2, 'Dessert', 'Tiramisu', 7.99, 'Italian'),
(2, 'Dessert', 'Churros', 5.99, 'Mexican'),
(2, 'Dessert', 'Mango Sticky Rice', 7.49, 'Asian'),
(2, 'Dessert', 'Gulab Jamun', 4.99, 'Indian'),
(2, 'Dessert', 'Apple Pie', 6.99, 'American'),

-- BIN 3
(3, 'Main Course', 'Pork Schnitzel', 22.99, 'German'),
(3, 'Main Course', 'Seafood Paella', 30.99, 'Spanish'),
(3, 'Main Course', 'Falafel Wrap', 15.99, 'Middle Eastern'),
(3, 'Main Course', 'Duck Confit', 35.99, 'French'),
(3, 'Main Course', 'Stuffed Bell Peppers', 18.99, 'Mediterranean'),

(3, 'Appetizer', 'Pretzel Bites', 7.99, 'German'),
(3, 'Appetizer', 'Patatas Bravas', 6.99, 'Spanish'),
(3, 'Appetizer', 'Hummus and Pita', 8.99, 'Middle Eastern'),
(3, 'Appetizer', 'Escargot', 12.99, 'French'),
(3, 'Appetizer', 'Greek Salad', 9.99, 'Mediterranean'),

(3, 'Dessert', 'Black Forest Cake', 8.99, 'German'),
(3, 'Dessert', 'Flan', 5.99, 'Spanish'),
(3, 'Dessert', 'Baklava', 6.49, 'Middle Eastern'),
(3, 'Dessert', 'Crème Brûlée', 9.49, 'French'),
(3, 'Dessert', 'Pistachio Ice Cream', 4.99, 'Mediterranean'),

-- BIN 4
(4, 'Main Course', 'Eggplant Parmesan', 21.99, 'Italian'),
(4, 'Main Course', 'Teriyaki Chicken', 23.99, 'Japanese'),
(4, 'Main Course', 'Beef Bulgogi', 25.99, 'Korean'),
(4, 'Main Course', 'Paneer Tikka Masala', 19.99, 'Indian'),
(4, 'Main Course', 'Fish and Chips', 18.99, 'British'),

(4, 'Appetizer', 'Fried Calamari', 11.99, 'Italian'),
(4, 'Appetizer', 'Edamame', 5.99, 'Japanese'),
(4, 'Appetizer', 'Kimchi', 4.99, 'Korean'),
(4, 'Appetizer', 'Onion Bhaji', 6.99, 'Indian'),
(4, 'Appetizer', 'Mushy Peas', 3.99, 'British'),

(4, 'Dessert', 'Pistachio Baklava', 7.99, 'Middle Eastern'),
(4, 'Dessert', 'Mochi Ice Cream', 6.99, 'Japanese'),
(4, 'Dessert', 'Bingsu', 8.49, 'Korean'),
(4, 'Dessert', 'Ras Malai', 5.99, 'Indian'),
(4, 'Dessert', 'Sticky Toffee Pudding', 6.49, 'British'),

-- BIN 5
(5, 'Main Course', 'Vegetable Curry', 17.99, 'Indian'),
(5, 'Main Course', 'Pasta Primavera', 19.99, 'Italian'),
(5, 'Main Course', 'Chicken Tikka Masala', 22.99, 'Indian'),
(5, 'Main Course', 'Shrimp Fried Rice', 20.99, 'Asian'),
(5, 'Main Course', 'Beef Wellington', 34.99, 'British'),

(5, 'Appetizer', 'Vegetable Spring Rolls', 6.99, 'Asian'),
(5, 'Appetizer', 'Paneer Pakora', 5.99, 'Indian'),
(5, 'Appetizer', 'Garlic Knots', 4.99, 'Italian'),
(5, 'Appetizer', 'Prawn Crackers', 3.99, 'Asian'),
(5, 'Appetizer', 'Scotch Eggs', 8.99, 'British'),

(5, 'Dessert', 'Gajar Halwa', 5.49, 'Indian'),
(5, 'Dessert', 'Panna Cotta', 6.99, 'Italian'),
(5, 'Dessert', 'Mango Mousse', 7.49, 'Asian'),
(5, 'Dessert', 'Sticky Date Pudding', 6.99, 'British'),
(5, 'Dessert', 'Chocolate Mousse', 7.99, 'French');

INSERT INTO Attendee (Email, F_Name, L_Name, Address, AttendeeType, Password, MobileNumber, AffiliatedOrganization) VALUES
('john.doe@example.com', 'John', 'Doe', '123 Main St, Kowloon', 'student', 'password123', '12345678', 'PolyU'),
('jane.smith@example.com', 'Jane', 'Smith', '456 Elm St, Kowloon', 'staff', 'password123', '23456789', 'SPEED'),
('mike.jones@example.com', 'Mike', 'Jones', '789 Pine St, Kowloon', 'alumni', 'password123', '34567890', 'HKCC'),
('lisa.white@example.com', 'Lisa', 'White', '321 Maple St, Kowloon', 'guest', 'password123', '45678901', 'Others'),
('tom.brown@example.com', 'Tom', 'Brown', '654 Oak St, Kowloon', 'student', 'password123', '56789012', 'PolyU'),
('sara.davis@example.com', 'Sara', 'Davis', '987 Cedar St, Kowloon', 'staff', 'password123', '67890123', 'SPEED'),
('paul.miller@example.com', 'Paul', 'Miller', '159 Birch St, Kowloon', 'alumni', 'password123', '78901234', 'HKCC'),
('anna.johnson@example.com', 'Anna', 'Johnson', '753 Walnut St, Kowloon', 'guest', 'password123', '89012345', 'Others'),
('chris.lee@example.com', 'Chris', 'Lee', '852 Cherry St, Kowloon', 'student', 'password123', '90123456', 'PolyU'),
('karen.wilson@example.com', 'Karen', 'Wilson', '951 Chestnut St, Kowloon', 'staff', 'password123', '01234567', 'SPEED'),
('david.moore@example.com', 'David', 'Moore', '258 Spruce St, Kowloon', 'alumni', 'password123', '12345678', 'HKCC'),
('emily.taylor@example.com', 'Emily', 'Taylor', '369 Fir St, Kowloon', 'guest', 'password123', '23456789', 'Others'),
('james.anderson@example.com', 'James', 'Anderson', '147 Poplar St, Kowloon', 'student', 'password123', '34567890', 'PolyU'),
('susan.thomas@example.com', 'Susan', 'Thomas', '258 Willow St, Kowloon', 'staff', 'password123', '45678901', 'SPEED'),
('robert.jackson@example.com', 'Robert', 'Jackson', '369 Sycamore St, Kowloon', 'alumni', 'password123', '56789012', 'HKCC'),
('linda.harris@example.com', 'Linda', 'Harris', '159 Elm St, Kowloon', 'guest', 'password123', '67890123', 'Others'),
('charles.martin@example.com', 'Charles', 'Martin', '753 Maple St, Kowloon', 'student', 'password123', '78901234', 'PolyU'),
('jessica.thompson@example.com', 'Jessica', 'Thompson', '951 Oak St, Kowloon', 'staff', 'password123', '89012345', 'SPEED'),
('daniel.white@example.com', 'Daniel', 'White', '852 Pine St, Kowloon', 'alumni', 'password123', '90123456', 'HKCC'),
('sophia.martinez@example.com', 'Sophia', 'Martinez', '654 Cedar St, Kowloon', 'guest', 'password123', '01234567', 'Others'),
('matthew.clark@example.com', 'Matthew', 'Clark', '321 Birch St, Kowloon', 'student', 'password123', '12345678', 'PolyU'),
('olivia.rodriguez@example.com', 'Olivia', 'Rodriguez', '456 Spruce St, Kowloon', 'staff', 'password123', '23456789', 'SPEED'),
('benjamin.lewis@example.com', 'Benjamin', 'Lewis', '789 Fir St, Kowloon', 'alumni', 'password123', '34567890', 'HKCC'),
('isabella.walker@example.com', 'Isabella', 'Walker', '159 Poplar St, Kowloon', 'guest', 'password123', '45678901', 'Others'),
('elijah.hall@example.com', 'Elijah', 'Hall', '753 Willow St, Kowloon', 'student', 'password123', '56789012', 'PolyU'),
('mia.allen@example.com', 'Mia', 'Allen', '951 Sycamore St, Kowloon', 'staff', 'password123', '67890123', 'SPEED'),
('noah.young@example.com', 'Noah', 'Young', '258 Elm St, Kowloon', 'alumni', 'password123', '78901234', 'HKCC'),
('ava.king@example.com', 'Ava', 'King', '369 Maple St, Kowloon', 'guest', 'password123', '89012345', 'Others'),
('lucas.scott@example.com', 'Lucas', 'Scott', '147 Oak St, Kowloon', 'student', 'password123', '90123456', 'PolyU'),
('charlotte.green@example.com', 'Charlotte', 'Green', '258 Pine St, Kowloon', 'staff', 'password123', '01234567', 'SPEED'),
('jack.adams@example.com', 'Jack', 'Adams', '369 Cedar St, Kowloon', 'alumni', 'password123', '12345678', 'HKCC'),
('amelia.baker@example.com', 'Amelia', 'Baker', '159 Birch St, Kowloon', 'guest', 'password123', '23456789', 'Others');

INSERT INTO Registration (Email, BIN, MainCourseChoice, AppetizerChoice, DessertChoice, Remarks) VALUES
-- BIN 1
('john.doe@example.com', 1, 1, 1, 1, 'Looking forward to it!'),
('jane.smith@example.com', 1, 2, 2, 2, 'Excited for the food!'),
('mike.jones@example.com', 1, 3, 3, 3, 'Can\'t wait!'),
('lisa.white@example.com', 1, 4, 4, 4, 'Hope it\'s delicious!'),
('tom.brown@example.com', 1, 5, 5, 5, 'I love this event!'),
('sara.davis@example.com', 1, 1, 2, 3, 'Allergic to nuts.'),

-- BIN 2
('paul.miller@example.com', 2, 6, 6, 6, 'Vegetarian option, please.'),
('anna.johnson@example.com', 2, 7, 7, 7, 'Excited to join!'),
('chris.lee@example.com', 2, 8, 8, 8, 'Looking forward to it!'),
('karen.wilson@example.com', 2, 9, 9, 9, 'Can\'t wait for the food!'),
('david.moore@example.com', 2, 10, 10, 10, 'Hope it\'s delicious!'),
('emily.taylor@example.com', 2, 11, 11, 11, 'Allergic to dairy.'),

-- BIN 3
('james.anderson@example.com', 3, 12, 12, 12, 'Excited for the event!'),
('susan.thomas@example.com', 3, 13, 13, 13, 'Looking forward to it!'),
('robert.jackson@example.com', 3, 14, 14, 14, 'Can\'t wait!'),
('linda.harris@example.com', 3, 15, 15, 15, 'Hope it\'s delicious!'),
('charles.martin@example.com', 3, 16, 16, 16, 'I love this event!'),
('jessica.thompson@example.com', 3, 17, 17, 17, 'Allergic to gluten.'),

-- BIN 4
('daniel.white@example.com', 4, 18, 18, 18, 'Looking forward to it!'),
('sophia.martinez@example.com', 4, 19, 19, 19, 'Excited for the food!'),
('matthew.clark@example.com', 4, 20, 20, 20, 'Can\'t wait!'),
('olivia.rodriguez@example.com', 4, 21, 21, 21, 'Hope it\'s delicious!'),
('benjamin.lewis@example.com', 4, 22, 22, 22, 'I love this event!'),
('isabella.walker@example.com', 4, 23, 23, 23, 'Allergic to seafood.'),

-- BIN 5
('elijah.hall@example.com', 5, 24, 24, 24, 'Excited for the event!'),
('mia.allen@example.com', 5, 25, 25, 25, 'Looking forward to it!'),
('noah.young@example.com', 5, 26, 26, 26, 'Can\'t wait!'),
('ava.king@example.com', 5, 27, 27, 27, 'Hope it\'s delicious!'),
('lucas.scott@example.com', 5, 28, 28, 28, 'I love this event!'),
('charlotte.green@example.com', 5, 29, 29, 29, 'Allergic to peanuts.');





INSERT INTO Admin (Name, Email, Password) VALUES
(123,123,123);

-- Commented operations for easy testing and debugging:
  -- SET FOREIGN_KEY_CHECKS = 0;
  -- Drop all tables
  -- DROP TABLE IF EXISTS SeatReservation;
  -- DROP TABLE IF EXISTS Registration;
  -- DROP TABLE IF EXISTS Meal;
  -- DROP TABLE IF EXISTS Banquet;
  -- DROP TABLE IF EXISTS Admin;
  -- DROP TABLE IF EXISTS Attendee;
  -- Re-enable foreign key checks
  -- SET FOREIGN_KEY_CHECKS = 1;

