package com.washinggod.remkey.mapper;

import com.washinggod.remkey.dto.response.OrderResponse;
import com.washinggod.remkey.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  @Mapping(target = "packages", ignore = true)
  OrderResponse toOrderResponse(Order order);
}
