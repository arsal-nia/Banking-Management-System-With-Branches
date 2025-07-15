import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BankingManagementSystem {

    static Scanner sc = new Scanner(System.in);

    // Revenue Variable:
    static int totalRevenue = 0;
    static String selectedBranch = "";

    // THESE ARRAYS ARE USED TO STORE ADMIN ARRAY
    static String[] adminUsername = new String[100];
    static String[] adminPassword = new String[100];
    static String[] adminFullName = new String[100];
    static String[] adminPhone = new String[100];
    static String[] adminCnic = new String[100];
    static String[] adminGender = new String[100];
    static String[] adminAddress = new String[100];
    static int[] adminAge = new int[100];
    static String[] adminPosition = new String[100];
    static String loggedInAdmin = null;
    // THE VARIABLE WHICH STORES THE NUMBER OF ADMINS
    static int adminCount = 0;

    // ARRAYS USED TO STORE CUSTOMER DATA
    static String[] customerFullName = new String[100];
    static String[] customerUsername = new String[100];
    static String[] customerPassword = new String[100];
    static String[] customerPhone = new String[100];
    static String[] customerCnic = new String[100];
    static String[] customerAddress = new String[100];
    static String[] customerGender = new String[100];
    static int[] customerAge = new int[100];
    static String[] customerAccountNumber = new String[100];
    static String[] pins = new String[100];
    static double[] balances = new double[100];
    // USED TO STORE THE NUMBER OF CUSTOMERS
    static int customerCount = 0;

    // A 2D ARRAY WHICH STORE TRANSACTION HISTORY WRT TO A SINGLE CUSTOMER
    static String[][] transactionHistory = new String[100][100];  // 100 users, 100 transactions each
    // NUMBER OF TRANSACTIONS
    static int[] transactionCount = new int[100];

    // THIS STORE THE INDEX OF THE LOGGED IN CUSTOMER WHICH ELIMINATES THE NEED TO LOOP AGAIN AND AGAIN
    static int loggedInCustomer = -1;
    // THIS IS THE SAME AS LOGGED IN CUSTOMER BUT FOR THE ADMIN
    static int loggedInAdminIndex = -1;

    // ARRAYS TO STORE LOAN RELATED DATA
    static double[] loanTime = new double[100];     // Time in years
    static double[] interest = new double[100];     // Calculated interest
    static double interestRate = 0.10;                         // 10% interest
    static double investorsAmount = 15000000;           // FROM WHERE WE GIVE LOAN
    static double[] loanRequests = new double[100];        // REQUESTED AMOUNT
    static boolean[] loanApproved = new boolean[100];
    static String[] loanStatus = new String[100];
    static int[] penaltyPoints = new int[100];
    // PENDING APPROVED OR REJECTED
    static double bankAmount = 0;       // COMBINES THE BALANCES OF CUSTOMERS TO CREATE A BANK AMOUNT

    // LOAN REPAYMENT RELATED ARRAYS
    static double[] originalLoanAmount = new double[100];
    static double[] originalInterest = new double[100];
    static double[] yearlyPay = new double[100];
    static String[] dates = new String[100];
    static int[] blockedAccount = new int[100];
    static int[] penaltyPoint = new int[100];


    //---------------------------------------------------------------------------------
    /**
     * PART OF MUHAMMAD ARSAL ABULLAH  (FA24-BDS-017)*/
    //----------------------------------------------------------------------------------
    // METHOD TO SAVE ALL DATA
    public static void saveAllData() {

        saveCustomerDataToFile();
        saveAdminDataToFile();
        saveTransactionData();
        saveRevenueDetails();
        saveLoanData();
        saveBankAmount();
        saveLoanRepaymentData();
    }

    // METHOD TO LOAD ALL DATA
    public static void loadAllData() {

        loadCustomerData();
        loadAdminData();
        loadTransactionData();
        loadRevenueDetails();
        loadLoanData();
        loadBankAmount();
        loadLoanRepaymentData();
    }

    // METHOD TO RESET BRANCH DATA
    public static void resetBranchData() {
        selectedBranch = "";
        adminCount = 0;
        loggedInAdmin = null;
        loggedInAdminIndex = -1;
        customerCount = 0;
        loggedInCustomer = -1;
        totalRevenue = 0;
        bankAmount = 0;

        for (int i = 0; i < 100; i++) {
            // Admin
            adminUsername[i] = null;
            adminPassword[i] = null;
            adminFullName[i] = null;
            adminPhone[i] = null;
            adminCnic[i] = null;
            adminGender[i] = null;
            adminAddress[i] = null;
            adminAge[i] = 0;
            adminPosition[i] = null;

            // Customer
            customerFullName[i] = null;
            customerUsername[i] = null;
            customerPassword[i] = null;
            customerPhone[i] = null;
            customerCnic[i] = null;
            customerAddress[i] = null;
            customerGender[i] = null;
            customerAge[i] = 0;
            customerAccountNumber[i] = null;
            pins[i] = null;
            balances[i] = 0;

            // Transaction
            transactionCount[i] = 0;
            for (int j = 0; j < 100; j++) {
                transactionHistory[i][j] = null;
            }

            // Loan
            loanTime[i] = 0;
            interest[i] = 0;
            loanRequests[i] = 0;
            loanApproved[i] = false;
            loanStatus[i] = null;

            // repay loan
            originalLoanAmount[i] = 0;
            originalInterest[i] = 0;
            yearlyPay[i] = 0;
            dates[i] = null;
            blockedAccount[i] = 0;
            penaltyPoint[i] = 0;

        }
    }

    // THIS METHOD GIVES THE INFO AND SUMAMRY OF THE BRANCH
    public static void branchStatusReport() {

        System.out.println("\u001B[1;95m\nBANK SUMMARY REPORT\u001B[0m");

        System.out.printf("\033[97m%-35s\033[0m%,15d\n", "Total Revenue:", totalRevenue);
        System.out.printf("\033[97m%-35s\033[0m%,15d\n", "Admin Accounts:", adminCount);
        System.out.printf("\033[97m%-35s\033[0m%,15d\n", "Customer Accounts:", customerCount);
        System.out.printf("\033[97m%-35s\033[0m%,15.2f\n", "Total Bank Balance:", bankAmount);

        int totalTransactions = 0;
        for (int i = 0; i < customerCount; i++) {
            totalTransactions += transactionCount[i];
        }


        System.out.printf("\033[97m%-35s\033[0m%,15d\n", "Total Transactions:", totalTransactions);
        System.out.println("\033[1;90m--------------------------------------------------------------\033[0m");

    }

    // THIS METHOD HELPS IN SWITCHING OF BRANCHES AND LOADING DATA RESPECTIVELY
    public static void startProgram() {
        while (true) {
            selectBranch();   // Ask user to select a branch
            loadAllData();    // Load the selected branch's data

            mainMenu();       // opening the main menu of respective branch

            // After mainMenu() ends (on branch change), loop continues and restarts from branch selection
        }
    }

    /**---------------OUR MAIN METHOD------------------------*/
    public static void main(String[] args) {
        heading();
        startProgram(); // CALLING THE METHOD TO HANDLE BRANCH SWITCHES AND MAIN MENU
    }

    // THIS IS OUR MAIN MENU WHICH WILL BE USED TO CALL ADMIN CUSTOMER AND CHANGE BRANCH METHODS
    // IT ALSO EXITS OUR PROGRAM
    public static void mainMenu() {
        while (true) {
            System.out.println("\n\033[1;94mMAIN MENU:\033[0m");
            System.out.println("1. Administrator Panel ");
            System.out.println("2. Customer Portal");
            System.out.println("3. Branch Management");
            System.out.println("4. Exit");
            System.out.print("Enter a Choice (1-4): ");

            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid number (1-4).");
                continue;
            }

            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        adminLogin();
                        break;
                    case 2:
                        customerLogin();
                        break;
                    case 3:
                        System.out.println("Returning to Branch Selection Menu...");
                        saveAllData();
                        resetBranchData();
                        return;  // Exits mainMenu() and goes back to startProgram()
                    case 4:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Please enter a number between 1 and 4.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a numeric value (1-4).");
            }
        }
    }

    //  ADMIN LOGIN METHOD
    public static void adminLogin() {
        // CHECKING IF THIS BRANCH HAS AN ADMIN OR NOT
        // IF THERE IS NO ADMIN WE HAVE TO CREATE ONE
        try {
            if (adminCount == 0) {
                System.out.println("\n\033[1;91mNo admin accounts exist yet!\033[0m");
                System.out.println("Please create an admin account first.");
                System.out.print("Would you like to create an admin account now? (Y/N): ");
                String choice = sc.nextLine().trim();

                if (choice.equalsIgnoreCase("Y")) {
                    createAdminAccount(); // calling create admin method
                    adminLogin(); // recursive call to admin login method
                }
                return;
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return;
        }

        int attempts = 3; // 3 attempts for login before it send you back to main menu

        while (attempts > 0) {
            try {
                System.out.println("\n\033[1;95m ADMIN LOGIN: \033[0m");

                System.out.print("Enter Username: ");
                String username = sc.nextLine();

                if (username.isEmpty()) {
                    System.out.println("Username cannot be empty. Please try again.");
                    continue;
                }

                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                if (password.isEmpty()) {
                    System.out.println("Password cannot be empty. Please try again.");
                    continue;
                }

                // CHECKS FOR ENTER ADMIN NAME AND PASSWORD IN ARRAYS AT THE SAME INDEX
                for (int i = 0; i < adminCount; i++) {
                    if (adminUsername[i] != null && adminUsername[i].equals(username) &&
                            adminPassword[i] != null && adminPassword[i].equals(password)) {
                        System.out.println("Login successful!");
                        loggedInAdmin = username;
                        loggedInAdminIndex = i;
                        showAdminMenu();
                        return;
                    }
                }
                // removing the attempts
                attempts--;
                System.out.println("Invalid credentials! Attempts remaining: " + attempts);

            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }

        System.out.println("Maximum login attempts reached. Access denied.");

    }

    // -------------OUR MAIN ADMIN MENU-------------------------

    public static void showAdminMenu() {
        while (true) {
            try {
                System.out.println("\033[1;96mADMIN MENU:\033[0m ");
                System.out.println("1. Create Admin Account");
                System.out.println("2. Customer Records Menu");
                System.out.println("3. Add Customer");
                System.out.println("4. Delete Customer");
                System.out.println("5. View Transactions");
                System.out.println("6. Display All Admins");
                System.out.println("7. Search An Admin");
                System.out.println("8. Delete An Admin");
                System.out.println("9. Edit Admin Details");
                System.out.println("10. Loan Management");
                System.out.println("11. Manage Block Accounts");
                System.out.println("12. Display Branch Report");
                System.out.println("13. Return To Main Menu");

                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();  // Clearing buffer

                switch (choice) {
                    case 1:
                        createAdminAccount(); // calling admin creation
                        break;
                    case 2:
                        customerRecordsMenu(); // calling customer records menu
                        break;
                    case 3:
                        createCustomerAccount(); // calling customer creation
                        break;
                    case 4:
                        deleteCustomerAccount();    // calling customer deletion
                        break;
                    case 5:
                        transactionMenu();      // transactions menu
                        break;
                    case 6:
                        displayAllAdmins(); // displaying all admins
                        break;
                    case 7:
                        searchAdmin();  // searching an admin
                        break;
                    case 8:
                        deleteAdminAccount(); // deleting an admin
                        break;
                    case 9:
                        editAdminDetails(); // editing an admin
                        break;
                    case 10:
                        manageLoans();      // loan management menu
                        break;
                    case 11:
                        manageBlockedAccounts();    // Blocked accounts
                        break;
                    case 12:
                        branchStatusReport();
                        break;
                    case 13:
                        System.out.println("Returning to main menu...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select a number between 1 and 13.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                sc.nextLine(); // clear the invalid input from scanner buffer
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                sc.nextLine(); // clearing buffer just in case
            }
        }
    }


    // METHOD TO DISPLAY ALL ADMIN THROUGH THE LOOP AND ADMIN COUNT
    public static void displayAllAdmins() {
        try {
            if (adminCount == 0) {
                System.out.println("No admin accounts found.");
                return;
            }

            System.out.println("\n\033[1;96m ALL ADMIN ACCOUNTS\033[0m");
            System.out.println("Total Admins: " + adminCount);

            for (int i = 0; i < adminCount; i++) {
                System.out.println("\033[1;97m\nAdmin #" + (i + 1) + "\033[0m");
                System.out.println("Full Name : " + adminFullName[i]);
                System.out.println("Gender    : " + adminGender[i]);
                System.out.println("Username  : " + adminUsername[i]);
                System.out.println("Age       : " + adminAge[i]);
                System.out.println("Phone     : " + adminPhone[i]);
                System.out.println("CNIC      : " + adminCnic[i]);
                System.out.println("Address   : " + adminAddress[i]);
                System.out.println("Position  : " + adminPosition[i]);
            }
        } catch (NullPointerException e) {
            System.out.println("Error: Some admin details are missing.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Admin data is inconsistent.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // SEARCHING AN ADMIN THROUGH ADMIN USERNAME
    public static void searchAdmin() {
        try {
            if (adminCount == 0) {
                System.out.println("No admin accounts found.");
                return;
            }
            System.out.println("\033[1;96m \n SEARCH ADMIN \033[0m");

            System.out.print("Enter the admin username to search the record: ");
            String nameRecord = sc.next();

            boolean found = false;
            for (int i = 0; i < adminCount; i++) {
                if (adminUsername[i] != null && adminUsername[i].equals(nameRecord)) {
                    System.out.println("\033[1;97m\nAdmin #" + (i + 1) + "\033[0m");
                    System.out.println("Full Name : " + adminFullName[i]);
                    System.out.println("Gender    : " + adminGender[i]);
                    System.out.println("Username  : " + adminUsername[i]);
                    System.out.println("Phone     : " + adminPhone[i]);
                    System.out.println("CNIC      : " + adminCnic[i]);
                    System.out.println("Address   : " + adminAddress[i]);
                    System.out.println("Age       : " + adminAge[i]);
                    System.out.println("Position  : " + adminPosition[i]);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No admin found with the username: " + nameRecord);
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid input type. Please enter a valid username.");
            sc.nextLine(); // Clear the invalid input
        } catch (NullPointerException e) {
            System.out.println("Error: Missing admin data.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // DELETING THE ADMIN ACCOUNT
    public static void deleteAdminAccount() {
        try {
            if (adminCount == 0) {
                System.out.println("No admin accounts to delete.");
                return;
            }

            // ASKING FOR THE USERNAME
            System.out.println("\033[1;91m \nDELETE ADMIN ACCOUNT \033[0m");
            System.out.print("Enter the username of the admin to delete: ");
            String username = sc.nextLine().trim();

            // Prevent currently logged-in admin from deleting their own account
            if (username.equals(loggedInAdmin)) {
                System.out.println("You cannot delete your own account while logged in.");
                return;
            }

            // IF CONDITIONS MEET WE GET THE INDEX WHICH WE HAVE TO DELETE
            int index = -1;
            for (int i = 0; i < adminCount; i++) {
                if (adminUsername[i] != null && adminUsername[i].equals(username)) {
                    index = i;
                    break;
                }
            }

            if (index == -1) {
                System.out.println("Admin with the given username was not found.");
                return;
            }

            // SHIFTING THE ENTERIES TO THE INDEX WHICH WE HAVE TO DELETE
            for (int i = index; i < adminCount - 1; i++) {
                adminUsername[i] = adminUsername[i + 1];
                adminPassword[i] = adminPassword[i + 1];
                adminFullName[i] = adminFullName[i + 1];
                adminGender[i] = adminGender[i + 1];
                adminPhone[i] = adminPhone[i + 1];
                adminCnic[i] = adminCnic[i + 1];
                adminAddress[i] = adminAddress[i + 1];
                adminAge[i] = adminAge[i + 1];
                adminPosition[i] = adminPosition[i + 1];
            }

            // CLEARING THE LAST SLOT
            int lastIndex = adminCount - 1;
            adminUsername[lastIndex] = null;
            adminPassword[lastIndex] = null;
            adminFullName[lastIndex] = null;
            adminGender[lastIndex] = null;
            adminPhone[lastIndex] = null;
            adminCnic[lastIndex] = null;
            adminAddress[lastIndex] = null;
            adminAge[lastIndex] = 0;
            adminPosition[lastIndex] = null;

            adminCount--;
            System.out.println("Admin account deleted successfully.");
            saveAdminDataToFile();

        } catch (NullPointerException e) {
            System.out.println("Error: Missing admin data. " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Internal data inconsistency. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // EDITING THE ADMIN DETAILS
    static void editAdminDetails() {
        while (true) {
            try {
                System.out.println("\n\033[1;92m Edit Admin Details \033[0m");
                System.out.println("1. Full Name");
                System.out.println("2. Phone Number");
                System.out.println("3. Admin Username");
                System.out.println("4. CNIC");
                System.out.println("5. Address");
                System.out.println("6. Age");
                System.out.println("7. Position");
                System.out.println("8. Password");
                System.out.println("9. Go Back ");
                System.out.print("Enter your choice: ");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        editAdminFullName();
                        break;
                    case 2:
                        editAdminPhone();
                        break;
                    case 3:
                        editAdminUsername();
                        break;
                    case 4:
                        editAdminCNIC();
                        break;
                    case 5:
                        editAdminAddress();
                        break;
                    case 6:
                        editAdminAge();
                        break;
                    case 7:
                        editAdminPosition();
                        break;
                    case 8:
                        editAdminPassword();
                        break;
                    case 9:
                        System.out.println("Returning to Admin Menu...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            } catch (NullPointerException e) {
                System.out.println("Error: Some data is missing. " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    static void editAdminFullName() {
        try {
            System.out.println("Current Full Name: " + adminFullName[loggedInAdminIndex]);
            String name;
            while (true) {
                System.out.print("Enter Full Name: ");
                name = sc.nextLine().trim();
                if (!name.isEmpty()) break;
                System.out.println("Name cannot be empty.");
            }
            adminFullName[loggedInAdminIndex] = name;
            System.out.println("Full Name updated successfully.");
        } catch (NullPointerException e) {
            System.out.println("Error: Admin account not found.");
        }
        saveAdminDataToFile();
    }

    static void editAdminPhone() {
        try {
            System.out.println("Current Phone Number: " + adminPhone[loggedInAdminIndex]);
            String phone;
            while (true) {
                System.out.print("Phone Number (11 digits): ");
                phone = sc.nextLine().trim();
                if (phone.length() == 11 && allDigits(phone)) break;
                System.out.println("Invalid phone number. It should be 11 digits.");
            }
            adminPhone[loggedInAdminIndex] = phone;
            System.out.println("Phone Number updated successfully.");
        } catch (NullPointerException e) {
            System.out.println("Error: Admin account not found.");
        }
        saveAdminDataToFile();
    }

    static void editAdminCNIC() {
        try {
            System.out.println("Current CNIC: " + adminCnic[loggedInAdminIndex]);
            String cnic;
            while (true) {
                System.out.print("Enter CNIC (XXXXX-XXXXXXX-X): ");
                cnic = sc.nextLine().trim();
                if (!isCnicValid(cnic)) {
                    System.out.println("Invalid format! Must be XXXXX-XXXXXXX-X (13 digits with dashes)");
                    System.out.println("Please try Again");
                    continue;
                }
                int check = checkCnicAvailability(cnic);
                if (check == 1) {
                    System.out.println("CNIC already exists in the current branch. Try a new one.");
                } else if (check == 2) {
                    System.out.println("CNIC already exists in another branch. Try a new one.");
                } else if (check == 0) {
                    adminCnic[adminCount] = cnic;
                    System.out.println("CNIC updated successfully.");
                    break;
                }
            }
            System.out.println("CNIC updated successfully.");
        } catch (NullPointerException e) {
            System.out.println("Error: Admin account not found.");
        }
        saveAdminDataToFile();
    }

    static void editAdminAddress() {
        try {
            System.out.println("Current Address: " + adminAddress[loggedInAdminIndex]);
            String address;
            while (true) {
                System.out.print("Address: ");
                address = sc.nextLine().trim();
                if (!address.isEmpty()) break;
                System.out.println("Address cannot be empty.");
            }
            adminAddress[loggedInAdminIndex] = address;
            System.out.println("Address updated successfully.");
        } catch (NullPointerException e) {
            System.out.println("Error: Admin account not found.");
        }
        saveAdminDataToFile();
    }

    static void editAdminAge() {
        try {
            int age;
            while (true) {
                System.out.print("Age (must be 18 or older): ");
                if (sc.hasNextInt()) {
                    age = sc.nextInt();
                    sc.nextLine();
                    if (age >= 18 && age <= 100) {
                        adminAge[loggedInAdminIndex] = age;
                        System.out.println("Age updated successfully.");
                        break;
                    } else {
                        System.out.println("Age must be between 18 and 100.");
                    }
                } else {
                    System.out.println("Please enter a valid number.");
                    sc.nextLine(); // Clear invalid input
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numbers only.");
        } catch (NullPointerException e) {
            System.out.println("Error: Admin account not found.");
        }
        saveAdminDataToFile();
    }

    static void editAdminPosition() {
        try {
            System.out.println("Current Position: " + adminPosition[loggedInAdminIndex]);
            String position;
            while (true) {
                System.out.print("Enter Position: ");
                position = sc.nextLine().trim();
                if (!position.isEmpty()) break;
                System.out.println("Position cannot be empty.");
            }
            adminPosition[loggedInAdminIndex] = position;
            System.out.println("Position updated successfully.");
        } catch (NullPointerException e) {
            System.out.println("Error: Admin account not found.");
        }
        saveAdminDataToFile();
    }

    static void editAdminUsername() {
        try {
            System.out.println("Current Username: " + adminUsername[loggedInAdminIndex]);
            String username;
            while (true) {
                System.out.print("Username: ");
                username = sc.nextLine().trim();
                if (username.isEmpty()) {
                    System.out.println("Username cannot be empty.");
                    continue;
                }

                boolean exists = false;
                for (int i = 0; i < adminCount; i++) {
                    if (adminUsername[i].equals(username)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    System.out.println("Username already exists. Choose another.");
                } else {
                    break;
                }
            }
            adminUsername[loggedInAdminIndex] = username;
            System.out.println("Username updated successfully.");
        } catch (NullPointerException e) {
            System.out.println("Error: Admin account not found.");
        }
        saveAdminDataToFile();
    }

    static void editAdminPassword() {
        try {
            System.out.println("Current Password: " + adminPassword[loggedInAdminIndex]);
            String password;
            while (true) {
                System.out.print("Password (8-12 chars, with upper, lower, digit, and special !@#$%^&*): ");
                password = sc.nextLine();

                if (!validatePassword(password)) {
                    System.out.println("Invalid password. Must be 8-12 characters long and include:");
                    System.out.println("Uppercase letter");
                    System.out.println("Lowercase letter");
                    System.out.println("Digit");
                    System.out.println("Special character (!@#$%^&*)");
                } else {
                    boolean exists = false;
                    for (int i = 0; i < adminCount; i++) {
                        if (adminPassword[i].equals(password)) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        System.out.println("Password already exists. Choose another.");
                    } else {
                        break;
                    }
                }
            }
            adminPassword[loggedInAdminIndex] = password;
            System.out.println("Password updated successfully.");
        } catch (NullPointerException e) {
            System.out.println("Error: Admin account not found.");
        }
        saveAdminDataToFile();
    }

    // Methods Of file Handling

    /**
     * T  H  I  S      P  O  R  T  I  O  N     I  S        F  O  R     F  I  L  E
     * H  A  N  D  L  I  N  G      M  E  T  H  O  D  S
     */

    // METHOD TO SAVE CUSTOMER DATA
    public static void saveCustomerDataToFile() {

        try {
            // USING FILEWRITER SO THAT IF THE FILE IS NOT PRESENT IT CREATES IT
            // SELECTED BRANCH IS HELPING US TO CHOOSE THE VALID FILE
            FileWriter file = new FileWriter("customerDataFile_" + selectedBranch + ".txt");
            // USING PRINTWRITER FOR FORMATTED OUTPUT
            PrintWriter pw = new PrintWriter(file);
            for (int i = 0; i < customerCount; i++) {
                // USING PRINTF TO STORE CUSTOMER DATA IN A LINE
                pw.printf("%s,%s,%s,%s,%s,%s,%d,%s,%s,%s,%.2f%n",
                        customerFullName[i],
                        customerUsername[i],
                        customerPassword[i],
                        customerPhone[i],
                        customerCnic[i],
                        customerAddress[i],
                        customerAge[i],
                        customerGender[i],
                        customerAccountNumber[i],
                        pins[i],
                        balances[i]
                );

            }
            // CLOSING THE PRINTWRITER SO THAT THE DATA IS STORED AND NOT LEFT IN THE BUFFER
            pw.close();

            // STATEMENT TO TELL IF DATA WAS SAVED OR NOT
            System.out.println("Customer data saved successfully!");
        } catch (IOException e) {
            // CATCHING EXCEPTION
            System.out.println("Error saving customer data: " + e.getMessage());
        }
    }

    // LOADING THE CUSTOMER DATA USING File AND Scanner

    public static void loadCustomerData() {
        // USNIG FILE TO REPRESENT AN ALREADY BUILT FILE/ TO LINK THE FILE FOR OPERATIONS
        File file = new File("customerDataFile_" + selectedBranch + ".txt");

        try {

            // SCANNER WHICH TAKES FILE AS AN INPUT AND READS CHARACTER STREAM FORM IT
            Scanner scanner = new Scanner(file);
            customerCount = 0; // THIS HELPS TO TELL HOW MANY CUSOTMER ARE THERE

            while (scanner.hasNextLine()) {  // IF HAS NEXT LINE THEN PROCEEDS
                String line = scanner.nextLine(); // READING THE LINE
                String[] parts = line.split(","); // WE STORED AS CSV WE SPLIT AS CSV

                if (parts.length < 11) {
                    System.out.println("Skipping invalid data line: " + line);
                    continue;  // WE ARE SKIPPING BROKEN LINES
                }

                // STORING THE DATA RESPECTIVELY IN THEIR ARRAYS
                // THE PARTS ARRAY STORES AS STRING WHICH CAN BE CONVERTED TO DIFFERNT TYPE
                customerFullName[customerCount] = parts[0];
                customerUsername[customerCount] = parts[1];
                customerPassword[customerCount] = parts[2];
                customerPhone[customerCount] = parts[3];
                customerCnic[customerCount] = parts[4];
                customerAddress[customerCount] = parts[5];
                customerAge[customerCount] = Integer.parseInt(parts[6]);       // STRING TO INT
                customerGender[customerCount] = parts[7];
                customerAccountNumber[customerCount] = parts[8];
                pins[customerCount] = parts[9];
                balances[customerCount] = Double.parseDouble(parts[10]);    // STRING TO DOUBLE

                customerCount++;    // INCREASING OUR CUSTOMER COUNT IN GLOBAL VARIABLE
            }
            // CLOSING TO AVOID DATA LEAK
            scanner.close();
            // DISPLAY MESSAGE FOR VALIDATION
            System.out.println("Customer data loaded successfully!");
        } catch (FileNotFoundException e) { // IF FILE NOT FOUD ARRAYS HAVE NO VALUES
            System.out.println("Customer data file not found. Starting fresh.");
        } catch (NumberFormatException e) {     // IF INT AND DOUBLE ARE NOT IN VALID FORMAT
            System.out.println("Number format error in customer data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error loading customer data: " + e.getMessage());
        }
    }

    // METHOD TO SAVE ADMIN DATA TO ARRAYS
    public static void saveAdminDataToFile() {

        try {
            // USING FILEWRITER SO THAT IF THE FILE IS NOT PRESENT IT CREATES IT
            FileWriter file = new FileWriter("adminDataFile_" + selectedBranch + ".txt");

            // USING PRINTWRITER FOR FORMATTED OUTPUT
            PrintWriter pw = new PrintWriter(file);

            //"FullName,Username,Password,Phone,CNIC,Address,Age,Gender,Position"

            for (int i = 0; i < adminCount; i++) {
                // LOOPS THROUGH ALL THE ADMINS AND STORES THEM AS LINES SEPERATED BY COMMAS
                pw.printf("%s,%s,%s,%s,%s,%s,%d,%s,%s%n",
                        adminFullName[i],
                        adminUsername[i],
                        adminPassword[i],
                        adminPhone[i],
                        adminCnic[i],
                        adminAddress[i],
                        adminAge[i],
                        adminGender[i],
                        adminPosition[i]);
            }
            // CLOSING THE PW
            pw.close();

            // DISPLAYING MESSAGE FOR ASSURANCE
            System.out.println("Admin data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving Admin data: " + e.getMessage());
        }
    }

    // METHOD TO LOAD ADMIN DATA FROM FILES
    public static void loadAdminData() {
        // USING FILE TO LINK IT TO ALREADY CREATED FILE

        /**SELECTED BRANCH VARIABLE IS AGAIN HELPING US TO CHOOSE THE DATA
         RESPECTIVE TO THE BRANCH WHICH WE HAVE SELECTED*/

        File file = new File("adminDataFile_" + selectedBranch + ".txt");

        // IF NO ADMIN FILE EXISTS THEN WE WILL HAVE TO CREATE AN ADMIN ACCOUNT

        /**THIS CHECK HELPS US INSTEAD OF POINTLESSLY
         * CHOOSING THROUGH MENUS WHICH WILL DO NOTHING
         BECAUSE THERE IS NO ACCOUNT IN THE BRANCH */

        if (!file.exists()) {
            System.out.println("Admin data file not found. Starting fresh.");
            createAdminAccount();
            return;
        }

        try {

            /** USING SCANNER CLASS TO READ THE DATA
             * THE STREAM IS CHARACTER BUT IT WAS CONVERTED FROM BITS TO CHARACTER BY
             * InputStreamReader */

            Scanner scanner = new Scanner(file);
            adminCount = 0;  // HELPS IN KEEPING THE COUNT OF ADMINS

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim(); // FOR WHITESPACES
                if (line.isEmpty()) continue; // SKIPPING BLANK LINES

                // SPLITTING AS CSV AND STORING IN AN STRING ARRAY
                String[] parts = line.split(",");
                if (parts.length < 9) continue; // SKIPPING INCOMPLETE LINES

                // USING THE PARTS ARRAY TO PUT DATA IN OUR ARRAYS AT DIFFERENT INDEXES
                adminFullName[adminCount] = parts[0];
                adminUsername[adminCount] = parts[1];
                adminPassword[adminCount] = parts[2];
                adminPhone[adminCount] = parts[3];
                adminCnic[adminCount] = parts[4];
                adminAddress[adminCount] = parts[5];
                adminAge[adminCount] = Integer.parseInt(parts[6]);
                adminGender[adminCount] = parts[7];
                adminPosition[adminCount] = parts[8];

                adminCount++; // INCREASING ADMIN COUNT WITH EACH LINE
            }

            // CLOSING SCANNER
            scanner.close();

            // IF THERE IS NO DATA IN ADMIN FILE WE ARE REQUIRED TO CREATE ONE
            if (adminCount == 0) {
                System.out.println("No admin records found in file. Please create a new one.");
                createAdminAccount();  // TRIGGERD ONLY IF FILE IS EMPTY
            } else {
                System.out.println("Admin data loaded successfully!"); // MESSAGE
            }

        } catch (FileNotFoundException e) {
            System.out.println("Admin data file not found. Starting fresh.");
        } catch (NumberFormatException e) {
            System.out.println("Number format error in admin data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error loading admin data: " + e.getMessage());
        }
    }

    // METHOD FOR SAVING TRANSACTION DATA AND TRANSACTION COUNT

    /**
     * Writes every users transaction history and count to  "transactions.txt".
     * File format (one line per user):
     * username | txnCount | txn1 ; txn2 ; txn3 ....
     * A user with zero transactions is still written so that the index order is preserved.
     */

    public static void saveTransactionData() {
        try {
            FileWriter file = new FileWriter("transactions_" + selectedBranch + ".txt");
            PrintWriter pw = new PrintWriter(file);
            //Username|TransactionCount|Transactions
            for (int i = 0; i < customerCount; i++) {
                pw.print(customerUsername[i]);       // CREATES THE FIRST COLUMN OF USERNAME
                pw.print("|");
                pw.print(transactionCount[i]);       // SECOND COLUMN OF TRANSACTION COUNT
                pw.print("|");

                /**THIS INNER LOOP FOR 2D ARRAY IS USED TO STORE THE TRANSACTION HISTORY
                 * WHICH IS STORED AS COLUMNS IN THE 2D ARRAY */
                // LOOPING TILL TRANSACTION COUNT BECAUSE ITS DIRECTLY RELATED TO TRANSACTIONS

                for (int j = 0; j < transactionCount[i]; j++) {
                    // PRINTING THE STRING TO THE FILE
                    pw.print(transactionHistory[i][j]);
                    // THIS IS IMPORTANT AS TO SEPERATE THE TRANSACTIONS
                    // THIS IS PRINTED AFTER EACH TRANSACTON BUT NOT AFTER THE LAST ONE
                    if (j < transactionCount[i] - 1) pw.print(";");
                }
                pw.println();   // AFTER STORING DATA OF ONE USER WE MOVE ON THE NEXT LINE
            }
            // CLOSING THE PRINT WRITER
            pw.close();

            // DISPLAYING THE SUCCESS MESSAGE
            System.out.println("Transaction history saved.");
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    // METHOD TO LOAD THE TRANSACTION DATA AND TRANSACTION COUNT

    public static void loadTransactionData() {
        File file = new File("transactions_" + selectedBranch + ".txt");
        if (!file.exists()) {
            System.out.println("No transaction file starting with empty histories.");
            return;
        }

        try {
            Scanner scFile = new Scanner(file);
            while (scFile.hasNextLine()) {
                String line = scFile.nextLine();
                String[] cols = line.split("\\|", 3);
                /** LIMIT = 3 WILL ONLY GIVE US 3 COLUMNS
                 * FIRST ONE OF "USERNAME"
                 * SECOND OF "TRANSACTION COUNT"
                 * THIRD OF = "ALL THE TRANSACTIONS SEPERATED BY ( ; )"*/

                if (cols.length < 2) continue;
                // IF THERE IS ONLY ONE FIELD THEN WE ARE SKIPPING IT BECAUSE ITS OF NO USE

                String username = cols[0]; // GETTING THE USERNAME OF THE CURRENT LINE
                int idx = -1;

                for (int i = 0; i < customerCount; i++) {
                    if (customerUsername[i] != null && customerUsername[i].equals(username)) {
                        // IF CUSTOMER USERNAME AT INDEX i EQUALS TO THE ONE IN THE FILE
                        idx = i; // WE ASSIGN idx THE INDEX
                        break;
                    }

                }

                if (idx == -1) continue; // IF NO CUSTOMER USERNAME MATCHES

                // NOW WE ARE USING THAT INDEX idx TO STORE THE TRANSACTION COUNT
                try {
                    transactionCount[idx] = Integer.parseInt(cols[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid transaction count for user: " + username);
                    continue;
                }

                // STORES TRANSACTIONS ONLY IF THE COUNT > 0 AND THERE ARE 3 COLUMNS
                if (transactionCount[idx] > 0 && cols.length == 3) {
                    // STORING THE ALL THE TRANSACTIONS IN AN ARRAY
                    String[] txns = cols[2].split(";");
                    // THIS LOOP GOES THROUGH THE TRANSACTION COUNT AND STORES THEM IN MAIN ARRAY
                    for (int k = 0; k < transactionCount[idx] && k < txns.length; k++) {
                        transactionHistory[idx][k] = txns[k];
                    }
                }
            }
            scFile.close(); // CLOSING THE SCANNER
            // DISPLAYING THE MESSAGE
            System.out.println("Transaction history loaded.");

        } catch (Exception e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    // SAVING THE REVENUE DETAILS FOR THE BRANCH
    public static void saveRevenueDetails() {

        try {
            FileWriter revenueDetails = new FileWriter("RevenueDetails_" + selectedBranch + ".txt");
            PrintWriter pw = new PrintWriter(revenueDetails);
            pw.println(totalRevenue);
            pw.close();
        } catch (IOException e) {
            System.out.println("Error saving revenue details: " + e.getMessage());
        }
    }

    // METHOD TO LOAD THE REVENUE DETAILS
    public static void loadRevenueDetails() {
        File file = new File("RevenueDetails_" + selectedBranch + ".txt");

        if (!file.exists()) {
            System.out.println("Revenue File not found. Starting with 0 revenue.");
            return;
        }

        try {
            Scanner scFile = new Scanner(file);
            if (scFile.hasNextLine()) {
                String line = scFile.nextLine().trim();

                totalRevenue = Integer.parseInt(line);

            }
            scFile.close();
        } catch (NumberFormatException e) {
            System.out.println("Invalid data in File. Starting with 0 revenue.");
            totalRevenue = 0;
        } catch (IOException e) {
            System.out.println("Error loading revenue details: " + e.getMessage());
        }
    }

    // SAVING THE LOAN DATA OF THE CUSTOMERS
    public static void saveLoanData() {
        try {
            // USING FILEWRITER TO CREATE FILE IF NOT PRESENT
            // USING BRANCH TO SAVE THE LOAN DATA OF THE SELECTED CURRENT BRANCH
            FileWriter file = new FileWriter("loan_" + selectedBranch + ".txt");
            // USING PRINTWRITER TO FORMAT THE SAVING
            PrintWriter pw = new PrintWriter(file);

            // LOOPING THROUGH EACH CUSTOMER TO SAVE THE LOAN DATA
            for (int i = 0; i < customerCount; i++) {
                pw.printf("%.2f|%.2f|%.2f|%b|%s%n",
                        loanRequests[i],    // AMOUNT REQUESTED
                        loanTime[i],        // YEARS
                        interest[i],        // INTEREST ON TOP
                        loanApproved[i],    // BOOLEAN
                        loanStatus[i] == null ? "Pending" : loanStatus[i]); // USING PENDING AS DEFAULT
            }
            // CLOSING THE PRINTWRITER
            pw.close();
            // DISPLAYING THE MESSAGE
            System.out.println("Loan data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving loan data: " + e.getMessage());
        }
    }

    // LOADING THE LOAN DATA SPECIFIC TO THE BRANCH
    public static void loadLoanData() {
        File file = new File("loan_" + selectedBranch + ".txt");

        // A CHECK TO SEE IF FILE EXIST OR NOT
        if (!file.exists()) {
            System.out.println("Loan file not found. Starting with empty loan records.");
            return;
        }

        try {
            // SCANNER TO READ THE DATA AS CHARACTER STREAM
            Scanner scFile = new Scanner(file);
            // USING i AS INDEXING VARIABLE
            int i = 0;
            // USING CUSTOMER COUNT AS LIMIT HERE TO STORE DATA IN LOAN ARRAYS
            while (scFile.hasNextLine() && i < customerCount) {
                String line = scFile.nextLine();
                String[] cols = line.split("\\|", 5);

                loanRequests[i] = Double.parseDouble(cols[0]);
                loanTime[i] = Double.parseDouble(cols[1]);
                interest[i] = Double.parseDouble(cols[2]);
                loanApproved[i] = Boolean.parseBoolean(cols[3]);
                loanStatus[i] = cols[4];

                i++;
            }
            // CLOSING
            scFile.close();
            System.out.println("Loan data loaded.");
        } catch (Exception e) {
            System.out.println("Error loading loan data: " + e.getMessage());
        }
    }

    // SAVING THE LOAN REPAYMENT DATA
    public static void saveLoanRepaymentData() {
        try {
            FileWriter file = new FileWriter("loanRepayment_" + selectedBranch + ".txt");
            PrintWriter pw = new PrintWriter(file);

            for (int i = 0; i < customerCount; i++) {
                pw.printf("%.2f|%.2f|%.2f|%s|%d|%d%n",
                        originalLoanAmount[i],
                        originalInterest[i],
                        yearlyPay[i],
                        dates[i] == null ? "null" : dates[i],
                        blockedAccount[i],
                        penaltyPoint[i]);
            }
            // CLOSING THE PRINT WRITER
            pw.close();
            System.out.println("Loan repayment data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving loan repayment data: " + e.getMessage());
        }
    }

    // LOADING THE REPAYMENT DATA
    public static void loadLoanRepaymentData() {
        // USING File TO LINK IT TO AN EXISTING FILE
        File file = new File("loanRepayment_" + selectedBranch + ".txt");

        // IF NOT PRESENT WE START FROM EMPTY ARRAYS
        if (!file.exists()) {
            System.out.println("Loan Repayment file not found. Starting with empty repayment records.");
            return;
        }

        try {
            Scanner scFile = new Scanner(file);
            int i = 0;

            while (scFile.hasNextLine() && i < customerCount) {
                String line = scFile.nextLine();
                // HELPS IN EMPTY FIELD TO PREVENT INDEX OUT OF BOUND
                String[] cols = line.split("\\|", -1);

                originalLoanAmount[i] = Double.parseDouble(cols[0]);
                originalInterest[i] = Double.parseDouble(cols[1]);
                yearlyPay[i] = Double.parseDouble(cols[2]);
                dates[i] = cols[3].equalsIgnoreCase("null") ? null : cols[3];
                blockedAccount[i] = Integer.parseInt(cols[4]);
                penaltyPoint[i] = Integer.parseInt(cols[5]);
                i++;
            }
            // CLOSING THE SCANNER
            scFile.close();
            System.out.println("Loan Repayment data loaded successfully!");
        } catch (NumberFormatException e){
            System.out.println("Error has occurred while converting data");
        }
        catch (Exception e) {
            System.out.println("Error loading loan repayment data: " + e.getMessage());
        }
    }

    // SAVING THE BANK AMOUNT
    public static void saveBankAmount() {

        try {
            FileWriter revenueDetails = new FileWriter("BankAmount_" + selectedBranch + ".txt");
            PrintWriter pw = new PrintWriter(revenueDetails);
            bankAmount = bankAmount();
            pw.println(bankAmount);
            pw.close();
        } catch (IOException e) {
            System.out.println("Error saving Bank Amount details: " + e.getMessage());
        }
    }

    // LOADING THE BANK AMOUNT
    public static void loadBankAmount() {
        File file = new File("BankAmount_" + selectedBranch + ".txt");

        if (!file.exists()) {
            System.out.println("Bank Amount File not found. Starting with 0 revenue.");
            return;
        }

        try {
            Scanner scFile = new Scanner(file);
            if (scFile.hasNextLine()) {
                String line = scFile.nextLine().trim();

                bankAmount = Double.parseDouble(line);

            }
            scFile.close();
        } catch (NumberFormatException e) {
            System.out.println("Invalid data in file. Starting with 0 revenue.");
            bankAmount = 0;
        } catch (IOException e) {
            System.out.println("Error loading Bank Amount details: " + e.getMessage());
        }
    }

    // METHOD TO SELECT BRANCH
    /** THIS METHOD HERE PRESNTS A MENU
     * THEN 5 CHOICES ARE GIVEN
     * WHEN A CHOICE IS SELECTED
     * THAT IS STORED AS A GLOBAL VARIABLE
     * AND THAT VARIABLE IS THEN USED TO LOAD AND STORE THE DATA*/
    public static void selectBranch() {
        while (true) {
            try {
                System.out.println("\n\033[1;95m BRANCH MENU \033[0m");
                System.out.println("\n\033[1;96m Select Branch to Login: \033[0m");
                System.out.println("1. Quetta");
                System.out.println("2. Islamabad");
                System.out.println("3. Karachi");
                System.out.println("4. Lahore");
                System.out.println("5. Peshawar");
                System.out.print("Enter your choice: ");

                String input = sc.nextLine().trim();
                int choice = Integer.parseInt(input);

                switch (choice) {
                    // AFTER SELECTING CHOICE IT INITIALIZES THAT CHOICE TO THE VARIABLE
                    case 1:
                        selectedBranch = "Quetta";
                        break;
                    case 2:
                        selectedBranch = "Islamabad";
                        break;
                    case 3:
                        selectedBranch = "Karachi";
                        break;
                    case 4:
                        selectedBranch = "Lahore";
                        break;
                    case 5:
                        selectedBranch = "Peshawar";
                        break;
                    default: {
                        System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                        continue;
                    }
                }

                System.out.println("Branch selected: " + selectedBranch);
                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    // METHOD TO TRANSFER DATA BETWEEN WHO BRANCHES
    public static void interBranchTransfer() {
        try {
            System.out.println("Enter 0 if you want to go back");
            String[] validBranches = {"Quetta", "Islamabad", "Karachi", "Lahore", "Peshawar"};
            String targetBranch;
            while (true) {
                System.out.print("Enter target branch (Quetta, Islamabad, Karachi, Lahore, Peshawar): ");
                targetBranch = sc.nextLine().trim();
                if(targetBranch.equals("0")){
                    return;
                }
                if (targetBranch.equalsIgnoreCase(selectedBranch)) {
                    System.out.println("Use same-branch transfer method for this!");
                    return;
                }

                boolean isValid = false;
                for (String branch : validBranches) {
                    if (targetBranch.equalsIgnoreCase(branch)) {
                        isValid = true;
                        break;
                    }
                }

                if (!isValid) {
                    System.out.println("Invalid branch! Please enter exactly as shown.");
                    continue;
                }

                break; // Valid branch entered
            }

            String targetAccountNumber;
            while(true) {
                System.out.print("Enter recipient's **account number**: ");
                targetAccountNumber = sc.nextLine().trim();
                if (!isNumeric(targetAccountNumber)) {
                    System.out.println("Account number must contain digits");
                } else if (targetAccountNumber.length() != 14) {
                    System.out.println("Please enter a 14 digit Account");
                } else {
                    break;
                }
            }

            double amount = 0;
            while (true) {
                System.out.print("Enter amount to transfer: ");
                String amtInput = sc.nextLine().trim();

                try {
                    amount = Double.parseDouble(amtInput);
                    if (amount <= 0) {
                        System.out.println("Amount must be greater than 0.");
                    } else {
                        break; // valid amount
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format. Please enter a valid amount.");
                }
            }

            if (balances[loggedInCustomer] < amount) {
                System.out.println("Insufficient balance.");
                return;
            }

            // Temporarily load full data from target branch
            String originalBranch = selectedBranch;

            String[] tempFullName = new String[100];
            String[] tempUsername = new String[100];
            String[] tempPassword = new String[100];
            String[] tempPhone = new String[100];
            String[] tempCnic = new String[100];
            String[] tempAddress = new String[100];
            int[] tempAge = new int[100];
            String[] tempGender = new String[100];
            String[] tempAccountNumber = new String[100];
            String[] tempPins = new String[100];
            double[] tempBalances = new double[100];

            int tempCount = 0;

            File file = new File("customerDataFile_" + targetBranch + ".txt");
            if (!file.exists()) {
                System.out.println("Target branch does not exist or has no customer data.");
                return;
            }

            Scanner scFile = new Scanner(file);
            while (scFile.hasNextLine()) {
                String[] cols = scFile.nextLine().split(",");
                if (cols.length >= 11) {
                    tempFullName[tempCount] = cols[0];
                    tempUsername[tempCount] = cols[1];
                    tempPassword[tempCount] = cols[2];
                    tempPhone[tempCount] = cols[3];
                    tempCnic[tempCount] = cols[4];
                    tempAddress[tempCount] = cols[5];
                    tempAge[tempCount] = Integer.parseInt(cols[6]);
                    tempGender[tempCount] = cols[7];
                    tempAccountNumber[tempCount] = cols[8];
                    tempPins[tempCount] = cols[9];
                    tempBalances[tempCount] = Double.parseDouble(cols[10]);
                    tempCount++;
                }
            }
            scFile.close();


            // Find target customer by account number
            int targetIndex = -1;
            for (int i = 0; i < tempCount; i++) {
                if (tempAccountNumber[i].trim().equals(targetAccountNumber)) {
                    targetIndex = i;
                    break;
                }
            }

            if (targetIndex == -1) {
                System.out.println("Target account number not found in branch " + targetBranch);
                return;
            }

            // Do transfer
            balances[loggedInCustomer] -= amount;
            tempBalances[targetIndex] += amount;
            totalRevenue -= amount;
            saveRevenueDetails();

            // Log transaction in current branch
            if (transactionCount[loggedInCustomer] < 100) {
                transactionHistory[loggedInCustomer][transactionCount[loggedInCustomer]++] =
                        "Transferred " + amount + " to account " + targetAccountNumber + " (" + targetBranch + ")";
            }

            saveTransactionData();

            // Log transaction in target branch
            String transactionFileName = "transactions_" + targetBranch + ".txt";
            String tempFileName = "temp_" + targetBranch + ".txt";

            File originalFile = new File(transactionFileName);
            File tempFile = new File(tempFileName);

            String targetUsername = tempUsername[targetIndex];

            if (targetUsername == null || targetUsername.trim().isEmpty()) {
                System.out.println("Target username is missing or invalid.");
                return;
            }

            try {
                BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

                String newTransaction = "Received " + amount + " from " + customerAccountNumber[loggedInCustomer] + " (" + originalBranch + ")";
                boolean userFound = false;

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");

                    if (parts.length == 3 && parts[0].trim().equals(targetUsername.trim())) {
                        int currentCount = Integer.parseInt(parts[1]);
                        String updatedTransactionLog = parts[2] + ";" + newTransaction;
                        writer.println(targetUsername + "|" + (currentCount + 1) + "|" + updatedTransactionLog);
                        userFound = true;
                    } else {
                        writer.println(line); // unchanged
                    }
                }

                if (!userFound) {
                    writer.println(targetUsername + "|1|" + newTransaction); // new user
                }
                reader.close();
                writer.close();

                if (originalFile.exists() && originalFile.delete()) {
                    tempFile.renameTo(originalFile);
                } else {
                    System.out.println("Error replacing the transaction file.");
                }

            } catch (IOException e) {
                System.out.println("Error during transaction log update: " + e.getMessage());
            }

            // Save updated data in both branches
            saveCustomerDataToFile(); // for current branch

            // Save target branch data
            FileWriter fw2 = new FileWriter("customerDataFile_" + targetBranch + ".txt");
            PrintWriter pw1 = new PrintWriter(fw2);
            for (int i = 0; i < tempCount; i++) {
                pw1.printf("%s,%s,%s,%s,%s,%s,%d,%s,%s,%s,%.2f%n",
                        tempFullName[i],
                        tempUsername[i],
                        tempPassword[i],
                        tempPhone[i],
                        tempCnic[i],
                        tempAddress[i],
                        tempAge[i],
                        tempGender[i],
                        tempAccountNumber[i],
                        tempPins[i],
                        tempBalances[i]);
            }
            pw1.close();


            System.out.println();
            System.out.println("\033[1;90m------------------------------------------\033[0m");
            System.out.println("\033[1;96m             TRANSACTION RECEIPT          \033[0m");
            System.out.println("\033[1;90m------------------------------------------\033[0m");

            System.out.printf("\033[1;97m%-20s\033[0m: %s%n", "Sender Account", customerAccountNumber[loggedInCustomer]);
            System.out.printf("\033[1;97m%-20s\033[0m: %s%n", "Recipient Account", targetAccountNumber);
            System.out.printf("\033[1;97m%-20s\033[0m: %s%n", "Branch Transferred To", targetBranch);
            System.out.printf("\033[1;97m%-20s\033[0m: %.2f%n", "Amount Transferred", amount);
            System.out.printf("\033[1;97m%-20s\033[0m: %.2f%n", "Remaining Balance", balances[loggedInCustomer]);

            System.out.println("\033[1;90m------------------------------------------\033[0m");
            System.out.println("\033[1;96m         Transfer Completed Successfully!\033[0m");
            System.out.println("\033[1;90m------------------------------------------\033[0m");
            System.out.println();




        } catch (Exception e) {
            System.out.println("Error during transfer: " + e.getMessage());
        }
    }


    //----------------------------------------------------------------------------------
    /**
     * MENAHIL SULEMAN
     * FA24-BDS-029
     * */
    //----------------------------------------------------------------------------------

    // METHOD TO CREATE CUSTOMER ACCOUNT
    public static void createCustomerAccount() {
        // THIS CHECKS IF THE LIMIT FOR CUSTOMERS IN A BANK IS REACHED OR NOT
        if (customerCount >= 100) {
            System.out.println("Customer limit reached.");
            return;
        }

        System.out.println("\033[1;92m \nCREATE NEW CUSTOMER ACCOUNT \033[0m");
        // Full Name
        String fullName;
        while (true) {
            try {
                System.out.println("If you want to go back to the Admin menu Input 0:");
                System.out.print("Full Name: ");
                fullName = sc.nextLine().trim();
                if (fullName.equals("0")) {
                    System.out.println("Returning to Admin Menu...");
                    return;
                }
                if (!fullName.isEmpty()) {
                    customerFullName[customerCount] = fullName;
                    break;
                } else {
                    System.out.println("Name cannot be empty.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }

        // Gender
        String gender;
        while (true) {
            try {
                System.out.print("Gender (Male/Female): ");
                gender = sc.nextLine().trim();
                if (gender.equalsIgnoreCase("Male") ||
                        gender.equalsIgnoreCase("Female")) {
                    customerGender[customerCount] = gender;
                    break;
                } else {
                    System.out.println("Invalid input. Enter Male or Female.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }

        // Username
        String username;
        while (true) {
            try {
                System.out.print("Username: ");
                username = sc.nextLine().trim();

                if (username.isEmpty()) {
                    System.out.println("Username cannot be empty.");
                    continue;
                }

                int usernameStatus = checkUsernameAvailability(username); // CALLING HELPER METHOD

                switch (usernameStatus) {
                    case 1:
                        System.out.println("Error: This username already exists in THIS branch.");
                        continue;
                    case 2:
                        System.out.println("Error: This username already exists in ANOTHER branch.");
                        continue;
                    case 0:
                        customerUsername[customerCount] = username;
                        break;
                }
                break;
            } catch (NullPointerException e) {
                System.out.println("Error: Input cannot be null. Please try again.");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: Maximum number of customers reached.");
                return;
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }

        // Password
        String password;
        while (true) {
            try {
                System.out.print("Password (8-12 chars, with upper, lower, digit, and special !@#$%^&*): ");
                password = sc.nextLine();
                if (validatePassword(password)) { // HELPER METHOD CALLED HERE
                    customerPassword[customerCount] = password;
                    break;
                } else {
                    System.out.println("Invalid password. Must meet all conditions.");
                }
            } catch (Exception e) {
                System.out.println("AN ERROR HAS OCCURED PLEASE TRY AGAIN " + e.getMessage());
            }
        }

        // Phone Number
        while (true) {
            try {
                System.out.print("Phone Number (11 digits): ");
                String phone = sc.nextLine().trim();
                if (phone.length() == 11 && allDigits(phone)) {
                    customerPhone[customerCount] = phone;
                    break;
                } else {
                    System.out.println("Invalid phone number. Must be 11 digits.");
                }
            } catch (Exception e) {
                System.out.println("Error reading phone number. Try again");
            }
        }

        // Age
        while (true) {
            try {
                System.out.print("Age (must be 18 or older): ");
                if (sc.hasNextInt()) {
                    int age = sc.nextInt();
                    sc.nextLine();
                    if (age >= 18 && age <= 100) {
                        customerAge[customerCount] = age;
                        break;
                    } else {
                        System.out.println("Age must be between 18 and 100.");
                    }
                } else {
                    System.out.println("Enter a valid number.");
                    sc.nextLine(); // clear invalid input
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid age input. Please enter numbers only.");
            }
        }

        // CNIC
        String cnic;
        while (true) {
            try {
                System.out.print("Enter CNIC (XXXXX-XXXXXXX-X): ");
                cnic = sc.nextLine().trim();
                if (!isCnicValid(cnic)) {
                    System.out.println("Invalid format! Must be XXXXX-XXXXXXX-X (13 digits with dashes)");
                    System.out.println("Please try Again");
                    continue;
                }
                int check = checkUserCnicAvailability(cnic);
                if (check == 1) {
                    System.out.println("CNIC already exists in the current branch. Try a new one.");
                } else if (check == 2) {
                    System.out.println("CNIC already exists in another branch. Try a new one.");
                } else if (check == 0) {
                    customerCnic[customerCount] = cnic;
                    System.out.println("CNIC updated successfully.");
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        // Address
        while (true) {
            try {
                System.out.print("Address: ");
                String address = sc.nextLine().trim();
                if (!address.isEmpty()) {
                    customerAddress[customerCount] = address;
                    break;
                } else {
                    System.out.println("Address cannot be empty.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }

        // Generate unique account number
        String accountNumber;
        while (true) {
            accountNumber = generateAccountNumber();
            boolean unique = true;
            for (int i = 0; i < customerCount; i++) {
                if (customerAccountNumber[i] != null && customerAccountNumber[i].equals(accountNumber)) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                customerAccountNumber[customerCount] = accountNumber;
                break;
            }
        }

        // 8-digit PIN
        while (true) {
            try {
                System.out.print("Enter 8-digit PIN: ");
                String pin = sc.nextLine();
                if (pin.length() == 8 && isNumeric(pin)) {
                    pins[customerCount] = pin;
                    break;
                } else {
                    System.out.println("Invalid PIN. Must be exactly 8 digits.");
                }
            } catch (Exception e) {
                System.out.println("An exception has occurred while getting PIN " + e.getMessage());
            }
        }

        // Initial Deposit
        while (true) {
            try {
                System.out.print("Initial Deposit (minimum 1000): ");
                if (sc.hasNextDouble()) {
                    double deposit = sc.nextDouble();
                    sc.nextLine();
                    if (deposit >= 1000) {
                        // Here we are deducting 200 for account creation fee
                        balances[customerCount] = deposit - 200;
                        totalRevenue += 200;
                        saveRevenueDetails();
                        break;
                    } else {
                        System.out.println("Deposit must be at least 1000.");
                    }
                } else {
                    System.out.println("Invalid amount. Please enter a number.");
                    sc.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
        System.out.println("Account created successfully!");
        System.out.println("Your Account Number: " + accountNumber);
        System.out.println("Rs 200 have been deducted as account creation fee.");
        System.out.println("Thank you for creating an account.");

        // increasing the customer count upon creating a new account
        customerCount++;
        // Calling saving methods to save the data in files
        saveLoanData();
        saveCustomerDataToFile();
        saveBankAmount();
    }

    // METHOD TO DELETE THE CUSTOMER ACCOUNT
    public static void deleteCustomerAccount() {
        if (customerCount == 0) {
            System.out.println("No customer accounts to delete.");
            return;
        }

        try {
            System.out.println("\033[1;91m \nDELETE CUSTOMER ACCOUNT \033[0m");
            System.out.print("Enter the username of the customer to delete: ");
            //ENTER USERNAME
            String username = sc.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("Error: Username cannot be empty.");
                return;
            }

            int index = -1;
            for (int i = 0; i < customerCount; i++) {
                if (customerUsername[i].equals(username)) {
                    index = i;
                    break;
                }


                if (index == -1) {
                    System.out.println("Customer with the given username was not found.");
                    return;
                }
            }
            // GO FROM THE INDEX TO THE END AND MAKE THE ELEMENTS SHIT BACK
            for (int i = index; i < customerCount - 1; i++) {
                customerUsername[i] = customerUsername[i + 1];
                customerPassword[i] = customerPassword[i + 1];
                customerFullName[i] = customerFullName[i + 1];
                customerPhone[i] = customerPhone[i + 1];
                customerCnic[i] = customerCnic[i + 1];
                customerAddress[i] = customerAddress[i + 1];
                balances[i] = balances[i + 1];
                customerAccountNumber[i] = customerAccountNumber[i + 1];
            }

            // EMPTYING THE LAST ARRAY
            customerUsername[customerCount - 1] = null;
            customerPassword[customerCount - 1] = null;
            customerFullName[customerCount - 1] = null;
            customerPhone[customerCount - 1] = null;
            customerCnic[customerCount - 1] = null;
            customerAddress[customerCount - 1] = null;
            customerAccountNumber[customerCount - 1] = null;
            balances[customerCount - 1] = 0.0;

            customerCount--;

            System.out.println("Customer account deleted successfully.");
            saveCustomerDataToFile();

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("System error: Customer data corruption detected");
        } catch (NullPointerException e) {
            System.out.println("Missing customer data");
        } catch (Exception e) {
            System.err.println("An exception has occured: " + e.getMessage());
        }

    }

    // METHOD TO DISPLAY ALL CUSTOMER RECORD
    public static void viewAllCustomerRecord() {
        if (customerCount == 0) {
            System.out.println("No customer accounts found.");
            return;
        }
        System.out.println("\033[1;95m  CUSTOMER ACCOUNTS REPORT  \033[0m");

        try {
            for (int i = 0; i < customerCount; i++) {
                System.out.println("\033[1;97m\nCustomer #" + (i + 1) + "\033[0m");
                System.out.println("Full Name      : " + customerFullName[i]);
                System.out.println("Gender         : " + customerGender[i]);
                System.out.println("Username       : " + customerUsername[i]);
                System.out.println("Age            : " + customerAge[i] + " years old");
                System.out.println("Phone Number   : " + customerPhone[i]);
                System.out.println("CNIC           : " + customerCnic[i]);
                System.out.println("Address        : " + customerAddress[i]);
                System.out.println("Account Number : " + customerAccountNumber[i]);
                System.out.println("Balance        : " + balances[i]);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Tried to access data beyond available records.");
        } catch (NullPointerException e) {
            System.out.println("Error: Some customer information is missing (null). Please check data initialization.");
        }
    }

    // METHOD TO DISPLAY ONLY ONE CUSTOMER RECORD BASED ON USERNAME
    public static void viewOneCustomerRecord() {
        if (customerCount == 0) {
            System.out.println("No customer accounts found.");
            return;
        }

        while (true) {
            try {
                System.out.println("Enter 0 if you want to exit this menu :");
                System.out.print("Enter the username to see the record: ");
                String username = sc.nextLine().trim();
                if (username.equals("0")) {
                    return;
                }

                boolean found = false;

                for (int i = 0; i < customerCount; i++) {
                    if (customerUsername[i].equals(username)) {
                        System.out.println("\033[1;97m\nCustomer #" + (i + 1) + "\033[0m");
                        System.out.println("Full Name      : " + customerFullName[i]);
                        System.out.println("Gender         : " + customerGender[i]);
                        System.out.println("Username       : " + customerUsername[i]);
                        System.out.println("Age            : " + customerAge[i] + " years old");
                        System.out.println("Phone Number   : " + customerPhone[i]);
                        System.out.println("CNIC           : " + customerCnic[i]);
                        System.out.println("Address        : " + customerAddress[i]);
                        System.out.println("Account Number : " + customerAccountNumber[i]);
                        System.out.println("Balance        : " + balances[i]);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    throw new IllegalArgumentException("No customer found with the username: " + username);
                } else {
                    break;  // EXITS THE LOOP UPON FINDING THE USER
                }

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Please enter a valid username.\n");
                sc.nextLine(); // Clearing buffer
            }
        }
    }


    // A HELPER MEHTOD TO VALIDATE PASSWORD IN BOTH ADMIN AND CUSTOMER MENU
    public static boolean validatePassword(String password) {
        try {
            boolean hasUpper = false;
            boolean hasLower = false;
            boolean hasDigit = false;
            boolean hasSpecial = false;

            String specialChars = "!@#$%^&*";

            if (password.length() < 8 || password.length() > 12) {
                return false;
            }

            for (int i = 0; i < password.length(); i++) {
                char ch = password.charAt(i);
                if (Character.isUpperCase(ch)) hasUpper = true;
                else if (Character.isLowerCase(ch)) hasLower = true;
                else if (Character.isDigit(ch)) hasDigit = true;
                else if (specialChars.indexOf(ch) != -1) hasSpecial = true;
            }

            return hasUpper && hasLower && hasDigit && hasSpecial;

        } catch (NullPointerException e) {
            System.out.println("Password cannot be null.");
            return false;
        }
    }

    // ATM Login menu
    public static void ATMlogin() {
        int attempts = 0;

        while (attempts < 3) {
            try {
                System.out.println("\033[1;95m ATM LOGIN \033[0m");
                String accNumber;
                while (true) {
                    System.out.println("ENTER 0 TO EXIT");
                    System.out.print("Enter Account Number: ");
                    accNumber = sc.nextLine().trim();

                    if(accNumber.equals("0")){
                        return;
                    }

                    if (!allDigits(accNumber)) {
                        System.out.println("Please enter digits only for the account number.");
                    } else {
                        break;
                    }
                }

                String pin;
                while (true) {
                    System.out.print("Enter 8-digit PIN: ");
                    pin = sc.nextLine().trim();

                    if (!allDigits(pin) || pin.length() != 8) {
                        System.out.println("PIN must be exactly 8 digits and contain digits only.");
                    } else {
                        break;
                    }
                }

                // Match ONLY the currently logged-in customer
                if (customerAccountNumber[loggedInCustomer] != null &&
                        customerAccountNumber[loggedInCustomer].equals(accNumber) &&
                        pins[loggedInCustomer] != null &&
                        pins[loggedInCustomer].equals(pin)) {

                    if (blockedAccount[loggedInCustomer] == 1) {
                        System.out.println("\033[1;91mThis account is blocked. Please contact the bank.\033[0m");
                        mainMenu();
                        return;
                    }

                    System.out.println("Login successful. Welcome, " + customerFullName[loggedInCustomer] + "!");
                    ATMMenu();
                    return;

                } else {
                    attempts++;
                    System.out.println("Invalid account number or PIN. Attempts left: " + (3 - attempts));
                }

            } catch (NullPointerException e) {
                System.out.println("Error: Missing customer information.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }

        System.out.println("Too many failed attempts. Returning to main menu.");
        mainMenu();
    }

    // OUR MAIN CUSTOMER MENU TO DISPLAY METHODS
    public static void customerMenu(int loggedInCustomer) {
        while (true) {
            System.out.println("\n\033[1;96mCUSTOMER MENU:\033[0m");
            System.out.println("1. ATM Menu");
            System.out.println("2. View Account Details");
            System.out.println("3. Edit Account Details");
            System.out.println("4. View Transaction History");
            System.out.println("5. Loan Services");
            System.out.println("6. Return to Main Menu");
            System.out.print("Enter your choice (1-6): ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        ATMlogin();  // CALLING ATM LOGIN WHICH IS RESPONSIBLE FOR DEPOSIT/WITHDRAW/TRANSACTION
                        break;
                    case 2:
                        viewCustomerDetails(loggedInCustomer); //CUSTOMER DETAIL MENU
                        break;
                    case 3:
                        editCustomerDetails(); // EDITING THE CUSTOMER DETAILS
                        break;
                    case 4:
                        viewTransactionOfSameAccount(); //TRANSACTION DETAILS
                        break;
                    case 5:
                        displayLoanMenu(loggedInCustomer); // CUSTOMER LOAN MENU
                        break;
                    case 6:
                        System.out.println("Returning to main menu...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1 to 6.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a Valid number.");
                sc.nextLine(); // CLEARING INVALID INPUT SO THAT IT DOESNT CREATE ANY PROBLEM
            }
        }
    }


    // METHOD TO RECORD THE TRANSACTIONS
    public static void recordTransaction(int customerIndex, String message) {
        try {
            if (transactionCount[customerIndex] < 100) {
                transactionHistory[customerIndex][transactionCount[customerIndex]] = message;
                transactionCount[customerIndex]++;
            } else {
                System.out.println("Transaction history limit reached for this customer.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Invalid customer index provided. Cannot record transaction.");
        } catch (NullPointerException e) {
            System.out.println("Error: Transaction history not initialized properly for this customer.");
        }
    }

    // TRANSACTION MENU IN ADMIN MENU
    public static void transactionMenu() {
        while (true) {
            System.out.println("\n\033[1;95m Admin Transaction History Menu: \033[0m");
            System.out.println("1. View All Transaction Histories");
            System.out.println("2. View One Customer's Transactions");
            System.out.println("3. Return to Admin Menu");

            System.out.print("Enter your choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        viewAllTransactionHistories(); // METHOD WHICH DISPLAYS ALL THE TRANSACTIONS
                        break;
                    case 2:
                        viewTransactionByAccountNumber();   // METHOD WHICH SEARCHES AND DISPLAYS
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                sc.nextLine(); // CLEARING INVALID INPUT
            }
        }
    }

    // METHOD TO DISPLAY THE TRANSACTION HISTORIES OF ALL CUSTOMERS
    public static void viewAllTransactionHistories() {
        try {
            System.out.println("\033[1;92m\nAll Users' Transaction Histories\033[0m");


            if (customerCount == 0) {
                System.out.println("No customers in the system.");
                return;
            }

            for (int i = 0; i < customerCount; i++) {
                System.out.println("\n----------------------------------");
                System.out.println("Customer: " + customerFullName[i]);
                System.out.println("Account Number: " + customerAccountNumber[i]);

                if (transactionCount[i] == 0) {
                    System.out.println("No transactions.");
                } else {
                    for (int j = 0; j < transactionCount[i]; j++) {
                        System.out.println(" - " + transactionHistory[i][j]);
                    }
                }
            }
            System.out.println("\n----------------------------------");
        } catch (NullPointerException e) {
            System.out.println("Error: Missing transaction data.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Data indexing problem occurred.");
        }
    }

    // THIS METHOD TAKES ACCOUNT NUMBER AS INPUT INSIDE IT AND THEN DISPLAYS THE TRANSACTIONS
    public static void viewTransactionByAccountNumber() {
        while (true) {
            try {
                System.out.println("\033[1;92m\nUser Transaction History\033[0m");
                System.out.print("\nEnter account number: ");
                String acc = sc.nextLine().trim();

                if (acc.isEmpty()) {
                    System.out.println("Account number cannot be empty. Please try again.");
                    continue;
                }

                // CHECKING IF THE ACC NO IS VALID
                if (!allDigits(acc)) {
                    throw new NumberFormatException("Account number must contain digits only.");
                }

                int index = -1;
                for (int i = 0; i < customerCount; i++) {
                    if (customerAccountNumber[i].equals(acc)) {
                        index = i;
                        break;
                    }
                }

                if (index == -1) {
                    System.out.println("Account not found. Please enter a valid account number.");
                    continue;
                }

                // USING THE VARIABLE index TO SHOW TRANSACTIONS
                System.out.println("\nTransaction History for " + customerFullName[index]);
                if (transactionCount[index] == 0) {
                    System.out.println("No transactions found.");
                } else {
                    for (int i = 0; i < transactionCount[index]; i++) {
                        System.out.println(" - " + transactionHistory[index][i]);
                    }
                }
                break;  // Success, exit loop

            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    // THIS METHOD IS USED IN CUSTOMER MENU AFTER LOGIN TO DISPLAY THE
    public static void viewTransactionOfSameAccount() {
        while (true) {
            try {
                System.out.println("\033[1;96m\n User' Transaction History\033[0m");
                int index = -1;
                for (int i = 0; i < customerCount; i++) {
                    if (customerUsername[i].equals(customerUsername[loggedInCustomer])) {
                        index = i;
                        break;
                    }
                }

                if (index == -1) {
                    System.out.println("Username not found. Please enter a valid username.");
                    continue;
                }

                System.out.println("\033[1;95m\nTransaction History for " + customerFullName[index] + "\033[0m");

                if (transactionCount[index] == 0) {
                    System.out.println("No transactions found.");
                } else {
                    for (int i = 0; i < transactionCount[index]; i++) {
                        System.out.println(" - " + transactionHistory[index][i]);
                    }
                }
                break;  // Success, exit loop

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    // METHOD TO SEE THE DATA OF THE CUSTOMER IN ADMIN MENU
    public static void customerRecordsMenu() {
        while (true) {
            System.out.println("\n\033[1;95m CUSTOMER RECORDS MENU\033[0m");
            System.out.println("1. View All Customer Records");
            System.out.println("2. View One Customer's Record");
            System.out.println("3. Return to Admin Menu");

            System.out.print("Enter your choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        viewAllCustomerRecord(); // ALL DATA IS DISPLAYED
                        break;
                    case 2:
                        viewOneCustomerRecord();    // DATA OF ONLY ONE IS SHOWN
                        break;
                    case 3:
                        return;  // Go back to admin menu
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number (1-3).");
                sc.nextLine();
            }
        }
    }

    // CUSTOMER LOGIN
    public static void customerLogin() {
        // IF NO ACCOUNT FOUND THEN THIS MESSAGE WILL BE DISPLAYED
        if (customerCount == 0) {
            System.out.println("\n\033[1;91mNo customer accounts exist yet!\033[0m");
            System.out.println("\n\033[1;91mPlease create a customer Account in Admin Menu first\033[0m");
            mainMenu();
            return;
        }

        int attempts = 3;

        while (attempts > 0) {
            try {
                System.out.println("\n\033[1;95mCUSTOMER LOGIN:\033[0m");
                System.out.print("Enter Username: ");
                String username = sc.nextLine();

                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                boolean found = false;

                for (int i = 0; i < customerCount; i++) {
                    if (customerUsername[i].equals(username) && customerPassword[i].equals(password)) {

                        if (blockedAccount[i] == 1) {
                            System.out.println("\033[1;91mThis account is blocked. Please contact the bank.\033[0m");
                            mainMenu();
                            return;
                        }

                        System.out.println("Login successful!");
                        loggedInCustomer = i;
                        customerMenu(loggedInCustomer);
                        return;
                    }
                }

                attempts--;
                System.out.println("Invalid credentials! Attempts remaining: " + attempts);

            } catch (InputMismatchException e) {
                System.out.println("Invalid input type. Please enter valid text.");
                sc.nextLine();
            } catch (NullPointerException e) {
                System.out.println("Data corruption detected. Please contact admin.");
                return;
            }
        }

        System.out.println("Maximum login attempts reached. Access denied.");
        mainMenu();
    }

    // TRANFERING INSIDE THE BRANCH
    public static void transferToSameBranch(int userIndex) {
        String targetAccount;
        int targetIndex;

        while (true) {
            System.out.println("\n\033[1;96m Tranfer Amount \033[0m");
            System.out.print("Enter target account number: ");
            targetAccount = sc.nextLine().trim();

            if (targetAccount.isEmpty()) {
                System.out.println("Account number cannot be empty.\n");
                continue;
            }

            // CHECKING IF ITS THE OWN ACCOUNT NUMBER
            if (targetAccount.equals(customerAccountNumber[userIndex])) {
                System.out.println("Cannot transfer to your own account.\n");
                continue;
            }

            targetIndex = -1;
            for (int i = 0; i < customerCount; i++) {
                if (customerAccountNumber[i].equals(targetAccount)) {
                    targetIndex = i;
                    break;
                }
            }

            if (targetIndex == -1) {
                System.out.println("Target account not found.\n");
                continue;
            }

            break;
        }

        double transferAmount;

        while (true) {
            System.out.print("Enter amount to transfer: ");
            String amountInput = sc.nextLine().trim();

            // CALLING THE VALID FORMAT METHOD
            if (!isValidAmountFormat(amountInput)) {
                System.out.println("Invalid amount format. Use numbers only (max 2 decimal places).\n");
                continue;
            }

            try {
                transferAmount = Double.parseDouble(amountInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number entered. Please try again.\n");
                continue;
            }

            if (transferAmount <= 0) {
                System.out.println("Amount must be greater than zero.\n");
            } else if (transferAmount > balances[userIndex]) {
                System.out.println("Insufficient balance.\n");
            } else {
                break;
            }
        }
        double cut = 0;
        if (transferAmount >= 100000) {
            cut = transferAmount * 0.01;
            totalRevenue += cut;
            saveRevenueDetails();
            transferAmount -= cut;
            System.out.println("A 1% fee of " + cut + " has been deducted. Final transfer amount: " + transferAmount);
        }

        balances[userIndex] -= transferAmount + cut;
        balances[targetIndex] += transferAmount;

        // TO RECORD THE TRANSACTIONS
        String senderMessage = "Transferred Rs. " + transferAmount + " to account " + customerAccountNumber[targetIndex];
        String receiverMessage = "Received Rs. " + transferAmount + " from account " + customerAccountNumber[userIndex];

        try {
            //CALLING EMTHOD TO RECORD THE TRANSACTIONS
            recordTransaction(userIndex, senderMessage);
            recordTransaction(targetIndex, receiverMessage);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error recording transaction. Transaction history may be full.");
            return;
        }

        System.out.println("\n\033[1;94mTransaction Successful!\033[0m");
        System.out.println("--------------------------------------------------");
        System.out.println("\033[1;95mTransfer Receipt\033[0m");
        System.out.println("--------------------------------------------------");
        System.out.println("Sender Name      : " + customerFullName[userIndex]);
        System.out.println("Sender Account   : " + customerAccountNumber[userIndex]);
        System.out.println("Receiver Name    : " + customerFullName[targetIndex]);
        System.out.println("Receiver Account : " + customerAccountNumber[targetIndex]);
        System.out.println("Amount           : Rs. " + transferAmount);
        System.out.println("Remaining Balance: Rs. " + balances[userIndex]);
        System.out.println("--------------------------------------------------\n");
        saveCustomerDataToFile();
        saveBankAmount();
        saveTransactionData();
    }


    // DISPLAY DETAILS METHOD
    public static void viewCustomerDetails(int userIndex) {
        try {
            System.out.println("\n\033[1;96mCustomer Profile Details\033[0m");
            System.out.println("Full Name       : " + customerFullName[userIndex]);
            System.out.println("Username        : " + customerUsername[userIndex]);
            System.out.println("Password        : " + customerPassword[userIndex]);
            System.out.println("Account Number  : " + customerAccountNumber[userIndex]);
            System.out.println("PIN             : " + pins[userIndex]);
            System.out.println("Phone Number    : " + customerPhone[userIndex]);
            System.out.println("CNIC            : " + customerCnic[userIndex]);
            System.out.println("Address         : " + customerAddress[userIndex]);
            System.out.println("Age             : " + customerAge[userIndex]);
            System.out.println("Current Balance : Rs. " + balances[userIndex]);
            System.out.println("\033[1;96m-------------------------------\033[0m\n");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Invalid customer index. Cannot retrieve details.");
        }
    }

    // ATM MENU
    public static void ATMMenu() {
        while (true) {
            try {
                System.out.println("\033[1;94m \nATM MENU\033[0m");
                System.out.println("1. Check Balance");
                System.out.println("2. Withdraw Cash");
                System.out.println("3. Deposit Cash");
                System.out.println("4. Transfer Amount");
                System.out.println("5. Rapid Withdrawal");
                System.out.println("6. Rapid Deposit");
                System.out.println("7. Transfer to another branch");
                System.out.println("8. Go to Customer Menu");


                System.out.print("Enter your choice (1-8): ");
                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        checkBalanceATM();
                        break;
                    case 2:
                        withdrawCashATM();
                        break;
                    case 3:
                        depositCashATM();
                        break;
                    case 4:
                        transferToSameBranch(loggedInCustomer);
                        break;
                    case 5:
                        rapidWithdraw();
                        break;
                    case 6:
                        rapidDeposit();
                        break;
                    case 7:
                        interBranchTransfer();
                        break;
                    case 8:
                        System.out.println("Going back to Customer Menu...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 8.");
                sc.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    // TO CHEKC BALANCE
    static void checkBalanceATM() {
        try {
            System.out.println("\n\033[1;96m Current Balance \033[0m");
            System.out.println("Your current balance is: Rs. " + balances[loggedInCustomer]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Invalid account access.");
        } catch (NullPointerException e) {
            System.out.println("Error: Account information is missing.");
        }
    }

    // TO WITHDRAW
    static void withdrawCashATM() {
        try {
            System.out.println("\n\033[1;96m Withdraw Cash \033[0m");
            System.out.print("Enter amount to withdraw: Rs. ");
            double amount = sc.nextDouble();
            sc.nextLine(); // Consume newline

            if (amount <= 0) {
                System.out.println("Invalid amount. Please enter a positive value.");
                return;
            }

            if (amount > balances[loggedInCustomer]) {
                System.out.println("Insufficient funds.");
                return;
            }

            balances[loggedInCustomer] -= amount;
            bankAmount += amount;
            recordTransaction(loggedInCustomer, "ATM Withdrawal: Rs. " + amount);
            System.out.println("Please take your cash.");
            System.out.println("Remaining balance: Rs. " + balances[loggedInCustomer]);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid numeric amount.");
            sc.nextLine(); // Clear invalid input
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Invalid account access.");
        } catch (NullPointerException e) {
            System.out.println("Error: Account information is missing.");
        }
        saveCustomerDataToFile();
        bankAmount();
        saveTransactionData();
    }

    // TO DEPOSIT
    public static void depositCashATM() {
        try {
            System.out.println("\n\033[1;96m Deposit Cash \033[0m");
            System.out.print("Enter amount to deposit: Rs. ");
            double amount = sc.nextDouble();
            sc.nextLine();

            if (amount <= 0) {
                System.out.println("Invalid amount. Please enter a positive value.");
                return;
            }

            balances[loggedInCustomer] += amount;
            bankAmount += amount;
            recordTransaction(loggedInCustomer, "ATM Deposit: Rs. " + amount);
            System.out.println("Deposit successful.");
            System.out.println("New balance: Rs. " + balances[loggedInCustomer]);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid numeric amount.");
            sc.nextLine();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Invalid account access.");
        } catch (NullPointerException e) {
            System.out.println("Error: Account information is missing.");
        }
        saveCustomerDataToFile();
        saveTransactionData();
        saveBankAmount();
    }


    public static void rapidDeposit() {
        try {
            System.out.println("\n\033[1;96m Rapid Deposit \033[0m");
            System.out.print("\nChoose amount to Deposit:\n1. Rs. 100\n2. Rs. 500\n3. Rs. 1000\n4. Rs. 5000\nEnter a Choice (1-4): ");
            int choice = sc.nextInt();
            sc.nextLine();
            int amount = 0;

            switch (choice) {
                case 1:
                    amount = 100;
                    break;
                case 2:
                    amount = 500;
                    break;
                case 3:
                    amount = 1000;
                    break;
                case 4:
                    amount = 5000;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    return;
            }

            balances[loggedInCustomer] += amount;
            bankAmount += amount;
            recordTransaction(loggedInCustomer, "ATM Rapid Deposit: Rs. " + amount);
            System.out.println("Rapid deposit of Rs. " + amount + " successful. New balance: Rs. " + balances[loggedInCustomer]);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number from 1 to 4.");
            sc.nextLine(); // clear invalid input
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("Error accessing account data. Please try again.");
        }
        saveCustomerDataToFile();
        saveBankAmount();
        saveTransactionData();
    }

    public static void rapidWithdraw() {
        try {
            System.out.println("\n\033[1;96m Rapid Withdraw\033[0m");
            System.out.print("\nChoose amount to Withdraw:\n1. Rs. 100\n2. Rs. 500\n3. Rs. 1000\n4. Rs. 5000\nEnter a Choice (1-4): ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline
            int amount = 0;

            switch (choice) {
                case 1:
                    amount = 100;
                    break;
                case 2:
                    amount = 500;
                    break;
                case 3:
                    amount = 1000;
                    break;
                case 4:
                    amount = 5000;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    return;
            }

            if (balances[loggedInCustomer] >= amount) {
                balances[loggedInCustomer] -= amount;
                recordTransaction(loggedInCustomer, "ATM Rapid Withdraw: Rs. " + amount);
                System.out.println("Rapid withdrawal of Rs. " + amount + " successful. New balance: Rs. " + balances[loggedInCustomer]);
                bankAmount -= amount;
            } else {
                System.out.println("Insufficient balance....");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number from 1 to 4.");
            sc.nextLine(); // clear invalid input
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("Error accessing account data. Please try again.");
        }
        saveCustomerDataToFile();
        saveBankAmount();
        saveTransactionData();
    }

    public static void heading() {
        System.out.println("\033[1;94mInstructor Name:\033[0m \033[1;97mSir Rizwan Rashid\033[0m\n");
        System.out.println("\033[1;94mCourse Code:\033[0m \033[1;97mProgramming Fundamentals CSC-103\033[0m\n");
        System.out.println("\033[1;94mSubmitted by:\033[0m\n");

        System.out.printf("\033[1;96m%-35s %-20s\033[0m\n", "Name", "Registration No.");
        System.out.println("\033[1;90m--------------------------------------------------------------\033[0m");
        System.out.printf("\033[1;97m%-35s %-20s\033[0m\n", "Abdullah Bin Abdul Mannan", "FA24-BDS-006");
        System.out.printf("\033[1;97m%-35s %-20s\033[0m\n", "Ahmed Abdullah", "FA24-BDS-009");
        System.out.printf("\033[1;97m%-35s %-20s\033[0m\n", "Arsal Abdullah Khan", "FA24-BDS-017");
        System.out.printf("\033[1;97m%-35s %-20s\033[0m\n", "Menahil Suleman", "FA24-BDS-029");

        System.out.println();
        System.out.println("\033[1;94mDate:\033[0m \033[1;97m17 - June - 2025\033[0m");
        System.out.println("\033[1;94mInstitute:\033[0m \033[1;97mCOMSATS University Islamabad\033[0m");
        System.out.println("\033[1;90m\n--------------------------------------------------------------\033[0m");
    }

    // HELPER METHOD FOR VALID CNIC
    public static boolean isCnicValid(String cnic) {
        if (cnic.length() != 15) {
            return false;
        }

        if (cnic.charAt(5) != '-' || cnic.charAt(13) != '-') {
            return false;
        }

        for (int i = 0; i < cnic.length(); i++) {
            char c = cnic.charAt(i);
            if (i == 5 || i == 13) {
                continue;
            }
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    // HELPER METHOD FOR AMOUNT TRANSFER
    public static boolean isValidAmountFormat(String input) {
        try {
            if (input == null) {
                throw new NullPointerException("Input cannot be null.");
            }
            int decimalPointCount = 0;
            int decimalPlaces = 0;

            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);

                if (c == '.') {
                    decimalPointCount++;
                    if (decimalPointCount > 1) return false;
                } else if (!Character.isDigit(c)) {
                    return false;
                } else if (decimalPointCount == 1) {
                    decimalPlaces++;
                    if (decimalPlaces > 2) return false;
                }
            }

            return !input.isEmpty();

        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }


    // METHOD TO MANAGE BLOCKED ACCOUNTS
    public static void manageBlockedAccounts() {
        System.out.println("\n\033[1;91m BLOCKED ACCOUNTS MANAGEMENT \033[0m");

        // List all blocked accounts
        boolean foundBlocked = false;
        for (int i = 0; i < customerCount; i++) {
            if (blockedAccount[i] == 1) {
                foundBlocked = true;
                System.out.println("\n\033[1;97m Account #" + (i + 1) +"\033[0m");
                System.out.println("Name: " + customerFullName[i]);
                System.out.println("Username: " + customerUsername[i]);
                System.out.println("Account Number: " + customerAccountNumber[i]);
                System.out.println("Blocked for 2 penalty points");
            }
        }

        if (!foundBlocked) {
            System.out.println("No blocked accounts found.");
            return;
        }

        // Option to unblock
        System.out.print("\nEnter account number to unblock (or '0' to cancel): ");
        String accNum = sc.nextLine().trim();

        if (accNum.equals("0")) {
            return;
        }

        int index = -1;
        for (int i = 0; i < customerCount; i++) {
            if (customerAccountNumber[i].equals(accNum)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Account not found.");
            return;
        }

        if (blockedAccount[index] == 0) {
            System.out.println("This account is not blocked.");
            return;
        }

        System.out.print("Are you sure you want to unblock " + customerFullName[index] + "'s account? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            blockedAccount[index] = 0;
            System.out.println("\033[1;92mAccount has been unblocked\033[0m");
            saveLoanData();
            saveLoanRepaymentData();
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    //--------------------------------------------------------------------------
    /** ABDULLAH BIN ABDUL MANNAN METHODS
     * FA24-BDS-006*/
    //--------------------------------------------------------------------------
    public static void displayLoanEligibilityReport(int index) {
        try {
            System.out.println("\n\033[1;95mLOAN ELIGIBILITY REPORT:\033[0m");
            System.out.println("\n\n\033[1;96mCUSTOMER INFO \033[0m");
            System.out.printf("%-25s: %s\n", "Customer Name", customerFullName[index]);
            System.out.printf("%-25s: %s\n", "Account Number", customerAccountNumber[index]);
            System.out.printf("%-25s: %d years\n", "Age", customerAge[index]);
            System.out.printf("%-25s: %s\n", "CNIC", customerCnic[index]);
            System.out.printf("%-25s: %s\n", "Phone", customerPhone[index]);
            System.out.printf("%-25s: %s\n", "Gender", customerGender[index]);
            System.out.printf("%-25s: %s\n", "Address", customerAddress[index]);

            // Financial Info
            System.out.println("\n\033[1;94mFINANCIAL INFO:\033[0m");
            System.out.printf("%-25s: Rs%.2f\n", "Current Balance", balances[index]);
            System.out.printf("%-25s: Rs%.2f\n", "Requested Loan Amount", loanRequests[index]);
            System.out.printf("%-25s: %.2f years\n", "Loan Duration", loanTime[index]);
            System.out.printf("%-25s: %.2f%%\n", "Interest Rate", interestRate * 100);
            System.out.printf("%-25s: Rs%.2f\n", "Total Interest", interest[index]);

            // Credit score (basic simulation)
            int creditScore = calculateCreditScore(index);
            System.out.printf("%-25s: %d / 100\n", "Estimated Credit Score", creditScore);

            // Eligibility verdict
            String verdict = (creditScore >= 60 && balances[index] >= 1000 && customerAge[index] >= 18)
                    ? "Likely Eligible" : "High Risk";
            System.out.printf("%-25s: %s\n", "System Verdict", verdict);

            System.out.println("-------------------------------------------------------------\n");
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("Error: Invalid customer data for index " + index + ". " + e.getMessage());
        }
    }

    public static int calculateCreditScore(int index) {
        int score = 0;

        // Age contribution
        if (customerAge[index] >= 21 && customerAge[index] <= 60) score += 25;
        else if (customerAge[index] >= 18 && customerAge[index] < 21) score += 15;

        // Balance contribution
        if (balances[index] >= 10000) score += 30;
        else if (balances[index] >= 5000) score += 20;
        else if (balances[index] >= 1000) score += 10;

        // Loan amount vs balance (less risky if smaller)
        if (loanRequests[index] <= balances[index] * 0.5) score += 25;
        else if (loanRequests[index] <= balances[index]) score += 15;

        // Loan duration (shorter = better)
        if (loanTime[index] <= 2) score += 10;

        return score;
    }

    //--------------------------------------------------------------------------------
    //--------------------------LOAN METHODS------------------------------------------
    //--------------------------------------------------------------------------------

    // Menu method for loan operations in customer menu

    static void displayLoanMenu(int loggedInCustomer) {
        int choice;
        do {
            System.out.println("\033[1;95mLOAN SERVICES\033[0m");
            System.out.println("1. Apply for Loan - Submit your loan request");
            System.out.println("2. Check Loan Status - View your current loan approval status");
            System.out.println("3. Repay Loan.");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    applyForLoan();
                    break;
                case 2:
                    checkLoanStatus();
                    break;
                case 3:
                    try {
                        repayLoanYearly(loggedInCustomer);
                    } catch (ParseException e) {
                        System.out.println("Invalid Date Format ! " + e.getMessage());
                    }

                case 4:
                    System.out.println("Exiting the system. Thank you!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    static void viewLoanRequests() {
        try {
            System.out.println("\033[1;93m \n PENDING LOAN REQUESTS \033[0m");
            boolean found = false;
            for (int i = 0; i < customerAccountNumber.length; i++) {
                if (loanRequests[i] > 0 && "Pending".equals(loanStatus[i])) {
                    System.out.println("Account: " + customerAccountNumber[i] + " - Amount: Rs" + loanRequests[i]);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No pending loan requests.");
            }
        } catch (NullPointerException npe) {
            System.out.println("Data not initialized properly: " + npe.getMessage());
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.out.println("Array index error: " + aioobe.getMessage());
        }
        saveLoanData();
    }


    public static void manageLoans() {
        while (true) {
            try {
                System.out.println("\033[1;94m \nLOAN MANAGEMENT \033[0m");
                System.out.println("1. View Pending Loan Requests");
                System.out.println("2. Approve/Reject Loan");
                System.out.println("3. Back to Admin Menu");

                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        viewLoanRequests();
                        break;
                    case 2:
                        processLoan();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("An error occurred while managing loans: " + e.getMessage());
            }
        }
    }

    public static void processLoan() {


        try {
            System.out.println("\033[1;95m ACCEPT/REJECT LOAN \033[0m");
            System.out.print("Enter account number to process loan: ");
            String accNum = sc.nextLine();

            for (int i = 0; i < customerAccountNumber.length; i++) {
                if (customerAccountNumber[i] != null && customerAccountNumber[i].equals(accNum)) {

                    if (loanRequests[i] <= 0) {
                        System.out.println("No loan request found for this account.");
                        return;
                    }

                    if ("Approved".equalsIgnoreCase(loanStatus[i])) {
                        System.out.println("This loan has already been approved.");
                        return;
                    }

                    // Show eligibility report
                    displayLoanEligibilityReport(i);

                    System.out.print("Do you want to approve or reject this loan? (approve/reject): ");
                    String decision = sc.nextLine().toLowerCase();

                    if (decision.equals("approve")) {
                        if (loanRequests[i] <= investorsAmount) {
                            balances[i] += loanRequests[i];
                            loanApproved[i] = true;
                            loanStatus[i] = "Approved";
                            investorsAmount -= loanRequests[i];
                            saveRevenueDetails();
                            originalInterest[i] = interest[i];
                            originalLoanAmount[i] = loanRequests[i];

                            String setDate = adminDateSet();
                            dates[i] = setDate;


                            System.out.println("\033[1;92mLoan approved\033[0m");
                            System.out.println("Rs" + loanRequests[i] + " added to account.");
                            System.out.println("Interest: Rs" + interest[i] + " over " + loanTime[i] + " years.");
                            saveLoanData();
                            saveLoanRepaymentData();

                        } else {
                            System.out.println("Loan amount exceeds bank reserves.");
                        }

                    } else if (decision.equals("reject")) {
                        loanApproved[i] = false;
                        loanStatus[i] = "Rejected";
                        System.out.println("Loan request rejected.");
                        //loanRequests[i] = 0;

                    } else {
                        System.out.println("Invalid choice. Action cancelled.");
                    }

                    return;
                }
            }

            System.out.println("Account number not found.");

        } catch (InputMismatchException ime) {
            System.out.println("Input type mismatch. Please enter valid data.");
            sc.nextLine(); // Clear the invalid input
        } catch (NullPointerException npe) {
            System.out.println("Unexpected null value encountered.");
        } catch (Exception e) { // fallback for any other runtime exceptions
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }

        saveLoanData();
        saveLoanRepaymentData();
    }


    static void applyForLoan() {
        try {
            System.out.println("\033[1;94mAPPLY FOR LOAN\033[0m");
            // Case 1: Already approved loan
            if (loanApproved[loggedInCustomer]) {
                System.out.println("You already have an approved loan. Repay it before applying for a new one.");
                return;
            }

            // Case 2: Loan was previously rejected
            if ("Rejected".equals(loanStatus[loggedInCustomer])) {
                System.out.println("Your previous loan request of Rs" + loanRequests[loggedInCustomer] +
                        " was rejected. You may apply for a new loan.");
                // Clear the rejected status to allow new application
                loanStatus[loggedInCustomer] = null;
                loanRequests[loggedInCustomer] = 0;
            }
            // Case 3: Loan request is still pending
            else if (loanRequests[loggedInCustomer] > 0 && "Pending".equals(loanStatus[loggedInCustomer])) {
                System.out.println("You already have a pending loan request of Rs" + loanRequests[loggedInCustomer] + ".");
                System.out.println("Please wait for admin approval before submitting another request.");
                return;
            }

            // Input loan details
            System.out.print("Enter loan amount: Rs");
            double amount = sc.nextDouble();
            if (amount <= 0) {
                System.out.println("Amount must be positive!");
                return;
            }

            System.out.print("Enter loan time (in years): ");
            double time = sc.nextDouble();
            sc.nextLine();
            if (time <= 0) {
                System.out.println("Time must be greater than 0!");
                return;
            }


            // Update loan info
            loanStatus[loggedInCustomer] = "Pending";
            loanRequests[loggedInCustomer] = amount;
            loanTime[loggedInCustomer] = time;
            interest[loggedInCustomer] = amount * interestRate * time;
            loanApproved[loggedInCustomer] = false;


            System.out.println("\nLoan request submitted for approval.");
            System.out.println("Total interest: Rs" + interest[loggedInCustomer]);

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numeric values for amount and time.");
            sc.nextLine(); // clear invalid input
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("Error accessing account data. Please try again.");
        }
        saveLoanData();
    }

    static void checkLoanStatus() {
        try {
            System.out.println("\033[1;93mLOAN STATUS\033[0m");
            // 1. Validate customer index first
            if (loggedInCustomer < 0 || loggedInCustomer >= customerAccountNumber.length) {
                System.out.println("Invalid customer session");
            }

            // 2. Get all relevant data with null checks
            String status = loanStatus[loggedInCustomer];
            if (status == null) {
                throw new NullPointerException("Loan status not found");
            }

            double amount = loanRequests[loggedInCustomer];
            boolean isApproved = loanApproved[loggedInCustomer];

            // 3. Determine the message
            switch (status) {
                case "Approved":
                    if (amount <= 0) {
                        System.out.println("Approved loan with invalid amount");
                    }
                    double totalAmount = amount + interest[loggedInCustomer];
                    System.out.println("Your loan of Rs" + amount + " has been approved!");

                    System.out.println("Use 'Repay Loan' option from menu to make payments.");
                    break;

                case "Rejected":
                    if (amount <= 0) {
                        System.out.println("Rejected loan with invalid amount");
                    }
                    System.out.println("Your loan request of Rs" + amount + " has been rejected.");
                    break;

                case "Pending":
                    if (amount <= 0) {
                        System.out.println("Pending loan with invalid amount");
                    }
                    System.out.println("Your loan of Rs" + amount + " is pending approval.");
                    break;

                default:
                    System.out.println("You have not applied for any loan.");
            }
            saveLoanData();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("System error: Loan data corruption detected");
        } catch (NullPointerException e) {
            System.out.println("System error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    //------------------------LOAN REPAYMENT------------------------------------------------

    // METHOD TO GET DATE BEFORE 2025 FOR LOAN REPAYMENT
    public static String getValidDateBefore2026() {
        Date validDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        Date maxDate = null;
        try {
            maxDate = sdf.parse("31/12/2025");
        } catch (ParseException e) {
            System.exit(1);
        }

        while (true) {
            System.out.print("Enter date in dd/MM/yyyy format (must be in year 2025 only): ");
            String input = sc.nextLine();
            try {
                validDate = sdf.parse(input);

                if (validDate.after(maxDate)) {
                    System.out.println("Date cannot be after 31/12/2025.");
                    continue;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(validDate);
                int year = calendar.get(Calendar.YEAR);

                if (year < 2025) {
                    System.out.println("Date must not be before the year 2025.");
                    continue;
                }

                break;
            } catch (ParseException e) {
                System.out.println("Invalid format! Please enter date as dd/MM/yyyy.");
            }
        }

        return sdf.format(validDate);
    }


    // METHOD TO GET DATE BEFORE 2036
    public static String getValidDateBefore2036() {
        Date validDate = null;

        // Set up date format
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);


        // Maximum allowed date: 31st December 2035
        Date maxDate = null;
        try {
            maxDate = sdf.parse("31/12/2035");
        } catch (ParseException e) {
            System.exit(1);
        }

        while (true) {
            System.out.print("Enter date in dd/MM/yyyy format (2025 - 2035): ");
            String input = sc.nextLine().trim();

            // --- Manually parse day, month, year from input ---
            String[] parts = new String[3];
            int index = 0;
            String temp = "";

            for (int i = 0; i < input.length(); i++) {
                char ch = input.charAt(i);
                if (ch == '/') {
                    parts[index++] = temp;
                    temp = "";
                } else {
                    temp += ch;
                }
            }
            parts[index] = temp;

            if (parts.length == 3) {
                String day = parts[0].length() == 1 ? "0" + parts[0] : parts[0];
                String month = parts[1].length() == 1 ? "0" + parts[1] : parts[1];
                String year = parts[2];

                if (year.length() == 2) {
                    int yr = Integer.parseInt(year);
                    year = (yr < 10 ? "200" : "20") + yr;
                }

                input = day + "/" + month + "/" + year;
            }

            try {
                validDate = sdf.parse(input);

                if (validDate.after(maxDate)) {
                    System.out.println("Date cannot be after 31/12/2035.");
                    continue;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(validDate);
                int year = calendar.get(Calendar.YEAR);

                if (year < 2025) {
                    System.out.println("Date must not be before the year 2025.");
                    continue;
                }

                break;
            } catch (ParseException e) {
                System.out.println("Invalid format! Please enter date as dd/MM/yyyy.");
            }
        }

        // Return the validated, formatted date string
        return sdf.format(validDate);
    }

    // MAIN LOAN REPAYMENT METHOD
    public static void repayLoanYearly(int loggedInCustomer) throws ParseException {
        if (!loanApproved[loggedInCustomer]) {
            System.out.println("Loan not approved.");
            return;
        }

        if (loanTime[loggedInCustomer] == 0 || (loanRequests[loggedInCustomer] <= 0 && interest[loggedInCustomer] <= 0)) {
            System.out.println("All of your loan has been paid off. No repayments to be made.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        while (true) {
            System.out.println("\nLoan Repayment Menu:");
            System.out.println("1. Repay yearly installment");
            System.out.println("2. Exit");
            System.out.print("Choose option: ");
            int choice = -1;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    if (loanTime[loggedInCustomer] == 0 ||
                            (loanRequests[loggedInCustomer] <= 0 && interest[loggedInCustomer] <= 0)) {
                        System.out.println("All of your loan has been paid off. No repayments to be made.");
                        break;
                    }

                    Date approvedDate = sdf.parse(dates[loggedInCustomer]);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(approvedDate);
                    calendar.add(Calendar.YEAR, 1);
                    Date oneYearLater = calendar.getTime();

                    // recalculating the remaining amount due based on latest time
                    double totalRemaining = loanRequests[loggedInCustomer] + interest[loggedInCustomer];
                    double yearlyDue = Math.round((totalRemaining / loanTime[loggedInCustomer]) * 100.0) / 100.0;

                    System.out.printf("Total yearly payment due: %.2f%n", yearlyDue);
                    System.out.println("Installment is due on: " + sdf.format(oneYearLater));

                    String todayStr = getValidDateBefore2036();
                    Date today = sdf.parse(todayStr);

                    if (today.before(oneYearLater)) {
                        long diffInMillies = oneYearLater.getTime() - today.getTime();
                        long daysRemaining = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                        System.out.println("Payment cannot be made before the due date. " + daysRemaining + " days remaining until repayment is allowed.");
                        break;
                    }

                    if (today.after(oneYearLater)) {
                        penaltyPoints[loggedInCustomer]++;
                        System.out.println("You are late. One penalty point added. Account will be blocked if 2 penalty points.");
                        if (penaltyPoints[loggedInCustomer] >= 2) {
                            System.out.println("Account blocked due to multiple late payments.");
                            blockedAccount[loggedInCustomer] = 1;
                            return;
                        }
                    }

                    double enteredAmount = 0;
                    boolean correct = false;
                    while (!correct) {
                        System.out.print("Enter amount to pay: ");
                        try {
                            enteredAmount = sc.nextDouble();
                            sc.nextLine();
                            if (Math.abs(enteredAmount - yearlyDue) > 0.009) {
                                System.out.printf("Incorrect amount. Please enter the correct amount: %.2f%n", yearlyDue);
                            } else {
                                correct = true;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a numeric value.");
                            sc.nextLine();
                        }
                    }

                    // Splitting the payment into interest and principal
                    if (interest[loggedInCustomer] >= enteredAmount) {
                        interest[loggedInCustomer] -= enteredAmount;
                        totalRevenue += enteredAmount;
                    } else {
                        double interestPart = interest[loggedInCustomer];
                        double principalPart = enteredAmount - interestPart;

                        interest[loggedInCustomer] = 0;
                        totalRevenue += interestPart;

                        loanRequests[loggedInCustomer] -= principalPart;
                        investorsAmount += principalPart;
                    }

                    loanTime[loggedInCustomer]--;

                    if (loanTime[loggedInCustomer] == 0 ||
                            (loanRequests[loggedInCustomer] <= 0 && interest[loggedInCustomer] <= 0)) {
                        System.out.println("All of your loan has been paid off.");
                        saveLoanRepaymentData();
                        saveLoanData();
                    } else {
                        calendar.setTime(oneYearLater);
                        calendar.add(Calendar.YEAR, 1);
                        Date nextDue = calendar.getTime();
                        System.out.println("Next payment is due at: " + sdf.format(nextDue));
                        dates[loggedInCustomer] = sdf.format(oneYearLater);
                        saveLoanData();
                        saveLoanRepaymentData();
                        saveBankAmount();
                    }
                    break;

                case 2:
                    System.out.println("Exiting repayment system.");
                    return;

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    // SETTING DATE IN ADMIN APPROVAL
    public static String adminDateSet() {
        String validDateStr = getValidDateBefore2026();
        System.out.println("You entered a valid date: " + validDateStr);
        dates[loggedInCustomer] = validDateStr;
        return validDateStr;
    }

    //-----------------------------------------------------------------------------------
    /**AHMED ABDULLAH METHODS
     * FA24-BDS-009*/

    // EDITING THE CUSTOMER DETAILS
    static void editCustomerDetails() {
        while (true) {
            System.out.println("\n\033[1;94m Edit Customer Details \033[0m");
            System.out.println("1. Full Name");
            System.out.println("2. Username");
            System.out.println("3. Password");
            System.out.println("4. Phone Number");
            System.out.println("5. CNIC");
            System.out.println("6. Address");
            System.out.println("7. Age");
            System.out.println("8. PIN");
            System.out.println("9. Go Back ");
            System.out.print("Enter your choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        editFullName();
                        break;
                    case 2:
                        editUsername();
                        break;
                    case 3:
                        editPassword();
                        break;
                    case 4:
                        editPhoneNumber();
                        break;
                    case 5:
                        editUserCNIC();
                        break;
                    case 6:
                        editCustomerAddress();
                        break;
                    case 7:
                        editCustomerAge();
                        break;
                    case 8:
                        editPIN();
                        break;
                    case 9:
                        System.out.println("Returning to Customer Menu...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 9.");
                sc.nextLine(); // clear the invalid input
            }
        }
    }

    // THE FOLLOWING METHODS ARE HELPER METHODS FOR UPDATING THE USER DATA

    public static void editFullName() {
        System.out.println("Current Full Name: " + customerFullName[loggedInCustomer]);
        String fullName;
        while (true) {
            try {
                System.out.print("Full Name: ");
                fullName = sc.nextLine().trim();
                if (!fullName.isEmpty()) break;
                System.out.println("Name cannot be empty.");
            } catch (Exception e) {
                System.out.println("Error occurred. Try again.");
            }
        }
        customerFullName[loggedInCustomer] = fullName;
        System.out.println("Full Name updated successfully.");
        saveCustomerDataToFile();
    }

    public static void editUsername() {
        System.out.println("Current Username: " + customerUsername[loggedInCustomer]);
        String username;
        while (true) {
            try {
                System.out.print("Username: ");
                username = sc.nextLine().trim();

                if (username.isEmpty()) {
                    System.out.println("Username cannot be empty.");
                    continue;
                }

                int usernameStatus = checkUsernameAvailability(username);

                switch (usernameStatus) {
                    case 1:
                        System.out.println("Error: This username already exists in THIS branch.");
                        continue;
                    case 2:
                        System.out.println("Error: This username already exists in ANOTHER branch.");
                        continue;
                    case 0:
                        break; // valid username
                }
                break;
            } catch (NullPointerException e) {
                System.out.println("Error: Input cannot be null. Please try again.");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: Maximum number of customers reached.");
                return;
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }

        customerUsername[loggedInCustomer] = username;
        System.out.println("Username updated successfully.");
        saveCustomerDataToFile();
    }


    public static void editPassword() {
        System.out.println("Current Password: " + customerPassword[loggedInCustomer]);
        String password;
        while (true) {
            try {
                System.out.print("Password (8-12 chars, with upper, lower, digit, and special !@#$%^&*): ");
                password = sc.nextLine();
                if (!validatePassword(password)) {
                    System.out.println("Invalid password. Must be 8-12 characters long and include:");
                    System.out.println("Uppercase letter");
                    System.out.println("Lowercase letter");
                    System.out.println("Digit");
                    System.out.println("Special character (!@#$%^&*)");
                } else {
                    boolean exists = false;
                    for (int i = 0; i < customerCount; i++) {
                        if (customerPassword[i].equals(password)) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        System.out.println("Password already exists. Choose another.");
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error occurred. Try again.");
            }
        }
        customerPassword[loggedInCustomer] = password;
        System.out.println("Password updated successfully.");
        saveCustomerDataToFile();
    }

    public static void editPhoneNumber() {
        System.out.println("Current Phone Number: " + customerPhone[loggedInCustomer]);
        String phone;
        while (true) {
            try {
                System.out.print("Phone Number (11 digits): ");
                phone = sc.nextLine().trim();
                if (phone.length() == 11 && allDigits(phone))
                    break;
                System.out.println("Invalid phone number. It should be 11 digits.");
            } catch (Exception e) {
                System.out.println("Error occurred. Try again.");
            }
        }
        customerPhone[loggedInCustomer] = phone;
        System.out.println("Phone Number updated successfully.");
        saveCustomerDataToFile();
    }

    public static void editUserCNIC() {
        System.out.println("Current CNIC: " + customerCnic[loggedInCustomer]);
        String cnic;
        while (true) {
            try {
                System.out.print("Enter CNIC (XXXXX-XXXXXXX-X): ");
                cnic = sc.nextLine().trim();
                if (!isCnicValid(cnic)) {
                    System.out.println("Invalid format! Must be XXXXX-XXXXXXX-X (13 digits with dashes)");
                    System.out.println("Please try Again");
                    continue;
                }
                int check = checkUserCnicAvailability(cnic);
                if (check == 1) {
                    System.out.println("CNIC already exists in the current branch. Try a new one.");
                } else if (check == 2) {
                    System.out.println("CNIC already exists in another branch. Try a new one.");
                } else if (check == 0) {
                    customerCnic[customerCount] = cnic;
                    System.out.println("CNIC updated successfully.");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Error occurred. Try again.");
            }
        }
        customerCnic[loggedInCustomer] = cnic;
        System.out.println("CNIC updated successfully.");
        saveCustomerDataToFile();
    }

    public static void editPIN() {
        System.out.println("Current PIN: " + pins[loggedInCustomer]);
        String pin;
        while (true) {
            try {
                System.out.print("Enter 8-digit PIN: ");
                pin = sc.nextLine();
                if (pin.length() == 8 && isNumeric(pin)) {
                    break;
                } else {
                    System.out.println("Invalid PIN. It must be exactly 8 digits.");
                }
            } catch (Exception e) {
                System.out.println("Error occurred. Try again.");
            }
        }
        pins[loggedInCustomer] = pin;
        System.out.println("PIN updated successfully.");
        saveCustomerDataToFile();
    }

    public static void editCustomerAddress() {
        System.out.println("Current Address: " + customerAddress[loggedInCustomer]);
        String address;
        while (true) {
            try {
                System.out.print("Address: ");
                address = sc.nextLine().trim();
                if (!address.isEmpty())
                    break;
                System.out.println("Address cannot be empty.");
            } catch (Exception e) {
                System.out.println("Error occurred. Try again.");
            }
        }
        customerAddress[loggedInCustomer] = address;
        System.out.println("Address updated successfully.");
        saveCustomerDataToFile();
    }

    public static void editCustomerAge() {
        System.out.println("Current Age: " + customerAge[loggedInCustomer]);
        int age;
        while (true) {
            try {
                System.out.print("Age (must be 18 or older): ");
                age = sc.nextInt();
                sc.nextLine(); // Clear buffer
                if (age >= 18 && age <= 100) {
                    break;
                } else {
                    System.out.println("Age must be between 18 and 100.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                sc.nextLine(); // Clear invalid input
            } catch (Exception e) {
                System.out.println("Unexpected error. Try again.");
                sc.nextLine();
            }
        }
        customerAge[loggedInCustomer] = age;
        System.out.println("Age updated successfully.");
        saveCustomerDataToFile();
    }

    // USERNAME OF A CUSTOMER SHOULD BE UNIQUE
    // THIS METHOD ENSURES THAT
    public static int checkUsernameAvailability(String input) {
        // 0 = available
        // 1 = exists in current branch
        // 2 = exists in another branch

        if (input == null || input.trim().isEmpty()) {
            System.out.println("Username cannot be null or empty");
            return -1;
        }

        try {
            for (int i = 0; i < customerCount; i++) {
                if (customerUsername[i] != null && customerUsername[i].equalsIgnoreCase(input)) {
                    return 1;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("customerUsername array contains unexpected null values");
        }

        String[] branches = {"Quetta", "Islamabad", "Karachi", "Lahore", "Peshawar"};

        for (String branch : branches) {
            if (branch.equals(selectedBranch))
                continue;

            File file = new File("customerDataFile_" + branch + ".txt");

            if (!file.exists()) {
                System.out.println("No Customer Accounts Found in: " + branch);
                continue;
            }

            Scanner sc = null;
            try {
                sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && parts[1].trim().equalsIgnoreCase(input)) {
                        return 2;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error checking username in branch " + branch + ": " + e.getMessage());
            } finally {
                if (sc != null) sc.close();
            }
        }

        return 0;
    }
    // HELPER METHOD FOR ACCOUNT NUMBER AVAILABILITY IN THE SAME BRANCH AND IN OTHER
    // USED IN ACCOUNT GENERATION SO THAT SAME NUMBER IS NOT GENERATED TWICE
    public static int checkAccountNumberAvailability(String input) {
        // 0 = available
        // 1 = exists in current branch
        // 2 = exists in another branch

        if (input == null || input.trim().isEmpty()) {
            System.out.println("Account number cannot be null or empty");
            return -1;
        }

        try {
            for (int i = 0; i < customerCount; i++) {
                if (customerAccountNumber[i] != null && customerAccountNumber[i].equals(input)) {
                    return 1;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("customerAccountNumber array contains unexpected null values");
        }

        String[] branches = {"Quetta", "Islamabad", "Karachi", "Lahore", "Peshawar"};

        for (String branch : branches) {
            if (branch.equals(selectedBranch))
                continue;

            File file = new File("customerDataFile_" + branch + ".txt");

            if (!file.exists()) {
                System.out.println("No Customer Accounts Found in: " + branch);
                continue;
            }

            Scanner sc = null;
            try {
                sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (line.contains("Account Number: ")) {
                        String existing = line.split("Account Number: ")[1].trim();
                        if (existing.equals(input)) {
                            return 2;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error checking account number in branch " + branch + ": " + e.getMessage());
            } finally {
                if (sc != null) sc.close();
            }
        }

        return 0;
    }

    // CNIC AVAILABILITY FOR ADMINS IN THE SAME BRANCH AND IN THE OTHERS
    public static int checkCnicAvailability(String input) {
        // 0 = available
        // 1 = exists in current branch
        // 2 = exists in another branch

        if (input == null || input.trim().isEmpty()) {
            System.out.println("CNIC cannot be null or empty");
            return -1;
        }

        // Check current branch
        try {
            for (int i = 0; i < adminCount; i++) {
                if (adminCnic[i] != null && adminCnic[i].equalsIgnoreCase(input)) {
                    return 1;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("adminCnic array contains unexpected null values");
        }

        // Check other branches
        String[] branches = {"Quetta", "Islamabad", "Karachi", "Lahore", "Peshawar"};

        for (String branch : branches) {
            if (branch.equals(selectedBranch))
                continue;

            File file = new File("adminDataFile_" + branch + ".txt");

            if (!file.exists()) {
                System.out.println("No Admin Records Found in: " + branch);
                continue;
            }

            Scanner sc = null;
            try {
                sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length > 4) {
                        String existing = parts[4].trim();  // CNIC is at index 4
                        if (existing.equalsIgnoreCase(input)) {
                            return 2;
                        }
                    }

                }
            } catch (Exception e) {
                System.out.println("Error checking CNIC in branch " + branch + ": " + e.getMessage());
            } finally {
                if (sc != null) sc.close();
            }
        }

        return 0;
    }

    // HELPER METHOD TO CHECK CNIC AVAILABILITY FOR USER IN THE SAME BRANCH AND IN OTHER
    public static int checkUserCnicAvailability(String input) {
        // 0 = available
        // 1 = exists in current branch
        // 2 = exists in another branch

        if (input == null || input.trim().isEmpty()) {
            System.out.println("CNIC cannot be null or empty");
            return -1;
        }

        // Check current branch
        try {
            for (int i = 0; i < customerCount; i++) {
                if (customerCnic[i] != null && customerCnic[i].equalsIgnoreCase(input)) {
                    return 1;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("CustomerCnic array contains unexpected null values");
        }

        // Check other branches
        String[] branches = {"Quetta", "Islamabad", "Karachi", "Lahore", "Peshawar"};

        for (String branch : branches) {
            if (branch.equals(selectedBranch))
                continue;

            File file = new File("customerDataFile_" + branch + ".txt");

            if (!file.exists()) {
                System.out.println("No Customer Records Found in: " + branch);
                continue;
            }

            Scanner sc = null;
            try {
                sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length > 4) {
                        String existing = parts[4].trim();  // CNIC is at index 4
                        if (existing.equalsIgnoreCase(input)) {
                            return 2;
                        }
                    }

                }
            } catch (Exception e) {
                System.out.println("Error checking CNIC in branch " + branch + ": " + e.getMessage());
            } finally {
                if (sc != null) sc.close();
            }
        }

        return 0;
    }

    // A HELPER METHOD USED IN PIN VERIFICATION
    public static boolean isNumeric(String str) {
        if (str == null) {
            System.out.println("Input string is null.");
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    // A HELPER METHOD TO GENERATE ACCOUNT NUMBER FOR THE USER
    public static String generateAccountNumber() {
        int length = 14; // THE LENGTH OF NUMBER IS 14
        String accountNumber = "";  // ITS STORED AS A STRING

        while (true) {
            accountNumber = "";
            for (int i = 0; i < length; i++) {
                int digit = (int) (Math.random() * 10);
                accountNumber += digit;
            }

            int check = checkAccountNumberAvailability(accountNumber);
            if (check == 0) {
                return accountNumber;
            } else if (check == 1) {
                System.out.println("Generated account number exists in this branch. Retrying");
            } else if (check == 2) {
                System.out.println("Generated account number exists in another branch. Retrying");
            }
        }
    }



    // Helper method For digit validation
    public static boolean allDigits(String str) {
        try {
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } catch (NullPointerException e) {
            System.out.println("Input string is null.");
            return false;
        }
    }


    public static double bankAmount() {
        double sum = 0;
        try {
            if (balances == null) {
                throw new NullPointerException("Balances array is not initialized.");
            }
            for (int i = 0; i < customerCount; i++) {
                sum += balances[i];
            }
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: customerCount exceeds balances array length.");
            return 0;
        }
        return sum;
    }

    // MAIN METHOD WHICH IS USED IN ADMIN CREATION
    public static void createAdminAccount() {
        while (true) {
            try {
                if (adminCount >= 100) {
                    System.out.println("Admin limit reached.");
                    return;
                }
                System.out.println("\033[1;94m \nCREATE NEW ADMIN ACCOUNT \033[0m");
                // Full Name
                String fullName;
                while (true) {
                    try {
                        System.out.println("ENTER 0 TO GO BACK");
                        System.out.print("Full Name: ");
                        fullName = sc.nextLine().trim();
                        if(fullName.equals("0")){
                            return;
                        }
                        if (!fullName.isEmpty()) {
                            adminFullName[adminCount] = fullName;
                            break;
                        } else {
                            System.out.println("Name cannot be empty.");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                // Gender
                String gender;
                while (true) {
                    try {
                        System.out.print("Gender (Male/Female): ");
                        gender = sc.nextLine().trim();
                        if (gender.equalsIgnoreCase("Male") ||
                                gender.equalsIgnoreCase("Female")) {
                            adminGender[adminCount] = gender;
                            break;
                        } else {
                            System.out.println("Invalid input. Enter Male / Female.");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                // Username
                String username;
                while (true) {
                    try {
                        System.out.print("Username: ");
                        username = sc.nextLine().trim();
                        if (username.isEmpty()) {
                            System.out.println("Username cannot be empty.");
                            continue;
                        }
                        boolean exists = false;
                        for (int i = 0; i < adminCount; i++) {
                            if (adminUsername[i].equals(username)) {
                                exists = true;
                                break;
                            }
                        }
                        if (exists) {
                            System.out.println("Username already exists. Choose another.");
                        } else {
                            adminUsername[adminCount] = username;
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                // Password
                String password;
                while (true) {
                    try {
                        System.out.print("Password (8-12 chars, with upper, lower, digit, special): ");
                        password = sc.nextLine();

                        if (!validatePassword(password)) {
                            System.out.println("Password must contain uppercase, lowercase, digit, and special (!@#$%^&*)");
                        } else {
                            boolean exists = false;
                            for (int i = 0; i < adminCount; i++) {
                                if (adminPassword[i].equals(password)) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (exists) System.out.println("Password already in use. Try a new one.");
                            else {
                                adminPassword[adminCount] = password;
                                break;

                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                // Age
                int age;
                while (true) {
                    System.out.print("Age (18 or older): ");
                    try {
                        age = sc.nextInt();
                        sc.nextLine();
                        if (age < 18 || age > 100) {
                            throw new IllegalArgumentException("Age must be between 18 and 100.");
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        sc.nextLine();
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                adminAge[adminCount] = age;

                // Phone
                while (true) {
                    try {
                        System.out.print("Phone Number (11 digits): ");
                        String phone = sc.nextLine().trim();
                        if (phone.length() == 11 && allDigits(phone)) {
                            adminPhone[adminCount] = phone;
                            break;
                        } else {
                            System.out.println("Invalid phone number. Must be 11 digits.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error reading phone number. Try again");
                    }
                }

                // CNIC
                String cnic;
                while (true) {
                    try {
                        System.out.print("Enter CNIC (XXXXX-XXXXXXX-X): ");
                        cnic = sc.nextLine().trim();
                        if (!isCnicValid(cnic)) {
                            System.out.println("Invalid format! Must be XXXXX-XXXXXXX-X (13 digits with dashes)");
                            System.out.println("Please try Again");
                            continue;
                        }
                        int check = checkCnicAvailability(cnic);
                        if (check == 1) {
                            System.out.println("CNIC already exists in the current branch. Try a new one.");
                        } else if (check == 2) {
                            System.out.println("CNIC already exists in another branch. Try a new one.");
                        } else if (check == 0) {
                            adminCnic[adminCount] = cnic;
                            System.out.println("CNIC updated successfully.");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                // Address
                String address;
                while (true) {
                    try {
                        System.out.print("Address: ");
                        address = sc.nextLine().trim();
                        if (!address.isEmpty()) {
                            adminAddress[adminCount] = address;
                            break;
                        } else {
                            System.out.println("Address cannot be empty.");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }


                // Position
                String position;
                while (true) {
                    System.out.print("Position (Manager/Officer): ");
                    position = sc.nextLine().trim();
                    if (!position.isEmpty()) break;
                    System.out.println("Position cannot be empty.");
                }
                adminPosition[adminCount] = position;

                adminCount++;
                System.out.println("Admin account created successfully!\n");

                // Ask if user wants to continue
                System.out.print("Do you want to create another admin? (yes/no): ");
                String choice = sc.nextLine().trim().toLowerCase();
                if (!choice.equals("yes")) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Unexpected error occurred: " + e.getMessage());
                break;
            }

        }
        saveAdminDataToFile();
    }



}
