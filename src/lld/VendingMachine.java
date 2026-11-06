
import java.util.*;

// ==================== Product ====================

class Product {
    private final String id;
    private final String name;
    private final int price;

    public Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}

// ==================== Inventory ====================

class Inventory {
    private final Map<String, Product> products = new HashMap<>();
    private final Map<String, Integer> stock = new HashMap<>();

    public void addProduct(Product product, int quantity) {
        products.put(product.getId(), product);
        stock.put(product.getId(), stock.getOrDefault(product.getId(), 0) + quantity);
    }

    public Product getProduct(String productId) {
        return products.get(productId);
    }

    public boolean isAvailable(String productId) {
        return stock.getOrDefault(productId, 0) > 0;
    }

    public void dispense(String productId) {
        if (!isAvailable(productId)) {
            throw new IllegalStateException("Product out of stock");
        }

        stock.put(productId, stock.get(productId) - 1);
    }

    public int getStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }
}

// ==================== Change Strategy ====================

interface ChangeStrategy {
    Map<Integer, Integer> getChange(int amount, Map<Integer, Integer> availableCoins);
}

class GreedyChangeStrategy implements ChangeStrategy {

    @Override
    public Map<Integer, Integer> getChange(int amount, Map<Integer, Integer> availableCoins) {
        Map<Integer, Integer> result = new LinkedHashMap<>();

        List<Integer> denominations = new ArrayList<>(availableCoins.keySet());
        denominations.sort(Collections.reverseOrder());

        for (int denomination : denominations) {
            int count = Math.min(amount / denomination, availableCoins.get(denomination));

            if (count > 0) {
                result.put(denomination, count);
                amount -= denomination * count;
            }
        }

        if (amount != 0) {
            throw new IllegalStateException("Exact change unavailable");
        }

        return result;
    }
}

// ==================== Payment ====================

class Payment {
    private int balance;

    public void addMoney(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        balance += amount;
    }

    public int getBalance() {
        return balance;
    }

    public void reset() {
        balance = 0;
    }
}

// ==================== Vending Machine ====================

class VendingMachine {
    private final Inventory inventory;
    private final Payment payment;
    private final ChangeStrategy changeStrategy;

    private final Map<Integer, Integer> coinInventory = new HashMap<>();

    private State idleState;
    private State readyState;
    private State dispensingState;
    private State currentState;

    private Product selectedProduct;

    public VendingMachine(Inventory inventory, ChangeStrategy changeStrategy) {
        this.inventory = inventory;
        this.changeStrategy = changeStrategy;
        this.payment = new Payment();

        idleState = new IdleState(this);
        readyState = new ReadyState(this);
        dispensingState = new DispensingState(this);

        currentState = idleState;

        coinInventory.put(1, 20);
        coinInventory.put(5, 20);
        coinInventory.put(10, 20);
        coinInventory.put(20, 20);
        coinInventory.put(50, 20);
    }

    public void selectProduct(String productId) {
        currentState.selectProduct(productId);
    }

    public void insertMoney(int amount) {
        currentState.insertMoney(amount);
    }

    public void dispense() {
        currentState.dispense();
    }

    public void cancel() {
        currentState.cancel();
    }

    public void setState(State state) {
        currentState = state;
    }

    public State getIdleState() {
        return idleState;
    }

    public State getReadyState() {
        return readyState;
    }

    public State getDispensingState() {
        return dispensingState;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Payment getPayment() {
        return payment;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product product) {
        selectedProduct = product;
    }

    public Map<Integer, Integer> getCoinInventory() {
        return coinInventory;
    }

    public Map<Integer, Integer> calculateChange(int amount) {
        return changeStrategy.getChange(amount, coinInventory);
    }

    public void addCoins(Map<Integer, Integer> coins) {
        for (Map.Entry<Integer, Integer> entry : coins.entrySet()) {
            coinInventory.put(entry.getKey(), coinInventory.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }
    }

    public void reset() {
        selectedProduct = null;
        payment.reset();
        currentState = idleState;
    }

    public void completeTransaction() {
        Product product = selectedProduct;
        int change = payment.getBalance() - product.getPrice();

        Map<Integer, Integer> changeCoins = calculateChange(change);

        // Update inventory only after change calculation succeeds.
        inventory.dispense(product.getId());

        for (Map.Entry<Integer, Integer> entry : changeCoins.entrySet()) {
            int denomination = entry.getKey();
            int count = entry.getValue();

            coinInventory.put(denomination, coinInventory.get(denomination) - count);
        }

        // Store inserted money in coin inventory.
        // This simplified implementation treats inserted money as a balance,
        // rather than tracking individual inserted denominations.

        System.out.println("Dispensing: " + product.getName());
        System.out.println("Change: " + changeCoins);

        reset();
    }
}

// ==================== State Interface ====================

interface State {
    void selectProduct(String productId);
    void insertMoney(int amount);
    void dispense();
    void cancel();
}

// ==================== Idle State ====================

class IdleState implements State {
    private final VendingMachine machine;

    public IdleState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void selectProduct(String productId) {
        Product product = machine.getInventory().getProduct(productId);

        if (product == null) {
            System.out.println("Invalid product");
            return;
        }

        if (!machine.getInventory().isAvailable(productId)) {
            System.out.println("Product out of stock");
            return;
        }

        machine.setSelectedProduct(product);
        machine.setState(machine.getReadyState());

        System.out.println("Selected: " + product.getName());
        System.out.println("Price: " + product.getPrice());
    }

    @Override
    public void insertMoney(int amount) {
        System.out.println("Select a product first");
    }

    @Override
    public void dispense() {
        System.out.println("Select a product first");
    }

    @Override
    public void cancel() {
        System.out.println("No active transaction");
    }
}

// ==================== Ready State ====================

class ReadyState implements State {
    private final VendingMachine machine;

    public ReadyState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void selectProduct(String productId) {
        System.out.println("Product already selected");
    }

    @Override
    public void insertMoney(int amount) {
        try {
            machine.getPayment().addMoney(amount);
            System.out.println("Balance: " + machine.getPayment().getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void dispense() {
        Product product = machine.getSelectedProduct();

        if (machine.getPayment().getBalance() < product.getPrice()) {
            System.out.println("Insufficient balance");
            return;
        }

        machine.setState(machine.getDispensingState());
        machine.dispense();
    }

    @Override
    public void cancel() {
        System.out.println("Refund: " + machine.getPayment().getBalance());
        machine.reset();
    }
}

// ==================== Dispensing State ====================

class DispensingState implements State {
    private final VendingMachine machine;

    public DispensingState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void selectProduct(String productId) {
        System.out.println("Dispensing in progress");
    }

    @Override
    public void insertMoney(int amount) {
        System.out.println("Dispensing in progress");
    }

    @Override
    public void dispense() {
        try {
            machine.completeTransaction();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            machine.setState(machine.getReadyState());
        }
    }

    @Override
    public void cancel() {
        System.out.println("Cannot cancel during dispensing");
    }
}

// ==================== Main ====================

public class Main {
    public static void main(String[] args) {

        Inventory inventory = new Inventory();

        inventory.addProduct(new Product("1", "Coke", 40), 5);
        inventory.addProduct(new Product("2", "Pepsi", 35), 3);
        inventory.addProduct(new Product("3", "Water", 20), 10);

        ChangeStrategy changeStrategy = new GreedyChangeStrategy();

        VendingMachine machine = new VendingMachine(inventory, changeStrategy);

        // Transaction 1
        machine.selectProduct("1");
        machine.insertMoney(50);
        machine.dispense();

        System.out.println("--------------------");

        // Transaction 2
        machine.selectProduct("2");
        machine.insertMoney(20);
        machine.dispense();

        machine.insertMoney(20);
        machine.dispense();

        System.out.println("--------------------");

        // Transaction 3: Cancellation
        machine.selectProduct("3");
        machine.insertMoney(50);
        machine.cancel();

        System.out.println("--------------------");

        // Transaction 4: Out of stock check
        machine.selectProduct("invalid");
    }
}
