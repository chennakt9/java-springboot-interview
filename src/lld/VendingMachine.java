package lld;

import java.util.HashMap;
import java.util.Map;

/*


*/

// ----------------------- Product -----------------------
class Product {
    private String id;        // -
    private String name;      // -
    private int price;        // -

    public Product(String id, String name, int price) {   // +
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() { return id; }                 // +
    public String getName() { return name; }             // +
    public int getPrice() { return price; }              // +
}

// ----------------------- InventoryItem -----------------------
class InventoryItem {
    private Product product;   // -
    private int quantity;      // -

    public InventoryItem(Product product, int quantity) {   // +
        this.product = product;
        this.quantity = quantity;
    }

    public boolean isAvailable() {                         // +
        return quantity > 0;
    }

    public void reduceQuantity(int count) {                // +
        this.quantity -= count;
    }

    public Product getProduct() { return product; }        // +
}

class Inventory {
    private Map<String, InventoryItem> items = new HashMap<>();   // -

    public void addProduct(Product product, int count) {          // +
        items.put(product.getId(), new InventoryItem(product, count));
    }

    public boolean hasStock(String productId) {                   // +
        return items.containsKey(productId) && items.get(productId).isAvailable();
    }

    public Product getProduct(String productId) {                 // +
        return items.get(productId).getProduct();
    }

    public void reduceStock(String productId) {                   // +
        items.get(productId).reduceQuantity(1);
    }
}

// ----------------------- PaymentService -----------------------
class PaymentService {
    public boolean process(int amount) {    // +
        // always succeeds because we trust humanity (bad idea IRL)
        return true;
    }
}

// ----------------------- VendingState (interface) -----------------------
interface VendingState {
    void insertMoney(int amount);      // +
    void selectProduct(String productId); // +
    void dispense();                   // +
}

// ----------------------- IdleState -----------------------
class IdleState implements VendingState {
    private VendingMachine machine;   // -

    public IdleState(VendingMachine machine) { // +
        this.machine = machine;
    }

    public void insertMoney(int amount) {      // +
        machine.addBalance(amount);
        machine.setState(machine.getHasMoneyState());
    }

    public void selectProduct(String productId) {  // +
        System.out.println("Insert money first.");
    }

    public void dispense() {                      // +
        System.out.println("No money inserted.");
    }
}

// ----------------------- HasMoneyState -----------------------
class HasMoneyState implements VendingState {
    private VendingMachine machine;   // -

    public HasMoneyState(VendingMachine machine) { // +
        this.machine = machine;
    }

    public void insertMoney(int amount) {      // +
        machine.addBalance(amount);
    }

    public void selectProduct(String productId) {  // +
        if (!machine.getInventory().hasStock(productId)) {
            System.out.println("Out of stock.");
            return;
        }

        Product p = machine.getInventory().getProduct(productId);

        if (machine.getBalance() < p.getPrice()) {
            System.out.println("Not enough money.");
            return;
        }

        machine.setSelectedProduct(p);
        machine.setState(machine.getDispensingState());
    }

    public void dispense() {                     // +
        System.out.println("Select a product first.");
    }
}

// ----------------------- DispensingState -----------------------
class DispensingState implements VendingState {
    private VendingMachine machine;   // -

    public DispensingState(VendingMachine machine) { // +
        this.machine = machine;
    }

    public void insertMoney(int amount) {            // +
        System.out.println("Wait, dispensing...");
    }

    public void selectProduct(String productId) {     // +
        System.out.println("Already dispensing...");
    }

    public void dispense() {                          // +
        Product p = machine.getSelectedProduct();
        System.out.println("Dispensing: " + p.getName());

        machine.getInventory().reduceStock(p.getId());
        machine.reduceBalance(p.getPrice());

        machine.setSelectedProduct(null);
        machine.setState(machine.getIdleState());
    }
}

// ----------------------- VendingMachine -----------------------
class VendingMachine {
    private VendingState idleState;        // -
    private VendingState hasMoneyState;    // -
    private VendingState dispensingState;  // -

    private VendingState currentState;     // -

    private Inventory inventory = new Inventory();   // -
    private PaymentService paymentService = new PaymentService(); // -

    private Product selectedProduct;        // -
    private int balance = 0;                // -

    public VendingMachine() {     // +
        idleState = new IdleState(this);
        hasMoneyState = new HasMoneyState(this);
        dispensingState = new DispensingState(this);

        currentState = idleState;
    }

    public void setState(VendingState state) {       // +
        this.currentState = state;
    }

    public void insertMoney(int amount) {            // +
        currentState.insertMoney(amount);
    }

    public void selectProduct(String productId) {    // +
        currentState.selectProduct(productId);
    }

    public void dispense() {                         // +
        currentState.dispense();
    }

    public void addProduct(Product p, int qty) {     // +
        inventory.addProduct(p, qty);
    }

    public void addBalance(int amount) { balance += amount; }      // +
    public void reduceBalance(int amount) { balance -= amount; }   // +
    public int getBalance() { return balance; }                     // +

    public void setSelectedProduct(Product p) { this.selectedProduct = p; } // +
    public Product getSelectedProduct() { return selectedProduct; }         // +

    public Inventory getInventory() { return inventory; }   // +

    public VendingState getIdleState() { return idleState; }              // +
    public VendingState getHasMoneyState() { return hasMoneyState; }      // +
    public VendingState getDispensingState() { return dispensingState; }  // +
}

// ----------------------- MAIN (demo) -----------------------
class Main {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();

        vm.addProduct(new Product("C1", "Coke", 25), 3);
        vm.addProduct(new Product("P1", "Pepsi", 20), 2);

        vm.insertMoney(30);
        vm.selectProduct("C1");
        vm.dispense();

        vm.insertMoney(10);
        vm.selectProduct("P1");
        vm.dispense();
    }
}

