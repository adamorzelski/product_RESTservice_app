package com.example.product_app.model.dto;


import com.example.product_app.model.type.ProductTypeEnum;

import java.math.BigDecimal;

public class ProductDTO {

    private String name;
    private String description;
    private ProductTypeEnum productType;
    private BigDecimal price;
    private long visitCounter;

    public ProductDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductTypeEnum getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeEnum productType) {
        this.productType = productType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getVisitCounter() {
        return visitCounter;
    }

    public void setVisitCounter(long visitCounter) {
        this.visitCounter = visitCounter;
    }
}
