package com.example.product_app.mapper;


import com.example.product_app.model.dto.ProductDTO;
import com.example.product_app.model.entity.Product;

public class ProductDTOMapper {

    public static ProductDTO mapToDTO(Product product, long visitCounter) {
        ProductDTO result = new ProductDTO();
        result.setDescription(product.getDescription());
        result.setName(product.getName());
        result.setProductType(product.getType().getProductType());
        result.setPrice(product.getPrice());
        result.setVisitCounter(visitCounter);

        return result;
    }

}
