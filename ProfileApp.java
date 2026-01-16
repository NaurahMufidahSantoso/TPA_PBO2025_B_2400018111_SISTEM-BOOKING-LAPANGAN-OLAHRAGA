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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class ProfileApp {

    private VBox mainContent;
    private String currentUsername;
    private String currentEmail;
    private String profileImagePath;

    public ProfileApp(String username) {
        this.currentUsername = username;
        this.currentEmail = username.toLowerCase() + "@gmail.com";
        this.profileImagePath = null;
    }

    public ProfileApp(String username, String email) {
        this.currentUsername = username;
        this.currentEmail = email;
        this.profileImagePath = null;
    }

    public Scene createScene(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ede6d4;");

        VBox header = createHeader();
        root.setTop(header);

        mainContent = new VBox(0);
        mainContent.setStyle("-fx-background-color: #ede6d4;");

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVvalue(0);
        scrollPane.setStyle("-fx-background: #ede6d4; -fx-background-color: #ede6d4;");

        root.setCenter(scrollPane);

        HBox footer = createFooter();
        root.setBottom(footer);

        showProfilePage();

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
        Label riwayatBtn = createNavButton("Riwayat", false);
        Label profileBtn = createNavButton("Profile", true);

        blogBtn.setOnMouseClicked(e -> {
            DashboardApp dashboard = new DashboardApp(currentUsername);
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            currentStage.setScene(dashboard.createScene(currentStage));
        });

        sewaBtn.setOnMouseClicked(e -> {
            DashboardApp dashboard = new DashboardApp(currentUsername);
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            currentStage.setScene(dashboard.createScene(currentStage));
        });

        riwayatBtn.setOnMouseClicked(e -> {
            RiwayatApp riwayat = new RiwayatApp(currentUsername);
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            currentStage.setScene(riwayat.createScene(currentStage));
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

    private void showProfilePage() {
        mainContent.getChildren().clear();
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(40, 100, 60, 100));
        mainContent.setSpacing(30);

        Label pageTitle = new Label("Profile");
        pageTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 60));
        pageTitle.setTextFill(Color.web("#8b6914"));
        pageTitle.setStyle("-fx-font-style: italic;");

        StackPane profilePicContainer = new StackPane();

        Circle profileCircle = new Circle(100);
        profileCircle.setFill(Color.web("#b8941e"));

        StackPane imageStack = new StackPane();
        if (profileImagePath != null) {
            try {
                ImageView profileImage = new ImageView(new Image("file:" + profileImagePath));
                profileImage.setFitWidth(200);
                profileImage.setFitHeight(200);
                profileImage.setPreserveRatio(false);

                Circle clip = new Circle(100);
                clip.setCenterX(100);
                clip.setCenterY(100);
                profileImage.setClip(clip);

                imageStack.getChildren().add(profileImage);
            } catch (Exception e) {
                Label profileIcon = new Label("👤");
                profileIcon.setFont(Font.font(80));
                imageStack.getChildren().add(profileIcon);
            }
        } else {
            Label profileIcon = new Label("👤");
            profileIcon.setFont(Font.font(80));
            imageStack.getChildren().add(profileIcon);
        }

        StackPane editIconContainer = new StackPane();
        Circle editCircle = new Circle(18);
        editCircle.setFill(Color.web("#1a1a1a"));

        Label editIcon = new Label("✏");
        editIcon.setFont(Font.font(16));
        editIcon.setTextFill(Color.WHITE);

        editIconContainer.getChildren().addAll(editCircle, editIcon);
        editIconContainer.setTranslateX(70);
        editIconContainer.setTranslateY(70);
        editIconContainer.setStyle("-fx-cursor: hand;");

        editIconContainer.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Pilih Foto Profil");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(mainContent.getScene().getWindow());
            if (selectedFile != null) {
                profileImagePath = selectedFile.getAbsolutePath();
                showProfilePage();
            }
        });

        profilePicContainer.getChildren().addAll(profileCircle, imageStack, editIconContainer);

        Label usernameLabel = new Label(currentUsername);
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        usernameLabel.setTextFill(Color.web("#1a237e"));

        Label emailLabel = new Label(currentEmail);
        emailLabel.setFont(Font.font("Arial", 20));
        emailLabel.setTextFill(Color.web("#1a237e"));

        VBox profileInfo = new VBox(15);
        profileInfo.setAlignment(Pos.CENTER);
        profileInfo.getChildren().addAll(pageTitle, profilePicContainer, usernameLabel, emailLabel);

        VBox menuContainer = new VBox(20);
        menuContainer.setAlignment(Pos.CENTER);
        menuContainer.setMaxWidth(700);

        HBox editProfileBtn = createMenuButton("📝", "Edit Profile", "#d4af37");
        HBox changePasswordBtn = createMenuButton("🔒", "Ubah Kata Sandi", "#d4af37");
        HBox settingsBtn = createMenuButton("⚙", "Pengaturan", "#d4af37");

        editProfileBtn.setOnMouseClicked(e -> showEditProfilePage());
        changePasswordBtn.setOnMouseClicked(e -> showChangePasswordPage());
        settingsBtn.setOnMouseClicked(e -> showSettingsPage());

        menuContainer.getChildren().addAll(editProfileBtn, changePasswordBtn, settingsBtn);

        Button logoutBtn = new Button("Log Out");
        logoutBtn.setGraphic(createLogoutIcon());
        logoutBtn.setContentDisplay(ContentDisplay.LEFT);
        logoutBtn.setGraphicTextGap(15);
        logoutBtn.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logoutBtn.setTextFill(Color.WHITE);
        logoutBtn.setPrefWidth(350);
        logoutBtn.setPrefHeight(70);
        logoutBtn.setStyle("-fx-background-color: #b8941e; -fx-background-radius: 15; " +
                "-fx-cursor: hand;");

        logoutBtn.setOnMouseEntered(e ->
                logoutBtn.setStyle("-fx-background-color: #9a7a18; -fx-background-radius: 15; " +
                        "-fx-cursor: hand;"));
        logoutBtn.setOnMouseExited(e ->
                logoutBtn.setStyle("-fx-background-color: #b8941e; -fx-background-radius: 15; " +
                        "-fx-cursor: hand;"));

        logoutBtn.setOnAction(e -> handleLogout());

        VBox logoutContainer = new VBox(40);
        logoutContainer.setAlignment(Pos.CENTER);
        logoutContainer.getChildren().add(logoutBtn);

        mainContent.getChildren().addAll(profileInfo, menuContainer, logoutContainer);
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
        textLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        textLabel.setTextFill(Color.web(color));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label arrow = new Label("›");
        arrow.setFont(Font.font(40));
        arrow.setTextFill(Color.web(color));

        button.getChildren().addAll(iconLabel, textLabel, spacer, arrow);

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 4); " +
                        "-fx-cursor: hand;"));
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3); " +
                        "-fx-cursor: hand;"));

        return button;
    }

    private Label createLogoutIcon() {
        Label icon = new Label("⎋");
        icon.setFont(Font.font(32));
        icon.setTextFill(Color.WHITE);
        return icon;
    }

    private void showEditProfilePage() {
        mainContent.getChildren().clear();
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(50, 100, 50, 100));
        mainContent.setSpacing(30);

        VBox container = new VBox(25);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(600);
        container.setPadding(new Insets(40));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        Label title = new Label("Edit Profile");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#1a237e"));

        VBox usernameBox = createEditField("Username", currentUsername);
        TextField usernameField = (TextField) usernameBox.getChildren().get(1);

        VBox emailBox = createEditField("Email", currentEmail);
        TextField emailField = (TextField) emailBox.getChildren().get(1);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button cancelBtn = new Button("Batal");
        styleButton(cancelBtn, "#e0e0e0", "#666666", false);
        cancelBtn.setOnAction(e -> showProfilePage());

        Button saveBtn = new Button("Simpan");
        styleButton(saveBtn, "#1a237e", "#ffffff", true);
        saveBtn.setOnAction(e -> {
            currentUsername = usernameField.getText();
            currentEmail = emailField.getText();
            showAlert("Berhasil", "Profil berhasil diperbarui!");
            showProfilePage();
        });

        buttonBox.getChildren().addAll(cancelBtn, saveBtn);

        container.getChildren().addAll(title, usernameBox, emailBox, buttonBox);
        mainContent.getChildren().add(container);
    }

    private void showChangePasswordPage() {
        mainContent.getChildren().clear();
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(50, 100, 50, 100));
        mainContent.setSpacing(30);

        VBox container = new VBox(25);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(600);
        container.setPadding(new Insets(40));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        Label title = new Label("Ubah Kata Sandi");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#1a237e"));

        VBox oldPasswordBox = createPasswordField("Password Lama");
        VBox newPasswordBox = createPasswordField("Password Baru");
        VBox confirmPasswordBox = createPasswordField("Konfirmasi Password Baru");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button cancelBtn = new Button("Batal");
        styleButton(cancelBtn, "#e0e0e0", "#666666", false);
        cancelBtn.setOnAction(e -> showProfilePage());

        Button saveBtn = new Button("Ubah Password");
        styleButton(saveBtn, "#1a237e", "#ffffff", true);
        saveBtn.setOnAction(e -> {
            showAlert("Berhasil", "Password berhasil diubah!");
            showProfilePage();
        });

        buttonBox.getChildren().addAll(cancelBtn, saveBtn);

        container.getChildren().addAll(title, oldPasswordBox, newPasswordBox, confirmPasswordBox, buttonBox);
        mainContent.getChildren().add(container);
    }

    private void showSettingsPage() {
        mainContent.getChildren().clear();
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(50, 100, 50, 100));
        mainContent.setSpacing(30);

        VBox container = new VBox(25);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(700);
        container.setPadding(new Insets(40));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        Label title = new Label("Pengaturan");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#1a237e"));

        VBox settingsItems = new VBox(15);
        settingsItems.setAlignment(Pos.CENTER);

        HBox notifSetting = createSettingItem("🔔", "Notifikasi", "Aktif");
        HBox languageSetting = createSettingItem("🌐", "Bahasa", "Indonesia");
        HBox themeSetting = createSettingItem("🎨", "Tema", "Terang");
        HBox privacySetting = createSettingItem("🔒", "Privasi", "Publik");

        settingsItems.getChildren().addAll(notifSetting, languageSetting, themeSetting, privacySetting);

        Button backBtn = new Button("Kembali");
        styleButton(backBtn, "#e0e0e0", "#666666", false);
        backBtn.setOnAction(e -> showProfilePage());

        container.getChildren().addAll(title, settingsItems, backBtn);
        mainContent.getChildren().add(container);
    }

    private HBox createSettingItem(String icon, String label, String value) {
        HBox item = new HBox(20);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(20, 25, 20, 25));
        item.setPrefWidth(650);
        item.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 15;");

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(28));

        Label textLabel = new Label(label);
        textLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        textLabel.setTextFill(Color.web("#333333"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", 16));
        valueLabel.setTextFill(Color.web("#666666"));

        item.getChildren().addAll(iconLabel, textLabel, spacer, valueLabel);
        return item;
    }

    private VBox createEditField(String label, String value) {
        VBox box = new VBox(10);

        Label fieldLabel = new Label(label);
        fieldLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        fieldLabel.setTextFill(Color.web("#333333"));

        TextField field = new TextField(value);
        field.setStyle("-fx-font-size: 14px; -fx-padding: 12;");
        field.setPrefHeight(45);

        box.getChildren().addAll(fieldLabel, field);
        return box;
    }

    private VBox createPasswordField(String label) {
        VBox box = new VBox(10);

        Label fieldLabel = new Label(label);
        fieldLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        fieldLabel.setTextFill(Color.web("#333333"));

        PasswordField field = new PasswordField();
        field.setStyle("-fx-font-size: 14px; -fx-padding: 12;");
        field.setPrefHeight(45);

        box.getChildren().addAll(fieldLabel, field);
        return box;
    }

    private void styleButton(Button button, String bgColor, String textColor, boolean primary) {
        button.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        button.setTextFill(Color.web(textColor));
        button.setPrefWidth(primary ? 200 : 150);
        button.setPrefHeight(45);
        button.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 25; " +
                "-fx-cursor: hand;");

        if (primary) {
            button.setOnMouseEntered(e ->
                    button.setStyle("-fx-background-color: #0d47a1; -fx-background-radius: 25; " +
                            "-fx-cursor: hand;"));
            button.setOnMouseExited(e ->
                    button.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 25; " +
                            "-fx-cursor: hand;"));
        } else {
            button.setOnMouseEntered(e ->
                    button.setStyle("-fx-background-color: #d0d0d0; -fx-background-radius: 25; " +
                            "-fx-cursor: hand;"));
            button.setOnMouseExited(e ->
                    button.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 25; " +
                            "-fx-cursor: hand;"));
        }
    }

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin keluar?");

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
}