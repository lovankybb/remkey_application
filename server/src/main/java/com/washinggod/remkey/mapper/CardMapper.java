package com.washinggod.remkey.mapper;

import com.washinggod.remkey.dto.request.CardCreationRequest;
import com.washinggod.remkey.dto.request.CardUpdateRequest;
import com.washinggod.remkey.dto.response.CardResponse;
import com.washinggod.remkey.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CardMapper {

  @Mapping(target = "language", ignore = true)
  @Mapping(target = "topic", ignore = true)
  Card toCard(CardCreationRequest request);

  @Mapping(target = "language", ignore = true)
  @Mapping(target = "topic", ignore = true)
  @Mapping(target = "mainImage", ignore = true)
  CardResponse toCardResponse(Card card);

  @Mapping(target = "language", ignore = true)
  @Mapping(target = "topic", ignore = true)
  void updateCard(@MappingTarget Card card, CardUpdateRequest request);
}
