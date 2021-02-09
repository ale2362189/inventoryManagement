package com.promineotech.inventoryManagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.promineotech.inventoryManagement.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
