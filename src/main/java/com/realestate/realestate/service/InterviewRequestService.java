package com.realestate.realestate.service;

import com.realestate.realestate.model.InterviewRequest;
import com.realestate.realestate.model.Property;
import com.realestate.realestate.repository.InterviewRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewRequestService {
    private final InterviewRequestRepository repo;
    public InterviewRequestService(InterviewRequestRepository repo) {
        this.repo = repo;
    }
    public InterviewRequest save(Property property, String name, String email, String phone, String message) {
        InterviewRequest req = new InterviewRequest(name,email,phone,message,property);
        return repo.save(req);
    }

    public List<InterviewRequest> getMessagesByProperty(Long propertyId) {
        return repo.findByPropertyId(propertyId);
    }
}
