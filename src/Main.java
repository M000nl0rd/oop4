import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome! Please enter your bank name:");
        Bank bank = new Bank(scanner.nextLine());
        System.out.printf("Welcome to %s bank!%n", bank.getName());
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Balance");
            System.out.println("2. Replenish amount");
            System.out.println("3. Withdraw amount");
            System.out.println("4. Debugging mode");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: { //balance
                    System.out.print("Enter your ID: ");
                    int number = scanner.nextInt();
                    System.out.print("Enter your PIN: ");
                    String pin = getValidPin(scanner);
                    displayBalance(number, pin);
                    break;
                }
                case 2: { //replenish
                    System.out.print("Enter account number: ");
                    int number = scanner.nextInt();
                    String pin = getValidPin(scanner);
                    System.out.print("Enter amount to replenish: ");
                    double amount = scanner.nextDouble();

                    AccountDAO.replenishAccount(number, amount);
                    displayBalance(number, pin);
                    break;
                }
                case 3: { //withdraw
                    System.out.print("Enter account number: ");
                    int number = scanner.nextInt();
                    String pin = getValidPin(scanner);
                    System.out.print("Enter amount to withdraw: ");
                    double amount = scanner.nextDouble();

                    AccountDAO.withdrawAccount(number, amount);
                    displayBalance(number, pin);
                    break;
                }
                case 4: { //debug
                    boolean inDebugMode = true;
                    while (inDebugMode){
                        System.out.println("\nMenu:");
                        System.out.println("1. Add ATM");
                        System.out.println("2. Remove ATM");
                        System.out.println("3. Add account");
                        System.out.println("4. Remove account");
                        System.out.println("5. Exit");
                        System.out.print("Enter your choice: ");
                        int debug_choice = scanner.nextInt();
                        switch (debug_choice) {
                            case 1: { // add atm
                                System.out.print("Enter ATM's identification number: ");
                                int atmNumber = scanner.nextInt();
                                System.out.print("Enter address: ");
                                scanner.nextLine();
                                String address = scanner.nextLine();

                                ATMDAO.addATM(atmNumber, address);
                                break;
                            }
                            case 2: { // remove atm
                                System.out.print("Enter ATM's identification number to remove: ");
                                int atmNumber = scanner.nextInt();
                                ATMDAO.removeATM(atmNumber);
                                break;
                            }
                            case 3: { // add account
                                System.out.print("Enter account number: ");
                                int number = scanner.nextInt();
                                String pin = getValidPin(scanner); // Используем метод для получения валидного PIN-кода
                                System.out.print("Enter initial balance: ");
                                double balance = scanner.nextDouble();
                                AccountDAO.addAccount(number, pin, balance);
                                break;
                            }
                            case 4: { //remove account
                                System.out.print("Enter account number to remove: ");
                                int number = scanner.nextInt();
                                String pin = getValidPin(scanner);
                                AccountDAO.removeAccount(number);
                                break;
                            }
                            case 5: { // Exit Debug Mode
                                System.out.println("Exiting Debugging Mode...");
                                inDebugMode = false;
                                break;
                            }
                            default:
                                System.out.println("Invalid choice! Please try again.");
                        }
                    }
                    break;
                }
                case 5: { //exit
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                }
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    public static String getValidPin(Scanner scanner) {
        String pin;
        while (true) {
            System.out.print("Enter PIN (4 digits): ");
            if (scanner.hasNext("\\d{4}")) { // Проверяем, является ли ввод 4-значным числом
                pin = scanner.next();
                return pin;
            } else {
                System.out.println("Invalid PIN! Please enter exactly 4 digits.");
                scanner.next(); // Чистим неверный ввод
            }
        }
    }

    public static void displayBalance(int number, String pin) {
        Account account = AccountDAO.getAccount(number, pin);
        if (account != null) {
            System.out.printf("Your balance: $%.2f%n", account.getRemainder());
        } else {
            System.out.println("Error retrieving account balance.");
        }
    }

}