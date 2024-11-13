package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.logic.History;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Encapsulates everything that has to do with the purchase tab (the tab
 * labelled "History" in the menu).
 */
public class HistoryController implements Initializable {

    private static final Logger log = LogManager.getLogger(HistoryController.class);

    private final History history;
    private Alert alert;
    List<Purchase> purchases; //TODO

    @FXML
    private Button showBetweenDates;
    @FXML
    private Button lastTen;
    @FXML
    private Button allHistory;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TableView<Purchase> historyOverview;
    @FXML
    private TableView<SoldItem> historyPrecise;

    public HistoryController(SalesSystemDAO dao) {
        this.history = new History(dao);
        this.purchases = this.history.getHistory();
        this.alert = new Alert(Alert.AlertType.ERROR);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
    }
    @FXML
    protected void SBTButtonClicked() {
        try {
            if(startDate.getValue()==null || endDate.getValue() == null) throw new SalesSystemException("Wrong date");
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();
            List<Purchase> temporal = history.getBetweenDates(start, end);
            historyOverview.setItems(FXCollections.observableList(temporal));
            log.info("Displaying transactions between dates: " + start + " and " + end);
        } catch (RuntimeException e) {
            alert.setContentText("wrong date");
            alert.showAndWait();
        }
    }

    @FXML
    protected void lastTenButtonClicked() {
        this.purchases = this.history.getHistory();
        List<Purchase> temporal = new ArrayList<>();
        for (int i = 0; i < purchases.size() && i < 10; i++) {
            LocalDate  date = purchases.get(i).getDate();
            temporal.add(purchases.get(i));
        }
        historyOverview.setItems(FXCollections.observableList(temporal));// TODO : get ten items from history dao method
        log.info("Displaying the last ten transactions.");
    }
    @FXML
    protected void allButtonClicked() {
        this.purchases = this.history.getHistory();
        historyOverview.setItems(FXCollections.observableList(purchases));
        log.info("Displaying all transactions.");
    }
    @FXML
    protected void transClicked() {
        Purchase temporal = historyOverview.getSelectionModel().getSelectedItem();
        historyPrecise.setItems(FXCollections.observableList(temporal.getItems()));
        log.debug("Displaying detailed information for a selected transaction.");//TODO debugs vs info
    }
}
