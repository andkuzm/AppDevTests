package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.NoResultException;
import java.util.List;

public class Warehouse {
    private static final Logger log = LogManager.getLogger(Warehouse.class);

    private final SalesSystemDAO dao;

    public Warehouse(SalesSystemDAO dao) {
        this.dao = dao;
    }

    public List<StockItem> getAllStockItems() {
        log.debug("Retrieving all stock items from the database.");
        return dao.findStockItems();
    }

    public void addStockItemToDAO(StockItem newItem) {
        log.debug("Adding a new stock item to the database: " + newItem.getName());
        try {
            dao.beginTransaction();
            dao.saveStockItem(newItem);
            dao.commitTransaction();
            log.debug("Added a new stock item: " + newItem.getName());
        } catch (Exception e) {
            dao.rollbackTransaction();
            log.error("Error occurred while adding item to warehouse: {}", e.getMessage());
            throw e;
        }
    }

    public StockItem findStockItem(long id) {
        log.debug("Finding stock item by ID: " + id);
        return dao.findStockItem(id);
    }

    public void addItemToWarehouse(StockItem stockItem, int quantity) {
        if (quantity < 0) {
            throw new SalesSystemException("Negative quantity is not allowed");
        }
        try {
            handleAddingItemToWarehouse(stockItem, quantity);
        } catch (Exception e) {
            dao.rollbackTransaction();
            log.error("Error occurred while adding item to warehouse: {}", e.getMessage());
            throw e;
        }
    }

    private void handleAddingItemToWarehouse(StockItem stockItem, int quantity) {
        dao.beginTransaction();
        StockItem existingItem = getStockItemByBarcode(stockItem.getId());
        if (existingItem != null) {
            handleItemComparison(stockItem, quantity, existingItem);
        }
    }

    private void handleItemComparison(StockItem stockItem, int quantity, StockItem existingItem) {
        //Item values match the existing item, updates current item.
        if (existingItem.getName().equals(stockItem.getName()) && existingItem.getPrice() == stockItem.getPrice()) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            dao.commitTransaction();
            log.debug("Updating existing stock item: " + existingItem.getName());
        } else { //Creates a new item.
            if (!dao.contains(stockItem)) {
                stockItem = dao.merge(stockItem);
            }
            dao.saveStockItem(stockItem);
            dao.commitTransaction();
            log.debug("Adding a new stock item: " + stockItem.getName());
        }
    }


    public StockItem getStockItemByBarcode(long barcode) {
        log.debug("Finding stock item by barcode: " + barcode);
        try{
        return dao.findStockItem(barcode);
        }
        catch (NoResultException a) {
            return null;
        }
    }

    public long generateNewStockItemId() {
        List<StockItem> stockItems = getAllStockItems();
        long maxId = stockItems.stream().mapToLong(StockItem::getId).max().orElse(0);
        log.debug("Generating a new stock item ID: " + (maxId + 1));
        return maxId + 1;
    }
}
