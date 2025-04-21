package com.realestate.realestate.controller;

import com.realestate.realestate.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping({"/", "/main"})
    public String mainPage(Model model) {
        model.addAttribute("properties", propertyService.getAllProperties());
        return "main";
    }
}
