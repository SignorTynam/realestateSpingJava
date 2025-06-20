package com.realestate.realestate.repository;

import com.realestate.realestate.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByUserId(Long userId);

    @Query("""
        SELECT p FROM Property p
         WHERE (:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))
           AND (:minPrice IS NULL OR p.price >= :minPrice)
           AND (:maxPrice IS NULL OR p.price <= :maxPrice)
           AND (:type IS NULL OR p.type = :type)
    """)
    List<Property> search(
        @Param("query") String query,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("type") String type
    );
}
