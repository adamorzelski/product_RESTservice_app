package com.example.product_app;


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
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


public class DataInit {

    public DataInit(
            ProductRepository productRepository,
            DiscountRepository discountRepository,
            ProductTypeRepository productTypeRepository,
            VisitCounterRepository visitCounterRepository
    ) {


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
