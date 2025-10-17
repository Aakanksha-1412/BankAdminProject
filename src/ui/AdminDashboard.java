package ui;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import controller.BankController;
import model.Customer;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AdminDashboard extends JFrame{
    private BankController bank;
    public AdminDashboard(BankController bankController){
        this.bank= bankController;
        //ADDED MODERN  NIMBUS LOOK AND FEEL
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }catch(Exception e){
            e.printStackTrace();
        }
        //ENSURE SMOOTH UI THREADING

        SwingUtilities.invokeLater(()->{
        setTitle("Admin Dashboard");
        setSize(1000,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents(); //call method to add GUI  components
        setVisible(true);
    });
}
    private void initComponents(){
        //USE BORDER LAYOUT FOR JFRAME

        //added padding and white background
        setLayout(new BorderLayout(10,10));
        getContentPane().setBackground(Color.WHITE);
        //---------Titel Panel--------
        JLabel titleLabel =new JLabel("MY ADMIN DASHBOARD",SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Semibold",Font.BOLD,26));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(52,152,219));
        titleLabel.setForeground(Color.white);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(titleLabel,BorderLayout.NORTH);
        //---------Top Panel----------
        //Shows total customers and total balance
        JPanel topPanel =new JPanel();
        topPanel.setLayout(new GridLayout(1,2,20,0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
        topPanel.setBackground(Color.WHITE);
        JLabel totalCustomersLabel = new JLabel("Total Customers: "+bank.totalCustomers());
        JLabel totalBalanceLabel = new JLabel("Total Balance: "+bank.totalBalance());
        totalCustomersLabel.setFont(new Font("Segoe UI",Font.PLAIN,16));
        totalBalanceLabel.setFont(new Font("Segoe UI",Font.PLAIN,16));

        topPanel.add(totalCustomersLabel);
        topPanel.add(totalBalanceLabel);
        add(topPanel, BorderLayout.BEFORE_FIRST_LINE);


        //---------------Center Panel -----------
        //Table to display customer details
        String[] columns ={"Name","Account Number","Type","Balance","Approved"};
    
        //Using Default TableModel explicitly to allow row removal

        DefaultTableModel model = new DefaultTableModel(columns,0);
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI",Font.BOLD,14));
        table.getTableHeader().setBackground(new Color(230,230,250));
        table.setGridColor(Color.LIGHT_GRAY);

        //populate the table
         List<Customer> customers= bank.getRecentlyAddedAccounts(bank.totalCustomers());
        for(Customer c : customers) {
            Object[] row ={
                c.getName(),
                c.getAccountNumber(),
                c.getAccountType(),
                c.getBalance(),
                c.isApproved() ? "YES" : "No"
            };
            model.addRow(row);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(scrollPane,BorderLayout.CENTER);
        //----------BOTTOM PANEL-----------
        JPanel bottomPanel =new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
        JButton approveButton =new JButton("Approve");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        //Button colors and style
        styleButton(approveButton,new Color(46,204,113));
        styleButton(editButton,new Color(52,152,219));
        styleButton(deleteButton,new Color(231,76,60));
        


        bottomPanel.add(approveButton);
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        add(bottomPanel,BorderLayout.SOUTH);
        //-----------Button Actions--------
        //Approve  selected customer
        approveButton.addActionListener(e->{
            int row=table.getSelectedRow();
            if(row>=0){
                String accNo=table.getValueAt(row,1).toString();
                bank.approveCustomer(accNo, true);
                table.setValueAt("Yes",row,4);
                //Refresh top lables
                totalCustomersLabel.setText("Total Customers: "+bank.totalCustomers());
                totalBalanceLabel.setText("Total Balance: "+bank.totalBalance());

            }
            else{
                JOptionPane.showMessageDialog(this, "Please select a customer to approve");
            }

        });
        JButton rejectButton = new JButton("Reject");
        styleButton(rejectButton, new Color(243, 156, 18));
        rejectButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String accNo = table.getValueAt(row, 1).toString();
                bank.approveCustomer(accNo, false);
                 table.setValueAt("No", row, 4);}
            else {
           JOptionPane.showMessageDialog(this, "Select a customer to reject");}
    
        });
      bottomPanel.add(rejectButton);

        //Edit selected customer
        editButton.addActionListener(e->{
            int row =table.getSelectedRow();
            if(row>=0){
                String accNo = table.getValueAt(row, 1).toString();
                String newName= JOptionPane.showInputDialog("Enter new name");
                if(newName== null || newName.trim().isEmpty()){
                    JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                    return;
                }
                double newBalance =0;
                try{
                    newBalance=Double.parseDouble(JOptionPane.showInputDialog("Enter new balance"));
                }
                catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Invalid balance input!");
                    return;//exit action if input invalid
                }
                String newType =JOptionPane.showInputDialog("Enter new account type");
                if(newType== null || newType.trim().isEmpty()){
                    JOptionPane.showMessageDialog(this, "Account type cannot be empty!");
                    return;
                }
                
                bank.editCustomer(accNo, newName, newBalance, newType);
                //update table
                table.setValueAt(newName, row, 0);
                table.setValueAt(newType, row, 2);
                table.setValueAt(newBalance, row, 3);
                //Refresh top labels
                totalBalanceLabel.setText("Total Balance: "+bank.totalBalance());
            
            }
            else{
                JOptionPane.showMessageDialog(this, "Plese select a customer to edit");
            }
        });
        //Deleted selected customer
        deleteButton.addActionListener(e->{
            int row =table.getSelectedRow();
            if(row>=0){
                String accNo = table.getValueAt(row, 1).toString();
                int confirm= JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?");
                if(confirm==JOptionPane.YES_OPTION){
                    bank.deleteCustomer(accNo);
                    model.removeRow(row);
                    //Refresh top lables
                    totalCustomersLabel.setText("Total Customers: "+bank.totalCustomers());
                    totalBalanceLabel.setText("Total Balance: "+bank.totalBalance());
                }
                else{
                    JOptionPane.showMessageDialog(this,"Please select customer to delete");
                }
            }
        });
    }
    //Helper function for stylling buttons
    private void styleButton(JButton button,Color color){
        button.setBackground(color);
        button.setForeground(color.WHITE);
        button.setFont(new Font("Segoe UI",Font.BOLD,14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8,20,8,20));
    }

 

    }
    

