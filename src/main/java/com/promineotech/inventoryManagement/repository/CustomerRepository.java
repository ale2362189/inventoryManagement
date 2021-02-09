package com.promineotech.inventoryManagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.promineotech.inventoryManagement.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
