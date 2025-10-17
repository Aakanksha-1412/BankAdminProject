package ui;
import java.util.Scanner;
import controller.BankController;
import model.Customer;

public class MainApp {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        BankController bank = new BankController();
        while(true){
            System.out.println("\n---Bank Admin Dashboard---");
            System.out.println("1.Add Customer");
            System.out.println("2.Show All Customers");
            System.out.println("3.Approve/Reject Customer");
            System.out.println("4.Edit Customer");
            System.out.println("5.Delete Customer");
            System.out.println("6.Total Customer");
            System.out.println("7.Total Balance");
            System.out.println("8.Exit");
            System.out.println("9. Open Admin Dashboard (GUI)");

            System.out.println("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch(choice){
                case 1: 
                //Add customer
                System.out.print("Enter name: ");
                String name= sc.nextLine();
                System.out.print("Enter account number: ");
                String accNo= sc.nextLine();
                System.out.print("Enter Balance: ");
                double balance= sc.nextDouble();
                sc.nextLine();
                System.out.print("Enter account type (Saving/Current):");
                String type = sc.nextLine();
                try{
                    Customer c=new Customer(name, accNo, balance, false, type);
                    bank.addCustomer(c);}
                    catch(IllegalArgumentException e){
                        System.out.println("Error:  "+e.getMessage());
                    }
                    break;
                case 2:
                   bank.showAllCustomers();
                   break;
                case 3:
                 System.out.print("Enter account  number : ");
                 String accToApprove = sc.nextLine();
                 System.out.print("Approve? (true/false): ");
                 boolean approve=sc.nextBoolean();
                 sc.nextLine();
                 bank.approveCustomer(accToApprove,approve);
                 break;

                
                case 4:
                 System.out.print("Enter account number to edit: ");
                 String accToEdit = sc.nextLine();
                 System.out.print("Enter new name: ");
                 String newName = sc.nextLine();
                 System.out.print("Enter new balance");
                 double newBalance = sc.nextDouble();
                 sc.nextLine();
                 System.out.print("Enter new account type: ");
                 String newType = sc.nextLine();
                 bank.editCustomer(accToEdit,newName,newBalance,newType);
                 break;
                case 5:
                 System.out.println("Enter account number to delete: ");
                 String accToDelete =sc.nextLine();
                 bank.deleteCustomer(accToDelete);
                 break;
                case 6:
                 System.out.println("Total customers: "+bank.totalCustomers());
                 break;
                case 7:
                 System.out.println("Total bank balance: "+bank.totalBalance());
                 break;
                case 8:
                 System.out.println("Exiting....");
                 sc.close();
                 System.exit(0);
                case 9 :
                 new ui.AdminDashboard(bank); // Launch your Swing Admin Dashboard
                 break;

                default:
                 System.out.println("Invalid choice! Try again.");
                }

                   
    
            }


        }
    }







                 
                
                

            
 
        

    
    

