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
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    private TableView<Expense> table;
    private ObservableList<Expense> expenses;
    private Label totalLabel;

    @Override
    public void start(Stage primaryStage) {
        // Input fields
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category (Food, Travel, etc)");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        Button addButton = new Button("Add Expense");
        addButton.setOnAction(e -> addExpense(categoryField, descriptionField, amountField));

        HBox inputBox = new HBox(10, categoryField, descriptionField, amountField, addButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));

        // Table
        table = new TableView<>();
        expenses = FXCollections.observableArrayList();
        table.setItems(expenses);

        TableColumn<Expense, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> data.getValue().categoryProperty());

        TableColumn<Expense, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(data -> data.getValue().descriptionProperty());

        TableColumn<Expense, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(data -> data.getValue().amountProperty().asObject());

        table.getColumns().addAll(categoryCol, descriptionCol, amountCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Total label
        totalLabel = new Label("Total: ₹0.0");
        Button clearButton = new Button("Clear All");
        clearButton.setOnAction(e -> clearExpenses());

        HBox bottomBox = new HBox(15, totalLabel, clearButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10));

        // Main layout
        VBox root = new VBox(10, inputBox, table, bottomBox);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 700, 400);
        primaryStage.setTitle("Expense Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addExpense(TextField category, TextField description, TextField amount) {
        try {
            String cat = category.getText().trim();
            String desc = description.getText().trim();
            double amt = Double.parseDouble(amount.getText().trim());

            if (cat.isEmpty() || desc.isEmpty()) {
                showAlert("Please fill all fields!");
                return;
            }

            Expense expense = new Expense(cat, desc, amt);
            expenses.add(expense);
            updateTotal();

            category.clear();
            description.clear();
            amount.clear();
        } catch (NumberFormatException e) {
            showAlert("Amount must be a valid number!");
        }
    }

    private void clearExpenses() {
        expenses.clear();
        updateTotal();
    }

    private void updateTotal() {
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        totalLabel.setText("Total: ₹" + total);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
