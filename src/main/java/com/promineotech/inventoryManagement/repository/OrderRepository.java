package com.promineotech.inventoryManagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.promineotech.inventoryManagement.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
