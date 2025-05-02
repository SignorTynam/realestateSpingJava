package com.realestate.realestate.repository;

import com.realestate.realestate.model.Message;
import com.realestate.realestate.model.User;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUser(User user);

    @Modifying
    @Transactional
    @Query("delete from Message m where m.property.id = :propertyId")
    void deleteByPropertyId(@Param("propertyId") Long propertyId);
}