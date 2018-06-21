package com.sparkling.inventory.repositories;

import com.sparkling.inventory.models.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductDao extends CrudRepository<Product, Integer> {
    Iterable<Product> findAllByOrderByNameAsc();

    //Finds all the products and their respective quantities for a time period given a customer id.
    @Query(value="SELECT p.id, p.name, COALESCE(SUM(t.quantity), 0), p.price " +
                 "FROM Product p LEFT JOIN Transaction t " +
                 "ON p.id = t.product_id " +
                 "AND t.customer_id = :custId " +
                 "AND t.customer_id <> '0' " +
                 "AND t.date_created BETWEEN :dateFrom AND :dateTo " +
                 "GROUP BY p.price, p.name, p.id " +
                 "ORDER BY p.name ASC")
    List<Object[]> findProductsByCustomerId(@Param("custId") int custId,
                                            @Param("dateFrom") Date dateFrom,
                                            @Param("dateTo") Date dateTo);

    //Finds products and their quantities (stock) for Sparkling Cleaning Services.
    @Query(value = "SELECT p.id, p.name, coalesce(sum(t.quantity), 0), p.price " +
                    "FROM Product p LEFT JOIN Transaction t " +
                    "ON p.id = t.product_id " +
                    "AND t.customer_id = 0 " +
                    "GROUP BY p.price, p.name, p.id " +
                    "ORDER BY p.name ASC")
    Iterable<Product> getSpkStock();


    //Finds total price for a given date period given a customer id.
    @Query(value = "SELECT COALESCE(sum(p.price * t.quantity), 0) " +
                   "FROM Transaction t JOIN Product p " +
                   "ON t.product_id = p.id " +
                   "AND t.customer_id = :custId " +
                   "AND t.date_created BETWEEN :dateFrom AND :dateTo")
    double findTotalPrice(@Param("custId") int custId,
                          @Param("dateFrom") Date dateFrom,
                          @Param("dateTo") Date dateTo);


    //Finds the stock count of a product given its id.
    @Query("SELECT coalesce(sum(t.quantity), 0) " +
            "FROM Product p LEFT JOIN Transaction t " +
            "ON t.product_id = p.id " +
            "AND t.customer_id = 0 " +
            "AND p.id = :prodId")
    int findSpkStockByProductId(@Param("prodId") int prodId);

}

