import javafx.application.Application;
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

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(0);

        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(40, 20, 40, 20));
        header.setStyle("-fx-background-color: #1a237e;");

        StackPane logoContainer = new StackPane();
        Circle circle = new Circle(80);
        circle.setFill(Color.web("#f4e4a3"));

        ImageView logo = null;
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/resources/logo.png"));
            logo = new ImageView(logoImage);
            logo.setFitWidth(170);
            logo.setFitHeight(170);
            logo.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Logo tidak ditemukan: " + e.getMessage());
        }

        logoContainer.getChildren().add(circle);
        if (logo != null) {
            logoContainer.getChildren().add(logo);
        }

        Label title = new Label("GoSport Book");
        title.setFont(Font.font("Elephant", FontWeight.BOLD, 60));
        title.setTextFill(Color.web("#ff8c5a"));
        title.setStyle("-fx-font-style: italic;");

        header.getChildren().addAll(logoContainer, title);

        VBox loginSection = new VBox(20);
        loginSection.setAlignment(Pos.TOP_CENTER);
        loginSection.setPadding(new Insets(50, 60, 50, 60));
        loginSection.setStyle("-fx-background-color: #f5f5f5;");

        Label loginTitle = new Label("Login");
        loginTitle.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        loginTitle.setTextFill(Color.web("#b0b0b0"));

        Label subtitle = new Label("Login dulu ya baru bisa booking ><");
        subtitle.setFont(Font.font("Arial", 16));
        subtitle.setTextFill(Color.web("#b0b0b0"));

        VBox usernameBox = new VBox(10);
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        usernameLabel.setTextFill(Color.web("#b0b0b0"));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Masukkan Username anda");
        usernameField.setPrefHeight(50);
        usernameField.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; " +
                "-fx-font-size: 14px; -fx-padding: 10;");

        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        VBox passwordBox = new VBox(10);
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        passwordLabel.setTextFill(Color.web("#b0b0b0"));

        HBox passwordContainer = new HBox();
        passwordContainer.setAlignment(Pos.CENTER_LEFT);
        passwordContainer.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 10; -fx-background-radius: 10;");

        PasswordField passwordField = new PasswordField();
        TextField passwordTextField = new TextField();
        passwordField.setPromptText("Masukkan pasword anda");
        passwordTextField.setPromptText("Masukkan pasword anda");

        String passwordStyle = "-fx-background-color: transparent; -fx-border-color: transparent; " +
                "-fx-font-size: 14px; -fx-padding: 10;";
        passwordField.setStyle(passwordStyle);
        passwordTextField.setStyle(passwordStyle);
        passwordField.setPrefHeight(50);
        passwordTextField.setPrefHeight(50);

        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        passwordTextField.setVisible(false);
        passwordTextField.setManaged(false);

        StackPane passwordStack = new StackPane();
        passwordStack.getChildren().addAll(passwordField, passwordTextField);
        HBox.setHgrow(passwordStack, Priority.ALWAYS);

        Button toggleButton = new Button("👁");
        toggleButton.setStyle("-fx-background-color: transparent; -fx-font-size: 20px; " +
                "-fx-cursor: hand; -fx-padding: 10;");
        toggleButton.setOnAction(e -> {
            if (passwordField.isVisible()) {
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                passwordTextField.setVisible(true);
                passwordTextField.setManaged(true);
                toggleButton.setText("👁");
            } else {
                passwordTextField.setVisible(false);
                passwordTextField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                toggleButton.setText("👁‍🗨");
            }
        });

        passwordContainer.getChildren().addAll(passwordStack, toggleButton);
        passwordBox.getChildren().addAll(passwordLabel, passwordContainer);

        Button loginButton = new Button("Log In");
        loginButton.setPrefWidth(700);
        loginButton.setPrefHeight(60);
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        loginButton.setTextFill(Color.WHITE);
        loginButton.setStyle("-fx-background-color: #1a237e; -fx-background-radius: 15; " +
                "-fx-cursor: hand;");

        loginButton.setOnMouseEntered(e ->
                loginButton.setStyle("-fx-background-color: #0d1654; -fx-background-radius: 15; " +
                        "-fx-cursor: hand;"));
        loginButton.setOnMouseExited(e ->
                loginButton.setStyle("-fx-background-color: #1a237e; -fx-background-radius: 15; " +
                        "-fx-cursor: hand;"));

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.equals("admin") && password.equals("admin123")) {
                AdminApp adminApp = new AdminApp();
                primaryStage.setScene(adminApp.createScene(primaryStage));
            }

            else if (username.equals("naurahmufidah") && password.equals("naurah123")) {
                DashboardApp dashboard = new DashboardApp(username);
                primaryStage.setScene(dashboard.createScene(primaryStage));
            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Gagal");
                alert.setHeaderText(null);
                alert.setContentText("Username atau password salah!");
                alert.showAndWait();
            }
        });

        loginSection.getChildren().addAll(loginTitle, subtitle, usernameBox, passwordBox, loginButton);

        root.getChildren().addAll(header, loginSection);

        Scene scene = new Scene(root, 900, 800);

        primaryStage.setTitle("GoSport Book - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}