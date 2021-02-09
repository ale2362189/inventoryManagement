package com.promineotech.inventoryManagement.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promineotech.inventoryManagement.entity.Customer;
import com.promineotech.inventoryManagement.entity.Order;
import com.promineotech.inventoryManagement.entity.Product;
import com.promineotech.inventoryManagement.repository.CustomerRepository;
import com.promineotech.inventoryManagement.repository.OrderRepository;
import com.promineotech.inventoryManagement.repository.ProductRepository;
import com.promineotech.inventoryManagement.util.MembershipLevel;
import com.promineotech.inventoryManagement.util.OrderStatus;

@Service
public class OrderService {
	
	private static final Logger logger = LogManager.getLogger(OrderService.class);
	private final int DELIVERY_DAYS = 7;
	
	@Autowired
	private OrderRepository repo;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	public Order submitNewOrder(Set<Long> productIds, Long customerId) throws Exception {
		try {
			Customer customer = customerRepo.findById(customerId)
					.orElseThrow(() -> new NoSuchElementException("Can't find customer with ID: " + customerId));
			Order order = initializeNewOrder(productIds, customer);
			return repo.save(order);
		} catch (Exception e) {
			logger.error("Exception occured while trying to create new order for customer: " + customerId, e);
			throw e;		
		}
	}
	
	public Order cancelOrder(Long orderId) {
			Order order = repo.findById(orderId)
					.orElseThrow(() -> new NoSuchElementException("Can't find order with ID: " + orderId));
			order.setStatus(OrderStatus.CANCELED);
			return repo.save(order);
		
	}
	
	public Order completeOrder(Long orderId) {
			Order order = repo.findById(orderId)
					.orElseThrow(() -> new NoSuchElementException("Can't find order with id:" + orderId));
			order.setStatus(OrderStatus.DELIVERED);
			return repo.save(order);
	}

	private Order initializeNewOrder(Set<Long> productIds, Customer customer) {
		Order order = new Order();
		order.setProducts(convertToProductSet(productRepo.findAllById(productIds)));
		order.setOrdered(LocalDate.now());
		order.setEstimatedDelivery(LocalDate.now().plusDays(DELIVERY_DAYS));
		order.setCustomer(customer);
		order.setInvoiceAmount(calculateOrderTotal(order.getProducts(), customer.getLevel()));
		order.setStatus(OrderStatus.ORDERED);
		addOrderToProducts(order);
		return order;
	}
	
	private void addOrderToProducts(Order order) {
		Set<Product> products = order.getProducts();
		for (Product product : products) {
			product.getOrders().add(order);
		}
	}
	
	private Set<Product> convertToProductSet(Iterable<Product> iterable) {
		Set<Product> set = new HashSet<Product>();
		for (Product product : iterable) {
			set.add(product);
		}
		return set;
	}
	
	private double calculateOrderTotal(Set<Product> products, MembershipLevel level) {
		double total = 0;
		for (Product product : products) {
			total += product.getPrice();
		}
		return total * level.getDiscount();
	}
}
