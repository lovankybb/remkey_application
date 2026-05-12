package com.washinggod.remkey.controller;

import com.washinggod.remkey.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileController {

  @Autowired private FileService fileService;

  @GetMapping("/images/{imageName}")
  public ResponseEntity<Resource> getImage(@PathVariable("imageName") String imagesName) {

    Resource resource = fileService.getImages(imagesName);

    return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
  }
}
