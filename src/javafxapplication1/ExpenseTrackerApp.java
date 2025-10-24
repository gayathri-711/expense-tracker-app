/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication1;

/**
 *
 * @author BEST SOLUTION SALEM
 */



import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class ExpenseTrackerApp extends Application {

    private ObservableList<Expense> expenses;
    private TableView<Expense> table;
    private Label totalLabel;
    private String currentUser;
    private final File userFile = new File("users.txt");
    private final File dataDir = new File("data"); // directory for user expenses

    @Override
    public void start(Stage stage) {
        if (!dataDir.exists()) dataDir.mkdir();
        showWelcomePage(stage);
    }

    // ------------------- WELCOME PAGE -------------------
    private void showWelcomePage(Stage stage) {
        Label title = new Label("Welcome to Expense Tracker");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button signInBtn = new Button("Sign In");
        Button signUpBtn = new Button("Sign Up");
        signInBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        signUpBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        signInBtn.setOnAction(e -> showLoginPage(stage));
        signUpBtn.setOnAction(e -> showRegisterPage(stage));

        VBox layout = new VBox(15, title, signInBtn, signUpBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f0f8ff;");

        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
    }

    // ------------------- SIGN UP PAGE -------------------
    private void showRegisterPage(Stage stage) {
        Label title = new Label("Sign Up");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Create Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create Password");

        Button registerBtn = new Button("Register");
        registerBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        Label messageLabel = new Label();

        registerBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("All fields required!");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            if (saveUser(username, password)) {
                messageLabel.setText("Registration successful! Please sign in.");
                messageLabel.setStyle("-fx-text-fill: green;");
            } else {
                messageLabel.setText("User already exists!");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showWelcomePage(stage));

        VBox layout = new VBox(10, title, usernameField, passwordField, registerBtn, messageLabel, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f0f8ff;");

        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Sign Up");
        stage.setScene(scene);
    }

    // ------------------- LOGIN PAGE -------------------
    private void showLoginPage(Stage stage) {
        Label title = new Label("Sign In");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        Label messageLabel = new Label();

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            if (validateUser(username, password)) {
                currentUser = username;
                loadExpenses(); // load user expenses from file
                showExpenseTracker(stage, username);
            } else {
                messageLabel.setText("Invalid credentials!");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> showWelcomePage(stage));

        VBox layout = new VBox(10, title, usernameField, passwordField, loginBtn, messageLabel, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f0f8ff;");

        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Sign In");
        stage.setScene(scene);
    }

    // ------------------- EXPENSE TRACKER -------------------
    private void showExpenseTracker(Stage stage, String username) {
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        Button addBtn = new Button("Add Expense");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        table = new TableView<>();
        table.setItems(expenses);

        TableColumn<Expense, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(d -> d.getValue().categoryProperty());
        TableColumn<Expense, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(d -> d.getValue().descriptionProperty());
        TableColumn<Expense, Double> amtCol = new TableColumn<>("Amount");
        amtCol.setCellValueFactory(d -> d.getValue().amountProperty().asObject());
        table.getColumns().setAll(catCol, descCol, amtCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        addBtn.setOnAction(e -> {
            try {
                String cat = categoryField.getText();
                String desc = descriptionField.getText();
                double amt = Double.parseDouble(amountField.getText());
                if (cat.isEmpty() || desc.isEmpty()) {
                    showAlert("Please fill all fields!");
                    return;
                }
                Expense exp = new Expense(cat, desc, amt);
                expenses.add(exp);
                saveExpense(exp); // save to file
                updateTotal();
                categoryField.clear();
                descriptionField.clear();
                amountField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Invalid amount!");
            }
        });

        totalLabel = new Label("Total: ₹" + expenses.stream().mapToDouble(Expense::getAmount).sum());

        Button clearBtn = new Button("Clear All");
        clearBtn.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-weight: bold;");
        clearBtn.setOnAction(e -> {
            expenses.clear();
            clearExpenseFile();
            updateTotal();
        });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutBtn.setOnAction(e -> showWelcomePage(stage));

        HBox inputBox = new HBox(10, categoryField, descriptionField, amountField, addBtn);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));

        HBox bottomBox = new HBox(15, totalLabel, clearBtn, logoutBtn);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10));

        VBox mainLayout = new VBox(10, new Label("Welcome, " + username + "!"), inputBox, table, bottomBox);
        mainLayout.setPadding(new Insets(10));

        Scene scene = new Scene(mainLayout, 700, 400);
        stage.setTitle("Expense Tracker");
        stage.setScene(scene);
    }

    // ------------------- USER FILE HANDLING -------------------
    private boolean saveUser(String username, String password) {
        try {
            if (!userFile.exists()) userFile.createNewFile();
            List<String> users = readUsers();
            for (String u : users) if (u.split(":")[0].equals(username)) return false;
            try (FileWriter fw = new FileWriter(userFile, true)) {
                fw.write(username + ":" + password + "\n");
            }
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    private boolean validateUser(String username, String password) {
        List<String> users = readUsers();
        for (String u : users) {
            String[] parts = u.split(":");
            if (parts[0].equals(username) && parts[1].equals(password)) return true;
        }
        return false;
    }

    private List<String> readUsers() {
        List<String> users = new ArrayList<>();
        if (!userFile.exists()) return users;
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) users.add(line);
        } catch (IOException e) { e.printStackTrace(); }
        return users;
    }

    // ------------------- EXPENSE FILE HANDLING -------------------
    private File getExpenseFile() {
        return new File(dataDir, currentUser + "_expenses.txt");
    }

    private void saveExpense(Expense exp) {
        try (FileWriter fw = new FileWriter(getExpenseFile(), true)) {
            fw.write(exp.getCategory() + ":" + exp.getDescription() + ":" + exp.getAmount() + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadExpenses() {
        expenses = FXCollections.observableArrayList();
        File file = getExpenseFile();
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) expenses.add(new Expense(parts[0], parts[1], Double.parseDouble(parts[2])));
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void clearExpenseFile() {
        File file = getExpenseFile();
        if (file.exists()) file.delete();
    }

    // ------------------- ALERT & TOTAL -------------------
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void updateTotal() {
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        totalLabel.setText("Total: ₹" + total);
    }

    // ------------------- EXPENSE CLASS -------------------
    public static class Expense {
        private final StringProperty category;
        private final StringProperty description;
        private final DoubleProperty amount;

        public Expense(String category, String description, double amount) {
            this.category = new SimpleStringProperty(category);
            this.description = new SimpleStringProperty(description);
            this.amount = new SimpleDoubleProperty(amount);
        }

        public StringProperty categoryProperty() { return category; }
        public StringProperty descriptionProperty() { return description; }
        public DoubleProperty amountProperty() { return amount; }

        public String getCategory() { return category.get(); }
        public String getDescription() { return description.get(); }
        public double getAmount() { return amount.get(); }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


