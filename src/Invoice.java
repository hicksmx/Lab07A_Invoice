import java.util.ArrayList;

public class Invoice {
    private Customer customer;
    private ArrayList<LineItem> items;
    private double totalAmount;

    public Invoice(Customer customer) {
        this.customer = customer;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
    }

    public void addLineItem(LineItem item) {
        items.add(item);
        calculateTotal();
    }

    public void calculateTotal() {
        totalAmount = 0.0;
        for (LineItem item : items) {
            totalAmount += item.getTotal();
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public ArrayList<LineItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}