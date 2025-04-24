package com.realestate.realestate.controller;

import com.realestate.realestate.model.Property;
import com.realestate.realestate.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping({"/", "/main"})
    public String mainPage(Model model) {
        model.addAttribute("properties", propertyService.getAllProperties());
        return "main";
    }

    @GetMapping("/properties/{id}")
    public String details(@PathVariable Long id, Model m) {
        Property p = propertyService.getPropertyById(id);
        m.addAttribute("property", p);
        return "property-details";
    }

}
