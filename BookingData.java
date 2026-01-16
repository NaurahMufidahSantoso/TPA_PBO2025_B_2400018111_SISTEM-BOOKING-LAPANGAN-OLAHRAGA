import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingData {
    // Static list untuk menyimpan semua booking (shared across apps)
    private static List<BookingData> allBookings = new ArrayList<>();

    // Data booking
    private String username;
    private String venueName;
    private String city;
    private String sport;
    private String price;
    private String imagePath;
    private LocalDate bookingDate;
    private String bookingTime;
    private LocalDate createdDate; // Kapan booking ini dibuat

    // Constructor
    public BookingData(String username, String venueName, String city, String sport,
                       String price, String imagePath, LocalDate bookingDate, String bookingTime) {
        this.username = username;
        this.venueName = venueName;
        this.city = city;
        this.sport = sport;
        this.price = price;
        this.imagePath = imagePath;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.createdDate = LocalDate.now();
    }

    // Method untuk menambah booking baru
    public static void addBooking(BookingData booking) {
        allBookings.add(booking);
    }

    // Method untuk mendapatkan semua booking
    public static List<BookingData> getAllBookings() {
        return new ArrayList<>(allBookings);
    }

    // Method untuk mendapatkan booking dalam 6 bulan terakhir (sudah selesai)
    public static List<BookingData> getCompletedBookings() {
        List<BookingData> completed = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate sixMonthsAgo = today.minusMonths(6);

        for (BookingData booking : allBookings) {
            // Booking yang tanggalnya sudah lewat dalam 6 bulan terakhir
            if (booking.bookingDate.isBefore(today) &&
                    booking.bookingDate.isAfter(sixMonthsAgo)) {
                completed.add(booking);
            }
        }
        return completed;
    }

    // Method untuk mendapatkan booking hari ini (mendatang)
    public static List<BookingData> getTodayBookings() {
        List<BookingData> today = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (BookingData booking : allBookings) {
            if (booking.bookingDate.equals(currentDate)) {
                today.add(booking);
            }
        }
        return today;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getCity() {
        return city;
    }

    public String getSport() {
        return sport;
    }

    public String getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    // Method untuk mendapatkan status booking
    public String getStatus() {
        LocalDate today = LocalDate.now();
        if (bookingDate.equals(today)) {
            return "mendatang";
        } else if (bookingDate.isBefore(today)) {
            return "selesai";
        } else {
            return "mendatang";
        }
    }
}