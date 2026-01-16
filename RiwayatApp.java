import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RiwayatApp {

    private VBox mainContent;
    private String currentUsername;

    public RiwayatApp(String username) {
        this.currentUsername = username;
    }

    public RiwayatApp() {
        this.currentUsername = "Guest";
    }

    public Scene createScene(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        VBox header = createHeader();
        root.setTop(header);

        mainContent = new VBox(30);
        mainContent.setPadding(new Insets(40, 100, 40, 100));
        mainContent.setStyle("-fx-background-color: #f5f5f5;");

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setVvalue(0);
        scrollPane.setStyle("-fx-background: #f5f5f5; -fx-background-color: #f5f5f5;");

        root.setCenter(scrollPane);

        HBox footer = createFooter();
        root.setBottom(footer);

        showRiwayatPage();

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
        Label sewaBtn = createNavButton("Sewa Lapangan", false);
        Label riwayatBtn = createNavButton("Riwayat", true);
        Label profileBtn = createNavButton("Profile", false);

        blogBtn.setOnMouseClicked(e -> {
            DashboardApp dashboard = new DashboardApp(currentUsername);
            Stage stage = (Stage) mainContent.getScene().getWindow();
            stage.setScene(dashboard.createScene(stage));
        });

        sewaBtn.setOnMouseClicked(e -> {
            DashboardApp dashboard = new DashboardApp(currentUsername);
            Stage stage = (Stage) mainContent.getScene().getWindow();
            Scene scene = dashboard.createScene(stage);
            stage.setScene(scene);
        });

        profileBtn.setOnMouseClicked(e -> {
            ProfileApp profileApp = new ProfileApp(currentUsername);
            Stage stage = (Stage) mainContent.getScene().getWindow();
            stage.setScene(profileApp.createScene(stage));
        });

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

    private void showRiwayatPage() {
        mainContent.getChildren().clear();
        mainContent.setPadding(Insets.EMPTY);
        mainContent.setSpacing(0);

        VBox orangeHeader = new VBox(10);
        orangeHeader.setAlignment(Pos.CENTER);
        orangeHeader.setPadding(new Insets(30, 0, 30, 0));
        orangeHeader.setStyle("-fx-background-color: #ff8c5a;");
        orangeHeader.setMaxWidth(Double.MAX_VALUE);

        Label pageTitle = new Label("Riwayat Booking");
        pageTitle.setFont(Font.font("Impact", FontWeight.BOLD, 48));
        pageTitle.setTextFill(Color.WHITE);
        pageTitle.setStyle("-fx-font-style: italic;");

        orangeHeader.getChildren().add(pageTitle);
        mainContent.getChildren().add(orangeHeader);

        VBox contentContainer = new VBox(30);
        contentContainer.setPadding(new Insets(40, 100, 40, 100));

        List<BookingData> completedBookings = BookingData.getCompletedBookings();
        if (!completedBookings.isEmpty()) {
            VBox completedSection = createBookingSection("6 bulan terakhir", completedBookings, "selesai");
            contentContainer.getChildren().add(completedSection);
        }

        List<BookingData> todayBookings = BookingData.getTodayBookings();
        if (!todayBookings.isEmpty()) {
            VBox todaySection = createBookingSection("Jadwal hari ini", todayBookings, "mendatang");
            contentContainer.getChildren().add(todaySection);
        }

        if (completedBookings.isEmpty() && todayBookings.isEmpty()) {
            VBox emptyState = new VBox(20);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(80));

            Label emptyIcon = new Label("📅");
            emptyIcon.setFont(Font.font(64));

            Label emptyText = new Label("Belum ada riwayat booking");
            emptyText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            emptyText.setTextFill(Color.web("#666666"));

            Label emptySubtext = new Label("Mulai booking lapangan favoritmu sekarang!");
            emptySubtext.setFont(Font.font("Arial", 16));
            emptySubtext.setTextFill(Color.web("#999999"));

            emptyState.getChildren().addAll(emptyIcon, emptyText, emptySubtext);
            contentContainer.getChildren().add(emptyState);
        }

        mainContent.getChildren().add(contentContainer);
    }

    private VBox createBookingSection(String sectionTitle, List<BookingData> bookings, String defaultStatus) {
        VBox section = new VBox(20);
        section.setPadding(new Insets(20, 0, 20, 0));

        HBox sectionHeader = new HBox(10);
        sectionHeader.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label(sectionTitle.contains("6 bulan") ? "🕐" : "📅");
        icon.setFont(Font.font(28));

        Label titleLabel = new Label(sectionTitle);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#666666"));

        sectionHeader.getChildren().addAll(icon, titleLabel);
        section.getChildren().add(sectionHeader);

        VBox cardsContainer = new VBox(20);
        for (BookingData booking : bookings) {
            HBox card = createBookingCard(booking);
            cardsContainer.getChildren().add(card);
        }

        section.getChildren().add(cardsContainer);
        return section;
    }

    private HBox createBookingCard(BookingData booking) {
        HBox card = new HBox(20);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(200, 150);
        imageContainer.setMaxSize(200, 150);
        imageContainer.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 10;");

        try {
            ImageView venueImg = new ImageView(new Image(getClass().getResourceAsStream(booking.getImagePath())));
            venueImg.setFitWidth(200);
            venueImg.setFitHeight(150);
            venueImg.setPreserveRatio(false);
            Rectangle clip = new Rectangle(200, 150);
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            venueImg.setClip(clip);
            imageContainer.getChildren().add(venueImg);
        } catch (Exception e) {
            Label placeholder = new Label("🏟️");
            placeholder.setFont(Font.font(48));
            imageContainer.getChildren().add(placeholder);
        }

        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label venueName = new Label(booking.getVenueName());
        venueName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        venueName.setTextFill(Color.web("#1a1a1a"));
        venueName.setWrapText(true);

        Label sportLabel = new Label(booking.getSport());
        sportLabel.setFont(Font.font("Arial", 15));
        sportLabel.setTextFill(Color.web("#888888"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String dateStr = booking.getBookingDate().format(formatter);

        Label dateTime = new Label("Tanggal: " + dateStr + ", Jam: " + booking.getBookingTime());
        dateTime.setFont(Font.font("Arial", 15));
        dateTime.setTextFill(Color.web("#666666"));

        Label price = new Label("Harga: Rp " + booking.getPrice() + "/sesi");
        price.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        price.setTextFill(Color.web("#1a1a1a"));

        infoBox.getChildren().addAll(venueName, sportLabel, dateTime, price);

        VBox actionBox = new VBox(15);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setPrefWidth(150);

        Label statusLabel = new Label(booking.getStatus());
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statusLabel.setPadding(new Insets(8, 15, 8, 15));
        statusLabel.setStyle("-fx-background-radius: 20;");

        if (booking.getStatus().equals("selesai")) {
            statusLabel.setTextFill(Color.web("#2e7d32"));
            statusLabel.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 20;");
        } else {
            statusLabel.setTextFill(Color.web("#f57c00"));
            statusLabel.setStyle("-fx-background-color: #fff3e0; -fx-background-radius: 20;");
        }

        Button bookAgainBtn = new Button("Booking lagi");
        bookAgainBtn.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        bookAgainBtn.setTextFill(Color.WHITE);
        bookAgainBtn.setStyle("-fx-background-color: #4fc3f7; -fx-background-radius: 20; " +
                "-fx-padding: 10 20 10 20; -fx-cursor: hand;");
        bookAgainBtn.setOnMouseEntered(e ->
                bookAgainBtn.setStyle("-fx-background-color: #29b6f6; -fx-background-radius: 20; " +
                        "-fx-padding: 10 20 10 20; -fx-cursor: hand;"));
        bookAgainBtn.setOnMouseExited(e ->
                bookAgainBtn.setStyle("-fx-background-color: #4fc3f7; -fx-background-radius: 20; " +
                        "-fx-padding: 10 20 10 20; -fx-cursor: hand;"));

        bookAgainBtn.setOnAction(e -> {
            System.out.println("Booking lagi: " + booking.getVenueName());
        });

        actionBox.getChildren().addAll(statusLabel, bookAgainBtn);

        card.getChildren().addAll(imageContainer, infoBox, actionBox);

        card.setOnMouseEntered(e ->
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);"));
        card.setOnMouseExited(e ->
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);"));

        return card;
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
}