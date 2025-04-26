package com.example.customer.services;

import com.example.customer.entities.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    Customer findById(Long id);
    Customer save(Customer customer);
    void deleteById(Long id);
} 