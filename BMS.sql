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

