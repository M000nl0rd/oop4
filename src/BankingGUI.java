import javax.swing.*;
import java.awt.*;


public class BankingGUI extends JFrame {
    private JTextField idField;
    private JPasswordField pinField;
    private JButton loginButton;
    private JTextArea outputArea;
    private boolean debugMode = false;

    public BankingGUI() {
        setTitle("Banking System");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.add(new JLabel("Account ID:"));
        idField = new JTextField();
        loginPanel.add(idField);
        loginPanel.add(new JLabel("PIN:"));
        pinField = new JPasswordField();
        loginPanel.add(pinField);
        loginButton = new JButton("Login");
        loginPanel.add(loginButton);
        add(loginPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            int accountId = Integer.parseInt(idField.getText());
            String pin = new String(pinField.getPassword());
            if (authenticate(accountId, pin)) {
                openMainMenu();
            } else {
                outputArea.setText("Invalid credentials! Try again.");
            }
        });

        setVisible(true);
    }

    private boolean authenticate(int accountId, String pin) {
        Account account = AccountDAO.getAccount(accountId, pin);
        return account != null;
    }

    private void openMainMenu() {
        JFrame mainMenu = new JFrame("Main Menu");
        mainMenu.setSize(400, 300);
        mainMenu.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainMenu.setLayout(new GridLayout(6, 1));

        JButton balanceButton = new JButton("Check Balance");
        JButton depositButton = new JButton("Deposit Money");
        JButton withdrawButton = new JButton("Withdraw Money");
        JButton debugButton = new JButton("Debug Mode");
        JButton exitButton = new JButton("Exit");

        balanceButton.addActionListener(e -> outputArea.setText("Balance: $" + getBalance()));
        depositButton.addActionListener(e -> depositAmount());
        withdrawButton.addActionListener(e -> withdrawAmount());
        debugButton.addActionListener(e -> openDebugMode());
        exitButton.addActionListener(e -> mainMenu.dispose());

        mainMenu.add(balanceButton);
        mainMenu.add(depositButton);
        mainMenu.add(withdrawButton);
        mainMenu.add(debugButton);
        mainMenu.add(exitButton);
        mainMenu.setVisible(true);
    }

    private void openDebugMode() {
        JFrame debugMenu = new JFrame("Debug Mode");
        debugMenu.setSize(400, 300);
        debugMenu.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        debugMenu.setLayout(new GridLayout(7, 1));

        JButton addATMButton = new JButton("Add ATM");
        JButton removeATMButton = new JButton("Remove ATM");
        JButton addAccountButton = new JButton("Add Account");
        JButton removeAccountButton = new JButton("Remove Account");
        JButton exitButton = new JButton("Exit Debug Mode");

        addATMButton.addActionListener(e -> addATM());
        removeATMButton.addActionListener(e -> removeATM());
        addAccountButton.addActionListener(e -> addAccount());
        removeAccountButton.addActionListener(e -> removeAccount());
        exitButton.addActionListener(e -> debugMenu.dispose());

        debugMenu.add(addATMButton);
        debugMenu.add(removeATMButton);
        debugMenu.add(addAccountButton);
        debugMenu.add(removeAccountButton);
        debugMenu.add(exitButton);
        debugMenu.setVisible(true);
    }

    private void addATM() {
        int atmNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter ATM Number:"));
        String address = JOptionPane.showInputDialog("Enter ATM Address:");
        ATMDAO.addATM(atmNumber, address);
        outputArea.setText("ATM added successfully!");
    }

    private void removeATM() {
        int atmNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter ATM Number to Remove:"));
        ATMDAO.removeATM(atmNumber);
        outputArea.setText("ATM removed successfully!");
    }

    private void addAccount() {
        int accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter Account ID:"));
        String pin = JOptionPane.showInputDialog("Enter PIN:");
        double balance = Double.parseDouble(JOptionPane.showInputDialog("Enter Initial Balance:"));
        AccountDAO.addAccount(accountId, pin, balance);
        outputArea.setText("Account added successfully!");
    }

    private void removeAccount() {
        int accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter Account ID to Remove:"));
        String pin = JOptionPane.showInputDialog("Enter PIN for verification:");
        Account account = AccountDAO.getAccount(accountId, pin);
        if (account != null) {
            AccountDAO.removeAccount(accountId);
            outputArea.setText("Account removed successfully!");
        } else {
            outputArea.setText("Invalid credentials! Account removal failed.");
        }
    }

    private double getBalance() {
        int accountId = Integer.parseInt(idField.getText());
        String pin = new String(pinField.getPassword());
        Account account = AccountDAO.getAccount(accountId, pin);
        return account != null ? account.getRemainder() : 0.0;
    }

    private void depositAmount() {
        int accountId = Integer.parseInt(idField.getText());
        double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount to deposit:"));
        AccountDAO.replenishAccount(accountId, amount);
        outputArea.setText("Deposited $" + amount + "\nCurrent Balance: $" + getBalance());
    }

    private void withdrawAmount() {
        int accountId = Integer.parseInt(idField.getText());
        double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount to withdraw:"));
        AccountDAO.withdrawAccount(accountId, amount);
        outputArea.setText("Withdrawn $" + amount + "\nCurrent Balance: $" + getBalance());
    }

    public static void main(String[] args) {
        new BankingGUI();
    }
}
