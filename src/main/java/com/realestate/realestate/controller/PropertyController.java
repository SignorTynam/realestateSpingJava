package com.realestate.realestate.controller;

import com.realestate.realestate.model.Property;
import com.realestate.realestate.model.User;
import com.realestate.realestate.repository.UserRepository;
import com.realestate.realestate.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final UserRepository userRepository;

    @Autowired
    public PropertyController(PropertyService propertyService,
                              UserRepository userRepository) {
        this.propertyService = propertyService;
        this.userRepository  = userRepository;
    }

    /** 
     * Show the logged‑in user’s properties at GET /properties 
     */
    @GetMapping
    public String viewProperties(@AuthenticationPrincipal UserDetails ud,
                                 Model model) {
        User user = userRepository.findByUsername(ud.getUsername());
        List<Property> props = propertyService.getPropertiesByUser(user);
        model.addAttribute("properties", props);
        model.addAttribute("username", ud.getUsername());
        return "profile";
    }

    /** 
     * Add a new property via POST /properties/add 
     */
    @PostMapping("/add")
    public String addProperty(@AuthenticationPrincipal UserDetails ud,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam Double price) {
        User user = userRepository.findByUsername(ud.getUsername());
        Property p = new Property();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setUser(user);
        propertyService.addProperty(p);
        return "redirect:/properties";
    }

    /** 
     * Delete a property via POST /properties/{id}/delete 
     */
    @PostMapping("/{id}/delete")
    public String deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return "redirect:/properties";
    }

    /** 
     * Show edit form via GET /properties/{id}/edit 
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails ud,
                           Model model) {
        User user = userRepository.findByUsername(ud.getUsername());
        Property p = propertyService.getPropertyById(id);
        if (p == null || !p.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied or property not found");
        }
        model.addAttribute("property", p);
        return "edit-property";
    }

    /** 
     * Process edit via POST /properties/{id}/edit 
     */
    @PostMapping("/{id}/edit")
    public String editProperty(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails ud,
                               @RequestParam String name,
                               @RequestParam String description,
                               @RequestParam Double price) {
        User user = userRepository.findByUsername(ud.getUsername());
        Property p = propertyService.getPropertyById(id);
        if (p == null || !p.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied or property not found");
        }
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        propertyService.updateProperty(p);
        return "redirect:/properties";
    }
}
