import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingApp {

    private VBox mainContent;
    private List<Venue> venues;
    private GridPane venuesGrid;
    private String currentUsername = "naurahmufidah";

    public Scene createScene(Stage stage) {
        initializeVenues();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        VBox header = createHeader();
        root.setTop(header);

        mainContent = new VBox();
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setVvalue(0);

        HBox footer = createFooter();
        root.setBottom(footer);

        showSewaLapanganPage();

        return new Scene(root, 1000, 800);
    }

    private VBox createHeader() {
        VBox header = new VBox(0);

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 40, 10, 40));
        topBar.setStyle("-fx-background-color: #1a237e;");

        StackPane logoContainer = new StackPane();
        Circle logoCircle = new Circle(25);
        logoCircle.setFill(Color.web("#f4e4a3"));

        ImageView logoImg = new ImageView();
        try {
            logoImg.setImage(new Image(getClass().getResourceAsStream("/resources/logo.png")));
            logoImg.setFitWidth(40);
            logoImg.setFitHeight(40);
            logoImg.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Logo not found");
        }

        logoContainer.getChildren().addAll(logoCircle, logoImg);

        Label title = new Label("GoSport Book");
        title.setFont(Font.font("Elephant", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#ff8c5a"));
        title.setStyle("-fx-font-style: italic;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label loginBtn = new Label("Login");
        loginBtn.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        loginBtn.setTextFill(Color.WHITE);
        loginBtn.setStyle("-fx-cursor: hand;");

        topBar.getChildren().addAll(logoContainer, title, spacer, loginBtn);

        HBox navbar = new HBox(60);
        navbar.setAlignment(Pos.CENTER);
        navbar.setPadding(new Insets(15, 40, 15, 40));
        navbar.setStyle("-fx-background-color: #f4e4a3;");

        Label blogBtn = createNavButton("Blog", false);
        Label sewaBtn = createNavButton("Sewa Lapangan", true);
        Label riwayatBtn = createNavButton("Riwayat", false);
        Label profileBtn = createNavButton("Profile", false);

        navbar.getChildren().addAll(blogBtn, sewaBtn, riwayatBtn, profileBtn);
        header.getChildren().addAll(topBar, navbar);
        return header;
    }

    private Label createNavButton(String text, boolean active) {
        Label label = new Label(text);
        label.setFont(Font.font("Lucida Bright", FontWeight.BOLD, 18));
        label.setTextFill(Color.web("#1a237e"));
        if (active) {
            label.setStyle("-fx-cursor: hand; -fx-underline: true;");
        } else {
            label.setStyle("-fx-cursor: hand;");
        }
        return label;
    }

    private void showSewaLapanganPage() {
        mainContent.getChildren().clear();
        mainContent.setStyle("-fx-background-color: #f5f5f5;");
        mainContent.setSpacing(0);
        mainContent.setPadding(Insets.EMPTY);
        mainContent.setAlignment(Pos.TOP_CENTER);

        VBox orangeHeader = new VBox(25);
        orangeHeader.setAlignment(Pos.CENTER);
        orangeHeader.setPadding(new Insets(20, 0, 20, 0));
        orangeHeader.setStyle("-fx-background-color: #ff8c5a; -fx-background-radius: 0;");
        orangeHeader.prefWidthProperty().bind(mainContent.widthProperty());

        Label pageTitle = new Label("Ayo Booking Lapangan Favoritemu!!");
        pageTitle.setFont(Font.font("Impact", FontWeight.BOLD, 50));
        pageTitle.setTextFill(Color.WHITE);
        pageTitle.setStyle("-fx-font-style: italic;");

        orangeHeader.getChildren().add(pageTitle);

        mainContent.getChildren().add(orangeHeader);

        // TODO
    }


    private void updateVenuesGrid(List<Venue> venuesToShow) {
        venuesGrid.getChildren().clear();
        int col = 0;
        int row = 0;
        for (Venue venue : venuesToShow) {
            VBox card = createVenueCard(venue);
            venuesGrid.add(card, col, row);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }
    }

    private void filterVenues(String city, String sport) {
        List<Venue> filtered = new ArrayList<>();
        String cityLower = city.toLowerCase().trim();

        for (Venue venue : venues) {
            boolean cityMatch = cityLower.isEmpty() || venue.city.toLowerCase().contains(cityLower);
            boolean sportMatch = sport == null || sport.equals("Pilih cabang olahraga") ||
                    sport.equals("Semua Cabang") || venue.sport.equals(sport);

            if (cityMatch && sportMatch) {
                filtered.add(venue);
            }
        }
        updateVenuesGrid(filtered);
    }

    private VBox createVenueCard(Venue venue) {
        VBox card = new VBox(0);
        card.setPrefWidth(420);
        card.setMaxWidth(420);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3); " +
                "-fx-cursor: hand;");

        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(280);
        imageContainer.setMaxHeight(280);
        imageContainer.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 15 15 0 0;");

        try {
            ImageView venueImg = new ImageView(new Image(getClass().getResourceAsStream(venue.imagePath)));
            venueImg.setFitWidth(420);
            venueImg.setFitHeight(280);
            venueImg.setPreserveRatio(false);
            imageContainer.getChildren().add(venueImg);
        } catch (Exception e) {
            Label placeholder = new Label("🏟️");
            placeholder.setFont(Font.font(64));
            imageContainer.getChildren().add(placeholder);
        }

        VBox content = new VBox(8);
        content.setPadding(new Insets(20, 20, 20, 20));

        Label venueLabel = new Label("Venue");
        venueLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        venueLabel.setTextFill(Color.web("#999999"));

        Label venueName = new Label(venue.name);
        venueName.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        venueName.setTextFill(Color.web("#1a1a1a"));
        venueName.setWrapText(true);

        HBox ratingBox = new HBox(8);
        ratingBox.setAlignment(Pos.CENTER_LEFT);

        Label star = new Label("⭐");
        star.setFont(Font.font(14));

        Label rating = new Label(venue.rating + " • " + venue.city);
        rating.setFont(Font.font("Arial", 13));
        rating.setTextFill(Color.web("#666666"));

        ratingBox.getChildren().addAll(star, rating);

        HBox sportBox = new HBox(8);
        sportBox.setAlignment(Pos.CENTER_LEFT);
        sportBox.setPadding(new Insets(5, 0, 0, 0));

        Label sportIcon = new Label(getSportIcon(venue.sport));
        sportIcon.setFont(Font.font(16));

        Label sportLabel = new Label(venue.sport);
        sportLabel.setFont(Font.font("Arial", 13));
        sportLabel.setTextFill(Color.web("#666666"));

        sportBox.getChildren().addAll(sportIcon, sportLabel);

        Label price = new Label("Mulai Rp" + venue.price + " /sesi");
        price.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        price.setTextFill(Color.web("#1a237e"));
        price.setPadding(new Insets(8, 0, 0, 0));

        content.getChildren().addAll(venueLabel, venueName, ratingBox, sportBox, price);
        card.getChildren().addAll(imageContainer, content);

        card.setOnMouseEntered(e ->
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 5); " +
                        "-fx-cursor: hand;"));
        card.setOnMouseExited(e ->
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3); " +
                        "-fx-cursor: hand;"));

        card.setOnMouseClicked(e -> showBookingPage(venue));

        return card;
    }

    private String getSportIcon(String sport) {
        switch (sport) {
            case "Badminton": return "🏸";
            case "Tennis": return "🎾";
            case "Basketball": return "🏀";
            case "Mini Soccer": return "⚽";
            case "Padel": return "🎾";
            default: return "🏟️";
        }
    }

    private void showBookingPage(Venue venue) {
        mainContent.getChildren().clear();
        mainContent.setStyle("-fx-background-color: #f5f5f5;");
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(50, 100, 50, 100));

        VBox bookingContainer = new VBox(30);
        bookingContainer.setMaxWidth(700);
        bookingContainer.setAlignment(Pos.TOP_CENTER);
        bookingContainer.setPadding(new Insets(40));
        bookingContainer.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        Label bookingTitle = new Label("Booking Lapangan");
        bookingTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        bookingTitle.setTextFill(Color.web("#1a237e"));

        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER_LEFT);

        VBox userBox = createFormField("Nama User", currentUsername, true);
        VBox venueBox = createFormField("Nama Lapangan", venue.name, true);

        VBox dateBox = new VBox(8);
        Label dateLabel = new Label("Pilih Tanggal");
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        dateLabel.setTextFill(Color.web("#333333"));

        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.setStyle("-fx-font-size: 14px;");
        datePicker.setPrefWidth(620);
        datePicker.setPrefHeight(45);

        dateBox.getChildren().addAll(dateLabel, datePicker);

        VBox timeBox = new VBox(8);
        Label timeLabel = new Label("Pilih Jam");
        timeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        timeLabel.setTextFill(Color.web("#333333"));

        ComboBox<String> timePicker = new ComboBox<>();
        timePicker.getItems().addAll(
                "08:00 - 10:00", "10:00 - 12:00", "12:00 - 14:00",
                "14:00 - 16:00", "16:00 - 18:00", "18:00 - 20:00",
                "20:00 - 22:00"
        );
        timePicker.setPromptText("Pilih jam booking");
        timePicker.setStyle("-fx-font-size: 14px;");
        timePicker.setPrefWidth(620);
        timePicker.setPrefHeight(45);

        timeBox.getChildren().addAll(timeLabel, timePicker);

        Label totalPrice = new Label("Total: Rp" + venue.price);
        totalPrice.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        totalPrice.setTextFill(Color.web("#ff8c5a"));
        totalPrice.setPadding(new Insets(10, 0, 0, 0));

        formBox.getChildren().addAll(userBox, venueBox, dateBox, timeBox, totalPrice);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button backBtn = new Button("Kembali");
        backBtn.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        backBtn.setTextFill(Color.web("#666666"));
        backBtn.setStyle("-fx-background-color: #e8e8e8; -fx-background-radius: 25; " +
                "-fx-padding: 12 30 12 30; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: #d0d0d0; " +
                "-fx-background-radius: 25; -fx-padding: 12 30 12 30; -fx-cursor: hand;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: #e8e8e8; " +
                "-fx-background-radius: 25; -fx-padding: 12 30 12 30; -fx-cursor: hand;"));
        backBtn.setOnAction(e -> showSewaLapanganPage());

        Button payBtn = new Button("Bayar Sekarang");
        payBtn.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        payBtn.setTextFill(Color.WHITE);
        payBtn.setStyle("-fx-background-color: #1a237e; -fx-background-radius: 25; " +
                "-fx-padding: 12 30 12 30; -fx-cursor: hand;");
        payBtn.setOnMouseEntered(e -> payBtn.setStyle("-fx-background-color: #0d47a1; " +
                "-fx-background-radius: 25; -fx-padding: 12 30 12 30; -fx-cursor: hand;"));
        payBtn.setOnMouseExited(e -> payBtn.setStyle("-fx-background-color: #1a237e; " +
                "-fx-background-radius: 25; -fx-padding: 12 30 12 30; -fx-cursor: hand;"));

        payBtn.setOnAction(e -> {
            if (timePicker.getValue() == null) {
                showAlert("Peringatan", "Silakan pilih jam booking terlebih dahulu!");
            } else {
                showSuccessBooking(venue, datePicker.getValue().toString(), timePicker.getValue());
            }
        });

        buttonBox.getChildren().addAll(backBtn, payBtn);

        bookingContainer.getChildren().addAll(bookingTitle, formBox, buttonBox);
        mainContent.getChildren().add(bookingContainer);
    }

    private VBox createFormField(String label, String value, boolean disabled) {
        VBox box = new VBox(8);
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        labelText.setTextFill(Color.web("#333333"));

        TextField field = new TextField(value);
        field.setDisable(disabled);
        field.setStyle("-fx-font-size: 14px; -fx-opacity: 1;");
        field.setPrefHeight(45);

        box.getChildren().addAll(labelText, field);
        return box;
    }

    private void showSuccessBooking(Venue venue, String date, String time) {
        try {
            LocalDate bookingDate = LocalDate.parse(date);
            BookingData newBooking = new BookingData(
                    currentUsername,
                    venue.name,
                    venue.city,
                    venue.sport,
                    venue.price,
                    venue.imagePath,
                    bookingDate,
                    time
            );
            BookingData.addBooking(newBooking);
            System.out.println("✅ Booking berhasil disimpan!");
        } catch (Exception e) {
            System.out.println("❌ Error menyimpan booking: " + e.getMessage());
        }
        mainContent.getChildren().clear();
        mainContent.setStyle("-fx-background-color: #f5f5f5;");
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(100));

        VBox successBox = new VBox(25);
        successBox.setAlignment(Pos.CENTER);
        successBox.setMaxWidth(500);
        successBox.setPadding(new Insets(50));
        successBox.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        Label checkIcon = new Label("✅");
        checkIcon.setFont(Font.font(80));

        Label successTitle = new Label("Booking Berhasil!!");
        successTitle.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        successTitle.setTextFill(Color.web("#1a237e"));

        VBox detailsBox = new VBox(10);
        detailsBox.setAlignment(Pos.CENTER);

        Label venueDetail = new Label(venue.name);
        venueDetail.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        venueDetail.setTextFill(Color.web("#333333"));

        Label dateDetail = new Label("Tanggal: " + date);
        dateDetail.setFont(Font.font("Arial", 16));
        dateDetail.setTextFill(Color.web("#666666"));

        Label timeDetail = new Label("Jam: " + time);
        timeDetail.setFont(Font.font("Arial", 16));
        timeDetail.setTextFill(Color.web("#666666"));

        detailsBox.getChildren().addAll(venueDetail, dateDetail, timeDetail);

        Button doneBtn = new Button("Kembali ke Halaman Utama");
        doneBtn.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        doneBtn.setTextFill(Color.WHITE);
        doneBtn.setStyle("-fx-background-color: #ff8c5a; -fx-background-radius: 25; " +
                "-fx-padding: 12 30 12 30; -fx-cursor: hand;");
        doneBtn.setOnMouseEntered(e -> doneBtn.setStyle("-fx-background-color: #ff7043; " +
                "-fx-background-radius: 25; -fx-padding: 12 30 12 30; -fx-cursor: hand;"));
        doneBtn.setOnMouseExited(e -> doneBtn.setStyle("-fx-background-color: #ff8c5a; " +
                "-fx-background-radius: 25; -fx-padding: 12 30 12 30; -fx-cursor: hand;"));
        doneBtn.setOnAction(e -> showSewaLapanganPage());

        successBox.getChildren().addAll(checkIcon, successTitle, detailsBox, doneBtn);
        mainContent.getChildren().add(successBox);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private HBox createFooter() {
        HBox footer = new HBox(50);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(25, 50, 25, 50));
        footer.setStyle("-fx-background-color: #1a237e;");

        Label copyright = new Label("© GoSport Booking");
        copyright.setFont(Font.font("Arial", 15));
        copyright.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox contactBox = new HBox(10);
        contactBox.setAlignment(Pos.CENTER_LEFT);

        Label emailIcon = new Label("✉");
        emailIcon.setFont(Font.font(18));
        emailIcon.setTextFill(Color.WHITE);

        Label email = new Label("gosportbook@gmail.com");
        email.setFont(Font.font("Arial", 15));
        email.setTextFill(Color.WHITE);

        contactBox.getChildren().addAll(emailIcon, email);

        HBox socialsBox = new HBox(15);
        socialsBox.setAlignment(Pos.CENTER_LEFT);

        ImageView instagram = new ImageView();
        ImageView tiktok = new ImageView();
        ImageView facebook = new ImageView();

        try {
            instagram.setImage(new Image(getClass().getResourceAsStream("/resources/instagram.png")));
            tiktok.setImage(new Image(getClass().getResourceAsStream("/resources/tiktok.png")));
            facebook.setImage(new Image(getClass().getResourceAsStream("/resources/facebook.png")));
        } catch (Exception e) {
            System.out.println("Social media icons not found");
        }

        instagram.setFitWidth(26);
        instagram.setFitHeight(26);
        tiktok.setFitWidth(26);
        tiktok.setFitHeight(26);
        facebook.setFitWidth(26);
        facebook.setFitHeight(26);

        instagram.setPreserveRatio(true);
        tiktok.setPreserveRatio(true);
        facebook.setPreserveRatio(true);

        instagram.setStyle("-fx-cursor: hand;");
        tiktok.setStyle("-fx-cursor: hand;");
        facebook.setStyle("-fx-cursor: hand;");

        socialsBox.getChildren().addAll(instagram, tiktok, facebook);

        footer.getChildren().addAll(copyright, spacer, contactBox, socialsBox);
        return footer;
    }

    private void initializeVenues() {
        venues = new ArrayList<>();

        venues.add(new Venue("Candu Padel", "4.94", "Kota Pekalongan", "Padel", "70.000", "/resources/lapangan1.png"));
        venues.add(new Venue("Ambassador Tennis", "4.93", "Kota Malang", "Tennis", "150.000", "/resources/lapangan2.png"));
        venues.add(new Venue("VIE Arena, Badminton Hall", "4.96", "Kabupaten Tangerang", "Badminton", "50.000", "/resources/lapangan3.png"));
        venues.add(new Venue("Eternal Arena Rooftop Grand City", "4.98", "Kota Surabaya", "Mini Soccer", "450.000", "/resources/lapangan4.png"));
        venues.add(new Venue("The Bucketlist Bogor", "4.96", "Kota Bogor", "Basketball", "75.000", "/resources/lapangan5.png"));
        venues.add(new Venue("The Racquet Club (TRC)", "4.90", "Kota Jakarta Selatan", "Tennis", "275.000", "/resources/lapangan6.png"));
        venues.add(new Venue("Gaskan Pendekar Arena", "4.71", "Kota Jakarta Utara", "Mini Soccer", "300.000", "/resources/lapangan7.png"));
        venues.add(new Venue("GOR Platinum Dieng", "4.90", "Kota Malang", "Badminton", "30.000", "/resources/lapangan8.png"));
        venues.add(new Venue("Kma - Kiss My Air", "5.00", "Kota Bandung", "Basketball", "100.000", "/resources/lapangan9.png"));
        venues.add(new Venue("Osseh Padel", "4.81", "Kota Surabaya", "Padel", "400.000", "/resources/lapangan10.png"));
    }

    class Venue {
        String name, rating, city, sport, price, imagePath;

        Venue(String name, String rating, String city, String sport, String price, String imagePath) {
            this.name = name;
            this.rating = rating;
            this.city = city;
            this.sport = sport;
            this.price = price;
            this.imagePath = imagePath;
        }
    }
}