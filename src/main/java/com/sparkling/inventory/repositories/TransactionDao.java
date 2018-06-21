package com.sparkling.inventory.repositories;

import com.sparkling.inventory.models.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionDao extends CrudRepository<Transaction, Integer> {
}
