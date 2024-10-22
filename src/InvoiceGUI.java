import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InvoiceGUI extends JFrame {
    private Customer customer;
    private Invoice invoice;
    private ArrayList<Product> products;

    private JTextField nameField;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField zipField;

    private JComboBox<String> productCombo;
    private JTextField quantityField;
    private JTextArea displayArea;

    public InvoiceGUI() {
        // Initialize sample products
        products = new ArrayList<>();
        products.add(new Product("Widget", 9.99));
        products.add(new Product("Gadget", 19.99));
        products.add(new Product("Tool", 14.99));

        setTitle("Invoice Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Customer Panel
        JPanel customerPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));

        customerPanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        customerPanel.add(nameField);

        customerPanel.add(new JLabel("Street:"));
        streetField = new JTextField(20);
        customerPanel.add(streetField);

        customerPanel.add(new JLabel("City:"));
        cityField = new JTextField(20);
        customerPanel.add(cityField);

        customerPanel.add(new JLabel("State:"));
        stateField = new JTextField(20);
        customerPanel.add(stateField);

        customerPanel.add(new JLabel("ZIP:"));
        zipField = new JTextField(20);
        customerPanel.add(zipField);

        // Line Item Panel
        JPanel lineItemPanel = new JPanel(new FlowLayout());
        lineItemPanel.setBorder(BorderFactory.createTitledBorder("Add Line Item"));

        String[] productNames = products.stream()
                .map(p -> p.getName())
                .toArray(String[]::new);
        productCombo = new JComboBox<>(productNames);
        lineItemPanel.add(new JLabel("Product:"));
        lineItemPanel.add(productCombo);

        lineItemPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        lineItemPanel.add(quantityField);

        JButton addButton = new JButton("Add Item");
        addButton.addActionListener(e -> addLineItem());
        lineItemPanel.add(addButton);

        // Display Area
        displayArea = new JTextArea(20, 40);
        displayArea.setEditable(false);

        // Main Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(customerPanel, BorderLayout.NORTH);
        topPanel.add(lineItemPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JButton createInvoiceButton = new JButton("Create Invoice");
        createInvoiceButton.addActionListener(e -> createInvoice());
        add(createInvoiceButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void createInvoice() {
        // Create customer and invoice
        Address address = new Address(
                streetField.getText(),
                cityField.getText(),
                stateField.getText(),
                zipField.getText()
        );

        customer = new Customer(nameField.getText(), address);
        invoice = new Invoice(customer);

        displayArea.setText("Invoice created! Add line items...\n");
    }

    private void addLineItem() {
        if (invoice == null) {
            JOptionPane.showMessageDialog(this,
                    "Please create an invoice first!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText());
            Product selectedProduct = products.get(productCombo.getSelectedIndex());

            LineItem item = new LineItem(selectedProduct, quantity);
            invoice.addLineItem(item);

            displayInvoice();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid quantity!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append("INVOICE\n");
        sb.append("=======\n\n");

        // Customer information
        sb.append("Customer:\n");
        sb.append(customer.getName()).append("\n");
        sb.append(customer.getAddress().getFullAddress()).append("\n\n");

        // Line items
        sb.append("Line Items:\n");
        sb.append("===========\n");
        sb.append(String.format("%-20s %-10s %-10s %-10s\n",
                "Product", "Price", "Quantity", "Total"));
        sb.append("-".repeat(50)).append("\n");

        for (LineItem item : invoice.getItems()) {
            sb.append(String.format("%-20s $%-9.2f %-10d $%-9.2f\n",
                    item.getProduct().getName(),
                    item.getProduct().getUnitPrice(),
                    item.getQuantity(),
                    item.getTotal()));
        }

        sb.append("-".repeat(50)).append("\n");
        sb.append(String.format("Total Amount Due: $%.2f\n", invoice.getTotalAmount()));

        displayArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InvoiceGUI().setVisible(true);
        });
    }
}