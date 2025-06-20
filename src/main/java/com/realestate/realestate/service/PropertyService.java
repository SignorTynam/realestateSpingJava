package com.realestate.realestate.service;

import com.realestate.realestate.model.Property;
import com.realestate.realestate.model.User;
import com.realestate.realestate.repository.MessageRepository;
import com.realestate.realestate.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<Property> getPropertiesByUser(User user) {
        List<Property> properties = propertyRepository.findByUserId(user.getId());
        return properties != null ? properties : Collections.emptyList();
    }

    public Property addProperty(Property property) {
        return propertyRepository.save(property);
    }

    public void deleteProperty(Long propertyId) {
        messageRepository.deleteByPropertyId(propertyId);
        propertyRepository.deleteById(propertyId);
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElse(null);
    }

    public Property updateProperty(Property property) {
        return propertyRepository.save(property);
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

public List<Property> search(String query, Double minPrice, Double maxPrice, String type) {
    return propertyRepository.search(query, minPrice, maxPrice, type);
}
}
