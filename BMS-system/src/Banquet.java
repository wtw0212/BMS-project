public class Banquet {
    private int BIN;
    private String banquetName;
    private String dateTime;
    private String address;
    private String location;
    private String contactFirstName;
    private String contactLastName;
    private String available;
    private int quota;

    public Banquet(int BIN, String banquetName, String dateTime, String address, String location,
                   String contactFirstName, String contactLastName, String available, int quota) {
        this.BIN = BIN;
        this.banquetName = banquetName;
        this.dateTime = dateTime;
        this.address = address;
        this.location = location;
        this.contactFirstName = contactFirstName;
        this.contactLastName = contactLastName;
        this.available = available;
        this.quota = quota;
    }

    // Getters
    public int getBIN() { return BIN; }
    public String getBanquetName() { return banquetName; }
    public String getDateTime() { return dateTime; }
    public String getAddress() { return address; }
    public String getLocation() { return location; }
    public String getContactFirstName() { return contactFirstName; }
    public String getContactLastName() { return contactLastName; }
    public String getAvailable() { return available; }
    public int getQuota() { return quota; }

    // Setters
    public void setBIN(int BIN) { this.BIN = BIN; }
    public void setBanquetName(String banquetName) { this.banquetName = banquetName; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public void setAddress(String address) { this.address = address; }
    public void setLocation(String location) { this.location = location; }
    public void setContactFirstName(String contactFirstName) { this.contactFirstName = contactFirstName; }
    public void setContactLastName(String contactLastName) { this.contactLastName = contactLastName; }
    public void setAvailable(String available) { this.available = available; }
    public void setQuota(int quota) { this.quota = quota; }

    @Override
    public String toString() {
        return "Banquet{" +
                "BIN=" + BIN +
                ", banquetName='" + banquetName + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", address='" + address + '\'' +
                ", location='" + location + '\'' +
                ", contactFirstName='" + contactFirstName + '\'' +
                ", contactLastName='" + contactLastName + '\'' +
                ", available='" + available + '\'' +
                ", quota=" + quota +
                '}';
    }
}