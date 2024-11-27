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

INSERT INTO Banquet (BanquetName, DateTime, Address, Location, Quota, F_NameOfTheContactStaff, L_NameOfTheContactStaff, Available) VALUES
('Annual Gala', '2024-12-15 18:00:00', '123 Gala St.', 'City Hall', 200,  'John', 'Doe', 'Y'),
('Charity Dinner', '2024-12-20 19:00:00', '456 Charity Ave.', 'Community Center', 150,  'Jane', 'Smith', 'Y'),
('Holiday Feast', '2024-12-25 17:00:00', '789 Holiday Rd.', 'Convention Center', 300,  'Tom', 'Jones', 'Y'),
('Spring Festival', '2025-03-10 12:00:00', '321 Spring Blvd.', 'Park Pavilion', 100,  'Sara', 'Davis', 'Y'),
('Summer Bash', '2025-06-15 16:00:00', '654 Summer Ln.', 'Beachside Resort', 250,  'Mike', 'Wilson', 'Y');

-- Meals
INSERT INTO Meal (BIN, Type, DishName, Price, SpecialCuisine) VALUES
-- BIN 1
(1, 'Main Course', 'Grilled Salmon with Asparagus', 29.99, 'Sushi'),
(1, 'Main Course', 'Chicken Parmesan', 24.99, 'Crown Roast of Pork with Mushroom Dressing'),
(1, 'Main Course', 'Vegetable Lasagna', 19.99, 'Bacon-Wrapped Pesto Pork Tenderloin'),
(1, 'Main Course', 'Beef Stroganoff', 26.99, 'Braised Short Ribs'),
(1, 'Main Course', 'Shrimp Scampi', 27.99, 'Stuffed Salmon'),

(1, 'Appetizer', 'Stuffed Mushrooms', 12.99, 'Italian'),
(1, 'Appetizer', 'Caprese Salad', 10.99, 'Italian'),
(1, 'Appetizer', 'Bruschetta', 9.99, 'Italian'),
(1, 'Appetizer', 'Garlic Bread', 5.99, 'Italian'),
(1, 'Appetizer', 'Antipasto Platter', 14.99, 'Italian'),

(1, 'Dessert', 'Chocolate Lava Cake', 8.99, 'Italian'),
(1, 'Dessert', 'Tiramisu', 7.99, 'Italian'),
(1, 'Dessert', 'Panna Cotta', 6.99, 'Italian'),
(1, 'Dessert', 'Cheesecake', 7.49, 'American'),
(1, 'Dessert', 'Fruit Tart', 5.99, 'French'),

-- BIN 2
(2, 'Main Course', 'Chicken Alfredo Pasta', 24.99, 'Crab Cakes'),
(2, 'Main Course', 'Beef Tacos', 19.99, 'Risotto alla Milanese'),
(2, 'Main Course', 'Vegetable Stir Fry', 18.99, 'Osso Buco alla Milanese'),
(2, 'Main Course', 'Lamb Curry', 27.99, 'Vitello Tonnato'),
(2, 'Main Course', 'BBQ Ribs', 32.99, 'Polenta '),

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
(3, 'Main Course', 'Pork Schnitzel', 22.99, 'Duck Liver Pâté'),
(3, 'Main Course', 'Seafood Paella', 30.99, 'Chimac '),
(3, 'Main Course', 'Falafel Wrap', 15.99, 'Prawn Gratin'),
(3, 'Main Course', 'Duck Confit', 35.99, 'Tacos al Pastor'),
(3, 'Main Course', 'Stuffed Bell Peppers', 18.99, 'Moussaka'),

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
(4, 'Main Course', 'Eggplant Parmesan', 21.99, 'Chili Crab'),
(4, 'Main Course', 'Teriyaki Chicken', 23.99, 'Polenta '),
(4, 'Main Course', 'Beef Bulgogi', 25.99, 'Bangers and Mash'),
(4, 'Main Course', 'Paneer Tikka Masala', 19.99, 'Barbecued Meats (Siu Mei)'),
(4, 'Main Course', 'Fish and Chips', 18.99, 'Dim Sum'),

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
(5, 'Main Course', 'Vegetable Curry', 17.99, 'Pho'),
(5, 'Main Course', 'Pasta Primavera', 19.99, 'Tacos al Pastor'),
(5, 'Main Course', 'Chicken Tikka Masala', 22.99, 'Baklava'),
(5, 'Main Course', 'Shrimp Fried Rice', 20.99, 'Ramen '),
(5, 'Main Course', 'Beef Wellington', 34.99, 'Moussaka '),

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
('jane.smith@example.com', 'Jane', 'Smith', '456 Elm St, Kowloon', 'staff', 'password123', '23456789', 'PolyU'),
('mike.jones@example.com', 'Mike', 'Jones', '789 Pine St, Kowloon', 'alumni', 'password123', '34567890', 'PolyU'),
('lisa.white@example.com', 'Lisa', 'White', '321 Maple St, Kowloon', 'guest', 'password123', '45678901', 'PolyU'),
('tom.brown@example.com', 'Tom', 'Brown', '654 Oak St, Kowloon', 'student', 'password123', '56789012', 'PolyU'),
('soo.bin@example.com', 'Soo', 'Bin', 'Maple St, Kowloon', 'student', 'password123', '12345678', 'PolyU'),
('jason.chan@example.com', 'Jason', 'Chan', '400 Origami St, Kowloon', 'student', 'password123', '12345678', 'SPEED'),
('ivan.black@example.com', 'Ivan', 'Black', '009 Super St, Kowloon', 'student', 'password123', '12345678', 'Others'),
('jessica.brown@example.com', 'Jessica', 'Brown', '106 Birch St, Kowloon', 'student', 'password123', '12345678', 'PolyU'),
('amanda.argent@example.com', 'Amanda', 'Argent', '980 Finch St, Kowloon', 'student', 'password123', '12345678', 'PolyU'),
('julia.carmen@example.com', 'Julia', 'Carmen', '230 Chrysanthemum St, Kowloon', 'staff', 'password123', '12345678', 'Others'),
('till.sue@example.com', 'Till', 'Sue', '730 Model St, Kowloon', 'staff', 'password123', '12345678', 'SPEED'),
('richard.wilson@example.com', 'Richard', 'Wilson', '178 Subway St, Kowloon', 'alumni', 'password123', '12345678', 'HKCC'),
('mia.krystal@example.com', 'Mia', 'Krystal', '122 Metro St, Kowloon', 'alumni', 'password123', '12345678', 'HKCC'),
('bryan.cheung@example.com', 'Bryan', 'Cheung', '234 Circus St, Kowloon', 'alumni', 'password123', '12345678', 'HKCC'),
('cherry.liu@example.com', 'Cherry', 'Liu', '329 Honey St, Kowloon', 'guest', 'password123', '12345678', 'PolyU'),
('nevaeh.christian@example.com', 'Nevaeh', 'Christian', '333 Christ St, Kowloon', 'guest', 'password123', '12345678', 'Others'),
('pollie.horse@example.com', 'Pollie', 'Horse', '300 Judgment St, Kowloon', 'guest', 'password123', '12345678', 'Others'),
('sabrina.lam@example.com', 'Sabrina', 'Lam', '013 College St, Kowloon', 'alumni', 'password123', '12345678', 'Others'),
('sara.davis@example.com', 'Sara', 'Davis', '987 Cedar St, Kowloon', 'staff', 'password123', '67890123', 'PolyU'),
('paul.miller@example.com', 'Paul', 'Miller', '159 Birch St, Kowloon', 'alumni', 'password123', '78901234', 'PolyU'),
('anna.johnson@example.com', 'Anna', 'Johnson', '753 Walnut St, Kowloon', 'guest', 'password123', '89012345', 'Others'),
('chris.lee@example.com', 'Chris', 'Lee', '852 Cherry St, Kowloon', 'student', 'password123', '90123456', 'PolyU'),
('karen.wilson@example.com', 'Karen', 'Wilson', '951 Chestnut St, Kowloon', 'staff', 'password123', '01234567', 'SPEED'),
('david.moore@example.com', 'David', 'Moore', '258 Spruce St, Kowloon', 'alumni', 'password123', '12345678', 'HKCC'),
('emily.taylor@example.com', 'Emily', 'Taylor', '369 Fir St, Kowloon', 'guest', 'password123', '23456789', 'SPEED'),
('james.anderson@example.com', 'James', 'Anderson', '147 Poplar St, Kowloon', 'student', 'password123', '34567890', 'PolyU'),
('susan.thomas@example.com', 'Susan', 'Thomas', '258 Willow St, Kowloon', 'staff', 'password123', '45678901', 'SPEED'),
('robert.jackson@example.com', 'Robert', 'Jackson', '369 Sycamore St, Kowloon', 'alumni', 'password123', '56789012', 'HKCC'),
('linda.harris@example.com', 'Linda', 'Harris', '159 Elm St, Kowloon', 'guest', 'password123', '67890123', 'SPEED'),
('charles.martin@example.com', 'Charles', 'Martin', '753 Maple St, Kowloon', 'student', 'password123', '78901234', 'HKCC'),
('jessica.thompson@example.com', 'Jessica', 'Thompson', '951 Oak St, Kowloon', 'staff', 'password123', '89012345', 'HKCC'),
('daniel.white@example.com', 'Daniel', 'White', '852 Pine St, Kowloon', 'alumni', 'password123', '90123456', 'HKCC'),
('sophia.martinez@example.com', 'Sophia', 'Martinez', '654 Cedar St, Kowloon', 'guest', 'password123', '01234567', 'Others'),
('matthew.clark@example.com', 'Matthew', 'Clark', '321 Birch St, Kowloon', 'student', 'password123', '12345678', 'PolyU'),
('olivia.rodriguez@example.com', 'Olivia', 'Rodriguez', '456 Spruce St, Kowloon', 'staff', 'password123', '23456789', 'SPEED'),
('benjamin.lewis@example.com', 'Benjamin', 'Lewis', '789 Fir St, Kowloon', 'alumni', 'password123', '34567890', 'HKCC'),
('isabella.walker@example.com', 'Isabella', 'Walker', '159 Poplar St, Kowloon', 'guest', 'password123', '45678901', 'Others'),
('elijah.hall@example.com', 'Elijah', 'Hall', '753 Willow St, Kowloon', 'student', 'password123', '56789012', 'PolyU'),
('ethan.hughes@example.com', 'Ethan', 'Hughes', '123 Maple St, Kowloon', 'student', 'password123', '34567891', 'PolyU'),
('madison.morris@example.com', 'Madison', 'Morris', '456 Oak St, Kowloon', 'staff', 'password123', '45678912', 'SPEED'),
('alexander.wood@example.com', 'Alexander', 'Wood', '789 Pine St, Kowloon', 'alumni', 'password123', '56789023', 'HKCC'),
('grace.martinez@example.com', 'Grace', 'Martinez', '321 Birch St, Kowloon', 'guest', 'password123', '67890134', 'HKCC'),
('joseph.brown@example.com', 'Joseph', 'Brown', '654 Cedar St, Kowloon', 'student', 'password123', '78901245', 'PolyU'),
('chloe.jones@example.com', 'Chloe', 'Jones', '987 Spruce St, Kowloon', 'staff', 'password123', '89012356', 'SPEED'),
('michael.garcia@example.com', 'Michael', 'Garcia', '159 Fir St, Kowloon', 'alumni', 'password123', '90123467', 'HKCC'),
('sophia.miller@example.com', 'Sophia', 'Miller', '753 Elm St, Kowloon', 'guest', 'password123', '01234578', 'HKCC'),
('william.rodriguez@example.com', 'William', 'Rodriguez', '258 Maple St, Kowloon', 'student', 'password123', '12345689', 'PolyU'),
('elizabeth.johnson@example.com', 'Elizabeth', 'Johnson', '369 Oak St, Kowloon', 'staff', 'password123', '23456790', 'SPEED'),
('james.wilson@example.com', 'James', 'Wilson', '147 Pine St, Kowloon', 'alumni', 'password123', '34567801', 'HKCC'),
('olivia.thomas@example.com', 'Olivia', 'Thomas', '258 Cedar St, Kowloon', 'guest', 'password123', '45678912', 'HKCC'),
('benjamin.martin@example.com', 'Benjamin', 'Martin', '369 Birch St, Kowloon', 'student', 'password123', '56789023', 'PolyU'),
('mia.clark@example.com', 'Mia', 'Clark', '951 Spruce St, Kowloon', 'staff', 'password123', '67890134', 'SPEED'),
('noah.lewis@example.com', 'Noah', 'Lewis', '753 Fir St, Kowloon', 'alumni', 'password123', '78901245', 'HKCC'),
('ava.walker@example.com', 'Ava', 'Walker', '159 Maple St, Kowloon', 'guest', 'password123', '89012356', 'Others'),
('jackson.harris@example.com', 'Jackson', 'Harris', '987 Cedar St, Kowloon', 'alumni', 'password123', '12345689', 'HKCC'),
('grace.jones@example.com', 'Grace', 'Jones', '258 Fir St, Kowloon', 'guest', 'password123', '23456790', 'Others'),
('mia.allen@example.com', 'Mia', 'Allen', '951 Sycamore St, Kowloon', 'staff', 'password123', '67890123', 'SPEED'),
('noah.young@example.com', 'Noah', 'Young', '258 Elm St, Kowloon', 'alumni', 'password123', '78901234', 'HKCC'),
('ava.king@example.com', 'Ava', 'King', '369 Maple St, Kowloon', 'guest', 'password123', '89012345', 'Others'),
('lucas.scott@example.com', 'Lucas', 'Scott', '147 Oak St, Kowloon', 'student', 'password123', '90123456', 'PolyU'),
('charlotte.green@example.com', 'Charlotte', 'Green', '258 Pine St, Kowloon', 'staff', 'password123', '01234567', 'SPEED'),
('jack.adams@example.com', 'Jack', 'Adams', '369 Cedar St, Kowloon', 'alumni', 'password123', '12345678', 'HKCC'),
('amelia.baker@example.com', 'Amelia', 'Baker', '159 Birch St, Kowloon', 'guest', 'password123', '23456789', 'Others');

-- For each banquet, register attendees who haven't registered yet
INSERT INTO Registration (Email, BIN, MainCourseChoice, AppetizerChoice, DessertChoice, Remarks)
SELECT
    a.Email,
    b.BIN,
    (SELECT MealID FROM Meal WHERE BIN = b.BIN AND Type = 'Main Course' ORDER BY RAND() LIMIT 1),
    (SELECT MealID FROM Meal WHERE BIN = b.BIN AND Type = 'Appetizer' ORDER BY RAND() LIMIT 1),
    (SELECT MealID FROM Meal WHERE BIN = b.BIN AND Type = 'Dessert' ORDER BY RAND() LIMIT 1),
    'Auto-registered for additional banquet'
FROM
    Attendee a
CROSS JOIN
    Banquet b
WHERE
    NOT EXISTS (
        SELECT 1
        FROM Registration r
        WHERE r.Email = a.Email AND r.BIN = b.BIN
    );

-- Display the new registrations
SELECT * FROM Registration WHERE Remarks = 'Auto-registered for additional banquet';

-- Show a summary of registrations per banquet
SELECT
    b.BIN,
    b.BanquetName,
    COUNT(r.RegistrationID) as TotalRegistrations,
    b.Quota,
    b.Quota - COUNT(r.RegistrationID) as RemainingSpots
FROM
    Banquet b
LEFT JOIN
    Registration r ON b.BIN = r.BIN
GROUP BY
    b.BIN, b.BanquetName, b.Quota
ORDER BY
    b.BIN;


INSERT INTO Admin (Name, Email, Password) VALUES
(admin,admin,admin);

-- Commented operations for easy testing and debugging:
     -- SET FOREIGN_KEY_CHECKS = 0;

  -- Drop all tables

    -- DROP TABLE IF EXISTS Registration;
    -- DROP TABLE IF EXISTS Meal;
    -- DROP TABLE IF EXISTS Banquet;
    -- DROP TABLE IF EXISTS Admin;
    -- DROP TABLE IF EXISTS Attendee;

  -- Re-enable foreign key checks

    -- SET FOREIGN_KEY_CHECKS = 1;

-- DELETE FROM Registration
-- WHERE RegistrationID %11 = 3;
