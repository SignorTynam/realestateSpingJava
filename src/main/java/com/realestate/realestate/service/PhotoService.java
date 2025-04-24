package com.realestate.realestate.service;

import com.realestate.realestate.model.Photo;
import com.realestate.realestate.model.Property;
import com.realestate.realestate.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PhotoService {

    private final PhotoRepository photoRepo;

    // injected from application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;

    public PhotoService(PhotoRepository photoRepo) {
        this.photoRepo = photoRepo;
    }

    public Photo savePhoto(Property property, MultipartFile file) throws IOException {
        // ensure folder exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // generate unique filename
        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID() + ext;

        // save file to disk
        Path filePath = uploadPath.resolve(filename);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // record URL (served via /uploads/**)
        String url = "/upload/" + filename;
        Photo photo = new Photo(url, property);
        return photoRepo.save(photo);
    }

    public List<Photo> getPhotosForProperty(Long propertyId) {
        return photoRepo.findByPropertyId(propertyId);
    }

    /** Delete the photo record (and you may manually delete the file if desired) */
    public void deletePhoto(Long photoId) {
        if (!photoRepo.existsById(photoId)) {
            throw new NoSuchElementException("Photo not found: " + photoId);
        }
        photoRepo.deleteById(photoId);
    }

}