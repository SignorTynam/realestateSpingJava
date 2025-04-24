package com.realestate.realestate.model;

import jakarta.persistence.*;

@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    public Photo() {}
    public Photo(String url, Property property) {
        this.url = url;
        this.property = property;
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }
}
