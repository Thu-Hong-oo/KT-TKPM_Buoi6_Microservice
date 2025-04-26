package com.example.demo.services.impl;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
   
    @Autowired
    private ProductRepository productRepository;
   
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }   
    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }
}
