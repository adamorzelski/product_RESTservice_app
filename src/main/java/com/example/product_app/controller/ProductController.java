package com.example.product_app.controller;

import com.example.product_app.model.dto.ProductDTO;
import com.example.product_app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable long id) {
        return productService.findByIdWithDiscountedPriceAndIncrementVisitCounter(id);
    }
}
