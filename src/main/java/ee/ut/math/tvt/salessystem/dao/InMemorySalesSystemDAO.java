package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InMemorySalesSystemDAO implements SalesSystemDAO {

    private final List<StockItem> stockItemList;
    private final List<SoldItem> soldItemList;
    private List<SoldItem> forTrans;
    private List<Purchase> history;
    private double total;
    private boolean transactionActive;

    public InMemorySalesSystemDAO() {
        List<StockItem> items = new ArrayList<StockItem>();
        this.stockItemList = items;
        this.soldItemList = new ArrayList<>();
        this.history= new ArrayList<>();
    }

    public List<Purchase> getHistory() {
        return history;
    }

    @Override
    public List<Purchase> getLastTenTransactions() {
        return null;
    }

    @Override
    public List<Purchase> getBetweenDates(LocalDate startDate, LocalDate endDate) {
        return null;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public List<StockItem> findStockItems() {
        return stockItemList;
    }

    @Override
    public StockItem findStockItem(long id) {
        for (StockItem item : stockItemList) {
            if (item.getId() == id)
                return item;
        }
        return null;
    }



    public List<SoldItem> getShoppingCartItems() {
        return soldItemList;
    }
    @Override
    public void saveSoldItem(SoldItem item) {
        soldItemList.add(item);
        StockItem chenge = findStockItem(item.getBarcode());
        int index=-1;
        for (int i = 0; i < stockItemList.size(); i++) {
            if(chenge.getId()==stockItemList.get(i).getId()) {
                index = i;
                break;
            }
            if(i == stockItemList.size()-1) throw new SalesSystemException("unexpected saveSoldItem from dao error");
        }
        chenge.setQuantity(chenge.getQuantity()-item.getQuantity());
        if (chenge.getQuantity()<0) throw new SalesSystemException("quantity exceeds acceptable");
        stockItemList.set(index, chenge);
        forTrans.add(item);
    }

    public void saveStockItem(StockItem stockItem) {
        stockItemList.add(stockItem);
    }

    @Override
    public void beginTransaction() {
        forTrans = new ArrayList<>();
        total = 0;

    }

    @Override
    public void rollbackTransaction() {
        forTrans = new ArrayList<>();
        total = 0;
    }

    @Override
    public void commitTransaction() {
        history.add(0, new Purchase(LocalDate.now(), LocalTime.now(), total, forTrans));
    }

    public boolean isTransactionActive() {
        return transactionActive;
    }

    @Override
    public boolean contains(Object entity) {
        return findStockItems().contains(entity);
    }

    @Override
    public <T> T merge(T entity) {
        return null;
    }

    @Override
    public void savePurchaseforhistory(Purchase purchase) {
        forTrans.add(purchase.getItems().get(0));
    }


    public void saveTransactionforhistory(Purchase trans){
        forTrans.add(trans.getItems().get(0));
    }


public Purchase getTransactionbyID(long id){return null;};


    }
