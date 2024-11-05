create database BMS;
use BMS;

create table admins(
username varchar(10),
password varchar(10));
insert into admins (username,password) values(123,123);

CREATE TABLE Banquets (
    banquet_id INT AUTO_INCREMENT PRIMARY KEY,  -- Banquet Identification Number (BIN)
    banquet_name VARCHAR(255) NOT NULL,         -- Name of the banquet
    banquet_date DATETIME NOT NULL,              -- Date and time of the banquet
    address VARCHAR(255) NOT NULL,               -- Address of the banquet
    location VARCHAR(255) NOT NULL,              -- Location of the banquet
    contact_first_name VARCHAR(100) NOT NULL,   -- First name of the contact staff
    contact_last_name VARCHAR(100) NOT NULL,    -- Last name of the contact staff
    available CHAR(1) CHECK (available IN ('Y', 'N')), -- Availability (Y or N)
    quota INT NOT NULL                            -- Quota for the banquet
);

CREATE TABLE Meals (
    meal_id INT AUTO_INCREMENT PRIMARY KEY,      -- Unique identifier for each meal
    banquet_id INT NOT NULL,                     -- Foreign key referencing Banquets
    meal_type VARCHAR(50) NOT NULL,              -- Type of meal (e.g., fish, chicken, beef, vegetarian)
    dish_name VARCHAR(255) NOT NULL,             -- Name of the dish
    price DECIMAL(10, 2) NOT NULL,               -- Price of the dish
    special_cuisine VARCHAR(255),                -- Special cuisine information
    FOREIGN KEY (banquet_id) REFERENCES Banquets(banquet_id) ON DELETE CASCADE -- Foreign key constraint
);

CREATE TABLE Attendees (
    attendee_id INT AUTO_INCREMENT PRIMARY KEY,  -- Unique identifier for each attendee
    first_name VARCHAR(100) NOT NULL,            -- First name of the attendee
    last_name VARCHAR(100) NOT NULL,             -- Last name of the attendee
    address VARCHAR(255) NOT NULL,                -- Address of the attendee
    attendee_type ENUM('staff', 'student', 'alumni', 'guest') NOT NULL, -- Type of attendee
    email VARCHAR(255) NOT NULL UNIQUE,          -- Email address of the attendee (account ID)
    password VARCHAR(255) NOT NULL,               -- Password for account authentication
    mobile_number CHAR(8) NOT NULL,              -- Mobile number (8-digit)
    affiliated_organization VARCHAR(255),         -- Affiliated organization
    registration_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Date of registration
    UNIQUE (email)                                -- Ensure email is unique
);

INSERT INTO Banquets (banquet_name, banquet_date, address, location, contact_first_name, contact_last_name, available, quota) VALUES
('Annual Charity Gala', '2024-12-01 18:00:00', '123 Charity St, Cityville', 'Grand Hall', 'John', 'Doe', 'Y', 100),
('Corporate Retreat', '2024-11-15 09:00:00', '456 Business Rd, Cityville', 'Conference Center', 'Jane', 'Smith', 'Y', 50),
('Wedding Reception', '2024-10-20 17:00:00', '789 Wedding Ave, Cityville', 'Banquet Hall', 'Emily', 'Johnson', 'Y', 200),
('Holiday Party', '2024-12-20 19:00:00', '321 Festive Blvd, Cityville', 'Rooftop Lounge', 'Michael', 'Brown', 'Y', 150);

INSERT INTO Meals (banquet_id, meal_type, dish_name, price, special_cuisine) VALUES
(1, 'Fish', 'Grilled Salmon', 25.00, 'Mediterranean'),
(1, 'Chicken', 'Herb Roasted Chicken', 20.00, 'Italian'),
(1, 'Beef', 'Beef Wellington', 30.00, 'British'),
(1, 'Vegetarian', 'Stuffed Bell Peppers', 18.00, 'Mexican'),

(2, 'Fish', 'Baked Cod', 22.00, 'French'),
(2, 'Chicken', 'Chicken Alfredo', 19.00, 'Italian'),
(2, 'Beef', 'Steak Frites', 28.00, 'French'),
(2, 'Vegetarian', 'Pasta Primavera', 17.00, 'Italian'),

(3, 'Fish', 'Seared Tuna', 27.00, 'Japanese'),
(3, 'Chicken', 'Chicken Marsala', 21.00, 'Italian'),
(3, 'Beef', 'Braised Short Ribs', 32.00, 'American'),
(3, 'Vegetarian', 'Eggplant Parmesan', 16.00, 'Italian'),

(4, 'Fish', 'Fish Tacos', 15.00, 'Mexican'),
(4, 'Chicken', 'BBQ Chicken Wings', 12.00, 'American'),
(4, 'Beef', 'Beef Tacos', 15.00, 'Mexican'),
(4, 'Vegetarian', 'Vegetable Stir Fry', 14.00, 'Asian');

INSERT INTO Attendees (first_name, last_name, address, attendee_type, email, password, mobile_number, affiliated_organization) VALUES
('Alice', 'Williams', '101 Main St, Cityville', 'student', 'alice.williams@example.com', 'password123', '12345678', 'PolyU'),
('Bob', 'Smith', '202 Second St, Cityville', 'staff', 'bob.smith@example.com', 'password123', '87654321', 'SPEED'),
('Charlie', 'Johnson', '303 Third St, Cityville', 'alumni', 'charlie.johnson@example.com', 'password123', '23456789', 'HKCC'),
('Diana', 'Brown', '404 Fourth St, Cityville', 'guest', 'diana.brown@example.com', 'password123', '34567890', 'Others'),
('Ethan', 'Davis', '505 Fifth St, Cityville', 'student', 'ethan.davis@example.com', 'password123', '45678901', 'PolyU');

