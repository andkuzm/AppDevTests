package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.Warehouse;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private static final Logger log = LogManager.getLogger(StockController.class);

    private final Warehouse warehouse;

    @FXML
    private TextField barCodeField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private Button addItem;
    @FXML
    private Button refreshWarehouse;
    @FXML
    private TableView<StockItem> warehouseTableView;
    private Alert alert;

    public StockController(SalesSystemDAO dao) {
        this.warehouse = new Warehouse(dao);
        this.alert = new Alert(Alert.AlertType.ERROR);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshStockItems();

        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");

        this.barCodeField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue) {
                fillInputsBySelectedStockItem();
            }
        });
    }

    private void fillInputsBySelectedStockItem() {
        long code = Long.parseLong(barCodeField.getText());
        StockItem stockItem = warehouse.getStockItemByBarcode(code);

        if (stockItem != null) {
            nameField.setText(stockItem.getName());
            priceField.setText(String.valueOf(stockItem.getPrice()));
        }
    }

    @FXML
    protected void refreshButtonClicked() {
        log.info("'Refresh Warehouse' button clicked.");
        refreshStockItems();
    }

    private void refreshStockItems() {
        warehouseTableView.setItems(FXCollections.observableList(warehouse.getAllStockItems()));
        warehouseTableView.refresh();
        log.info("Warehouse refreshed.");
    }

    private void resetProductField() {
        barCodeField.setText("");
        quantityField.setText("1");
        nameField.setText("");
        priceField.setText("");
    }

    @FXML
    public void addItemEventHandler() {
        try {
            long barcode = barcodeCheck();
            int quantity = quantityCheck();
            double price = priceCheck();

            StockItem stockItem = warehouse.getStockItemByBarcode(barcode);

            if (stockItem != null) {
                handleExistingStockItem(stockItem, price, quantity);
            } else {
                handleNewStockItem(barcode, price, quantity);
            }

            refreshStockItems();
        } catch (SalesSystemException e) {
            salesSystemError(e);
        }
    }

    private void handleExistingStockItem(StockItem stockItem, double price, int quantity) {
        if (!stockItem.getName().equals(nameField.getText())) {
            handleNameMismatch(quantity);
        } else if (stockItem.getPrice() != price || priceField.getText().isEmpty()) {
            handlePriceMismatch();
        } else {
            updateExistingStockItem(stockItem, quantity);
        }
    }

    private void updateExistingStockItem(StockItem stockItem,int quantity) {
        warehouse.addItemToWarehouse(stockItem,quantity);
    }


    private void handleNewStockItem(long barcode, double price, int quantity) {
        StockItem newItem;
        if (warehouse.getStockItemByBarcode(barcode) == null) {
            newItem = new StockItem(barcode, nameField.getText(), "", price, quantity);
        } else {
            long newId = warehouse.generateNewStockItemId();
            newItem = new StockItem(newId, nameField.getText(), "", price, quantity);
        }
        log.debug("Added a new stock item with ID: {}", barcode);
        warehouse.addStockItemToDAO(newItem);
        barCodeField.setText(Long.toString(newItem.getId()));
    }


    private void handlePriceMismatch() {
        log.debug("An user input error occurred: Invalid price.");
        alert.setContentText("Price doesn't match the price in the warehouse.");
        alert.showAndWait();
        log.error("Price doesn't match the price in the database");
    }

    private void handleNameMismatch(int quantity) {
        long newId = warehouse.generateNewStockItemId();
        StockItem newItem = new StockItem(newId, nameField.getText(), "", Integer.parseInt(priceField.getText()), quantity);
        warehouse.addStockItemToDAO(newItem);
        barCodeField.setText(Long.toString(newId));
        log.debug("Added a new stock item with ID: {}", newId);
    }

    private double priceCheck() throws SalesSystemException {
        if (priceField.getText().isEmpty()) {
            handleMissingPrice();
        }
        try {
            double price = Double.parseDouble(priceField.getText());
            checkIfNegativePrice(price);
            return price;
        } catch (NumberFormatException e) {
            numberError(e, "price");
            throw new SalesSystemException("Invalid price format.");
        }
    }

    private static void checkIfNegativePrice(double price) {
        if (price < 0) {
            log.debug("An user input error occurred: Negative price");
            throw new SalesSystemException("Negative price entered.");
        }
    }

    private void handleMissingPrice() {
        alert.setContentText("No price entered");
        alert.showAndWait();
        throw new SalesSystemException("No price entered.");
    }


    private int quantityCheck() throws SalesSystemException{
        int quantity;
        try {
            checkQuantityField();
            quantity = Integer.parseInt(quantityField.getText());
            checkIfNegativeQuantity(quantity);
            return quantity;
        } catch (NumberFormatException e) {
            numberError(e, "quantity");
            throw new SalesSystemException();
        } catch (SalesSystemException e) {
            salesSystemError(e);
            throw new SalesSystemException();
        }
    }

    private void checkIfNegativeQuantity(int quantity) {
        if (quantity < 0) {
            numberError(new NumberFormatException("Negative quantity entered"), "quantity");
        }
    }

    private void checkQuantityField() {
        if (quantityField.getText().equals("")){
            numberError(new NumberFormatException("No quantity entered"), "quantity");
        }
    }

    private long barcodeCheck() throws SalesSystemException {
        try {
            return getBarcodeValue();
        } catch (NumberFormatException e) {
            numberError(e, "barcode");
            throw new SalesSystemException();
        }
    }

    private long getBarcodeValue() {
        long barcode;
        if (barCodeField.getText().equals("")) {
            barcode = warehouse.generateNewStockItemId();
        } else {
            barcode = Long.parseLong(barCodeField.getText());
        }
        return barcode;
    }

    private void salesSystemError(SalesSystemException e) {
        log.error(e.getMessage(), e);
    }

    private void numberError(NumberFormatException e, String error) {
        log.debug("An user input error occurred: Invalid {}.", error);
        alert.setContentText("Invalid "+error+" entered.");
        alert.showAndWait();
        log.error("Invalid " + error +" entered: " + e.getMessage());
    }


}
