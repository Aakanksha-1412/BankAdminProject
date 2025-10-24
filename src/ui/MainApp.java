package ui;
import controller.BankController;
import ui.AdminDashboard;
public class MainApp {
    public static void main(String[] args) {
        //create Bankcontroller instances
        BankController bank = new BankController();
        //launch the Admin dashboard gui directly
        new AdminDashboard(bank);
    }
    
}

