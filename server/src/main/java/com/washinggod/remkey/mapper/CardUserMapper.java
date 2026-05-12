package com.washinggod.remkey.mapper;

import com.washinggod.remkey.dto.response.CardUserResponse;
import com.washinggod.remkey.entity.CardUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardUserMapper {

  @Mapping(target = "language", ignore = true)
  @Mapping(target = "topic", ignore = true)
  @Mapping(target = "mainImage", ignore = true)
  CardUserResponse toCardUserResponse(CardUser cardUser);
}
