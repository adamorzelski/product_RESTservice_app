package com.example.product_app.controller;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private VisitCounterRepository visitCounterRepository;
    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Test
    @Transactional
    void shouldReturnProductDTOWithDiscountedPriceAndIncrementVisitCounter() throws Exception {

        //given
        ProductType productType = new ProductType(ProductTypeEnum.KID);
        Product product = new Product("TestProduct", "Test desc", productType, BigDecimal.valueOf(100));
        Discount discount = new Discount(productType, 20);
        VisitCounter visitCounter = new VisitCounter(product, 0L);

        productTypeRepository.save(productType);
        Product productWithId = productRepository.save(product);
        visitCounterRepository.save(visitCounter);
        discountRepository.save(discount);

        long productId = productWithId.getId();
        long expectedVisitCounter = 1L;
        double expectedPrice = 80.0;

        //when + then
        MvcResult result = mockMvc.perform(get("/api/products/" + productId))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();

        ProductDTO productDTO = objectMapper.readValue(result.getResponse().getContentAsString(), ProductDTO.class);

        assertEquals(product.getName(), productDTO.getName());
        assertEquals(product.getDescription(), productDTO.getDescription());
        assertEquals(product.getType().getProductType(), productDTO.getProductType());
        assertEquals(expectedPrice, productDTO.getPrice().doubleValue());
        assertEquals(expectedVisitCounter, productDTO.getVisitCounter());
    }
}
