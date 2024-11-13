package ee.ut.math.tvt.salessystem;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.logic.Warehouse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class TestWarehouse {
    @Mock
    SalesSystemDAO dao;

    @InjectMocks // creates warehouse with mocked dao injected into it
    Warehouse warehouse;

    @Captor
    ArgumentCaptor<StockItem> stockItemCaptor;

    @Test
    public void testAddingItemBeginsAndCommitsTransaction() {
        StockItem mockItem = new StockItem(1L, "Banaan", "Puuvili", 1.45, 2);
        Mockito.when(dao.findStockItem(Mockito.anyLong())).thenReturn(mockItem);
        warehouse.addStockItemToDAO(mockItem);
        Mockito.verify(dao, Mockito.times(1)).beginTransaction();
        Mockito.verify(dao, Mockito.times(1)).commitTransaction();
    }
    @Test
    public void testAddingNewItem() {

        StockItem mockItem = new StockItem(1001L, "Banaan", "Puuvili", 1.45, 2);

        Mockito.when(dao.findStockItem(Mockito.anyLong())).thenReturn(mockItem);
        warehouse.addStockItemToDAO(mockItem);

        Mockito.verify(dao, Mockito.times(1)).saveStockItem(mockItem);
    }
    @Test
    public void testAddingExistingItem() {

        StockItem realMockItem = new StockItem(2l, "Chupa-chups", "Sweets", 8.0, 8);

        int quantity = realMockItem.getQuantity();
        Mockito.when(dao.findStockItem(Mockito.anyLong())).thenReturn(realMockItem);
        warehouse.addStockItemToDAO(realMockItem);
        int expectedQuantity = quantity + 8;
        //Assert.assertEquals(expectedQuantity, stockItemCaptor.getValue().getQuantity());
        Mockito.verify(dao, Mockito.never()).saveStockItem(Mockito.any(StockItem.class));
    }
    @Test
    public void testAddingItemWithNegativeQuantity() {

    StockItem negQuantityItem = new StockItem(3L, "-Item", "Should not be allowed", 10.0, -10);

    assertThrows(SalesSystemException.class, () -> warehouse.addStockItemToDAO(negQuantityItem));
    }

    @Test
    public void testviewinghistory(){
        List<Purchase> abi = dao.getLastTenTransactions();
        for (Purchase elem : abi)
            System.out.println(abi);
    }

}
