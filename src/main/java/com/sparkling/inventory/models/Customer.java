package com.sparkling.inventory.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 2, message="Company name length must be at least 1 character long.")
    private String name;

    //This maps to the Products class.
    @ManyToMany
    private List<Product> products;

    public Customer() {}

    public Customer(String name) {
        this.name = name;
    }

    public void addProduct(Product product) {products.add(product);}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
