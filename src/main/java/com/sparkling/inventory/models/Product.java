package com.sparkling.inventory.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Product {
    
    @Id
    @GeneratedValue
    private int id;
    
    @NotNull
    @Size(min=1, message="Size must be at least 1 character long" )
    private String name;

    @NotNull
    private BigDecimal price;

    /*
    This maps to the Customers model class. Spring Boot scans the classes and says: Look in the Customer class
    as defined by the List<Customers> for a property named "products" as defined by the value of the mappedBy property
    of the @ManyToMany annotation.

    @ManyToMany(mappedBy = "products")
    private List<Customer> customers;
    */

    public Product() {}

    public Product(String name,  BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {this.price = price;}

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
