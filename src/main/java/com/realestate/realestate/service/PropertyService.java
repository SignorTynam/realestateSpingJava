package com.realestate.realestate.service;

import com.realestate.realestate.model.Property;
import com.realestate.realestate.model.User;
import com.realestate.realestate.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> getPropertiesByUser(User user) {
        return propertyRepository.findByUserId(user.getId());
    }

    public Property addProperty(Property property) {
        return propertyRepository.save(property);
    }
}