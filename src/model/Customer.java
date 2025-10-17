package model;
import java.time.LocalDateTime;

public class Customer {
    private String name;
    private String accountNumber;
    private double balance;
    private String accountType;
    private boolean isApproved;
    private LocalDateTime createdDate;
    public Customer(String name, String accountNumber , double balance , boolean isApproved,String accountType)throws
    IllegalArgumentException{
        if (name == null || name.isEmpty() )
            throw new IllegalArgumentException("Name cannot be empty !");
        if (name.matches(".*\\d.*"))
            throw new IllegalArgumentException("Name cannot contain numbers !");
        if(!name.matches("[a-zA-Z]+"))
            throw new IllegalArgumentException("Name can only contain alphabets and spaces !");
        if(accountNumber==null || accountNumber.isEmpty())
            throw new  IllegalArgumentException("Account Number cannot be empty!");
        if (balance<0)
            throw new IllegalArgumentException("Balance cannot be negative !");
        if(accountType==null || accountType.isEmpty())
            throw new IllegalArgumentException("Accoun type cannot be empty ");
            this.name=name;
            this.accountNumber=accountNumber;
            this.balance=balance;
            this.isApproved=isApproved;
            this.accountType=accountType;
            this.createdDate = LocalDateTime.now();

        }
    


                

    
    //Getter  for name 
    public String getName() {
        return name;
    } 
    //Setter for name with validation
    public void setName(String name)throws IllegalArgumentException{
        if(name == null ||name.isEmpty() )
             throw new IllegalArgumentException("Name cannot be empty !");
         this.name =name;}
    //Getter for balance
    public double getBalance(){
        return balance;
    }
    //Setter for balance  with validation
    public void setBalance(double balance ) throws IllegalArgumentException {
        if(balance<0)
            throw new IllegalArgumentException("Balance cannot be negative !");
         this.balance=balance;

    }
    //Getter and Setter for accountNumber
    public String  getAccountNumber(){return accountNumber ;}

    public void setAccountNumber(String accountNumber) throws IllegalArgumentException{
        if(accountNumber == null || accountNumber.isEmpty())
            throw new IllegalArgumentException("Account Number cannot be empty !");
            this.accountNumber =accountNumber;}

    // Getter and Setter for isApproved
    public boolean isApproved(){return isApproved;}
    public void setApproved(boolean approved ){
        this.isApproved=approved;
    }
    //Getter and Setter for Account type
    public String getAccountType(){ return accountType; }
    public void setAccountType(String accountType) throws
    IllegalArgumentException{
        if(accountType==null || accountType.isEmpty())
           throw new IllegalArgumentException("Account Type cannot be empty!");
           this.accountType=accountType;


    }
     // Getter for createdDate
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public void displayInfo(){
        System.out.println("Name: " +name);
        System.out.println("Account Number : " +accountNumber);
        System.out.println("Account Type :" +accountType);
        System.out.println("Balance :"+balance);
        System.out.println("Approved: " + (isApproved ? "Yes":"No"));
        System.out.println("Created Date: " + createdDate); 
    }
    @Override
    public String toString(){
        return "Customer{" + "name= '" + name + '\'' + ", accountNumber='" + accountNumber + '\''+",accountType='"+ accountType + '\'' + ", balance=" + balance + ", isApproved="+ isApproved + ", createdDate=" + createdDate +'}';
    }  
    }


    

