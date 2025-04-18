package com.realestate.realestate.controller;

import com.realestate.realestate.model.Property;
import com.realestate.realestate.model.User;
import com.realestate.realestate.service.PropertyService;
import com.realestate.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Controller
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/properties")
    public String viewProperties(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        List<Property> properties = propertyService.getPropertiesByUser(user);
        model.addAttribute("properties", properties);
        model.addAttribute("username", username);

        return "profile";
    }

    @PostMapping("/properties/add")
    public String addProperty(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam Double price) {

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // build and save
        Property property = new Property();
        property.setName(name);
        property.setDescription(description);
        property.setPrice(price);
        property.setUser(user);
        propertyService.addProperty(property);

        return "redirect:/properties";
    }
}
