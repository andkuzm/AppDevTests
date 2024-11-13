package ee.ut.math.tvt.salessystem.dataobjects;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.List.copyOf;

@Entity
public class Purchase {
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "time")
    private LocalTime time;
    @Column(name = "total")
    private double total;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.PERSIST)
    private List<SoldItem> items;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "purchase_id")
    private Long id;

    public Purchase() {
    }

    public Purchase(List<SoldItem> items) {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.items = copyOf(items);
        this.total = items.stream().mapToDouble(SoldItem::getSum).sum();
        items.forEach(item -> item.setPurchase(this));
    }

    public Purchase(LocalDate date, LocalTime time, double total, List<SoldItem> items) {//TODO: rm
        this.date = date;
        this.time = time;
        this.total = total;
        this.items = items;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        return LocalTime.parse(time.format(formatter));
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<SoldItem> getItems() {
        return items;
    }

    public void setItems(List<SoldItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "date=" + date + ", time=" + time + ", total=" + total + ", items=" + Arrays.toString(items.stream().map(i -> i.getName()).toArray());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
