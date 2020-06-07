
import java.util.Scanner;
public class ATM {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Bank theBank = new Bank("Universal Bank");

        //add a user which also creates a savings account
        User aUser = theBank.addUser("Charu","Kulshrestha","1234");
        //ass a checking account for our user
        Account newAccount = new Account("checking", theBank, aUser);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true){
            //stay in thr login prompt untill successful login
            curUser = ATM.mainMenuPrompt(theBank, sc);
            //stay in main until user quits
            ATM.printUserMenu(curUser,sc);
        }
    }

    private static User mainMenuPrompt(Bank theBank, Scanner sc) {
        String userId;
        String pin ;
        User authUser;
        //rpomt user for userId and pin combo untill correcy
        do{
            System.out.println("Welcome to the bank, "+theBank.getName());
            System.out.print("Enter user ID: ");
            userId = sc.nextLine();
            System.out.print("Enter pin: ");

            pin = sc.nextLine();
            //try to get the user object corresponding to the id and pin combo
            authUser = theBank.userLogin(userId,pin);

            if(authUser == null)
                System.out.println("Incorrect userId/pin combination. Please try again");


        }while(authUser == null);

        return authUser;
    }

    public static void printUserMenu(User theUser,Scanner sc){
        //summary of user account
        theUser.printAccountsSummary();

        int choice;
        //user menu
        do{
            System.out.println("Welcome "+ theUser.getFirstName()+" What would you like to do?");
            System.out.println(" 1. Show Account Transaction History");
            System.out.println(" 2. Withdraw");
            System.out.println(" 3. Deposit");
            System.out.println(" 4. Transfer");
            System.out.println(" 5. Quit");
            System.out.println();
            System.out.print(" Enter Choice: ");
            choice = sc.nextInt();

            if(choice < 1 || choice > 5)
                System.out.println("Invalid Choice. Please choose 1-5");

        }while(choice < 1 || choice > 5);

        switch (choice){

            case 1: ATM.showTransHistory(theUser, sc);
                break;
            case 2: ATM.withdrawFunds(theUser, sc);
                break;
            case 3: ATM.depositFunds(theUser, sc);
                break;
            case 4: ATM.transferFunds(theUser, sc);
                break;
            case 5: //gobble up the previous input
                sc.nextLine();
                break;
        }

        //redisplay this menu unless the user wants to quit
        if(choice != 5)
            ATM.printUserMenu(theUser, sc);

    }




    private static void showTransHistory(User theUser, Scanner sc) {
        int theAcct;
        //get account whose history to look at
        do{
            System.out.printf("Enter the number (1-%d) of the account\n"
                    + "whose transactions you want to see: ", theUser.numAccounts());
            theAcct = sc.nextInt()-1;
            if(theAcct < 0 || theAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again.");
            }

        }while (theAcct < 0 || theAcct >= theUser.numAccounts());

        //print transaction history
        theUser.printAccTransHistory(theAcct);
    }

    private static void transferFunds(User theUser, Scanner sc) {
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        //get t he account to trnasfer from

        do {
            System.out.printf("Enter the number (1-%d) of the account\n"+
                    "to transfer from: ",theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if(fromAcct < 0 || fromAcct >= theUser.numAccounts())
                System.out.println("Invalid account. Please try again.");
        }while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAccBalance(fromAcct);
        //get the account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account\n"+
                    "to transfer to: ",theUser.numAccounts());
            toAcct = sc.nextInt()-1;
            if(toAcct < 0 || toAcct >= theUser.numAccounts())
                System.out.println("Invalid account. Please try again.");
        }while (toAcct < 0 || toAcct >= theUser.numAccounts());

        //get the amount to transfer
        do{
            System.out.printf("Enter the amount to transfer (max $%.02f): ",acctBal);
            amount = sc.nextDouble();
            if(amount < 0 ){
                System.out.println("Amount must be greater than 0");
            } else if(amount > acctBal)
                System.out.printf("Amount must not be greater than\n"+
                        "Balance of $%.02f.\n",acctBal);
        }while (amount < 0 || amount > acctBal);

        //finally do the transfer

        theUser.addAcctTransaction(fromAcct, -1*amount, String.format(
                "Transfer to account %s", theUser.getAcctUUID(toAcct)));

        theUser.addAcctTransaction(toAcct, amount, String.format(
                "Transfer to account %s", theUser.getAcctUUID(fromAcct)));

    }

    private static void withdrawFunds(User theUser, Scanner sc) {
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        //get t he account to trnasfer from

        do {
            System.out.printf("Enter the number (1-%d) of the account\n"+
                    "to transfer from: ",theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if(fromAcct < 0 || fromAcct >= theUser.numAccounts())
                System.out.println("Invalid account. Please try again.");
        }while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAccBalance(fromAcct);

        //amount to transfer
        do{
            System.out.printf("Enter the amount to transfer (max $%.02f.): ",acctBal);
            amount = sc.nextDouble();
            if(amount < 0 ){
                System.out.println("Amount must be greater than 0");
            } else if(amount > acctBal)
                System.out.printf("Amount must not be greater than\n"+
                        "Balance of $%.02f.\n",acctBal);
        }while (amount < 0 || amount > acctBal);
        //gobble up the previous input line
        sc.nextLine();
        //get a memo
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        //do the withdraw
        theUser.addAcctTransaction(fromAcct, -1*amount, memo);

    }

    private static void depositFunds(User theUser, Scanner sc) {
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        //get t he account to trnasfer from

        do {
            System.out.printf("Enter the number (1-%d) of the account\n"+
                    "to transfer from: ",theUser.numAccounts());
            toAcct = sc.nextInt()-1;
            if(toAcct < 0 || toAcct >= theUser.numAccounts())
                System.out.println("Invalid account. Please try again.");
        }while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAccBalance(toAcct);

        //amount to transfer
        do{
            System.out.printf("Enter the amount to transfer (max $%.02f): ",acctBal);
            amount = sc.nextDouble();
            if(amount < 0 ){
                System.out.println("Amount must be greater than 0");
            }
//            else if(amount > acctBal)
//                System.out.printf("Amount must not be greater than\n"+
//                        "Balance of $%0.2f.\n",acctBal);
        }while (amount < 0 );
        //gobble up the previous input line
        sc.nextLine();
        //get a memo
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        //do the withdraw
        theUser.addAcctTransaction(toAcct, amount, memo);
    }


}
