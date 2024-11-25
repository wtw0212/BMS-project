package Registration;

public class Registration {
    private int registrationID;
    private String email;
    private int BIN;
    private int mealChoice;
    private String remarks;
    private String registrationTime;
    private String seatNumber;

    public Registration(int registrationID, String email, int BIN, int mealChoice, String remarks, String registrationTime) {
        this.registrationID = registrationID;
        this.email = email;
        this.BIN = BIN;
        this.mealChoice = mealChoice;
        this.remarks = remarks;
        this.registrationTime = registrationTime;
    }

    // Getters
    public int getRegistrationID() { return registrationID; }
    public String getEmail() { return email; }
    public int getBIN() { return BIN; }
    public int getMealChoice() { return mealChoice; }
    public String getRemarks() { return remarks; }
    public String getRegistrationTime() { return registrationTime; }
    public String getSeatNumber() { return seatNumber; }

    // Setters
    public void setRegistrationID(int registrationID) { this.registrationID = registrationID; }
    public void setEmail(String email) { this.email = email; }
    public void setBIN(int BIN) { this.BIN = BIN; }
    public void setMealChoice(int mealChoice) { this.mealChoice = mealChoice; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public void setRegistrationTime(String registrationTime) { this.registrationTime = registrationTime; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    @Override
    public String toString() {
        return "Registration{" +
                "registrationID=" + registrationID +
                ", email='" + email + '\'' +
                ", BIN=" + BIN +
                ", mealChoice=" + mealChoice +
                ", remarks='" + remarks + '\'' +
                ", registrationTime='" + registrationTime + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                '}';
    }
}