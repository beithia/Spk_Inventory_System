package com.sparkling.inventory.repositories;

import com.sparkling.inventory.models.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;



public interface CustomerDao extends CrudRepository<Customer, Integer> {
    Iterable<Customer> findAllByOrderByNameAsc();

    @Query(value = "SELECT c FROM Customer c " +
                   "WHERE c.id <> 0 " +
                   "ORDER BY c.name ASC")
    Iterable<Customer> customerList();
}
