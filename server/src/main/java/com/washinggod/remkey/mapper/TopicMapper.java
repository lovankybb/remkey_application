package com.washinggod.remkey.mapper;

import com.washinggod.remkey.dto.request.TopicCreationRequest;
import com.washinggod.remkey.dto.request.TopicUpdateRequest;
import com.washinggod.remkey.dto.response.TopicResponse;
import com.washinggod.remkey.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TopicMapper {

  Topic toTopic(TopicCreationRequest topic);

  TopicResponse toTopicResponse(Topic topic);

  void updateTopic(@MappingTarget Topic topic, TopicUpdateRequest request);
}
