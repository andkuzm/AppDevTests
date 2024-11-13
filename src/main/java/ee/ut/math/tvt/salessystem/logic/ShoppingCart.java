package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;


import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private static final Logger log = LogManager.getLogger(ShoppingCart.class);

    private final SalesSystemDAO dao;
    private final List<SoldItem> items;

    public ShoppingCart(SalesSystemDAO dao) {
        this.dao = dao;
        //this.items = dao.getShoppingCartItems();
        this.items = new ArrayList<>();
    }

    /**
     * Add new SoldItem to table.
     */
    public void addItem(SoldItem item) {
        int amount = getAmount(item);
        int[] i = new int[]{0};
        boolean itemExists = checkForItem(item, i);
        System.out.println(i);
        if (itemExists) {
            addToExistingItem(item, amount, i);
        } else {
            addNewItem(item, amount);
        }
        log.debug("Added " + item.getQuantity() + " of " + item.getName() + " to cart");
    }

    private void addNewItem(SoldItem item, int amount) {
        if (amount < item.getQuantity())
            throw new SalesSystemException(item.getName() + " there is currently not enough of " + item.getName() + " in stock to complete current purchase");
        items.add(item);
    }

    private void addToExistingItem(SoldItem item, int amount, int[] i) {
        int newQuantity = item.getQuantity() + items.get(i[0]).getQuantity();
        if (amount < newQuantity)
            throw new SalesSystemException("there is currently not enough of " + item.getName() + " in stock to complete current purchase");
        item.setQuantity(newQuantity);
        items.set(i[0], item);
        log.debug("Added " + item.getQuantity() + " of " + item.getName() + " to cart");
    }

    private boolean checkForItem(SoldItem item, int[] i) {
        for (int index = 0; index < items.size(); index++) {
            if (items.get(index).getName().equals(item.getName())) {
                i[0] = index;
                return true;
            }
        } return false;
    }

    private int getAmount(SoldItem item) {
        if (item.getQuantity() < 0) throw new SalesSystemException("Quantity can not be less than 0");
        int amount = dao.findStockItem(item.getBarcode()).getQuantity();
        if (item.getQuantity() > amount)
            throw new SalesSystemException("there is currently not enough of " + item.getName() + " in stock to complete current purchase");
        return amount;
    }

    public double getTotal() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            SoldItem item = items.get(i);
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public List<SoldItem> getAll() {
        return items;
    }

    public void cancelCurrentPurchase() {
        items.clear();
    }

    public void submitCurrentPurchase() {
        // note the use of transactions. InMemorySalesSystemDAO ignores transactions
        // but when you start using hibernate in lab5, then it will become relevant.
        // what is a transaction? https://stackoverflow.com/q/974596
        if (!dao.isTransactionActive() && items.size() > 0) {
            dao.beginTransaction();
            try {
                makePurchase();
            } catch (Exception e) {
                dao.rollbackTransaction();
                log.error("Error occurred during purchase submission: {}", e.getMessage());
                throw e;
            }
        } else {
            if (items.size() == 0) log.debug("Cart is empty. Skipping purchase submission.");
            else log.debug("Transaction is already active. Skipping purchase submission.");
        }
        items.clear();

    }

    private void makePurchase() {
        double total = 0.0;
        for (SoldItem item : items) {
            StockItem stockItem = dao.findStockItem(item.getBarcode());

            stockItem.setQuantity(stockItem.getQuantity() - item.getQuantity());
            total += item.getPrice() * item.getQuantity();

        }
        dao.savePurchaseforhistory(new Purchase(items));
        log.debug("Saved items: {}",items);
        dao.commitTransaction();
        items.clear();
    }


    public List<SoldItem> getShoppingCart() {
        return items;
    }

    public List<StockItem> findStockItems() {
        return dao.findStockItems();
    }

    public StockItem findStockItem(long code) {
        return dao.findStockItem(code);
    }
}
