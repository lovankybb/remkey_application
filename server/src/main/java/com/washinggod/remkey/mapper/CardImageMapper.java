package com.washinggod.remkey.mapper;

import com.washinggod.remkey.dto.response.CardImageResponse;
import com.washinggod.remkey.entity.CardImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardImageMapper {

  @Mapping(target = "cardId", ignore = true)
  @Mapping(target = "cardUserId", ignore = true)
  CardImageResponse toCardImageResponse(CardImage cardImage);
}
