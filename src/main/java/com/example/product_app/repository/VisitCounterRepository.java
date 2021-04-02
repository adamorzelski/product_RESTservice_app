package com.example.product_app.repository;

import com.example.product_app.model.entity.Product;
import com.example.product_app.model.entity.VisitCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitCounterRepository extends JpaRepository<VisitCounter, Long> {

    Optional<VisitCounter> findByProduct(Product product);
}
