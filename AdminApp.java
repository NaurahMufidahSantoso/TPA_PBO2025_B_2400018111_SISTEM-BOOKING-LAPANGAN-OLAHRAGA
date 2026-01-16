import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminApp {

    private VBox mainContent;

    public Scene createScene(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Header
        VBox header = createHeader();
        root.setTop(header);

        // Main content dengan ScrollPane
        mainContent = new VBox(30);
        mainContent.setPadding(new Insets(40, 100, 40, 100));
        mainContent.setStyle("-fx-background-color: #f5f5f5;");

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setVvalue(0);
        scrollPane.setStyle("-fx-background: #f5f5f5; -fx-background-color: #f5f5f5;");

        root.setCenter(scrollPane);

        // Footer
        HBox footer = createFooter();
        root.setBottom(footer);

        // Show dashboard
        showDashboard();

        return new Scene(root, 1000, 800);
    }

    private VBox createHeader() {
        VBox header = new VBox(0);

        // Top bar
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

        Label title = new Label("GoSport Book - ADMIN");
        title.setFont(Font.font("Elephant", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#ff8c5a"));
        title.setStyle("-fx-font-style: italic;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label adminLabel = new Label("Admin");
        adminLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        adminLabel.setTextFill(Color.WHITE);
        adminLabel.setPadding(new Insets(8, 15, 8, 15));
        adminLabel.setStyle("-fx-background-color: #ff8c5a; -fx-background-radius: 15;");

        topBar.getChildren().addAll(logoContainer, title, spacer, adminLabel);

        header.getChildren().add(topBar);
        return header;
    }

    private void showDashboard() {
        mainContent.getChildren().clear();

        // Page title
        Label pageTitle = new Label("Dashboard Admin");
        pageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        pageTitle.setTextFill(Color.web("#1a237e"));

        // Statistics cards
        HBox statsBox = new HBox(25);
        statsBox.setAlignment(Pos.CENTER);

        // Total Booking
        List<BookingData> allBookings = BookingData.getAllBookings();
        VBox totalBookingCard = createStatCard("📊", "Total Booking", String.valueOf(allBookings.size()), "#4fc3f7");

        // Total Revenue
        int totalRevenue = 0;
        for (BookingData booking : allBookings) {
            try {
                String priceStr = booking.getPrice().replace(".", "");
                totalRevenue += Integer.parseInt(priceStr);
            } catch (Exception e) {
                // Skip jika error parsing
            }
        }
        String revenueStr = String.format("Rp %,d", totalRevenue).replace(",", ".");
        VBox totalRevenueCard = createStatCard("💰", "Total Revenue", revenueStr, "#66bb6a");

        // Today's Booking
        List<BookingData> todayBookings = BookingData.getTodayBookings();
        VBox todayBookingCard = createStatCard("📅", "Booking Hari Ini", String.valueOf(todayBookings.size()), "#ffa726");

        statsBox.getChildren().addAll(totalBookingCard, totalRevenueCard, todayBookingCard);

        // Menu buttons
        VBox menuContainer = new VBox(20);
        menuContainer.setAlignment(Pos.CENTER);
        menuContainer.setMaxWidth(700);

        HBox viewBookingsBtn = createMenuButton("📋", "Lihat Semua Booking", "#1a237e");
        HBox viewVenuesBtn = createMenuButton("🏟️", "Lihat Semua Venue", "#1a237e");

        viewBookingsBtn.setOnMouseClicked(e -> showAllBookings());
        viewVenuesBtn.setOnMouseClicked(e -> showAllVenues());

        menuContainer.getChildren().addAll(viewBookingsBtn, viewVenuesBtn);

        // Logout button
        Button logoutBtn = new Button("Logout");
        logoutBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        logoutBtn.setTextFill(Color.WHITE);
        logoutBtn.setPrefWidth(200);
        logoutBtn.setPrefHeight(50);
        logoutBtn.setStyle("-fx-background-color: #e53935; -fx-background-radius: 25; -fx-cursor: hand;");
        logoutBtn.setOnMouseEntered(e ->
                logoutBtn.setStyle("-fx-background-color: #c62828; -fx-background-radius: 25; -fx-cursor: hand;"));
        logoutBtn.setOnMouseExited(e ->
                logoutBtn.setStyle("-fx-background-color: #e53935; -fx-background-radius: 25; -fx-cursor: hand;"));
        logoutBtn.setOnAction(e -> handleLogout());

        VBox logoutContainer = new VBox(30);
        logoutContainer.setAlignment(Pos.CENTER);
        logoutContainer.getChildren().add(logoutBtn);

        mainContent.getChildren().addAll(pageTitle, statsBox, menuContainer, logoutContainer);
    }

    private VBox createStatCard(String icon, String label, String value, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setPrefWidth(250);
        card.setPrefHeight(150);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(48));

        Label titleLabel = new Label(label);
        titleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        titleLabel.setTextFill(Color.web("#666666"));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.web(color));

        card.getChildren().addAll(iconLabel, titleLabel, valueLabel);
        return card;
    }

    private HBox createMenuButton(String icon, String text, String color) {
        HBox button = new HBox(20);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(20, 30, 20, 30));
        button.setPrefWidth(700);
        button.setPrefHeight(80);
        button.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3); " +
                "-fx-cursor: hand;");

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(32));

        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        textLabel.setTextFill(Color.web(color));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label arrow = new Label("›");
        arrow.setFont(Font.font(40));
        arrow.setTextFill(Color.web(color));

        button.getChildren().addAll(iconLabel, textLabel, spacer, arrow);

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 4); " +
                        "-fx-cursor: hand;"));
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3); " +
                        "-fx-cursor: hand;"));

        return button;
    }

    private void showAllBookings() {
        mainContent.getChildren().clear();

        // Header with back button
        HBox headerBox = new HBox(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = new Button("← Kembali");
        backBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backBtn.setTextFill(Color.WHITE);
        backBtn.setStyle("-fx-background-color: #1a237e; -fx-background-radius: 20; " +
                "-fx-padding: 10 20 10 20; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e ->
                backBtn.setStyle("-fx-background-color: #0d47a1; -fx-background-radius: 20; " +
                        "-fx-padding: 10 20 10 20; -fx-cursor: hand;"));
        backBtn.setOnMouseExited(e ->
                backBtn.setStyle("-fx-background-color: #1a237e; -fx-background-radius: 20; " +
                        "-fx-padding: 10 20 10 20; -fx-cursor: hand;"));
        backBtn.setOnAction(e -> showDashboard());

        Label pageTitle = new Label("Semua Booking");
        pageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        pageTitle.setTextFill(Color.web("#1a237e"));

        headerBox.getChildren().addAll(backBtn, pageTitle);

        VBox bookingsContainer = new VBox(20);
        List<BookingData> allBookings = BookingData.getAllBookings();

        if (allBookings.isEmpty()) {
            VBox emptyState = new VBox(20);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(80));

            Label emptyIcon = new Label("📋");
            emptyIcon.setFont(Font.font(64));

            Label emptyText = new Label("Belum ada booking");
            emptyText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            emptyText.setTextFill(Color.web("#666666"));

            emptyState.getChildren().addAll(emptyIcon, emptyText);
            bookingsContainer.getChildren().add(emptyState);
        } else {
            for (BookingData booking : allBookings) {
                HBox card = createBookingCard(booking);
                bookingsContainer.getChildren().add(card);
            }
        }

        mainContent.getChildren().addAll(headerBox, bookingsContainer);
    }

    private HBox createBookingCard(BookingData booking) {
        HBox card = new HBox(20);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        // Image container
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(150, 100);
        imageContainer.setMaxSize(150, 100);
        imageContainer.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 10;");

        try {
            ImageView venueImg = new ImageView(new Image(getClass().getResourceAsStream(booking.getImagePath())));
            venueImg.setFitWidth(150);
            venueImg.setFitHeight(100);
            venueImg.setPreserveRatio(false);
            Rectangle clip = new Rectangle(150, 100);
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            venueImg.setClip(clip);
            imageContainer.getChildren().add(venueImg);
        } catch (Exception e) {
            Label placeholder = new Label("🏟️");
            placeholder.setFont(Font.font(36));
            imageContainer.getChildren().add(placeholder);
        }

        // Info container
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label userLabel = new Label("User: " + booking.getUsername());
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        userLabel.setTextFill(Color.web("#1a237e"));

        Label venueName = new Label(booking.getVenueName());
        venueName.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        venueName.setTextFill(Color.web("#1a1a1a"));
        venueName.setWrapText(true);

        Label sportLabel = new Label(booking.getSport() + " • " + booking.getCity());
        sportLabel.setFont(Font.font("Arial", 14));
        sportLabel.setTextFill(Color.web("#666666"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String dateStr = booking.getBookingDate().format(formatter);

        Label dateTime = new Label("📅 " + dateStr + " | ⏰ " + booking.getBookingTime());
        dateTime.setFont(Font.font("Arial", 14));
        dateTime.setTextFill(Color.web("#666666"));

        Label price = new Label("💰 Rp " + booking.getPrice());
        price.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        price.setTextFill(Color.web("#4fc3f7"));

        infoBox.getChildren().addAll(userLabel, venueName, sportLabel, dateTime, price);

        // Status label
        VBox statusBox = new VBox();
        statusBox.setAlignment(Pos.CENTER_RIGHT);

        Label statusLabel = new Label(booking.getStatus());
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        statusLabel.setPadding(new Insets(8, 15, 8, 15));
        statusLabel.setStyle("-fx-background-radius: 20;");

        if (booking.getStatus().equals("selesai")) {
            statusLabel.setTextFill(Color.web("#2e7d32"));
            statusLabel.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 20;");
        } else {
            statusLabel.setTextFill(Color.web("#f57c00"));
            statusLabel.setStyle("-fx-background-color: #fff3e0; -fx-background-radius: 20;");
        }

        statusBox.getChildren().add(statusLabel);

        card.getChildren().addAll(imageContainer, infoBox, statusBox);

        // Hover effect
        card.setOnMouseEntered(e ->
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);"));
        card.setOnMouseExited(e ->
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);"));

        return card;
    }

    private void showAllVenues() {
        mainContent.getChildren().clear();

        // Header with back button
        HBox headerBox = new HBox(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = new Button("← Kembali");
        backBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backBtn.setTextFill(Color.WHITE);
        backBtn.setStyle("-fx-background-color: #1a237e; -fx-background-radius: 20; " +
                "-fx-padding: 10 20 10 20; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e ->
                backBtn.setStyle("-fx-background-color: #0d47a1; -fx-background-radius: 20; " +
                        "-fx-padding: 10 20 10 20; -fx-cursor: hand;"));
        backBtn.setOnMouseExited(e ->
                backBtn.setStyle("-fx-background-color: #1a237e; -fx-background-radius: 20; " +
                        "-fx-padding: 10 20 10 20; -fx-cursor: hand;"));
        backBtn.setOnAction(e -> showDashboard());

        Label pageTitle = new Label("Semua Venue");
        pageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        pageTitle.setTextFill(Color.web("#1a237e"));

        headerBox.getChildren().addAll(backBtn, pageTitle);

        // Venue info text
        Label infoText = new Label("Total 10 venue tersedia di sistem");
        infoText.setFont(Font.font("Arial", 16));
        infoText.setTextFill(Color.web("#666666"));

        // Venues grid
        GridPane venuesGrid = new GridPane();
        venuesGrid.setHgap(25);
        venuesGrid.setVgap(25);
        venuesGrid.setAlignment(Pos.CENTER);

        // Hardcoded venue list (sama seperti di BookingApp)
        String[][] venues = {
                {"Candu Padel", "4.94", "Kota Pekalongan", "Padel", "70.000"},
                {"Ambassador Tennis", "4.93", "Kota Malang", "Tennis", "150.000"},
                {"VIE Arena, Badminton Hall", "4.96", "Kabupaten Tangerang", "Badminton", "50.000"},
                {"Eternal Arena Rooftop", "4.98", "Kota Surabaya", "Mini Soccer", "450.000"},
                {"The Bucketlist Bogor", "4.96", "Kota Bogor", "Basketball", "75.000"},
                {"The Racquet Club (TRC)", "4.90", "Kota Jakarta Selatan", "Tennis", "275.000"},
                {"Gaskan Pendekar Arena", "4.71", "Kota Jakarta Utara", "Mini Soccer", "300.000"},
                {"GOR Platinum Dieng", "4.90", "Kota Malang", "Badminton", "30.000"},
                {"Kma - Kiss My Air", "5.00", "Kota Bandung", "Basketball", "100.000"},
                {"Osseh Padel", "4.81", "Kota Surabaya", "Padel", "400.000"}
        };

        int col = 0;
        int row = 0;
        for (String[] venue : venues) {
            VBox card = createVenueCard(venue[0], venue[1], venue[2], venue[3], venue[4]);
            venuesGrid.add(card, col, row);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }

        mainContent.getChildren().addAll(headerBox, infoText, venuesGrid);
    }

    private VBox createVenueCard(String name, String rating, String city, String sport, String price) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setPrefWidth(380);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        Label venueLabel = new Label("Venue");
        venueLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        venueLabel.setTextFill(Color.web("#999999"));

        Label venueName = new Label(name);
        venueName.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        venueName.setTextFill(Color.web("#1a1a1a"));
        venueName.setWrapText(true);

        HBox ratingBox = new HBox(8);
        ratingBox.setAlignment(Pos.CENTER_LEFT);

        Label star = new Label("⭐");
        star.setFont(Font.font(14));

        Label ratingText = new Label(rating + " • " + city);
        ratingText.setFont(Font.font("Arial", 13));
        ratingText.setTextFill(Color.web("#666666"));

        ratingBox.getChildren().addAll(star, ratingText);

        HBox sportBox = new HBox(8);
        sportBox.setAlignment(Pos.CENTER_LEFT);

        Label sportIcon = new Label(getSportIcon(sport));
        sportIcon.setFont(Font.font(16));

        Label sportLabel = new Label(sport);
        sportLabel.setFont(Font.font("Arial", 13));
        sportLabel.setTextFill(Color.web("#666666"));

        sportBox.getChildren().addAll(sportIcon, sportLabel);

        Label priceLabel = new Label("Rp " + price + " /sesi");
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        priceLabel.setTextFill(Color.web("#1a237e"));

        card.getChildren().addAll(venueLabel, venueName, ratingBox, sportBox, priceLabel);

        // Hover effect
        card.setOnMouseEntered(e ->
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);"));
        card.setOnMouseExited(e ->
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);"));

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

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin keluar dari Admin?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Stage stage = (Stage) mainContent.getScene().getWindow();
                LoginApp loginApp = new LoginApp();
                try {
                    loginApp.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private HBox createFooter() {
        HBox footer = new HBox(50);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(25, 50, 25, 50));
        footer.setStyle("-fx-background-color: #1a237e;");

        Label copyright = new Label("© GoSport Booking - Admin Panel");
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
}