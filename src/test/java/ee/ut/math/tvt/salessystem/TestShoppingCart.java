package ee.ut.math.tvt.salessystem;

import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestShoppingCart {

    @Spy
    SalesSystemDAO dao = new InMemorySalesSystemDAO();

    @InjectMocks
    ShoppingCart sc;

    @Captor
    ArgumentCaptor<StockItem> stockItemCaptor;

    /**
     * testAddingNewItem is covered here, as to test whether adding the same item
     * increases its quantity a new item was added first, and from assertions it is possible to confirm that the new item was added
     */

    @Test
    public void testAddingExistingItem() {
        StockItem mockTItem = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 1);
        dao.saveStockItem(new StockItem(1L, "Lays chips", "Potato chips", 11.0, 2));
        SoldItem mockOItem = new SoldItem(mockTItem.getId(),  mockTItem.getName(),  mockTItem.getPrice(), mockTItem.getQuantity());
        sc.addItem(mockOItem);
        assertEquals(1, sc.getAll().size(), 0.001);
        assertEquals(1, sc.getAll().get(0).getQuantity(), 0.001);
        sc.addItem(mockOItem);
        assertEquals(1, sc.getAll().size(), 0.001);
        assertEquals(2, sc.getAll().get(0).getQuantity(), 0.001);
        assertEquals(22, sc.getAll().get(0).getSum(), 0.001);
    }

    @Test
    public void testAddingItemWithNegativeQuantity() {
        StockItem mockTItem = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 1);
        dao.saveStockItem(mockTItem);
        SoldItem mockOItem = new SoldItem(mockTItem.getId(),  mockTItem.getName(),  mockTItem.getPrice(), -1);
        assertThrows(SalesSystemException.class, () -> sc.addItem(mockOItem));
    }

    @Test
    public void testAddingItemWithQuantityTooLarge() {
        StockItem mockTItem = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 1);
        dao.saveStockItem(mockTItem);
        SoldItem mockOItem = new SoldItem(mockTItem.getId(),  mockTItem.getName(),  mockTItem.getPrice(), 2);
        assertThrows(SalesSystemException.class, () -> sc.addItem(mockOItem));
    }

    @Test
    public void testAddingItemWithQuantitySumTooLarge() {
        StockItem mockTItem = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 3);
        dao.saveStockItem(mockTItem);
        SoldItem mockOItem = new SoldItem(mockTItem.getId(),  mockTItem.getName(),  mockTItem.getPrice(), 2);
        sc.addItem(mockOItem);
        assertThrows(SalesSystemException.class, () -> sc.addItem(mockOItem));
    }

    @Test
    public void testSubmittingCurrentTransactionBeginsAndCommitsTransaction() {
        StockItem mockTItem = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 3);
        dao.saveStockItem(mockTItem);
        StockItem mockTItemt = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 2);
        SoldItem mockOItem = new SoldItem(mockTItemt.getId(),  mockTItemt.getName(),  mockTItemt.getPrice(), 2);
        sc.addItem(mockOItem);
        sc.submitCurrentPurchase();
        InOrder inOrder = inOrder(dao, dao);
        inOrder.verify(dao, times(1)).beginTransaction();
        inOrder.verify(dao, times(1)).commitTransaction();
    }
    @Test
    public void testSubmittingCurrentPurchaseDecreasesStockItemQuantity() {
        sc = new ShoppingCart(dao);
        StockItem mockTItem = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 3);
        dao.saveStockItem(mockTItem);
        StockItem mockTItemt = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 2);
        SoldItem mockOItem = new SoldItem(mockTItemt.getId(),  mockTItemt.getName(),  mockTItemt.getPrice(), 2);
        assertEquals(3, dao.findStockItem(1L).getQuantity(), 0.001);
        sc.addItem(mockOItem);
        sc.submitCurrentPurchase();
        verify(dao).saveStockItem(stockItemCaptor.capture());
        StockItem capturedStockItem = stockItemCaptor.getValue();
        assertEquals(1, capturedStockItem.getId().longValue());
        assertEquals("Lays chips", capturedStockItem.getName());
        assertEquals("Potato chips", capturedStockItem.getDescription());
        assertEquals(1, capturedStockItem.getQuantity());
    }

    @Test
    public void testSubmittingCurrentOrderCreatesHistoryItem() {
        StockItem mockTItem = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 1);
        dao.saveStockItem(mockTItem);
        StockItem mockTItemt = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 1);
        SoldItem mockOItem = new SoldItem(mockTItemt.getId(),  mockTItemt.getName(),  mockTItemt.getPrice(), 1);
        sc.addItem(mockOItem);
        sc.submitCurrentPurchase();
        assertEquals(mockOItem, dao.getHistory().get(0).getItems().get(0));
    }

    @Test
    public void testSubmittingCurrentOrderSavesCorrectTime() {
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("hh:mm:ss");
        StockItem mockTItem = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 1);
        dao.saveStockItem(mockTItem);
        StockItem mockTItemt = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 1);
        SoldItem mockOItem = new SoldItem(mockTItemt.getId(),  mockTItemt.getName(),  mockTItemt.getPrice(), 1);
        sc.addItem(mockOItem);
        sc.submitCurrentPurchase();
        assertEquals(LocalDate.now(), dao.getHistory().get(0).getDate());
        assertEquals(LocalTime.parse(LocalTime.now().format(tf)).getHour(), dao.getHistory().get(0).getTime().getHour());
        assertEquals(LocalTime.parse(LocalTime.now().format(tf)).getMinute(), dao.getHistory().get(0).getTime().getMinute());
        assertEquals(LocalTime.parse(LocalTime.now().format(tf)).getSecond(), dao.getHistory().get(0).getTime().getSecond(), 5);
    }

    @Test
    public void testCancellingOrder() {
        StockItem mockTItemFirst = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 1);
        dao.saveStockItem(mockTItemFirst);
        StockItem mockTItemt = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 1);
        SoldItem mockOItemFirst = new SoldItem(mockTItemt.getId(),  mockTItemt.getName(),  mockTItemt.getPrice(), 1);
        sc.addItem(mockOItemFirst);
        sc.cancelCurrentPurchase();
        assertEquals(0, sc.getShoppingCart().size());
        StockItem mockTItemSec = new StockItem(2L, "name", "placeHolder", 1.0, 1);
        dao.saveStockItem(mockTItemSec);
        SoldItem mockOItemSec = new SoldItem(mockTItemSec.getId(),  mockTItemSec.getName(),  mockTItemSec.getPrice(), 1);
        sc.addItem(mockOItemSec);
        assertEquals(1, sc.getShoppingCart().size());
        assertEquals(mockOItemSec, sc.getShoppingCart().get(0));
    }

    @Test
    public void testCancellingOrderQuanititesUnchanged() {
        StockItem mockTItemFirst = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 1);
        dao.saveStockItem(mockTItemFirst);
        SoldItem mockOItemFirst = new SoldItem(mockTItemFirst.getId(),  mockTItemFirst.getName(),  mockTItemFirst.getPrice(), 1);
        sc.addItem(mockOItemFirst);
        sc.cancelCurrentPurchase();
        assertEquals(1, dao.findStockItem(1L).getQuantity());
    }
}
