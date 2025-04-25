package com.realestate.realestate.repository;

import com.realestate.realestate.model.InterviewRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRequestRepository extends JpaRepository<InterviewRequest,Long> {
    List<InterviewRequest> findByPropertyId(Long propertyId);
}
