package com.Backend.Jewellery.repository;

import com.Backend.Jewellery.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(""" 
            update Product p set p.stockQuantity = p.stockQuantity - :qty
            where p.id = :id and p.stockQuantity >= :qty
         """)

    int decrementStock(@Param("id") Long id, @Param("qty") int qty);

    @Override
    @EntityGraph(attributePaths = "catehory")
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}
