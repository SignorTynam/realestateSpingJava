package com.realestate.realestate.controller;

import com.realestate.realestate.model.InterviewRequest;
import com.realestate.realestate.model.Photo;
import com.realestate.realestate.model.Property;
import com.realestate.realestate.model.User;
import com.realestate.realestate.repository.UserRepository;
import com.realestate.realestate.service.InterviewRequestService;
import com.realestate.realestate.service.PhotoService;
import com.realestate.realestate.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/properties")
public class PropertyController {

    @Autowired private PropertyService propertyService;
    @Autowired private UserRepository userRepository;
    @Autowired private PhotoService photoService;
    @Autowired private InterviewRequestService inquiryService;

    @Autowired
    public PropertyController(PropertyService propertyService, UserRepository userRepository) {
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

        // Fetch all inquiries for these properties to display message count
        List<InterviewRequest> messages = new ArrayList<>();
        for (Property property : props) {
            messages.addAll(inquiryService.getMessagesByProperty(property.getId()));
        }

        model.addAttribute("properties", props);
        model.addAttribute("username", ud.getUsername());
        model.addAttribute("messages", messages);
        return "profile";
    }

    /** 
     * Add a new property via POST /properties/add 
     */
    @PostMapping("/add")
    public String addProperty(@AuthenticationPrincipal UserDetails ud,
                              @RequestParam String name,
                              @RequestParam String description,
                              @RequestParam Double price,
                              @RequestParam String location) {
        User user = userRepository.findByUsername(ud.getUsername());
        Property p = new Property();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setLocation(location);
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

    /** 
     * Show property details via GET /properties/{id} 
     */
    @GetMapping("/{id}/photos")
    public String photoForm(@PathVariable Long id, Model model) {
        Property p = propertyService.getPropertyById(id);
        model.addAttribute("property", p);
        List<Photo> photos = photoService.getPhotosForProperty(id);
        model.addAttribute("photos", photos);
        return "upload-photo";
    }

    /** 
     * Process photo upload via POST /properties/{id}/photos 
     */
    @PostMapping("/{id}/photos")
    public String handlePhotoUpload(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        Property p = propertyService.getPropertyById(id);
        photoService.savePhoto(p, file);
        return "redirect:/properties/" + id + "/photos";
    }

    /** 
     * Delete a photo via POST /properties/{propertyId}/photos/{photoId}/delete 
     */
    @PostMapping("/{propertyId}/photos/{photoId}/delete")
    public String deletePhoto(
            @PathVariable Long propertyId,
            @PathVariable Long photoId,
            @AuthenticationPrincipal UserDetails ud) {
    
        photoService.deletePhoto(photoId);
    
        return "redirect:/properties/" + propertyId + "/photos";
    }

    /** 
     * Show property details via GET /properties/{id} 
     */
    @PostMapping("/{id}/inquire")
    public String submitInquiry(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String message,
            Model model) {

        Property p = propertyService.getPropertyById(id);
        inquiryService.save(p, name, email, phone, message);
        model.addAttribute("property", p);
        model.addAttribute("inquirySent", true);
        return "property-details";
    }

    @GetMapping("/messages")
    public String viewMessages(@AuthenticationPrincipal UserDetails ud, Model model) {
        User user = userRepository.findByUsername(ud.getUsername());
        List<Property> properties = propertyService.getPropertiesByUser(user);
        List<InterviewRequest> messages = new ArrayList<>();
        for (Property property : properties) {
            messages.addAll(inquiryService.getMessagesByProperty(property.getId()));
        }
        model.addAttribute("messages", messages);
        return "messages";
    }

@GetMapping("/search")
public String searchProperties(@RequestParam(required = false) String query,
                               @RequestParam(required = false) String priceRange,
                               @RequestParam(required = false) String type,
                               Model model) {
    Double minPrice = null;
    Double maxPrice = null;

    if (priceRange != null && !priceRange.isBlank()) {
        if (priceRange.equals("$0 - $100,000")) {
            minPrice = 0.0;
            maxPrice = 100000.0;
        } else if (priceRange.equals("$100,000 - $500,000")) {
            minPrice = 100000.0;
            maxPrice = 500000.0;
        } else if (priceRange.equals("$500,000+")) {
            minPrice = 500000.0;
        }
    }

    // Nëse lëshohet fushë e zbrazët, trajtoje si null
    if (type != null && type.isBlank()) type = null;
    if (query != null && query.isBlank()) query = null;

    List<Property> properties = propertyService.search(query, minPrice, maxPrice, type);
    model.addAttribute("properties", properties);
    return "search-results"; // ose emri i template-it tënd
}
}
