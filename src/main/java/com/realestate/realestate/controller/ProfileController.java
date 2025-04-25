package com.realestate.realestate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import com.realestate.realestate.model.User;
import com.realestate.realestate.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

  private final UserService userService;  // your service to load & update user

  public ProfileController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/settings")
  public String settingsForm(@AuthenticationPrincipal UserDetails ud, Model model) {
    User user = userService.findByUsername(ud.getUsername());
    model.addAttribute("user", user);
    return "profile-settings";
  }

  @PostMapping("/settings")
  public String saveSettings(
      @AuthenticationPrincipal UserDetails ud,
      @ModelAttribute User userForm,
      @RequestParam String currentPassword,
      @RequestParam String newPassword,
      @RequestParam String confirmPassword,
      Model model) {

    User me = userService.findByUsername(ud.getUsername());
    // verify current password
    if (!userService.checkPassword(me, currentPassword)) {
      model.addAttribute("error", "Current password is incorrect");
      model.addAttribute("user", me);
      return "profile-settings";
    }
    if (!newPassword.equals(confirmPassword)) {
      model.addAttribute("error", "New password and confirmation do not match");
      model.addAttribute("user", me);
      return "profile-settings";
    }

    // update fields
    me.setUsername(userForm.getUsername());
    me.setEmail(userForm.getEmail());
    userService.changePassword(me, newPassword);
    userService.updateProfile(me);

    model.addAttribute("success", true);
    model.addAttribute("user", me);
    return "profile-settings";
  }
}
