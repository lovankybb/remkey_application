package com.washinggod.remkey.service;

import com.washinggod.remkey.dto.request.FindLanguageByNameRequest;
import com.washinggod.remkey.dto.request.LanguageCreationRequest;
import com.washinggod.remkey.dto.request.LanguageUpdateRequest;
import com.washinggod.remkey.dto.response.LanguageResponse;
import com.washinggod.remkey.entity.Language;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.LanguageMapper;
import com.washinggod.remkey.repository.LanguageRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LanguageService {

  LanguageRepository languageRepository;

  LanguageMapper languageMapper;

  @PreAuthorize("hasRole('ADMIN')")
  public LanguageResponse create(LanguageCreationRequest request) {

    Language language = languageMapper.toLanguage(request);

    return this.save(language);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public LanguageResponse update(Long id, LanguageUpdateRequest request) {

    Language language = this.getLanguageById(id);
    languageMapper.updateLanguage(language, request);

    return this.save(language);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void delete(Long id) {

    Language language = this.getLanguageById(id);
    languageRepository.delete(language);
  }

  public List<LanguageResponse> getAll() {
    return languageRepository.findAll().stream().map(languageMapper::toLanguageResponse).toList();
  }

  public LanguageResponse getLanguageResponseByName(FindLanguageByNameRequest request) {

    return this.languageMapper.toLanguageResponse(this.getLanguageByName(request.getName()));
  }

  public LanguageResponse geLanguageResponseById(Long id) {

    return languageMapper.toLanguageResponse(this.getLanguageById(id));
  }

  private LanguageResponse save(Language language) {
    try {
      return languageMapper.toLanguageResponse(languageRepository.save(language));
    } catch (DataIntegrityViolationException exception) {
      throw new AppException(ErrorCode.SAVE_LANGUAGE_FAILED);
    }
  }

  private Language getLanguageById(Long id) {
    return languageRepository
        .findById(id)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.LANGUAGE_NOT_EXIST);
            });
  }

  private Language getLanguageByName(String name) {
    return languageRepository
        .findByName(name)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.LANGUAGE_NOT_EXIST);
            });
  }
}
