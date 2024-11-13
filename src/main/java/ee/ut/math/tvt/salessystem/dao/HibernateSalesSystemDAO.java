package ee.ut.math.tvt.salessystem.dao;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.cache.internal.DefaultCacheKeysFactory.getEntityId;

public class HibernateSalesSystemDAO implements SalesSystemDAO {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public HibernateSalesSystemDAO() {
        // if you get ConnectException / JDBCConnectionException then you
        // probably forgot to start the database before starting the application
        emf = Persistence.createEntityManagerFactory ("pos");
        em = emf.createEntityManager ();
       /* beginTransaction();
        em.persist(new StockItem(1L, "Lays chips", "Potato chips", 11.0, 5));
        commitTransaction();

        */
    }
    public void close() {
        em.close();
        emf.close();
    }

    @Override
    public List<StockItem> findStockItems() {
        return em.createQuery("select s from StockItem s", StockItem.class).getResultList();
    }


    @Override
    public StockItem findStockItem(long id) {
            return em.createQuery("FROM StockItem si WHERE si.id = :itemId", StockItem.class).setParameter("itemId", id).getSingleResult();
    }

    @Override
    public void saveStockItem(StockItem stockItem) {
        em.persist(stockItem);
    }



    @Override
    public void saveSoldItem (SoldItem item){
        em.persist(em.merge(item));
    }

    @Override
    public void beginTransaction () {
        em.getTransaction (). begin ();
    }
    @Override
    public void rollbackTransaction () {
        em.getTransaction (). rollback ();
    }
    @Override
    public void commitTransaction () {
        em.getTransaction (). commit ();
    }

    @Override
    public List<Purchase> getHistory() {
            return em.createQuery("select s from Purchase s", Purchase.class).getResultList();
    }

    @Override
    public <T> T merge(T entity) {
        return em.merge(entity);
    }



    @Override
    public boolean contains(Object entity) {
        return em.contains(entity) || (em.find(entity.getClass(), getEntityId(entity)) != null);
    }


    public boolean isTransactionActive() {
        return em.getTransaction().isActive();
    }


    @Override
    public List<Purchase> getLastTenTransactions() {
        return em.createQuery("select t from Purchase t order by t.date desc", Purchase.class)
                .setMaxResults(10)
                .getResultList();
    }
    @Override
    public List<Purchase> getBetweenDates(LocalDate startDate, LocalDate endDate) {
        return em.createQuery("select t from Purchase t where t.date between :startDate and :endDate", Purchase.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    @Override
    public void savePurchaseforhistory(Purchase purchase){
        em.persist(purchase);
    }
    @Override
    public Purchase getTransactionbyID(long id){
       return em.createQuery("select p from Purchase p where p.id = :itemid", Purchase.class).setParameter("itemid", id).getSingleResult();
       }
    }


