package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Encapsulates everything that has to do with the purchase tab (the tab
 * labelled "Point-of-sale" in the menu). Consists of the purchase menu,
 * current purchase dialog and shopping cart table.
 */
public class PurchaseController implements Initializable {

    private static final Logger log = LogManager.getLogger(PurchaseController.class);
    private final ShoppingCart shoppingCart;

    @FXML
    private Button newPurchase;
    @FXML
    private Button submitPurchase;
    @FXML
    private Button cancelPurchase;
    @FXML
    private TextField barCodeField;
    @FXML
    private TextField quantityField;
    @FXML
    private ComboBox nameField;
    @FXML
    private TextField priceField;
    @FXML
    private Button addItemButton;
    @FXML
    private TableView<SoldItem> purchaseTableView;
    @FXML
    private TextField totalTextField;

    private Alert alert;
    private Purchase curTrans;

    public PurchaseController(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        this.alert = new Alert(Alert.AlertType.ERROR);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelPurchase.setDisable(true);
        submitPurchase.setDisable(true);
        purchaseTableView.setItems(FXCollections.observableList(shoppingCart.getAll()));
        disableProductField(true);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");


        nameField.setItems(FXCollections.observableList(shoppingCart.findStockItems().stream().map(i -> i.getName()).toList()));
        TableColumn<SoldItem, Double> sumColumn = new TableColumn<>("Sum");
        sumColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getPrice() * cellData.getValue().getQuantity()).asObject());
        purchaseTableView.getColumns().add(sumColumn);
        this.barCodeField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    fillInputsBySelectedStockItemBC();
                }
            }
        });

        this.nameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue) {
                    fillInputsBySelectedStockItemNF();
                }
            }
        });
    }

    private void fillInputsBySelectedStockItemNF() {
        String current = (String) nameField.getSelectionModel().getSelectedItem();
        if (current != null) {
            StockItem item = null;
            List<StockItem> list = shoppingCart.findStockItems();
            for (int i = 0; i < list.size(); i++) {
                if (current.equals(list.get(i).getName())) {
                    item = list.get(i);
                    break;
                }
            }
            barCodeField.setText(String.valueOf(item.getId()));
            priceField.setText(String.valueOf(item.getPrice()));
        } else {
            resetProductField();
        }
    }

    private StockItem getStockItemByName(String name) {
        try {
            for (StockItem item : shoppingCart.findStockItems()) {
                if (item.getName().equals(name))
                    return item;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    public void updateTotal() {
        double totalSum = shoppingCart.getTotal();
        totalTextField.setText("Total: "+String.format("%.2f", totalSum)); // Display the total sum in the TextField
    }

    /**
     * Event handler for the <code>new purchase</code> event.
     */
    @FXML
    protected void newPurchaseButtonClicked() {
        log.info("New sale process started");
        try {
            //curTrans = new Purchase(LocalDate.now(), LocalTime.now(), 0, null);
            if(nameField.getItems().size()>0) {
                nameField.setItems(null);
                nameField.setValue(null);
            }
            nameField.setItems(FXCollections.observableList(shoppingCart.findStockItems().stream().map(i -> i.getName().toString()).toList()));
            updateTotal();
            enableInputs();
        } catch (SalesSystemException e) {
            showAlert(e);
        }
    }

    /**
     * Event handler for the <code>cancel purchase</code> event.
     */
    @FXML
    protected void cancelPurchaseButtonClicked() {
        log.info("Sale cancelled");
        try {
            shoppingCart.cancelCurrentPurchase();
            disableInputs();
            purchaseTableView.refresh();
            updateTotal();
        } catch (SalesSystemException e) {
            showAlert(e);
        }
    }
    @FXML
    protected void nameMenuClicked() {
//        if(nameField.getItems().size()>0) {
//            //nameField.getItems().clear();
//            nameField.setValue(null);
//        }
        nameField.setItems(FXCollections.observableList(shoppingCart.findStockItems().stream().map(i -> i.getName().toString()).toList()));
    }

    /**
     * Event handler for the <code>submit purchase</code> event.
     */
    @FXML
    protected void submitPurchaseButtonClicked() {
        log.info("Sale complete");
        try {
            log.debug("Contents of the current basket:\n" + shoppingCart.getAll());
            shoppingCart.submitCurrentPurchase();
            disableInputs();
            purchaseTableView.getItems().clear();
            purchaseTableView.refresh();
            updateTotal();
        } catch (SalesSystemException e) {
            showAlert(e);
        }
    }

    private void showAlert(SalesSystemException e) {
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        log.error(e.getMessage(), e);
    }

    // switch UI to the state that allows to proceed with the purchase
    private void enableInputs() {
        resetProductField();
        disableProductField(false);
        cancelPurchase.setDisable(false);
        submitPurchase.setDisable(false);
        nameField.setDisable(false);
        newPurchase.setDisable(true);
    }

    // switch UI to the state that allows to initiate new purchase
    private void disableInputs() {
        resetProductField();
        cancelPurchase.setDisable(true);
        submitPurchase.setDisable(true);
        newPurchase.setDisable(false);
        nameField.setDisable(true);
        disableProductField(true);
    }

    private void fillInputsBySelectedStockItemBC() {
        StockItem stockItem = getStockItemByBarcode();
        if (stockItem != null) {
            nameField.setPromptText(stockItem.getName());
            nameField.setDisable(true);
            priceField.setText(String.valueOf(stockItem.getPrice()));
        } else {
            resetProductField();
        }
    }

    // Search the warehouse for a StockItem with the bar code entered
    // to the barCode textfield.
    private StockItem getStockItemByBarcode() {
        try {
            long code = Long.parseLong(barCodeField.getText());
            return shoppingCart.findStockItem(code);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Add new item to the cart.
     */
    @FXML
    public void addItemEventHandler() {
        // add chosen item to the shopping cart.
        StockItem stockItem = getStockItemByBarcode();
        if (stockItem != null) {
            int quantity;
            quantity = getQuantity();
            try {
                shoppingCart.addItem(new SoldItem(stockItem.getId(), stockItem.getName(), stockItem.getPrice(), quantity));
            } catch (SalesSystemException e) {
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            purchaseTableView.refresh();
            updateTotal();
        }
    }

    private int getQuantity() {
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            quantity = 1;
        }
        return quantity;
    }

    /**
     * Sets whether or not the product component is enabled.
     */
    private void disableProductField(boolean disable) {
        this.addItemButton.setDisable(disable);
        this.barCodeField.setDisable(disable);
        this.quantityField.setDisable(disable);
        this.nameField.setDisable(disable);
        this.priceField.setDisable(disable);
    }

    /**
     * Reset dialog fields.
     */
    private void resetProductField() {
        barCodeField.setText("");
        quantityField.setText("1");
        nameField.setPromptText("");
        nameField.setDisable(false);
        priceField.setText("");
    }
}
