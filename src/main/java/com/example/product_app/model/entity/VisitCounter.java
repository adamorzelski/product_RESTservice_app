package com.example.product_app.model.entity;

import javax.persistence.*;

@Entity
public class VisitCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private Product product;
    private long visitCounter;

    public VisitCounter() {
    }

    public VisitCounter(Product product, long visitCounter) {
        this.product = product;
        this.visitCounter = visitCounter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getVisitCounter() {
        return visitCounter;
    }

    public void setVisitCounter(long visitCounter) {
        this.visitCounter = visitCounter;
    }

    @Override
    public String toString() {
        return "VisitCounter{" +
                "id=" + id +
                ", product=" + product +
                ", visitCounter=" + visitCounter +
                '}';
    }
}
