// src/main/java/com/realestate/realestate/repository/PhotoRepository.java
package com.realestate.realestate.repository;

import com.realestate.realestate.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo,Long> {
    List<Photo> findByPropertyId(Long propertyId);
}
