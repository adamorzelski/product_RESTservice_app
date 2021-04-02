package com.example.product_app.service;


import com.example.product_app.exception.DiscountNotFoundException;
import com.example.product_app.exception.ProductNotFoundException;
import com.example.product_app.exception.VisitCounterNotFoundException;
import com.example.product_app.mapper.ProductDTOMapper;
import com.example.product_app.model.dto.ProductDTO;
import com.example.product_app.model.entity.Discount;
import com.example.product_app.model.entity.Product;
import com.example.product_app.model.entity.VisitCounter;
import com.example.product_app.repository.DiscountRepository;
import com.example.product_app.repository.ProductRepository;
import com.example.product_app.repository.VisitCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final VisitCounterRepository visitCounterRepository;

    @Autowired
    public ProductService(
            ProductRepository productRepository,
            DiscountRepository discountRepository,
            VisitCounterRepository visitCounterRepository
    ) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.visitCounterRepository = visitCounterRepository;
    }

    public ProductDTO findByIdWithDiscountedPriceAndIncrementVisitCounter(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));

        Optional<Discount> optionalDiscount = discountRepository.findByType(product.getType());
        Discount discount = optionalDiscount.orElseThrow(() -> new DiscountNotFoundException("Internal server error"));

        long visitCounterAfterIncrement = incrementProductVisitCounter(product);

        ProductDTO productDTO = ProductDTOMapper.mapToDTO(product, visitCounterAfterIncrement);
        BigDecimal priceAfterDiscount = countDiscountedPrice(productDTO.getPrice(), discount.getPercent());
        productDTO.setPrice(priceAfterDiscount);

        return productDTO;
    }

    public long incrementProductVisitCounter(Product product) {
        Optional<VisitCounter> optionalVisitCounter = visitCounterRepository.findByProduct(product);
        VisitCounter visitCounter = optionalVisitCounter.orElseThrow(() -> new VisitCounterNotFoundException("Internal server error"));

        long counterAfterIncrement = visitCounter.getVisitCounter() + 1;
        visitCounter.setVisitCounter(counterAfterIncrement);
        visitCounterRepository.save(visitCounter);
        return counterAfterIncrement;
    }

    public BigDecimal countDiscountedPrice(BigDecimal price, int discountPercent) {
        BigDecimal discountValue = price.multiply(BigDecimal.valueOf(discountPercent).divide(BigDecimal.valueOf(100)));
        return price.subtract(discountValue).setScale(2, RoundingMode.UP);
    }

}
