package Registration;

public class SeatReservation {
    private int BIN;
    private String seatNumber;
    private String email;

    public SeatReservation(int BIN, String seatNumber, String email) {
        this.BIN = BIN;
        this.seatNumber = seatNumber;
        this.email = email;
    }

    // Getters
    public int getBIN() { return BIN; }
    public String getSeatNumber() { return seatNumber; }
    public String getEmail() { return email; }

    // Setters
    public void setBIN(int BIN) { this.BIN = BIN; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "SeatReservation{" +
                "BIN=" + BIN +
                ", seatNumber='" + seatNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}