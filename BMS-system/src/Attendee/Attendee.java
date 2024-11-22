package Attendee;

public class Attendee {
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String attendeeType;
    private String password;
    private String mobileNumber;
    private String affiliatedOrganization;

    public Attendee(String email, String firstName, String lastName, String address, String attendeeType,
                    String password, String mobileNumber, String affiliatedOrganization) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.attendeeType = attendeeType;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.affiliatedOrganization = affiliatedOrganization;
    }

    // Getters
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getAttendeeType() { return attendeeType; }
    public String getPassword() { return password; }
    public String getMobileNumber() { return mobileNumber; }
    public String getAffiliatedOrganization() { return affiliatedOrganization; }

    // Setters
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAddress(String address) { this.address = address; }
    public void setAttendeeType(String attendeeType) { this.attendeeType = attendeeType; }
    public void setPassword(String password) { this.password = password; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public void setAffiliatedOrganization(String affiliatedOrganization) { this.affiliatedOrganization = affiliatedOrganization; }

    @Override
    public String toString() {
        return "Attendee.Attendee{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", attendeeType='" + attendeeType + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", affiliatedOrganization='" + affiliatedOrganization + '\'' +
                '}';
    }
}