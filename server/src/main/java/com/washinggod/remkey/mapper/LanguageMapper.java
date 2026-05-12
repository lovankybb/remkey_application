package com.washinggod.remkey.mapper;

import com.washinggod.remkey.dto.request.LanguageCreationRequest;
import com.washinggod.remkey.dto.request.LanguageUpdateRequest;
import com.washinggod.remkey.dto.response.LanguageResponse;
import com.washinggod.remkey.entity.Language;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LanguageMapper {

  LanguageResponse toLanguageResponse(Language language);

  Language toLanguage(LanguageCreationRequest request);

  void updateLanguage(@MappingTarget Language language, LanguageUpdateRequest request);
}
