package com.company.dao.repository;

import com.company.dao.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p.price from Product p where p.id =:id")
    Optional<BigDecimal> findPriceById(Long id);
}
