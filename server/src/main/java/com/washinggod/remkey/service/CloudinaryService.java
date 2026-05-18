package com.washinggod.remkey.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {

    Cloudinary cloudinary;

    public Map<String, String> duplicateImage(String imageUrl) {

        Map params = ObjectUtils.asMap(
                "resource_type", "image"
        );

        try {
            Map res = cloudinary.uploader().upload(imageUrl, params);


            Map<String, String> result = new HashMap<>();
            result.put("secure_url", res.get("secure_url").toString());
            result.put("public_id", res.get("public_id").toString());

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.CLOUDINARY_ERROR);
        }
    }

    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new AppException(ErrorCode.CLOUDINARY_ERROR);
        }
    }
}
