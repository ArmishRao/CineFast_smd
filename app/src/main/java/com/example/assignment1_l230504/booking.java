package com.example.assignment1_l230504;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class booking implements Serializable {
    private String bookingId;
    private String userId;
    private String movieName;
    private ArrayList<Integer> seats;
    private double totalPrice;
    private String date;
    private String time;
    private String theater;
    private String hall;
    private String screenType;
    private ArrayList<SnackItem> snacks;
    private long timestamp;

    public booking() {
    }

    public booking(String bookingId, String userId, String movieName, ArrayList<Integer> seats,
                   double totalPrice, String date, String time, String theater,
                   String hall, String screenType, ArrayList<SnackItem> snacks) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.movieName = movieName;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.date = date;
        this.time = time;
        this.theater = theater;
        this.hall = hall;
        this.screenType = screenType;
        this.snacks = snacks;
        this.timestamp = System.currentTimeMillis();
    }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }

    public ArrayList<Integer> getSeats() { return seats; }
    public void setSeats(ArrayList<Integer> seats) { this.seats = seats; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getTheater() { return theater; }
    public void setTheater(String theater) { this.theater = theater; }

    public String getHall() { return hall; }
    public void setHall(String hall) { this.hall = hall; }

    public String getScreenType() { return screenType; }
    public void setScreenType(String screenType) { this.screenType = screenType; }

    public ArrayList<SnackItem> getSnacks() { return snacks; }
    public void setSnacks(ArrayList<SnackItem> snacks) { this.snacks = snacks; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("bookingId", bookingId);
        map.put("userId", userId);
        map.put("movieName", movieName);
        map.put("seats", seats);
        map.put("totalPrice", totalPrice);
        map.put("date", date);
        map.put("time", time);
        map.put("theater", theater);
        map.put("hall", hall);
        map.put("screenType", screenType);
        map.put("timestamp", timestamp);
        if (snacks != null && !snacks.isEmpty()) {
            List<Map<String, Object>> snacksList = new ArrayList<>();
            for (SnackItem snack : snacks) {
                Map<String, Object> snackMap = new HashMap<>();
                snackMap.put("name", snack.getName());
                snackMap.put("description", snack.getDescription());
                snackMap.put("price", snack.getPrice());
                snackMap.put("quantity", snack.getQuantity());
                snackMap.put("totalPrice", snack.getTotalPrice());
                snacksList.add(snackMap);
            }
            map.put("snacks", snacksList);
        }

        return map;
    }
}