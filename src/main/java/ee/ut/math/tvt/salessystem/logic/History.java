package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.HibernateSalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class History {
    private List<Purchase> history;
    private final SalesSystemDAO dao;

    public History(SalesSystemDAO dao) {
        this.dao = dao;
    }


    public List<Purchase> getHistory() {
        this.history = dao.getHistory();
        return history;
    }

    public List<Purchase> getLastTenTransactions() {
        return dao.getLastTenTransactions();
    }

    /*
    public List<Purchase> getBetweenDates(LocalDate start, LocalDate end) {
        this.history = dao.getHistory(); //Todo asendada getbetweendates dao meetodiga.
        List<Purchase> ret = new ArrayList<>();
        try {
            for (int i = 0; i < history.size(); i++) {
                LocalDate date = history.get(i).getDate();
                if (start.isBefore(date) && end.isAfter(date) || start.equals(date) || end.equals(date))
                    ret.add(history.get(i));
            }
        } catch (RuntimeException e) {
            throw new SalesSystemException("wrong date");
        }
        return ret;
    }

     */

    public List<Purchase> getBetweenDates(LocalDate start, LocalDate end) {
        return dao.getBetweenDates(start,end);


    }
}