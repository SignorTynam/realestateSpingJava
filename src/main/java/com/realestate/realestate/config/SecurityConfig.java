package com.realestate.realestate.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService uds, PasswordEncoder pe) {
        this.userDetailsService = uds;
        this.passwordEncoder    = pe;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .authenticationProvider(authenticationProvider())
          .authorizeHttpRequests(auth -> auth
              // allow anyone to view home, main, static resources, signup, errors...
              .requestMatchers(
                  "/",            // root
                  "/main",        // your main page
                  "/signup",
                  "/css/**",
                  "/error",      
                  "/error/**",
                  "/upload/**"
              ).permitAll()
              // allow ANYONE (anonymous too) to GET properties and property‐details
              .requestMatchers(HttpMethod.GET, "/properties/**").permitAll()
              // everything else (POST/PUT/DE3LETE) requires login
              .anyRequest().authenticated()
          )
          .formLogin(form -> form
              .loginPage("/login")
              .defaultSuccessUrl("/profile", true)
              .failureUrl("/login-error")
              .permitAll()
          )
          .logout(logout -> logout
              .logoutUrl("/logout")
              .logoutSuccessUrl("/login")
              .invalidateHttpSession(true)
              .deleteCookies("JSESSIONID")
              .addLogoutHandler((request, response, authentication) ->
                  logger.info("User logged out: {}",
                              authentication != null ? authentication.getName() : "Anonymous"))
              .permitAll()
          );
        return http.build();
    }
}
