package com.washinggod.remkey.mapper;

import com.washinggod.remkey.dto.request.SubscriptionPackageCreationRequest;
import com.washinggod.remkey.dto.request.SubscriptionPackageUpdateRequest;
import com.washinggod.remkey.dto.response.SubscriptionPackageResponse;
import com.washinggod.remkey.entity.SubscriptionPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubscriptionPackageMapper {

  @Mapping(target = "name", ignore = true)
  SubscriptionPackage toSubscriptionPackage(SubscriptionPackageCreationRequest request);

  SubscriptionPackageResponse toSubscriptionPackageResponse(
      SubscriptionPackage subscriptionPackage);

  @Mapping(target = "name", ignore = true)
  void updateSubscriptionPackage(
      @MappingTarget SubscriptionPackage subscriptionPackage,
      SubscriptionPackageUpdateRequest request);
}
