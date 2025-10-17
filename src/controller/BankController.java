  
package controller;
import java.util.*;
import java.util.stream.*;

import model.Customer;  

public class BankController {
    private List<Customer> customers = new ArrayList<>();
    //Add new customer
    public void addCustomer(Customer c){
        customers.add(c);
        System.out.println("Customer added sucessfully: " +c.getName());
    }
    // Show all customers
    public void showAllCustomers(){
        System.out.println("\n------CUSTOMER LIST------");
        for(Customer c : customers){
            c.displayInfo();
            System.out.println("--------------------------");


        }
    }
    //Approve or Reject customer
    public void approveCustomer(String accNo, boolean approve){
        boolean found = false;
        for(Customer c : customers){
            
            if(c.getAccountNumber().equals(accNo)){
                c.setApproved(approve);
                System.out.println("Account"+accNo +"approval set to : " + approve);
                found = true;
                break;
            }


            }
            if(!found){
            System.out.println("Customer with account " + accNo + " not found! " );}
        }
    //Delete Customer
    public void deleteCustomer(String accNo){
        Iterator<Customer> itr =customers.iterator();
        boolean found = false;
        while(itr.hasNext()){
            Customer c =itr.next();
            if(c.getAccountNumber().equals(accNo)){
                itr.remove();
                System.out.println("Deleted customer : "+ accNo);
                found=true;
                break;
                
            }

        }
        if(!found){
         System.out.println("Customer not found! ");}

    }
    //Calculate total balance  in bank
    public double totalBalance() {
        double total =0;
        for(Customer c: customers){
            total+=c.getBalance();
        }
        return total ;
        }
    //Count  total customers
    public int totalCustomers(){
        return customers.size();
    }
    // Edit customer details 
    public void editCustomer(String accNo , String newName , double newBalance, String newType){
        boolean found=false;
        for(Customer c : customers){
            if(c.getAccountNumber().equals(accNo)){
                c.setName(newName);
                c.setBalance(newBalance);
                c.setAccountType(newType);
                System.out.println("Customer details updated for account"+accNo);
                found=true;
                break;
                
            }
        }
        if(!found){
            System.out.println("Customer not found");
        }
    }
    // method for recently added accounts
    public List<Customer> getRecentlyAddedAccounts(int n) {
    return customers.stream()
            .sorted(Comparator.comparing(Customer::getCreatedDate).reversed())
            .limit(n)
            .collect(Collectors.toList());
}
    }


