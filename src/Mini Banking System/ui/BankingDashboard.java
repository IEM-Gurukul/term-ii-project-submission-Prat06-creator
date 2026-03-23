package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import factory.AccountFactory;
import model.Account;
import model.Transaction;
import repository.AccountRepository;
import service.BankingService;

import java.util.List;

public class BankingDashboard extends Application {

    private BankingService service;
    private TextArea logArea;

    // ===== COLOUR PALETTE =====
    private static final String PRIMARY_BG      = "#c5d0eb";
    private static final String CARD_BG         = "#1E293B";
    private static final String CARD_BORDER     = "#334155";
    private static final String ACCENT_BLUE     = "#3B82F6";
    private static final String ACCENT_GREEN    = "#10B981";
    private static final String ACCENT_AMBER    = "#F59E0B";
    private static final String ACCENT_RED      = "#EF4444";
    private static final String ACCENT_PURPLE   = "#8B5CF6";
    private static final String TEXT_PRIMARY    = "#F1F5F9";
    private static final String TEXT_SECONDARY  = "#94A3B8";
    private static final String INPUT_BG        = "#0F172A";
    private static final String INPUT_BORDER    = "#475569";

    @Override
    public void start(Stage stage) {

        AccountRepository repo = new AccountRepository();
        service = new BankingService(repo);

        // ===== INPUT FIELDS =====
        TextField idField     = styledField("Account ID");
        TextField nameField   = styledField("Full Name");
        TextField amountField = styledField("Amount (₹)");
        TextField fromField   = styledField("From Account ID");
        TextField toField     = styledField("To Account ID");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("SAVINGS", "CURRENT");
        typeBox.setPromptText("Select Account Type");
        typeBox.setMaxWidth(Double.MAX_VALUE);
        typeBox.setStyle(
            "-fx-background-color: " + INPUT_BG + ";" +
            "-fx-border-color: " + INPUT_BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-prompt-text-fill: " + TEXT_SECONDARY + ";" +
            "-fx-padding: 8 12;" +
            "-fx-pref-height: 38;"
        );

        // ===== LOG AREA =====
        logArea = new TextArea();
        logArea.setPrefHeight(180);
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle(
            "-fx-control-inner-background: #0D1117;" +
            "-fx-background-color: #0D1117;" +
            "-fx-text-fill: #E2E8F0;" +
            "-fx-font-family: 'Consolas';" +
            "-fx-font-size: 13;" +
            "-fx-border-color: " + CARD_BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10;"
        );

        // ===== BUTTONS =====
        Button createBtn   = styledButton("＋  Create",    ACCENT_BLUE,   "#2563EB");
        Button depositBtn  = styledButton("↓  Deposit",   ACCENT_GREEN,  "#059669");
        Button withdrawBtn = styledButton("↑  Withdraw",  ACCENT_AMBER,  "#D97706");
        Button transferBtn = styledButton("⇄  Transfer",  ACCENT_PURPLE, "#7C3AED");
        Button balanceBtn  = styledButton("◎  Balance",   ACCENT_RED,    "#DC2626");

        // ===== ACTIONS (unchanged logic) =====

        createBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String type = typeBox.getValue();

                Account acc = AccountFactory.createAccount(type, id, name, amount);
                service.createAccount(acc);

                log("✔  ACCOUNT CREATED");
                log("    ID: " + id + "  |  Type: " + type + "  |  Opening Balance: ₹" + String.format("%.2f", amount));
                logDivider();

                clearFields(idField, nameField, amountField);

            } catch (Exception ex) {
                logError(ex.getMessage());
            }
        });

        depositBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                double amount = Double.parseDouble(amountField.getText());

                service.deposit(id, amount);

                log("↓  DEPOSIT SUCCESSFUL");
                log("    Account ID: " + id + "  |  Amount Credited: ₹" + String.format("%.2f", amount));
                showTransactions(id);

                clearFields(amountField);

            } catch (Exception ex) {
                logError(ex.getMessage());
            }
        });

        withdrawBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                double amount = Double.parseDouble(amountField.getText());

                service.withdraw(id, amount);

                log("↑  WITHDRAWAL SUCCESSFUL");
                log("    Account ID: " + id + "  |  Amount Debited: ₹" + String.format("%.2f", amount));
                showTransactions(id);

                clearFields(amountField);

            } catch (Exception ex) {
                logError(ex.getMessage());
            }
        });

        transferBtn.setOnAction(e -> {
            try {
                int from = Integer.parseInt(fromField.getText());
                int to   = Integer.parseInt(toField.getText());
                double amount = Double.parseDouble(amountField.getText());

                service.transfer(from, to, amount);

                log("⇄  TRANSFER SUCCESSFUL");
                log("    From ID: " + from + "  →  To ID: " + to + "  |  Amount: ₹" + String.format("%.2f", amount));
                showTransactions(from);
                showTransactions(to);

                clearFields(fromField, toField, amountField);

            } catch (Exception ex) {
                logError(ex.getMessage());
            }
        });

        balanceBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                double balance = service.getBalance(id);

                log("◎  BALANCE ENQUIRY");
                log("    Account ID: " + id + "  |  Available Balance: ₹" + String.format("%.2f", balance));
                logDivider();

            } catch (Exception ex) {
                logError(ex.getMessage());
            }
        });

        // ===== HEADER =====
        HBox header = buildHeader();

        // ===== LEFT PANEL — Account Details =====
        VBox leftCard = new VBox(10);
        leftCard.getChildren().add(sectionLabel("ACCOUNT DETAILS"));
        leftCard.getChildren().addAll(typeBox, idField, nameField, amountField);
        styleCard(leftCard);
        HBox.setHgrow(leftCard, Priority.ALWAYS);

        // ===== RIGHT PANEL — Transfer Details =====
        VBox rightCard = new VBox(10);
        rightCard.getChildren().add(sectionLabel("TRANSFER DETAILS"));
        Label transferNote = new Label("Enter From/To IDs below, and set Amount in Account Details.");
        transferNote.setWrapText(true);
        transferNote.setStyle(
            "-fx-text-fill: " + TEXT_SECONDARY + ";" +
            "-fx-font-size: 11;"
        );
        rightCard.getChildren().addAll(fromField, toField, transferNote);
        styleCard(rightCard);
        HBox.setHgrow(rightCard, Priority.ALWAYS);

        // ===== FIELD ROW =====
        HBox fieldRow = new HBox(14, leftCard, rightCard);
        fieldRow.setFillHeight(true);

        // ===== QUICK ACTIONS CARD =====
        HBox buttons = new HBox(10, createBtn, depositBtn, withdrawBtn, transferBtn, balanceBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);
        VBox actionCard = new VBox(10, sectionLabel("QUICK ACTIONS"), buttons);
        styleCard(actionCard);

        // ===== LOG CARD =====
        VBox logCard = new VBox(10, sectionLabel("ACTIVITY LOG & TRANSACTION HISTORY"), logArea);
        styleCard(logCard);

        // ===== ROOT =====
        VBox content = new VBox(14, fieldRow, actionCard, logCard);
        content.setPadding(new Insets(18));

        VBox root = new VBox(0, header, content);
        root.setStyle("-fx-background-color: " + PRIMARY_BG + ";");

        Scene scene = new Scene(root, 900, 660);
        stage.setTitle("NexBank — Banking System");
        stage.setScene(scene);
        stage.show();
    }

    // ===== HEADER =====
    private HBox buildHeader() {
        StackPane logoIcon = new StackPane();
        Circle circle = new Circle(22);
        circle.setFill(Color.web(ACCENT_BLUE));
        Text n = new Text("N");
        n.setFill(Color.WHITE);
        n.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        logoIcon.getChildren().addAll(circle, n);

        VBox brandText = new VBox(2);
        Text bankName = new Text("NexBank");
        bankName.setFill(Color.web(TEXT_PRIMARY));
        bankName.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        Text tagline = new Text("Secure. Smart. Seamless.");
        tagline.setFill(Color.web(TEXT_SECONDARY));
        tagline.setFont(Font.font("Arial", 11));
        brandText.getChildren().addAll(bankName, tagline);

        HBox brand = new HBox(12, logoIcon, brandText);
        brand.setAlignment(Pos.CENTER_LEFT);

        HBox pill = new HBox(6);
        pill.setAlignment(Pos.CENTER);
        pill.setStyle(
            "-fx-background-color: #14532D;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 6 16;"
        );
        Circle dot = new Circle(5, Color.web(ACCENT_GREEN));
        Text status = new Text("System Online");
        status.setFill(Color.web("#86EFAC"));
        status.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        pill.getChildren().addAll(dot, status);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(0, brand, spacer, pill);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 20, 16, 20));
        header.setStyle(
            "-fx-background-color: #1E293B;" +
            "-fx-border-color: " + CARD_BORDER + ";" +
            "-fx-border-width: 0 0 1 0;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#000000", 0.4));
        shadow.setRadius(10);
        shadow.setOffsetY(3);
        header.setEffect(shadow);

        return header;
    }

    // ===== STYLED INPUT FIELD =====
    private TextField styledField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setMaxWidth(Double.MAX_VALUE);
        String base =
            "-fx-background-color: " + INPUT_BG + ";" +
            "-fx-border-color: " + INPUT_BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-prompt-text-fill: " + TEXT_SECONDARY + ";" +
            "-fx-padding: 8 12;" +
            "-fx-pref-height: 38;" +
            "-fx-font-size: 13;";
        f.setStyle(base);
        f.focusedProperty().addListener((obs, old, focused) -> {
            if (focused) {
                f.setStyle(base.replace(INPUT_BORDER, ACCENT_BLUE));
            } else {
                f.setStyle(base);
            }
        });
        return f;
    }

    // ===== STYLED BUTTON =====
    private Button styledButton(String label, String bgColor, String hoverColor) {
        Button btn = new Button(label);
        String base =
            "-fx-background-color: " + bgColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 20;" +
            "-fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(base.replace(bgColor, hoverColor)));
        btn.setOnMouseExited(e  -> btn.setStyle(base));

        DropShadow glow = new DropShadow();
        glow.setColor(Color.web(bgColor, 0.45));
        glow.setRadius(12);
        btn.setEffect(glow);

        return btn;
    }

    // ===== CARD STYLE =====
    private void styleCard(VBox card) {
        card.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-border-color: " + CARD_BORDER + ";" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 16;"
        );
    }

    // ===== SECTION LABEL =====
    private Label sectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle(
            "-fx-text-fill: " + TEXT_SECONDARY + ";" +
            "-fx-font-size: 10;" +
            "-fx-font-weight: bold;"
        );
        return lbl;
    }

    // ===== LOG HELPERS =====

    private void log(String message) {
        logArea.appendText(message + "\n");
    }

    private void logError(String message) {
        logArea.appendText("✘  ERROR: " + (message != null ? message.toUpperCase() : "UNKNOWN") + "\n");
        logDivider();
    }

    private void logDivider() {
        logArea.appendText("    ─────────────────────────────────────────\n");
    }

    private void clearFields(TextField... fields) {
        for (TextField f : fields) {
            f.clear();
        }
    }

    private void showTransactions(int id) {
        try {
            List<Transaction> list = service.getTransactions(id);

            log("    ┌─ Transaction History  [Account ID: " + id + "]");
            if (list.isEmpty()) {
                log("    │  No transactions found.");
            } else {
                for (Transaction t : list) {
                    log("    │  " + t.toString());
                }
            }
            log("    └─────────────────────────────────────────");

        } catch (Exception e) {
            log("    ✘  Could not fetch transactions for ID: " + id);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}