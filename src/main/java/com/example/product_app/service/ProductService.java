package com.example.product_app.service;


import com.example.product_app.exception.DiscountNotFoundException;
import com.example.product_app.exception.ProductNotFoundException;
import com.example.product_app.exception.VisitCounterNotFoundException;
import com.example.product_app.mapper.ProductDTOMapper;
import com.example.product_app.model.dto.ProductDTO;
import com.example.product_app.model.entity.Discount;
import com.example.product_app.model.entity.Product;
import com.example.product_app.model.entity.ProductType;
import com.example.product_app.model.entity.VisitCounter;
import com.example.product_app.model.type.ProductTypeEnum;
import com.example.product_app.repository.DiscountRepository;
import com.example.product_app.repository.ProductRepository;
import com.example.product_app.repository.ProductTypeRepository;
import com.example.product_app.repository.VisitCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final VisitCounterRepository visitCounterRepository;
    private final ProductTypeRepository productTypeRepository;

    @Autowired
    public ProductService(
            ProductRepository productRepository,
            DiscountRepository discountRepository,
            VisitCounterRepository visitCounterRepository,
            ProductTypeRepository productTypeRepository
    ) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.visitCounterRepository = visitCounterRepository;
        this.productTypeRepository = productTypeRepository;
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


    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {

        ProductType productTypeFemale = new ProductType(ProductTypeEnum.FEMALE);
        ProductType productTypeKid = new ProductType(ProductTypeEnum.KID);
        ProductType productTypeMale = new ProductType(ProductTypeEnum.MALE);

        productTypeRepository.save(productTypeFemale);
        productTypeRepository.save(productTypeKid);
        productTypeRepository.save(productTypeMale);

        Product p1 = new Product("Product1", "Product1 desc", productTypeFemale, new BigDecimal("100.0"));
        Product p2 = new Product("Product2", "Product2 desc", productTypeKid, new BigDecimal("100.0"));
        Product p3 = new Product("Product3", "Product3 desc", productTypeMale, new BigDecimal("100.0"));

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);

        Discount d1 = new Discount(productTypeFemale, 5);
        Discount d2 = new Discount(productTypeMale, 5);
        Discount d3 = new Discount(productTypeKid, 10);

        discountRepository.save(d1);
        discountRepository.save(d2);
        discountRepository.save(d3);

        VisitCounter visitCounter1 = new VisitCounter(p1, 0);
        VisitCounter visitCounter2 = new VisitCounter(p2, 0);
        VisitCounter visitCounter3 = new VisitCounter(p3, 0);

        visitCounterRepository.save(visitCounter1);
        visitCounterRepository.save(visitCounter2);
        visitCounterRepository.save(visitCounter3);
    }

}
