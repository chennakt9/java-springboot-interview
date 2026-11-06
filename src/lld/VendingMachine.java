
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
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

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

// ==================== Payment ====================

class Payment {
    private final Set<Integer> acceptedDenominations = new HashSet<>(
        Arrays.asList(1, 2, 5, 10, 20, 50, 100)
    );

    private final Map<Integer, Integer> insertedCoins = new HashMap<>();
    private int balance;

    public void addMoney(int denomination) {
        if (!acceptedDenominations.contains(denomination)) {
            throw new IllegalArgumentException("Invalid denomination");
        }

        insertedCoins.put(denomination, insertedCoins.getOrDefault(denomination, 0) + 1);
        balance += denomination;
    }

    public int getBalance() {
        return balance;
    }

    public Map<Integer, Integer> getInsertedCoins() {
        return new HashMap<>(insertedCoins);
    }

    public void reset() {
        balance = 0;
        insertedCoins.clear();
    }
}

// ==================== Cash Inventory ====================

class CashInventory {
    private final Map<Integer, Integer> coins = new HashMap<>();

    public void addCoins(int denomination, int quantity) {
        if (denomination <= 0 || quantity <= 0) {
            throw new IllegalArgumentException("Invalid coin details");
        }

        coins.put(denomination, coins.getOrDefault(denomination, 0) + quantity);
    }

    public Map<Integer, Integer> getAvailableCoins(Map<Integer, Integer> insertedCoins) {
        Map<Integer, Integer> available = new HashMap<>(coins);

        for (Map.Entry<Integer, Integer> entry : insertedCoins.entrySet()) {
            available.put(
                entry.getKey(),
                available.getOrDefault(entry.getKey(), 0) + entry.getValue()
            );
        }

        return available;
    }

    public void settle(
        Map<Integer, Integer> insertedCoins,
        Map<Integer, Integer> changeCoins
    ) {
        Map<Integer, Integer> updatedCoins = new HashMap<>(coins);

        // Add inserted money.
        for (Map.Entry<Integer, Integer> entry : insertedCoins.entrySet()) {
            updatedCoins.put(
                entry.getKey(),
                updatedCoins.getOrDefault(entry.getKey(), 0) + entry.getValue()
            );
        }

        // Deduct returned change.
        for (Map.Entry<Integer, Integer> entry : changeCoins.entrySet()) {
            int denomination = entry.getKey();
            int count = entry.getValue();

            if (updatedCoins.getOrDefault(denomination, 0) < count) {
                throw new IllegalStateException("Insufficient change coins");
            }

            updatedCoins.put(denomination, updatedCoins.get(denomination) - count);
        }

        coins.clear();
        coins.putAll(updatedCoins);
    }

    public Map<Integer, Integer> getCoins() {
        return new HashMap<>(coins);
    }
}

// ==================== Change Strategy ====================

interface ChangeStrategy {
    Map<Integer, Integer> getChange(
        int amount,
        Map<Integer, Integer> availableCoins
    );
}

// Finds the minimum number of coins using bounded DP.
class MinimumCoinChangeStrategy implements ChangeStrategy {

    private static final int INF = Integer.MAX_VALUE;

    @Override
    public Map<Integer, Integer> getChange(
        int amount,
        Map<Integer, Integer> availableCoins
    ) {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid change amount");
        }

        if (amount == 0) {
            return new LinkedHashMap<>();
        }

        List<Integer> denominations = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : availableCoins.entrySet()) {
            if (entry.getKey() > 0 && entry.getValue() > 0) {
                denominations.add(entry.getKey());
            }
        }

        denominations.sort(Collections.reverseOrder());

        Map<String, Integer> dp = new HashMap<>();
        Map<String, Integer> selectedCount = new HashMap<>();

        int result = recur(
            0,
            amount,
            denominations,
            availableCoins,
            dp,
            selectedCount
        );

        if (result == INF) {
            throw new IllegalStateException("Exact change unavailable");
        }

        Map<Integer, Integer> change = new LinkedHashMap<>();
        int remaining = amount;

        for (int i = 0; i < denominations.size() && remaining > 0; i++) {
            String key = i + ":" + remaining;
            int denomination = denominations.get(i);
            int count = selectedCount.getOrDefault(key, 0);

            if (count > 0) {
                change.put(denomination, count);
                remaining -= count * denomination;
            }
        }

        if (remaining != 0) {
            throw new IllegalStateException("Unable to calculate change");
        }

        return change;
    }

    private int recur(
        int index,
        int amount,
        List<Integer> denominations,
        Map<Integer, Integer> availableCoins,
        Map<String, Integer> dp,
        Map<String, Integer> selectedCount
    ) {
        if (amount == 0) {
            return 0;
        }

        if (index == denominations.size()) {
            return INF;
        }

        String key = index + ":" + amount;

        if (dp.containsKey(key)) {
            return dp.get(key);
        }

        int denomination = denominations.get(index);
        int maxCount = Math.min(
            availableCoins.getOrDefault(denomination, 0),
            amount / denomination
        );

        int minCoins = INF;
        int bestCount = 0;

        for (int count = 0; count <= maxCount; count++) {
            int remaining = amount - count * denomination;

            int next = recur(
                index + 1,
                remaining,
                denominations,
                availableCoins,
                dp,
                selectedCount
            );

            if (next != INF && count + next < minCoins) {
                minCoins = count + next;
                bestCount = count;
            }
        }

        dp.put(key, minCoins);
        selectedCount.put(key, bestCount);

        return minCoins;
    }
}

// ==================== State Interface ====================

interface State {
    void selectProduct(String productId);
    void insertMoney(int denomination);
    void dispense();
    void cancel();
}

// ==================== Vending Machine ====================

class VendingMachine {
    private final Inventory inventory;
    private final Payment payment;
    private final CashInventory cashInventory;
    private final ChangeStrategy changeStrategy;

    private final State idleState;
    private final State readyState;
    private final State dispensingState;

    private State currentState;
    private Product selectedProduct;

    public VendingMachine(
        Inventory inventory,
        ChangeStrategy changeStrategy
    ) {
        this.inventory = inventory;
        this.payment = new Payment();
        this.cashInventory = new CashInventory();
        this.changeStrategy = changeStrategy;

        // Initial cash available in the machine.
        cashInventory.addCoins(1, 20);
        cashInventory.addCoins(2, 20);
        cashInventory.addCoins(5, 20);
        cashInventory.addCoins(10, 20);
        cashInventory.addCoins(20, 20);
        cashInventory.addCoins(50, 20);
        cashInventory.addCoins(100, 20);

        idleState = new IdleState(this);
        readyState = new ReadyState(this);
        dispensingState = new DispensingState(this);

        currentState = idleState;
    }

    public void selectProduct(String productId) {
        currentState.selectProduct(productId);
    }

    public void insertMoney(int denomination) {
        currentState.insertMoney(denomination);
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

    public CashInventory getCashInventory() {
        return cashInventory;
    }

    public ChangeStrategy getChangeStrategy() {
        return changeStrategy;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product product) {
        selectedProduct = product;
    }

    public void reset() {
        selectedProduct = null;
        payment.reset();
        currentState = idleState;
    }
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
    public void insertMoney(int denomination) {
        System.out.println("Please select a product first");
    }

    @Override
    public void dispense() {
        System.out.println("Please select a product first");
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
    public void insertMoney(int denomination) {
        try {
            machine.getPayment().addMoney(denomination);

            System.out.println("Inserted: " + denomination);
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
            System.out.println("Required: " + product.getPrice());
            System.out.println("Current: " + machine.getPayment().getBalance());
            return;
        }

        machine.setState(machine.getDispensingState());
        machine.dispense();
    }

    @Override
    public void cancel() {
        Map<Integer, Integer> refund = machine.getPayment().getInsertedCoins();

        System.out.println("Refund amount: " + machine.getPayment().getBalance());
        System.out.println("Refund denominations: " + refund);

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
    public void dispense() {
        Product product = machine.getSelectedProduct();
        Payment payment = machine.getPayment();

        try {
            // 1. Validate product availability.
            if (!machine.getInventory().isAvailable(product.getId())) {
                throw new IllegalStateException("Product out of stock");
            }

            // 2. Calculate required change.
            int changeAmount = payment.getBalance() - product.getPrice();

            // 3. Get available coins, including inserted coins.
            Map<Integer, Integer> availableCoins =
                machine.getCashInventory().getAvailableCoins(
                    payment.getInsertedCoins()
                );

            // 4. Calculate change using Strategy Pattern.
            Map<Integer, Integer> changeCoins =
                machine.getChangeStrategy().getChange(
                    changeAmount,
                    availableCoins
                );

            // 5. Deduct product stock.
            machine.getInventory().dispense(product.getId());

            // 6. Deposit inserted money and deduct returned change.
            machine.getCashInventory().settle(
                payment.getInsertedCoins(),
                changeCoins
            );

            // 7. Complete transaction.
            System.out.println("Dispensing: " + product.getName());
            System.out.println("Change amount: " + changeAmount);
            System.out.println("Change denominations: " + changeCoins);
            System.out.println("Transaction successful");

            machine.reset();

        } catch (IllegalStateException e) {
            System.out.println("Transaction failed: " + e.getMessage());
            machine.setState(machine.getReadyState());
        }
    }

    @Override
    public void selectProduct(String productId) {
        System.out.println("Dispensing in progress");
    }

    @Override
    public void insertMoney(int denomination) {
        System.out.println("Dispensing in progress");
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

        ChangeStrategy changeStrategy = new MinimumCoinChangeStrategy();

        VendingMachine machine = new VendingMachine(
            inventory,
            changeStrategy
        );

        // Transaction 1: Successful purchase.
        System.out.println("===== Transaction 1 =====");

        machine.selectProduct("1");
        machine.insertMoney(50);
        machine.dispense();

        // Transaction 2: Insufficient balance.
        System.out.println("\n===== Transaction 2 =====");

        machine.selectProduct("2");
        machine.insertMoney(20);
        machine.dispense();

        machine.insertMoney(20);
        machine.dispense();

        // Transaction 3: Cancel transaction.
        System.out.println("\n===== Transaction 3 =====");

        machine.selectProduct("3");
        machine.insertMoney(50);
        machine.cancel();

        // Transaction 4: Invalid product.
        System.out.println("\n===== Transaction 4 =====");

        machine.selectProduct("invalid");

        // Transaction 5: Stock check.
        System.out.println("\n===== Inventory =====");

        System.out.println("Coke stock: " + inventory.getStock("1"));
        System.out.println("Pepsi stock: " + inventory.getStock("2"));
        System.out.println("Water stock: " + inventory.getStock("3"));
    }
}
