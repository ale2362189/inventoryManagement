package com.promineotech.inventoryManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.promineotech.inventoryManagement.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
