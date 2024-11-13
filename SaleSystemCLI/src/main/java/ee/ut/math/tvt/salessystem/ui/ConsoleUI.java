package ee.ut.math.tvt.salessystem.ui;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.HibernateSalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import ee.ut.math.tvt.salessystem.logic.History;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import ee.ut.math.tvt.salessystem.logic.TeamInfo;
import ee.ut.math.tvt.salessystem.logic.Warehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * A simple CLI (limited functionality).
 */
public class ConsoleUI {
    private static final Logger log = LogManager.getLogger(ConsoleUI.class);
    private final ShoppingCart cart;
    private final String teamName;
    private final String teamLeader;
    private final String leaderEmail;
    //private final TextArea teamMembers=new TextArea();
    private final String teamMembers;
    private List<Purchase> history;
    private History histori;
    private Warehouse warehouse;
    private Purchase curTrans;


    public ConsoleUI(SalesSystemDAO dao, TeamInfo teamInfo) {
        this.cart = new ShoppingCart(dao);
        this.warehouse = new Warehouse(dao);
        this.histori = new History(dao);
        this.history = histori.getHistory();
        Properties properties = new Properties();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/application.properties");
            properties.load(inputStream);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.teamName = properties.getProperty("name");
        this.teamLeader = properties.getProperty("leader");
        this.leaderEmail = properties.getProperty("mail");
        //this.teamMembers.setText(properties.getProperty("members"));
        this.teamMembers = properties.getProperty("members");


    }

    public static void main(String[] args) throws Exception {
        SalesSystemDAO dao = new HibernateSalesSystemDAO();
        TeamInfo teamInfo = new TeamInfo();
        ConsoleUI console = new ConsoleUI(dao, teamInfo);
        console.run();
    }

    /**
     * Run the sales system CLI.
     */
    public void run() throws IOException {
        System.out.println("===========================");
        System.out.println("=       Sales System      =");
        System.out.println("===========================");
        printUsage();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("> ");
            processCommand(in.readLine().trim().toLowerCase());
            System.out.println("Done. ");
        }
    }

    private void showStock() {
        List<StockItem> stockItems = cart.findStockItems();
        System.out.println("-------------------------");
        for (StockItem si : stockItems) {
            System.out.println(si.getId() + " " + si.getName() + " " + si.getPrice() + "Euro (" + si.getQuantity() + " items)");
        }
        if (stockItems.size() == 0) {
            System.out.println("\tNothing");
        }
        System.out.println("-------------------------");
    }

    private void showCart() {
        System.out.println("-------------------------");
        double total = 0.0;
        for (SoldItem si : cart.getAll()) {
            System.out.println(si.getName() + " " + si.getPrice() + "Euro (" + si.getQuantity() + " items)");
        }
        if (cart.getAll().size() == 0) {
            System.out.println("\tNothing");
        }
        System.out.println("Total sum: " + cart.getTotal());
        System.out.println("-------------------------");
    }

    private void printUsage() {
        System.out.println("-------------------------");
        System.out.println("Usage:");
        System.out.println("h\t\t\tShow this help");
        System.out.println("w\t\t\tShow warehouse contents");
        System.out.println("c\t\t\tShow cart contents");
        System.out.println("a IDX NR \tAdd NR of stock item with index IDX to the cart");
        System.out.println("p\t\t\tPurchase the shopping cart");
        System.out.println("aw IDX NR \tIncrease item with index IDX by NR in the warehouse");
        System.out.println("aw IDX NR NAME PRC (DESC)\tAdd NR amount of item with index IDX, name NAME, price PRC (and description DESC) to warehouse");
        System.out.println("all\t\t\tShow the whole History");
        System.out.println("ten\t\t\tShow the last 10 purchases");
        System.out.println("between START END\t\tShow the purchases between START and END date (yyyy-mm-dd)");
        System.out.println("r\t\t\tReset the shopping cart");
        System.out.println("t\t\t\tPrint team info");
        System.out.println("-------------------------");
    }

    private void processCommand(String command) {
        String[] c = command.split(" ");

        if (c[0].equals("h"))
            printUsage();
        else if (c[0].equals("q"))
            System.exit(0);
        else if (c[0].equals("all"))
            for (int i = 0; i < history.size(); i++) {
                System.out.println(history.get(i).toString());
            }
        else if (c[0].equals("ten")) {
            for (int i = 0; i < history.size() && i < 10; i++) {
                System.out.println(history.get(i).toString());
            }
        } else if (c[0].equals("between")) {
            showBetweenDates(c);
        } else if (c[0].equals("w"))
            showStock();
        else if (c[0].equals("c"))
            showCart();
        else if (c[0].equals("p"))
            cart.submitCurrentPurchase();
        else if (c[0].equals("r"))
            cart.cancelCurrentPurchase();
        else if (c[0].equals("t"))
            teamInfo();
        else if (c[0].equals("a") && c.length == 3) {
            try {
                addToCart(c); // TODO: verify
            } catch (SalesSystemException | NoSuchElementException e) {
                log.error(e.getMessage(), e);
            }
        } else if (c[0].equals("aw")) {
            addToWarehouse(c);
        } else {
            System.out.println("unknown command");
        }
    }

    private void showBetweenDates(String[] c) {
        try {
            LocalDate start = LocalDate.parse(c[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate end = LocalDate.parse(c[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<Purchase> temp = histori.getBetweenDates(start, end);
            for (int i = 0; i < temp.size(); i++) {
                System.out.println(temp.get(i).toString());
            }
        } catch (RuntimeException e ) {
            System.out.println("Wrong date input");
        }
    }

    private void addToCart(String[] c) {
        long idx = Long.parseLong(c[1]);
        int amount = Integer.parseInt(c[2]);
        StockItem item = cart.findStockItem(idx);
        if (item != null) {
            if (amount > item.getQuantity())
                System.out.println("currently there is not enough of this product to complete this purchase, will be added " + item.getQuantity() + " of " + item.getName());
            cart.addItem(new SoldItem(item.getId(), item.getName(), item.getPrice(), Math.min(amount, item.getQuantity())));
        } else {
            System.out.println("no stock item with id " + idx);
        }
    }

    private void addToWarehouse(String[] input) {
        if (input.length > 2) {
            try {
                long idx = Long.parseLong(input[1]);
                int amount = Integer.parseInt(input[2]);

                StockItem existingItem = warehouse.getStockItemByBarcode(idx);

                if (existingItem != null) {
                    handleExistingItem(input, amount, existingItem);
                } else {
                    handleNewItem(input, amount, idx);
                }
            } catch (NumberFormatException e) {
                handleInvalidInput();
            }
        } else {
            handleInvalidInput();
        }
    }

    private void handleExistingItem(String[] input, int amount, StockItem existingItem) {
        if (input.length >= 5) {
            handleExistingItemWithDetails(input, amount, existingItem);
        } else if (input.length > 3) {
            handleMissingDetails();
        } else {
            existingItem.setQuantity(existingItem.getQuantity() + amount);
            System.out.println("Added " + amount + " of " + existingItem.getName() + " to the warehouse.");
        }
    }

    private void handleExistingItemWithDetails(String[] input, int amount, StockItem existingItem) {
        String name = input[3];
        double price = Double.parseDouble(input[4]);

        if (existingItem.getName().equals(name) && existingItem.getPrice() == price) {
            existingItem.setQuantity(existingItem.getQuantity() + amount);
            System.out.println("Added " + amount + " of " + existingItem.getName() + " to the warehouse.");
        } else {
            System.out.println("Name and/or price do not match the existing item with barcode " + existingItem.getId());
        }
    }

    private void handleNewItem(String[] input, int amount, long idx) {
        if (input.length >= 5) {
            handleNewItemWithDetails(input, amount, idx);
        } else {
            System.out.println("Item with barcode " + idx + " does not exist in the warehouse, and you must provide additional information to add it.");
        }
    }

    private void handleNewItemWithDetails(String[] input, int amount, long idx) {
        String name = input[3];
        double price = Double.parseDouble(input[4]);
        String description = (input.length > 5) ? String.join(" ", Arrays.copyOfRange(input, 5, input.length)) : (input.length > 3) ? "" : null;

        StockItem newItem = new StockItem(idx, name, description, price, amount);
        warehouse.addStockItemToDAO(newItem);
        System.out.println("Added " + amount + " of " + newItem.getName() + " to the warehouse.");
    }

    private void handleMissingDetails() {
        System.out.println("Name and price information are required to match an existing item.");
    }

    private void handleInvalidInput() {
        log.error("Invalid input for adding items to the warehouse.");
        System.out.println("Invalid input for adding items to the warehouse.");
    }




    private void teamInfo() {
        System.out.println("-------------------------");
        System.out.println("Team name:" + teamName);
        System.out.println("Team leader: " + teamLeader);
        System.out.println("Team leader email: " + leaderEmail);
        System.out.println("Team members: " + teamMembers);
        System.out.println("-------------------------");
    }
}
