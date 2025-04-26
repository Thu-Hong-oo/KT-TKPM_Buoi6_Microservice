package com.example.demo.services;

import com.example.demo.entities.Product;

import java.util.List;

public interface ProductService {
    public List<Product> findAll();
    public Product findById(int id);
    public Product save(Product product);
    public void delete(Product product);

}
