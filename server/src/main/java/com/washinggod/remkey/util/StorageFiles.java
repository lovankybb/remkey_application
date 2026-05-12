package com.washinggod.remkey.util;

import com.washinggod.remkey.configuration.properties.StaticLocation;
import com.washinggod.remkey.enums.FileType;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageFiles {

  @Autowired private StaticLocation staticLocation;

  public String storageFile(MultipartFile multipartFile, FileType type) {

    String path = this.getLocationByType(type);
    String fileName = UUID.randomUUID().toString();

    File file = new File(path + fileName);

    try {
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(multipartFile.getBytes());
      fileOutputStream.close();
    } catch (IOException exception) {
      throw new AppException(ErrorCode.SAVE_FILE_FAILED);
    }
    return fileName;
  }

  public String duplicateFile(String url, FileType type) {

    try {
      String directory = this.getLocationByType(FileType.IMAGE);
      String newUrl = UUID.randomUUID().toString();

      Path sourcePath = Path.of(directory, url);
      Path targetPath = Path.of(directory, newUrl);

      if (Files.exists(sourcePath)) {
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        return newUrl;
      } else {
        throw new AppException(ErrorCode.FILE_NOT_FOUND);
      }
    } catch (IOException e) {
      throw new AppException(ErrorCode.FILE_NOT_FOUND);
    }
  }

  public void deleteFile(String fileName, FileType type) {

    String path = this.getLocationByType(type);
    File file = new File(path + fileName);
    System.out.println("Delete file" + path + fileName);

    file.delete();
  }

  private String getLocationByType(FileType type) {
    return switch (type) {
      case IMAGE -> staticLocation.getCardImageLocation();
      case SOUND -> staticLocation.getCardSoundLocation();
    };
  }
}
