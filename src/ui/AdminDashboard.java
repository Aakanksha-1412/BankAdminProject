package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.BankController;
import model.Customer;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private BankController bank;

    private JLabel totalCustomersLabel;
    private JLabel totalBalanceLabel;
    private DefaultTableModel model;
    private JTable table;

    public AdminDashboard(BankController bankController) {
        this.bank = bankController;

        // Modern Nimbus look & feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            setTitle("Bank Admin Dashboard");
            setSize(1050, 650);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            initComponents();
            setVisible(true);
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // ---------------- Title ----------------
        JLabel titleLabel = new JLabel("Bank Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(52, 152, 219));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // ---------------- Top Summary ----------------
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));

        totalCustomersLabel = new JLabel("Total Customers: " + bank.totalCustomers());
        totalBalanceLabel = new JLabel("Total Balance: ₹" + bank.totalBalance());
        totalCustomersLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalBalanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        topPanel.add(totalCustomersLabel);
        topPanel.add(totalBalanceLabel);
        add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        // ---------------- Customer Table ----------------
        String[] columns = {"Name", "Account Number", "Type", "Balance", "Approved"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(230, 230, 250));
        table.setGridColor(Color.LIGHT_GRAY);

        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // ---------------- Buttons Panel ----------------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton addButton = new JButton("Add Customer");
        JButton editButton = new JButton("Edit Customer");
        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");
        JButton deleteButton = new JButton("Delete");
        JButton exitButton = new JButton("Exit");

        styleButton(addButton, new Color(52, 152, 219));
        styleButton(editButton, new Color(46, 204, 113));
        styleButton(approveButton, new Color(39, 174, 96));
        styleButton(rejectButton, new Color(243, 156, 18));
        styleButton(deleteButton, new Color(231, 76, 60));
        styleButton(exitButton, new Color(149, 165, 166));

        bottomPanel.add(addButton);
        bottomPanel.add(editButton);
        bottomPanel.add(approveButton);
        bottomPanel.add(rejectButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(exitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // ---------------- Button Actions ----------------
        addButton.addActionListener(e -> addCustomerDialog());
        editButton.addActionListener(e -> editCustomerDialog());

        approveButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String accNo = table.getValueAt(row, 1).toString();
                bank.approveCustomer(accNo, true);
                table.setValueAt("Yes", row, 4);
            } else {
                JOptionPane.showMessageDialog(this, "Select a customer to approve");
            }
            refreshSummary();
        });

        rejectButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String accNo = table.getValueAt(row, 1).toString();
                bank.approveCustomer(accNo, false);
                table.setValueAt("No", row, 4);
            } else {
                JOptionPane.showMessageDialog(this, "Select a customer to reject");
            }
            refreshSummary();
        });

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String accNo = table.getValueAt(row, 1).toString();
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this customer?");
                if (confirm == JOptionPane.YES_OPTION) {
                    bank.deleteCustomer(accNo);
                    model.removeRow(row);
                    refreshSummary();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a customer to delete");
            }
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    // ---------------- Helper Functions ----------------
    private void refreshTable() {
        model.setRowCount(0);
        List<Customer> customers = bank.getRecentlyAddedAccounts(bank.totalCustomers());
        for (Customer c : customers) {
            Object[] row = {
                    c.getName(),
                    c.getAccountNumber(),
                    c.getAccountType(),
                    c.getBalance(),
                    c.isApproved() ? "Yes" : "No"
            };
            model.addRow(row);
        }
    }

    private void refreshSummary() {
        totalCustomersLabel.setText("Total Customers: " + bank.totalCustomers());
        totalBalanceLabel.setText("Total Balance: ₹" + bank.totalBalance());
        refreshTable();
    }

    // ---------------- Add Customer ----------------
    private void addCustomerDialog() {
        JTextField nameField = new JTextField();
        JTextField accField = new JTextField();
        JTextField balanceField = new JTextField();
        String[] types = {"Saving", "Current"};
        JComboBox<String> typeBox = new JComboBox<>(types);

        // Enter key moves to next field only
        nameField.addActionListener(e -> accField.requestFocus());
        accField.addActionListener(e -> balanceField.requestFocus());
        balanceField.addActionListener(e -> typeBox.requestFocus());

        Object[] inputs = {
                "Name (letters & spaces only):", nameField,
                "Account Number (digits only):", accField,
                "Balance (numbers only):", balanceField,
                "Account Type:", typeBox
        };

        int result = JOptionPane.showConfirmDialog(this, inputs, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            addCustomerSubmit(nameField, accField, balanceField, typeBox);
        }
    }

    private void addCustomerSubmit(JTextField nameField, JTextField accField, JTextField balanceField, JComboBox<String> typeBox) {
        try {
            String name = nameField.getText().trim();
            String acc = accField.getText().trim();
            String balanceText = balanceField.getText().trim();
            String type = typeBox.getSelectedItem().toString();

            if (name.isEmpty() || !name.matches("[A-Za-z ]+")) {
                JOptionPane.showMessageDialog(this, "Name can contain only letters and spaces!");
                return;
            }

            if (!acc.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Account Number must be digits only!");
                return;
            }

            if (!balanceText.matches("\\d+(\\.\\d{1,2})?")) {
                JOptionPane.showMessageDialog(this, "Balance must be a number!");
                return;
            }

            double balance = Double.parseDouble(balanceText);
            Customer c = new Customer(name, acc, balance, false, type);
            bank.addCustomer(c);
            refreshSummary();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // ---------------- Edit Customer ----------------
    private void editCustomerDialog() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a customer to edit");
            return;
        }

        Customer c = bank.getRecentlyAddedAccounts(bank.totalCustomers()).get(row);

        JTextField nameField = new JTextField(c.getName());
        JTextField accField = new JTextField(c.getAccountNumber());
        JTextField balanceField = new JTextField(String.valueOf(c.getBalance()));
        String[] types = {"Saving", "Current"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setSelectedItem(c.getAccountType());

        // Enter key moves to next field only
        nameField.addActionListener(e -> accField.requestFocus());
        accField.addActionListener(e -> balanceField.requestFocus());
        balanceField.addActionListener(e -> typeBox.requestFocus());

        Object[] inputs = {
                "Name (letters & spaces only):", nameField,
                "Account Number (digits only):", accField,
                "Balance (numbers only):", balanceField,
                "Account Type:", typeBox
        };

        int result = JOptionPane.showConfirmDialog(this, inputs, "Edit Customer", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            editCustomerSubmit(c, nameField, accField, balanceField, typeBox);
        }
    }

    private void editCustomerSubmit(Customer c, JTextField nameField, JTextField accField, JTextField balanceField, JComboBox<String> typeBox) {
        try {
            String newName = nameField.getText().trim();
            if (!newName.isEmpty()) {
                if (!newName.matches("[A-Za-z ]+")) {
                    JOptionPane.showMessageDialog(this, "Name can contain only letters and spaces!");
                    return;
                }
                c.setName(newName);
            }

            String newAcc = accField.getText().trim();
            if (!newAcc.isEmpty()) {
                if (!newAcc.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Account Number must be digits only!");
                    return;
                }
                c.setAccountNumber(newAcc);
            }

            String balanceText = balanceField.getText().trim();
            if (!balanceText.isEmpty()) {
                if (!balanceText.matches("\\d+(\\.\\d{1,2})?")) {
                    JOptionPane.showMessageDialog(this, "Balance must be a number!");
                    return;
                }
                c.setBalance(Double.parseDouble(balanceText));
            }

            c.setAccountType(typeBox.getSelectedItem().toString());
            refreshSummary();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
    }
}
