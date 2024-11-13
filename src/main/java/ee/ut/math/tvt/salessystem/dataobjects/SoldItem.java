package ee.ut.math.tvt.salessystem.dataobjects;


import javax.persistence.*;

/**
 * Already bought StockItem. SoldItem duplicates name and price for preserving history.
 */
@Entity
public class SoldItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sold_id")
    private Long id; //TODO: do two fields, barcode for renderring, and id, do not use by useful
//    @OneToOne
//    @JoinColumn(name = "stock_id")

    @Column(name = "barcode")
    private long barcode;

    //All the info about the item can be gotten from the other fields. This is not needed.
    /*
    @Transient
    private StockItem stockItem;

     */
    @Column(name = "name")
    private String name;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private double price;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    public SoldItem() {
    }
//TODO: do not use id
    public SoldItem( long barcode, String name, Double price, int quantity) {
        //this.id= id;
        this.barcode=barcode;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getSum() {
        return price * ((double) quantity);
    }

    /*
    public StockItem getStockItem() {
        return this.stockItem;
    }

     */

    /*
    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

     */

    @Override
    public String toString() {
        return String.format("SoldItem{id=%d, name='%s'}", id, name);
    }

    public Purchase getPurchase(){
        return this.purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }
}

