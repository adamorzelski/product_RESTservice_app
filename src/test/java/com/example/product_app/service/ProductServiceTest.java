package com.example.product_app.service;

import com.example.product_app.exception.DiscountNotFoundException;
import com.example.product_app.exception.ProductNotFoundException;
import com.example.product_app.exception.VisitCounterNotFoundException;
import com.example.product_app.model.dto.ProductDTO;
import com.example.product_app.model.entity.Discount;
import com.example.product_app.model.entity.Product;
import com.example.product_app.model.entity.ProductType;
import com.example.product_app.model.entity.VisitCounter;
import com.example.product_app.model.type.ProductTypeEnum;
import com.example.product_app.repository.DiscountRepository;
import com.example.product_app.repository.ProductRepository;
import com.example.product_app.repository.VisitCounterRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private VisitCounterRepository visitCounterRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private ProductService productService;


    @Test
    void shouldReturnIncrementedProductVisitCounter() {

        //given
        long visitCounterValue = 0L;
        long expectedVisitCounterValue = visitCounterValue + 1L;

        Product product = new Product();
        VisitCounter visitCounter = new VisitCounter(product,  visitCounterValue);
        Optional<VisitCounter> optionalVisitCounter = Optional.of(visitCounter);
        doReturn(optionalVisitCounter).when(visitCounterRepository).findByProduct(any(Product.class));

        //when
        long result = productService.incrementProductVisitCounter(product);

        //then
        assertEquals(expectedVisitCounterValue, result);
        assertEquals(expectedVisitCounterValue, visitCounter.getVisitCounter());

    }

    @Test
    void shouldThrowVisitCounterNotFoundException() {

        //given
        Optional<VisitCounter> optionalVisitCounter = Optional.ofNullable(null);
        doReturn(optionalVisitCounter).when(visitCounterRepository).findByProduct(any(Product.class));

        //when + then
        assertThrows(VisitCounterNotFoundException.class, () -> productService.incrementProductVisitCounter(new Product()));

    }

    @Test
    void shouldReturnDiscountedPrice() {

        //given
        BigDecimal price = BigDecimal.valueOf(100);
        int discountPercent = 20;

        //when
        BigDecimal discountedPrice = productService.countDiscountedPrice(price, discountPercent);

        //then
        assertEquals(80.0, discountedPrice.doubleValue());

    }

    @Test
    void shouldReturnProductDTOWithDiscountedPriceAndIncrementedVisitCounter() {

        //given
        ProductType productType = new ProductType(ProductTypeEnum.KID);
        Discount discount = new Discount(productType, 20);
        Product product = new Product("Product1", "Desc 1", productType, BigDecimal.valueOf(100));
        VisitCounter visitCounter = new VisitCounter(product, 0);

        Optional<Product> optionalProduct = Optional.of(product);
        Optional<Discount> optionalDiscount = Optional.of(discount);
        Optional<VisitCounter> optionalVisitCounter = Optional.of(visitCounter);

        doReturn(optionalProduct).when(productRepository).findById(anyLong());
        doReturn(optionalDiscount).when(discountRepository).findByType(any(ProductType.class));
        doReturn(optionalVisitCounter).when(visitCounterRepository).findByProduct(any(Product.class));

        //when
        ProductDTO productDTO = productService.findByIdWithDiscountedPriceAndIncrementVisitCounter(1L);

        //then
        assertEquals("Product1", productDTO.getName());
        assertEquals("Desc 1", productDTO.getDescription());
        assertEquals(ProductTypeEnum.KID, productDTO.getProductType());
        assertEquals(80, productDTO.getPrice().doubleValue());
        assertEquals(1L, productDTO.getVisitCounter());

    }

    @Test
    void shouldReturnProductDTOWithDiscountedPrice() {

        //given
        int priceBeforeDiscount = 100;
        int discountPercent = 20;
        int expectedPrice = 80;

        ProductType productType = new ProductType(ProductTypeEnum.KID);
        Discount discount = new Discount(productType, discountPercent);
        Product product = new Product("Product1", "Desc 1", productType, BigDecimal.valueOf(priceBeforeDiscount));
        VisitCounter visitCounter = new VisitCounter(product, 0);

        Optional<Product> optionalProduct = Optional.of(product);
        Optional<Discount> optionalDiscount = Optional.of(discount);
        Optional<VisitCounter> optionalVisitCounter = Optional.of(visitCounter);

        doReturn(optionalProduct).when(productRepository).findById(anyLong());
        doReturn(optionalDiscount).when(discountRepository).findByType(any(ProductType.class));
        doReturn(optionalVisitCounter).when(visitCounterRepository).findByProduct(any(Product.class));

        //when
        ProductDTO productDTO = productService.findByIdWithDiscountedPriceAndIncrementVisitCounter(1L);

        //then
        assertEquals(expectedPrice, productDTO.getPrice().doubleValue());

    }

    @Test
    void shouldReturnProductDTOWithIncrementedVisitCounter() {

        //given
        long visitCounterBefore = 0L;
        long expectedVisitCounter = visitCounterBefore + 1;

        ProductType productType = new ProductType(ProductTypeEnum.KID);
        Discount discount = new Discount(productType, 20);
        Product product = new Product("Product1", "Desc 1", productType, BigDecimal.valueOf(100));
        VisitCounter visitCounter = new VisitCounter(product, visitCounterBefore);

        Optional<Product> optionalProduct = Optional.of(product);
        Optional<Discount> optionalDiscount = Optional.of(discount);
        Optional<VisitCounter> optionalVisitCounter = Optional.of(visitCounter);

        doReturn(optionalProduct).when(productRepository).findById(anyLong());
        doReturn(optionalDiscount).when(discountRepository).findByType(any(ProductType.class));
        doReturn(optionalVisitCounter).when(visitCounterRepository).findByProduct(any(Product.class));

        //when
        ProductDTO productDTO = productService.findByIdWithDiscountedPriceAndIncrementVisitCounter(1L);

        //then
        assertEquals(expectedVisitCounter, productDTO.getVisitCounter());
    }

    @Test
    void shouldThrowProductNotFoundException() {

        //given
        Optional<Product> optionalProduct = Optional.ofNullable(null);

        doReturn(optionalProduct).when(productRepository).findById(anyLong());

        //when + then
        assertThrows(ProductNotFoundException.class, () -> productService.findByIdWithDiscountedPriceAndIncrementVisitCounter(1L));

    }

    @Test
    void shouldThrowDiscountNotFoundException() {

        //given
        ProductType productType = new ProductType(ProductTypeEnum.KID);
        Product product = new Product("Product1", "Desc 1", productType, BigDecimal.valueOf(100));

        Optional<Product> optionalProduct = Optional.of(product);
        Optional<Discount> optionalDiscount = Optional.ofNullable(null);

        doReturn(optionalProduct).when(productRepository).findById(anyLong());
        doReturn(optionalDiscount).when(discountRepository).findByType(any(ProductType.class));

        //when + then
        assertThrows(DiscountNotFoundException.class, () -> productService.findByIdWithDiscountedPriceAndIncrementVisitCounter(1L));

    }


}
