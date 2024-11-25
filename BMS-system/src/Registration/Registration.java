package Registration;

public class Registration {
    private int registrationID;
    private String email;
    private int BIN;
    private int mainCourseChoice;
    private int appetizerChoice;
    private int dessertChoice;
    private String remarks;
    private String registrationTime;
    private String mainCourseName;
    private String appetizerName;
    private String dessertName;
    private String banquetName;
    private String dateTime;
    private String address;
    private String location;

    public Registration(int registrationID, String email, int BIN, int mainCourseChoice, int appetizerChoice, int dessertChoice, String remarks, String registrationTime) {
        this.registrationID = registrationID;
        this.email = email;
        this.BIN = BIN;
        this.mainCourseChoice = mainCourseChoice;
        this.appetizerChoice = appetizerChoice;
        this.dessertChoice = dessertChoice;
        this.remarks = remarks;
        this.registrationTime = registrationTime;
    }

    // Getters and setters
    public int getRegistrationID() { return registrationID; }
    public void setRegistrationID(int registrationID) { this.registrationID = registrationID; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getBIN() { return BIN; }
    public void setBIN(int BIN) { this.BIN = BIN; }

    public int getMainCourseChoice() { return mainCourseChoice; }
    public void setMainCourseChoice(int mainCourseChoice) { this.mainCourseChoice = mainCourseChoice; }

    public int getAppetizerChoice() { return appetizerChoice; }
    public void setAppetizerChoice(int appetizerChoice) { this.appetizerChoice = appetizerChoice; }

    public int getDessertChoice() { return dessertChoice; }
    public void setDessertChoice(int dessertChoice) { this.dessertChoice = dessertChoice; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getRegistrationTime() { return registrationTime; }
    public void setRegistrationTime(String registrationTime) { this.registrationTime = registrationTime; }

    public String getMainCourseName() { return mainCourseName; }
    public void setMainCourseName(String mainCourseName) { this.mainCourseName = mainCourseName; }

    public String getAppetizerName() { return appetizerName; }
    public void setAppetizerName(String appetizerName) { this.appetizerName = appetizerName; }

    public String getDessertName() { return dessertName; }
    public void setDessertName(String dessertName) { this.dessertName = dessertName; }

    public String getBanquetName() { return banquetName; }
    public void setBanquetName(String banquetName) { this.banquetName = banquetName; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }



@Override
    public String toString() {
        return "Registration{" +
                "registrationID=" + registrationID +
                ", email='" + email + '\'' +
                ", BIN=" + BIN +
                ", mealChoice=" + mainCourseChoice + appetizerChoice + dessertChoice +
                ", remarks='" + remarks + '\'' +
                ", registrationTime='" + registrationTime + '\'' +
                '}';
    }
}