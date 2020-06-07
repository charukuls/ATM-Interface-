import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;

public class User {

    //public String getFirstName;
    private String firstName;   //firstname
    private String lastName;    //lastname
    private String uuid;        //unique universal identifier
    private byte pinHash[];     //MD5 hash of users pin number
    private ArrayList<Account> accounts;       //list of all accounts of the user

    public User(String firstName, String lastName, String pin, Bank theBank) {
        this.firstName = firstName;
        this.lastName = lastName;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        //get a new unique universal Id for the user
        this.uuid = theBank.getNewUserUUID();
        //empty list of accounts
        this.accounts = new ArrayList<Account>();
        System.out.printf("New user %s, %s with ID %s created \n", lastName, firstName,this.uuid);

    }
    /*add account from bank*/
    public void addAccount(Account anAcct){
        this.accounts.add(anAcct);
    }

    public String getUUID() {
        return this.uuid;
    }

    public boolean validatePin(String apin){
        try {
            MessageDigest md =  MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(apin.getBytes()),this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);

        }
        return  false;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void printAccountsSummary() {
        System.out.println(this.firstName + "'s account summary");
        for(int a = 0; a < this.accounts.size(); a++){
            System.out.printf("%d) %s\n",a+1,this.accounts.get(a).getSummaryLine());
        }
        System.out.println();
    }

    public int numAccounts() {
        return this.accounts.size();
    }

    public void printAccTransHistory(int accIdx) {
        this.accounts.get(accIdx).printTransHistory();
    }

    public double getAccBalance(int acctIdx) {
        return  this.accounts.get(acctIdx).getBalance();
    }

    public  String getAcctUUID(int acctIdx){
        return this.accounts.get(acctIdx).getUUID();
    }

    public void addAcctTransaction(int acctIdx, double amount, String memo) {
        this.accounts.get(acctIdx).addTransaction(amount,memo);
    }
}
