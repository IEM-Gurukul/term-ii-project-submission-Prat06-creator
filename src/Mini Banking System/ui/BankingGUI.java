package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import factory.AccountFactory;
import model.Account;
import repository.AccountRepository;
import service.BankingService;

public class BankingGUI extends Application {

    private BankingService service;

    @Override
    public void start(Stage stage) {

        AccountRepository repo = new AccountRepository();
        service = new BankingService(repo);

        // Input Fields
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("SAVINGS", "CURRENT");
        typeBox.setPromptText("Account Type");

        TextField idField = new TextField();
        idField.setPromptText("Account ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        TextField fromIdField = new TextField();
        fromIdField.setPromptText("From Account ID");

        TextField toIdField = new TextField();
        toIdField.setPromptText("To Account ID");

        TextArea output = new TextArea();
        output.setEditable(false);

        // Buttons
        Button createBtn = new Button("Create Account");
        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button transferBtn = new Button("Transfer");
        Button balanceBtn = new Button("Check Balance");

        // Create Account
        createBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String type = typeBox.getValue();

                Account acc = AccountFactory.createAccount(type, id, name, amount);
                service.createAccount(acc);

                output.setText("✅ Account Created Successfully");

            } catch (Exception ex) {
                output.setText("❌ Error: " + ex.getMessage());
            }
        });

        // Deposit
        depositBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                double amount = Double.parseDouble(amountField.getText());

                service.deposit(id, amount);

                output.setText("✅ Deposit Successful");

            } catch (Exception ex) {
                output.setText("❌ Error: " + ex.getMessage());
            }
        });

        // Withdraw
        withdrawBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                double amount = Double.parseDouble(amountField.getText());

                service.withdraw(id, amount);

                output.setText("✅ Withdraw Successful");

            } catch (Exception ex) {
                output.setText("❌ Error: " + ex.getMessage());
            }
        });

        // Transfer
        transferBtn.setOnAction(e -> {
            try {
                int from = Integer.parseInt(fromIdField.getText());
                int to = Integer.parseInt(toIdField.getText());
                double amount = Double.parseDouble(amountField.getText());

                service.transfer(from, to, amount);

                output.setText("✅ Transfer Successful");

            } catch (Exception ex) {
                output.setText("❌ Error: " + ex.getMessage());
            }
        });

        // Check Balance
        balanceBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                double balance = service.getBalance(id);

                output.setText("💰 Balance: " + balance);

            } catch (Exception ex) {
                output.setText("❌ Error: " + ex.getMessage());
            }
        });

        // Layout
        VBox root = new VBox(10,
                new Label("Mini Banking System"),
                typeBox,
                idField,
                nameField,
                amountField,
                fromIdField,
                toIdField,
                createBtn,
                depositBtn,
                withdrawBtn,
                transferBtn,
                balanceBtn,
                output
        );

        Scene scene = new Scene(root, 400, 550);

        stage.setTitle("Mini Banking System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}