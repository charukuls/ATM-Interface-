import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;
    public Bank(String name){
        this.name = name;
        this.accounts = new ArrayList<Account>();
        this.users = new ArrayList<User>();
    }

    public Bank(String name, ArrayList<User> user, ArrayList<Account> accounts) {
        this.name = name;
        this.users = user;
        this.accounts = accounts;
    }

    public String getNewUserUUID() {
        String uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique;
        //continue till we get a unique uuid
        do{
            uuid = "";
            for(int c = 0; c < len; c++){
                uuid += ((Integer)rng.nextInt(10)).toString();
            }
            nonUnique = false;
            for(User u: this.users){
                if(uuid.compareTo(u.getUUID())==0){
                    nonUnique = true;
                    break;
                }
            }
        }while (nonUnique);

        return uuid;

    }

    public String getNewAccountUUID() {
        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique;
        //continue till we get a unique uuid
        do{
            uuid = "";
            for(int c = 0; c < len; c++){
                uuid += ((Integer)rng.nextInt(6)).toString();
            }
            nonUnique = false;
            for(Account a: this.accounts){
                if(uuid.compareTo(a.getUUID())==0){
                    nonUnique = true;
                    break;
                }
            }
        }while (nonUnique);

        return uuid;

    }

    public void addAccount(Account anAcc){
        this.accounts.add(anAcc);
    }
    public User addUser(String firstName, String lastName, String pin){
         User newUser = new User(firstName,lastName,pin,this);
         this.users.add(newUser);

         //create savings account
        Account newAccount = new Account("Savings",this,newUser);
        newUser.addAccount(newAccount);
        this.accounts.add(newAccount);

        return  newUser;
    }
    public  User userLogin(String userId, String pin){
        for (User u : this.users){
            if(u.getUUID().compareTo(userId)==0 && u.validatePin(pin))
                return u;
        }
        return null;
    }

    public  String getName(){
        return this.name;
    }
}
