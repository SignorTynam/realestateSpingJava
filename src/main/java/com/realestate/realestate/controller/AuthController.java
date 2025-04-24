// src/main/java/com/realestate/realestate/controller/AuthController.java
package com.realestate.realestate.controller;

import com.realestate.realestate.model.User;
import com.realestate.realestate.model.Property;
import com.realestate.realestate.repository.UserRepository;
import com.realestate.realestate.service.PropertyService;
import com.realestate.realestate.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuthController {

    private final UserService userService;
    private final PropertyService propertyService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserService userService,
                          PropertyService propertyService,
                          UserRepository userRepository) {
        this.userService     = userService;
        this.propertyService = propertyService;
        this.userRepository  = userRepository;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult
    ) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.user", "Passwords do not match");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.user", "Username already taken");
        }
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        userService.register(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value="error", required=false) String error,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Exception ex = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            model.addAttribute("error", ex != null ? ex.getMessage() : "Login failed");
        }
        return "login";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails,
                          Model model) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        List<Property> properties = propertyService.getPropertiesByUser(user);
        model.addAttribute("username", username);
        model.addAttribute("properties", properties);
        return "profile";
    }
}