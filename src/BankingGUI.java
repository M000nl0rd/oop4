import javax.swing.*;
import java.awt.*;

public class BankingGUI extends JFrame {
    private JTextField atmField;
    private JTextField idField;
    private JPasswordField pinField;
    private JButton loginButton;
    private JTextArea outputArea;
    private Bank bank;
    private ATM selectedATM;

    public BankingGUI(Bank bank) {
        this.bank = bank;
        setTitle("Banking System - " + bank.getName());
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Bank display panel
        JPanel bankPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bankPanel.add(new JLabel("Bank: " + bank.getName()));
        add(bankPanel, BorderLayout.SOUTH);

        // Login form: includes ATM number, account number, and PIN fields
        JPanel loginPanel = new JPanel(new GridLayout(4, 2));
        loginPanel.add(new JLabel("ATM Number:"));
        atmField = new JTextField();
        loginPanel.add(atmField);
        loginPanel.add(new JLabel("Account Number:"));
        idField = new JTextField();
        loginPanel.add(idField);
        loginPanel.add(new JLabel("PIN:"));
        pinField = new JPasswordField();
        loginPanel.add(pinField);
        loginPanel.add(new JLabel("")); // empty cell for alignment
        loginButton = new JButton("Login");
        loginPanel.add(loginButton);
        add(loginPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Login button action listener
        loginButton.addActionListener(e -> {
            int atmNumber;
            try {
                atmNumber = Integer.parseInt(atmField.getText());
            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid ATM number format!");
                return;
            }
            ATM atm = ATMDAO.getATM(atmNumber, bank.getName());
            if (atm == null) {
                outputArea.setText("ATM with that number not found!");
                return;
            }
            selectedATM = atm; // store the selected ATM

            int accountId;
            try {
                accountId = Integer.parseInt(idField.getText());
            } catch (NumberFormatException ex) {
                outputArea.setText("Invalid account number format!");
                return;
            }
            String pin = new String(pinField.getPassword());
            if (authenticate(accountId, pin)) {
                openMainMenu();
            } else {
                outputArea.setText("Invalid credentials! Please try again.");
            }
        });

        setVisible(true);
    }

    private boolean authenticate(int accountId, String pin) {
        Account account = AccountDAO.getAccount(accountId, pin, bank.getName());
        return account != null;
    }

    private void openMainMenu() {
        JFrame mainMenu = new JFrame("Main Menu - " + bank.getName());
        mainMenu.setSize(400, 300);
        mainMenu.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        mainMenu.setLayout(new GridLayout(6, 1));

        JButton balanceButton = new JButton("Check Balance");
        JButton depositButton = new JButton("Deposit Money");
        JButton withdrawButton = new JButton("Withdraw Money");
        JButton debugButton = new JButton("Debug Mode");
        JButton exitButton = new JButton("Exit");

        balanceButton.addActionListener(e -> {
            Account account = AccountDAO.getAccount(
                    Integer.parseInt(idField.getText()),
                    new String(pinField.getPassword()),
                    bank.getName()
            );
            if (account != null) {
                outputArea.setText("Account Owner: " + account.getFirstName() + " " + account.getLastName() +
                        "\nBalance: $" + account.getBalance());
            }
        });
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
        JFrame debugMenu = new JFrame("Debug Mode - " + bank.getName());
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
        int atmNumber;
        try {
            atmNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter ATM Number:"));
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid ATM number format!");
            return;
        }
        String address = JOptionPane.showInputDialog("Enter ATM Address:");
        double balance;
        try {
            balance = Double.parseDouble(JOptionPane.showInputDialog("Enter initial ATM balance:"));
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid amount format!");
            return;
        }
        ATMDAO.addATM(atmNumber, address, balance, bank.getName());
        outputArea.setText("ATM added successfully!");
    }

    private void removeATM() {
        int atmNumber;
        try {
            atmNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter ATM Number to remove:"));
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid ATM number format!");
            return;
        }
        ATMDAO.removeATM(atmNumber, bank.getName());
        outputArea.setText("ATM removed successfully!");
    }

    private void addAccount() {
        int accountId;
        try {
            accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter Account Number:"));
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid account number format!");
            return;
        }
        String pin = JOptionPane.showInputDialog("Enter PIN:");
        String firstName = JOptionPane.showInputDialog("Enter First Name:");
        String lastName = JOptionPane.showInputDialog("Enter Last Name:");
        double balance;
        try {
            balance = Double.parseDouble(JOptionPane.showInputDialog("Enter initial balance:"));
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid amount format!");
            return;
        }
        AccountDAO.addAccount(accountId, pin, balance, firstName, lastName, bank.getName());
        outputArea.setText("Account added successfully!");
    }

    private void removeAccount() {
        int accountId;
        try {
            accountId = Integer.parseInt(JOptionPane.showInputDialog("Enter Account Number to remove:"));
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid account number format!");
            return;
        }
        String pin = JOptionPane.showInputDialog("Enter PIN for verification:");
        Account account = AccountDAO.getAccount(accountId, pin, bank.getName());
        if (account != null) {
            AccountDAO.removeAccount(accountId, bank.getName());
            outputArea.setText("Account removed successfully!");
        } else {
            outputArea.setText("Invalid credentials! Account removal failed.");
        }
    }

    // Get account balance
    private double getBalance() {
        int accountId = Integer.parseInt(idField.getText());
        String pin = new String(pinField.getPassword());
        Account account = AccountDAO.getAccount(accountId, pin, bank.getName());
        return account != null ? account.getBalance() : 0.0;
    }

    // Deposit money
    private void depositAmount() {
        int accountId = Integer.parseInt(idField.getText());
        double amount;
        try {
            amount = Double.parseDouble(JOptionPane.showInputDialog("Enter deposit amount:"));
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid amount format!");
            return;
        }
        // Update the account balance
        AccountDAO.replenishAccount(accountId, amount, bank.getName());
        // Also update the ATM's balance
        ATMDAO.depositToATM(selectedATM.getAtmNumber(), amount, bank.getName());
        outputArea.setText("Deposited $" + amount + "\nCurrent account balance: $" + getBalance());
    }

    // Withdraw money
    private void withdrawAmount() {
        int accountId = Integer.parseInt(idField.getText());
        double amount;
        try {
            amount = Double.parseDouble(JOptionPane.showInputDialog("Enter withdrawal amount:"));
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid amount format!");
            return;
        }

        // Check if the ATM has enough cash
        if (selectedATM.getBalance() < amount) {
            outputArea.setText("The ATM does not have enough cash.\nWithdrawal not possible. Please wait.");
            return;
        }

        // Attempt to withdraw from the account
        boolean accountWithdrawSuccess = AccountDAO.withdrawAccount(accountId, amount, bank.getName());
        if (!accountWithdrawSuccess) {
            outputArea.setText("Insufficient funds in account or error during withdrawal.");
            return;
        }

        // Update the ATM balance
        boolean atmWithdrawSuccess = ATMDAO.withdrawFromATM(selectedATM.getAtmNumber(), amount, bank.getName());
        if (!atmWithdrawSuccess) {
            outputArea.setText("Error updating ATM balance. Please try again later.");
            return;
        }
        // Update the selected ATM object's balance
        selectedATM.setBalance(selectedATM.getBalance() - amount);

        outputArea.setText("Withdrew $" + amount + "\nNew account balance: $" + getBalance());
    }

    public static void main(String[] args) {
        // Bank selection
        String[] bankNames = {"Halyk", "Kaspi"};
        String bankName = (String) JOptionPane.showInputDialog(
                null,
                "Select a bank:",
                "Bank Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                bankNames,
                bankNames[0]
        );
        if (bankName == null || bankName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No bank selected. Exiting program.");
            System.exit(0);
        }
        // Create a Bank object using the selected bank name
        Bank selectedBank = new Bank(bankName);
        SwingUtilities.invokeLater(() -> new BankingGUI(selectedBank));
    }
}
