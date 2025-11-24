package lld;

/*
    - The online stockbrokerage system should allow users to create and manage their trading accounts.
    - Users should be able to buy and sell stocks, as well as view their portfolio and transaction history.
    - The system should provide real-time stock quotes and market data to users.
    - The system should handle order placement, execution, and settlement processes.
    - The system should enforce various business rules and validations, such as checking account balances and stock availability.
    - The system should handle concurrent user requests and ensure data consistency and integrity.
    - The system should be scalable and able to handle a large number of users and transactions.
    - The system should be secure and protect sensitive user information.

    Entities
    - User
    - Stock
    - Transaction
    - Account

    functionality:
    createAccount;
    buyStock
    sellStock
    viewPortfolio
    viewTransactionHistory
    viewStocks

    Services
    User
    + createAccount()
    + addStockInPortfolio(stock, qty)
    + removeStock(stock, qty)

    Account
    + addStock(stock, qty)
    + removeStock(stock, qty)
    + addTransaction(txn)
    + viewPortfolio()
    + viewTransactions()

    StockManager
    - instance: StockManager
    - stocks: Map<int, Stock>
    - observers: List<StockObserver>
    + getInstance()
    + addStock(stock)
    + registerObserver(observer)
    + notifyObservers(stock)
    + updateStockPrice(stockId, newPrice)
    + buyStock(account, stockId, shares)
    + sellStock(account, stockId, shares)
    + listStocks()

*/
public class StockBroker {
}
