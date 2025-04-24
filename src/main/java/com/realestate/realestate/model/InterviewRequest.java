package com.realestate.realestate.model;

import jakarta.persistence.*;

@Entity
public class InterviewRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    @Lob
    private String message;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    // constructors, getters, setters
    public InterviewRequest() {}
    public InterviewRequest(String name, String email, String phone, String message, Property property) {
        this.name = name; this.email = email; this.phone = phone; this.message = message; this.property = property;
    }
    // ... getters/setters omitted for brevity
}
