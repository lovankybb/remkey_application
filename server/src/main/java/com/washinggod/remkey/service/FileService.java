package com.washinggod.remkey.service;

import com.washinggod.remkey.configuration.properties.StaticLocation;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {

  StaticLocation staticLocation;

  public Resource getImages(String imageName) {

    Path path = Paths.get(staticLocation.getCardImageLocation()).resolve(imageName).normalize();
    try {
      return new UrlResource(path.toUri());
    } catch (MalformedURLException e) {
      throw new AppException(ErrorCode.FILE_NOT_FOUND);
    }
  }
}
