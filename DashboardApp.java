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

public class DashboardApp {

    private VBox mainContent;
    private List<Article> articles;
    private GridPane articlesGrid;

    private List<Venue> venues;
    private GridPane venuesGrid;
    private String currentUsername;
    private HBox navbar;

    public DashboardApp(String username) {
        this.currentUsername = username;
    }

    public Scene createScene(Stage stage) {
        initializeArticles();
        initializeVenues();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        VBox header = createHeader();
        root.setTop(header);

        mainContent = new VBox();
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setPadding(Insets.EMPTY);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;"
        );

        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f5f5; -fx-background-color: #f5f5f5;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setCenter(scrollPane);

        HBox footer = createFooter();
        root.setBottom(footer);

        showBlogPage();

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

        navbar = new HBox(60);
        navbar.setAlignment(Pos.CENTER);
        navbar.setPadding(new Insets(15, 40, 15, 40));
        navbar.setStyle("-fx-background-color: #f4e4a3;");

        Label blogBtn = createNavButton("Blog", true);
        Label sewaBtn = createNavButton("Sewa Lapangan", false);
        Label riwayatBtn = createNavButton("Riwayat", false);
        Label profileBtn = createNavButton("Profile", false);

        blogBtn.setOnMouseClicked(e -> {
            resetNavButtons(navbar);
            blogBtn.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a237e; -fx-cursor: hand; -fx-underline: true;");
            showBlogPage();
        });

        sewaBtn.setOnMouseClicked(e -> {
            resetNavButtons(navbar);
            sewaBtn.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a237e; -fx-cursor: hand; -fx-underline: true;");
            showSewaLapanganPage();
        });

        riwayatBtn.setOnMouseClicked(e -> {
            resetNavButtons(navbar);
            riwayatBtn.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a237e; -fx-cursor: hand; -fx-underline: true;");
            RiwayatApp riwayatApp = new RiwayatApp(currentUsername);
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            currentStage.setScene(riwayatApp.createScene(currentStage));
        });

        profileBtn.setOnMouseClicked(e -> {
            resetNavButtons(navbar);
            profileBtn.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a237e; -fx-cursor: hand; -fx-underline: true;");
            ProfileApp profileApp = new ProfileApp(currentUsername);
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            currentStage.setScene(profileApp.createScene(currentStage));
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

    private void resetNavButtons(HBox navbar) {
        for (var node : navbar.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                label.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a237e; -fx-cursor: hand;");
            }
        }
    }

    private void showBlogPage() {
        mainContent.getChildren().clear();
        mainContent.setPadding(Insets.EMPTY);
        mainContent.setSpacing(0);

        mainContent.setStyle("-fx-background-color: #f5f5f5;");
        mainContent.setSpacing(0);

        VBox orangeHeader = new VBox(25);
        orangeHeader.setMaxWidth(Double.MAX_VALUE);

        orangeHeader.setAlignment(Pos.CENTER);
        orangeHeader.setPadding(new Insets(40, 60, 40, 60));
        orangeHeader.setStyle("-fx-background-color: #ff8c5a;");

        Label blogTitle = new Label("Artikel & Tips GoSport");
        blogTitle.setFont(Font.font("Impact", FontWeight.BOLD, 50));
        blogTitle.setTextFill(Color.WHITE);
        blogTitle.setStyle("-fx-font-style: italic;");

        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setMaxWidth(700);
        searchBox.setPrefHeight(50);
        searchBox.setPadding(new Insets(5, 20, 5, 20));
        searchBox.setStyle("-fx-background-color: white; -fx-background-radius: 30;");

        Label searchIcon = new Label("🔍");
        searchIcon.setFont(Font.font(24));

        TextField searchField = new TextField();
        searchField.setPromptText("Cari artikel...");
        searchField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 16px;");
        searchField.setFont(Font.font("Arial", 16));
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterArticles(newVal);
        });

        searchBox.getChildren().addAll(searchIcon, searchField);
        orangeHeader.getChildren().addAll(blogTitle, searchBox);

        VBox articlesSection = new VBox(30);
        articlesSection.setPadding(new Insets(40, 80, 40, 80));
        articlesSection.setStyle("-fx-background-color: #f5f5f5;");

        articlesGrid = new GridPane();
        articlesGrid.setHgap(30);
        articlesGrid.setVgap(35);

        int col = 0;
        int row = 0;
        for (Article article : articles) {
            VBox card = createArticleCard(article);
            articlesGrid.add(card, col, row);

            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }

        articlesSection.getChildren().add(articlesGrid);
        mainContent.getChildren().addAll(orangeHeader, articlesSection);
    }

    private VBox createArticleCard(Article article) {
        VBox card = new VBox(0);
        card.setPrefWidth(420);
        card.setMaxWidth(420);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3); " +
                "-fx-cursor: hand;");

        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(280);
        imageContainer.setMaxHeight(280);
        imageContainer.setStyle("-fx-background-color: " + article.bgColor + "; " +
                "-fx-background-radius: 15 15 0 0;");

        try {
            ImageView articleImg = new ImageView(new Image(getClass().getResourceAsStream(article.imagePath)));
            articleImg.setFitWidth(420);
            articleImg.setFitHeight(280);
            articleImg.setPreserveRatio(false);
            imageContainer.getChildren().add(articleImg);
        } catch (Exception e) {
            Label placeholder = new Label("📷");
            placeholder.setFont(Font.font(64));
            imageContainer.getChildren().add(placeholder);
        }

        VBox content = new VBox(10);
        content.setPadding(new Insets(20, 20, 20, 20));

        Label category = new Label(article.category.toUpperCase());
        category.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        category.setTextFill(Color.web("#d32f2f"));

        Label titleLabel = new Label(article.title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(380);
        titleLabel.setTextFill(Color.web("#1a1a1a"));

        Label preview = new Label(article.preview);
        preview.setFont(Font.font("Arial", 14));
        preview.setTextFill(Color.web("#666666"));
        preview.setWrapText(true);
        preview.setMaxWidth(380);
        preview.setMaxHeight(60);

        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(10, 0, 0, 0));

        Label author = new Label("oleh " + article.author);
        author.setFont(Font.font("Arial", 13));
        author.setTextFill(Color.web("#999999"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label date = new Label(article.date);
        date.setFont(Font.font("Arial", 13));
        date.setTextFill(Color.web("#999999"));

        footer.getChildren().addAll(author, spacer, date);

        content.getChildren().addAll(category, titleLabel, preview, footer);
        card.getChildren().addAll(imageContainer, content);

        card.setOnMouseEntered(e ->
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 5); " +
                        "-fx-cursor: hand;"));
        card.setOnMouseExited(e ->
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3); " +
                        "-fx-cursor: hand;"));

        card.setOnMouseClicked(e -> showArticleDetail(article));

        return card;
    }

    private void showArticleDetail(Article article) {
        mainContent.getChildren().clear();
        mainContent.setStyle("-fx-background-color: white;");
        mainContent.setSpacing(25);
        mainContent.setPadding(new Insets(40, 120, 60, 120));

        VBox headerSection = new VBox(12);

        Label titleLabel = new Label(article.title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setWrapText(true);
        titleLabel.setTextFill(Color.web("#1a1a1a"));

        HBox metadata = new HBox(30);
        metadata.setAlignment(Pos.CENTER_LEFT);

        Label author = new Label(article.author);
        author.setFont(Font.font("Arial", 15));
        author.setTextFill(Color.web("#666666"));

        Label date = new Label(article.date);
        date.setFont(Font.font("Arial", 15));
        date.setTextFill(Color.web("#666666"));

        metadata.getChildren().addAll(author, date);
        headerSection.getChildren().addAll(titleLabel, metadata);

        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(450);
        imageContainer.setMaxHeight(450);
        imageContainer.setStyle("-fx-background-color: " + article.bgColor + "; -fx-background-radius: 10;");

        try {
            ImageView articleImg = new ImageView(new Image(getClass().getResourceAsStream(article.imagePath)));
            articleImg.setFitWidth(760);
            articleImg.setFitHeight(450);
            articleImg.setPreserveRatio(false);
            imageContainer.getChildren().add(articleImg);
        } catch (Exception e) {
            Label placeholder = new Label("📷");
            placeholder.setFont(Font.font(64));
            imageContainer.getChildren().add(placeholder);
        }

        VBox articleContent = new VBox(20);
        articleContent.setPadding(new Insets(10, 0, 0, 0));

        Label subtitle = new Label(article.subtitle);
        subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        subtitle.setWrapText(true);
        subtitle.setTextFill(Color.web("#1a1a1a"));

        Label bodyText = new Label(article.content);
        bodyText.setFont(Font.font("Arial", 16));
        bodyText.setTextFill(Color.web("#333333"));
        bodyText.setWrapText(true);
        bodyText.setLineSpacing(3);

        VBox benefitsList = new VBox(12);
        benefitsList.setPadding(new Insets(10, 0, 10, 0));

        Label benefitsTitle = new Label("Manfaat Sparring:");
        benefitsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        benefitsTitle.setTextFill(Color.web("#1a1a1a"));

        benefitsList.getChildren().add(benefitsTitle);

        for (String benefit : article.benefits) {
            Label benefitItem = new Label(benefit);
            benefitItem.setFont(Font.font("Arial", 16));
            benefitItem.setWrapText(true);
            benefitItem.setTextFill(Color.web("#333333"));
            benefitsList.getChildren().add(benefitItem);
        }

        Label closingText = new Label(article.closing);
        closingText.setFont(Font.font("Arial", 16));
        closingText.setWrapText(true);
        closingText.setTextFill(Color.web("#333333"));

        articleContent.getChildren().addAll(subtitle, bodyText, benefitsList, closingText);

        Button backBtn = new Button("⬅ Kembali");
        backBtn.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
        backBtn.setTextFill(Color.web("#666666"));
        backBtn.setStyle("-fx-background-color: #e8e8e8; -fx-background-radius: 25; " +
                "-fx-padding: 12 25 12 25; -fx-cursor: hand; -fx-border-color: transparent;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: #d0d0d0; -fx-background-radius: 25; " +
                "-fx-padding: 12 25 12 25; -fx-cursor: hand; -fx-border-color: transparent;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: #e8e8e8; -fx-background-radius: 25; " +
                "-fx-padding: 12 25 12 25; -fx-cursor: hand; -fx-border-color: transparent;"));
        backBtn.setOnAction(e -> showBlogPage());

        mainContent.getChildren().addAll(headerSection, imageContainer, articleContent, backBtn);
    }

    private void filterArticles(String keyword) {
        keyword = keyword.trim().toLowerCase();

        articlesGrid.getChildren().clear();

        int col = 0;
        int row = 0;

        for (Article article : articles) {
            if (keyword.isEmpty()
                    || article.title.toLowerCase().contains(keyword)
                    || article.category.toLowerCase().contains(keyword)
                    || article.content.toLowerCase().contains(keyword)) {

                VBox card = createArticleCard(article);
                articlesGrid.add(card, col, row);

                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private void showSewaLapanganPage() {
        mainContent.getChildren().clear();
        mainContent.setPadding(Insets.EMPTY);
        mainContent.setSpacing(0);
        mainContent.setStyle("-fx-background-color: #f5f5f5;");
        mainContent.setAlignment(Pos.TOP_CENTER);

        VBox orangeHeader = new VBox(25);
        orangeHeader.setMaxWidth(Double.MAX_VALUE);
        orangeHeader.setAlignment(Pos.CENTER);
        orangeHeader.setPadding(new Insets(40, 60, 40, 60));
        orangeHeader.setStyle("-fx-background-color: #ff8c5a;");

        Label pageTitle = new Label("Ayo Booking Lapangan Favoritemu!!");
        pageTitle.setFont(Font.font("Impact", FontWeight.BOLD, 50));
        pageTitle.setTextFill(Color.WHITE);
        pageTitle.setStyle("-fx-font-style: italic;");

        HBox searchRow = new HBox(20);
        searchRow.setAlignment(Pos.CENTER);
        searchRow.setMaxWidth(900);

        HBox searchBox = new HBox(15);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefHeight(50);
        searchBox.setPrefWidth(450);
        searchBox.setPadding(new Insets(5, 20, 5, 20));
        searchBox.setStyle("-fx-background-color: white; -fx-background-radius: 30;");

        Label searchIcon = new Label("🔍");
        searchIcon.setFont(Font.font(24));

        TextField searchField = new TextField();
        searchField.setPromptText("Cari kota...");
        searchField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 16px;");
        searchField.setFont(Font.font("Arial", 16));
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchBox.getChildren().addAll(searchIcon, searchField);

        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPrefHeight(50);
        filterBox.setPrefWidth(450);
        filterBox.setPadding(new Insets(5, 20, 5, 20));
        filterBox.setStyle("-fx-background-color: white; -fx-background-radius: 30;");

        Label filterIcon = new Label("☰");
        filterIcon.setFont(Font.font(24));

        ComboBox<String> sportFilter = new ComboBox<>();
        sportFilter.getItems().addAll("Semua Cabang", "Badminton", "Basketball", "Mini Soccer", "Padel", "Tennis");
        sportFilter.setValue("Pilih cabang olahraga");
        sportFilter.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 16px;");
        sportFilter.setPrefWidth(350);
        HBox.setHgrow(sportFilter, Priority.ALWAYS);

        filterBox.getChildren().addAll(filterIcon, sportFilter);
        searchRow.getChildren().addAll(searchBox, filterBox);
        orangeHeader.getChildren().addAll(pageTitle, searchRow);

        VBox venuesSection = new VBox(30);
        venuesSection.setPadding(new Insets(40, 80, 40, 80));
        venuesSection.setStyle("-fx-background-color: #f5f5f5;");

        venuesGrid = new GridPane();
        venuesGrid.setHgap(30);
        venuesGrid.setVgap(35);

        updateVenuesGrid(venues);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterVenues(newVal, sportFilter.getValue());
        });

        sportFilter.setOnAction(e -> {
            filterVenues(searchField.getText(), sportFilter.getValue());
        });

        venuesSection.getChildren().add(venuesGrid);
        mainContent.getChildren().addAll(orangeHeader, venuesSection);
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
            System.out.println("❌ Error: " + e.getMessage());
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

    private void showPlaceholder(String pageName) {
        mainContent.getChildren().clear();
        mainContent.setStyle("-fx-background-color: #f5f5f5;");
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(100));

        VBox placeholder = new VBox(20);
        placeholder.setAlignment(Pos.CENTER);

        Label icon = new Label("🚧");
        icon.setFont(Font.font(64));

        Label label = new Label("Halaman " + pageName + " Segera Hadir!");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        label.setTextFill(Color.web("#666666"));

        Label sublabel = new Label("Fitur ini sedang dalam pengembangan");
        sublabel.setFont(Font.font("Arial", 16));
        sublabel.setTextFill(Color.web("#999999"));

        placeholder.getChildren().addAll(icon, label, sublabel);
        mainContent.getChildren().add(placeholder);
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
        venues.add(new Venue("Sportivo Futsal Arena", "4.92", "Kota Bandung", "Mini Soccer", "200.000", "/resources/lapangan7.png"));
        venues.add(new Venue("Prima Badminton Court", "4.88", "Kota Semarang", "Badminton", "65.000", "/resources/lapangan8.png"));
        venues.add(new Venue("Galaxy Basketball Center", "4.95", "Kota Yogyakarta", "Basketball", "85.000", "/resources/lapangan9.png"));
        venues.add(new Venue("Elite Padel Club", "4.91", "Kota Medan", "Padel", "120.000", "/resources/lapangan10.png"));
    }

    private void initializeArticles() {
        articles = new ArrayList<>();

        articles.add(new Article(
                "BADMINTON",
                "6 Manfaat Sparing Badminton yang Wajib Diketahui Pebulutangkis",
                "Untuk Anda yang hobi badminton, pernah tidak, merasa stuck dengan perkembangan permainan? Nah, sparring badminton jadi solusinya!",
                "Eren",
                "28 March 2025",
                "/resources/badminton.png",
                "#17a2b8",
                "Tingkatkan Skill Badminton dengan Sparring!",
                "Pernah merasa stuck dalam permainan badminton? Solusinya: sparring! Selain seru, sparring membantu meningkatkan teknik, fisik, mental, dan strategi.",
                new String[]{
                        "1. Teknik Terarah     : Belajar pukulan, footwork, dan strategi baru.",
                        "2. Fisik & Mental Kuat  : Latihan intens menghadapi tekanan.",
                        "3. Adaptasi Lawan     : Belajar menghadapi berbagai gaya main.",
                        "4. Keputusan Cepat    : Terbiasa mengambil langkah tepat di lapangan.",
                        "5. Percaya Diri       : Meningkatkan keyakinan saat bertanding.",
                        "6. Persaingan Sehat   : Belajar respek sambil tetap kompetitif."
                },
                "Ingin sparring mudah dan praktis? GoSport Book memungkinkan Anda sewa lapangan terbaik loh."
        ));

        articles.add(new Article(
                "TENNIS",
                "Teknik Dasar Tenis untuk Pemula yang Ingin Cepat Mahir",
                "Teknik Dasar Tenis untuk Pemula yang Ingin Cepat Mahir",
                "Jovian",
                "11 January 2026",
                "/resources/tenis.png",
                "#90ee90",
                "Teknik Dasar Tenis untuk Pemula",
                "Menguasai teknik dasar tenis sejak awal membantu pemula bermain lebih stabil, nyaman, dan terhindar dari cedera.",
                new String[]{
                        "1. Grip                : Gunakan Eastern forehand atau Continental untuk kontrol pukulan.",
                        "2. Stance & Footwork   : Posisi kaki stabil dan aktif bergerak ke arah bola.",
                        "3. Swing               : Ayunan halus, terkontrol, fokus ke bola saat kontak.",
                        "4. Servis & Return     : Utamakan akurasi sebelum kekuatan.",
                        "5. Konsistensi         : Latihan rutin agar gerakan lebih natural."
                },
                "Mulai latihan tenis Anda dengan booking lapangan praktis lewat GoSport Book!"
        ));

        articles.add(new Article(
                "SOCCER",
                "Inilah Ukuran Standar Lapangan Mini Soccer yang Harus Diketahui",
                "Ukuran lapangan mini soccer ternyata bervariasi. Sampai saat ini, belum ada standar jelas mengenai ukuran lapangan sepak bola mini.",
                "Dite",
                "29 July 2024",
                "/resources/soccer.png",
                "#90ee90",
                "Mini Soccer: Ukuran Lapangan dan Peraturannya",
                "Mini soccer merupakan varian sepak bola yang semakin populer di Indonesia sebagai olahraga rekreasi. Olahraga ini sering dibandingkan dengan futsal karena sama-sama dimainkan di lapangan yang lebih kecil dari sepak bola konvensional.",
                new String[]{
                        "Lapangan mini soccer memiliki ukuran yang lebih besar dari futsal, namun lebih kecil dari lapangan sepak bola standar. Hingga saat ini, belum ada ukuran resmi, sehingga dimensi lapangan bisa berbeda-beda.",
                        "Umumnya, lapangan mini soccer memiliki panjang sekitar 20–25 meter dengan lebar 12–15 meter. Beberapa lapangan juga menggunakan ukuran lebih besar, seperti 60 × 40 meter, tergantung fasilitas.",
                        "Permainan mini soccer biasanya dimainkan oleh 5 hingga 7 pemain per tim. Semakin besar ukuran lapangan, semakin banyak pemain yang dapat bermain.",
                        "Pertandingan berlangsung selama dua babak dengan durasi masing-masing 20 menit. Tidak ada lemparan ke dalam, bola yang keluar lapangan dikembalikan dengan tendangan.",
                        "Gawang berukuran 2 × 5 meter, dilengkapi area penalti dan titik penalti. Aturan tendangan bebas dan sudut mengikuti peraturan sepak bola pada umumnya."
                },
                "Temukan dan booking lapangan mini soccer terbaik dengan mudah melalui GoSport Book!"

        ));

        articles.add(new Article(
                "BASKET",
                "Trik Kuasai Teknik Overhead Pass agar Umpan Makin Akurat",
                "Pelajari teknik overhead pass agar umpan makin akurat. Temukan tips terbaik untuk menguasai teknik ini dan tingkatkan permainan Anda!",
                "Eren",
                "23 March 2025",
                "/resources/basket.png",
                "#d3d3d3",
                "Teknik Overhead Pass Basket",
                "Overhead pass adalah teknik passing dari atas kepala yang sering digunakan untuk mengirim bola jarak jauh atau melewati hadangan lawan. Meski terlihat sederhana, teknik yang kurang tepat bisa membuat umpan mudah dipotong.",
                new String[]{
                        "Overhead pass paling efektif digunakan saat lawan menekan ketat, ingin mempercepat serangan, atau mengalihkan bola ke sisi lain lapangan. Teknik ini juga berguna saat menghadapi lawan yang lebih tinggi atau pertahanan zona yang rapat.",
                        "Agar overhead pass akurat dan bertenaga, teknik dasar harus dilakukan dengan benar. Posisi tubuh, gerakan kaki, hingga timing pelepasan bola sangat menentukan keberhasilan umpan.",
                        "1. Posisi Awal     : Pegang bola dengan kedua tangan di atas kepala secara stabil.",
                        "2. Gerakan Kaki    : Langkahkan kaki dominan ke depan untuk menjaga keseimbangan.",
                        "3. Tenaga Dorong   : Gunakan tenaga dari kaki dan tubuh, bukan hanya tangan.",
                        "4. Waktu Lepas     : Lepaskan bola dengan dorongan lurus ke depan di timing yang tepat.",
                        "5. Akurasi         : Arahkan umpan ke pergerakan rekan setim, bukan posisi diam."
                },
                "Yuk, praktikkan overhead pass langsung di lapangan dengan booking lapangan basket mudah lewat GoSport Book!"

        ));
    }

    class Article {
        String category, title, preview, author, date, imagePath, bgColor, subtitle, content, closing;
        String[] benefits;

        Article(String category, String title, String preview, String author, String date,
                String imagePath, String bgColor, String subtitle, String content,
                String[] benefits, String closing) {
            this.category = category;
            this.title = title;
            this.preview = preview;
            this.author = author;
            this.date = date;
            this.imagePath = imagePath;
            this.bgColor = bgColor;
            this.subtitle = subtitle;
            this.content = content;
            this.benefits = benefits;
            this.closing = closing;
        }
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