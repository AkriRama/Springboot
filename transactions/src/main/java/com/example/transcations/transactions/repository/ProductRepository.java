package com.example.transcations.transactions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.transcations.transactions.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
