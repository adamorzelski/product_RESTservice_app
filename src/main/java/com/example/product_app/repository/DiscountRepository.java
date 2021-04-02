package com.example.product_app.repository;

import com.example.product_app.model.entity.Discount;
import com.example.product_app.model.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> findByType(ProductType type);
}
