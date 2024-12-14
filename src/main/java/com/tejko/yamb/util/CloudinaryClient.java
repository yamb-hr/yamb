package com.tejko.yamb.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

@Component
public class CloudinaryClient {

    @Resource
    private Cloudinary cloudinary;

    private static final double MAX_FILE_SIZE = 520 * 1024;
    private static final String[] ALLOWED_MIME_TYPES = {"image/jpeg", "image/png"};

    public Map uploadFile(MultipartFile file, String folderName, String oldPublicId) {
        try {
            validateFile(file);
            if (oldPublicId != null && !oldPublicId.isEmpty()) {
                deleteFile(oldPublicId);
            }
    
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName);
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            return uploadedFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map deleteFile(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, new HashMap<>());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while deleting file with public_id: " + publicId, e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum limit of 520 KB.");
        }

        String mimeType = file.getContentType();
        boolean allowed = false;
        for (String allowedType : ALLOWED_MIME_TYPES) {
            if (allowedType.equalsIgnoreCase(mimeType)) {
                allowed = true;
                break;
            }
        }
        if (!allowed) {
            throw new IllegalArgumentException("Unsupported file type: " + mimeType);
        }

        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.matches("[a-zA-Z0-9._-]+")) {
            throw new IllegalArgumentException("File name contains invalid characters.");
        }
    }
}
