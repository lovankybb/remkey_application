package com.washinggod.remkey.service;

import com.washinggod.remkey.dto.request.FindTopicByNameRequest;
import com.washinggod.remkey.dto.request.TopicCreationRequest;
import com.washinggod.remkey.dto.request.TopicUpdateRequest;
import com.washinggod.remkey.dto.response.TopicResponse;
import com.washinggod.remkey.entity.Topic;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.TopicMapper;
import com.washinggod.remkey.repository.TopicRepository;
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
public class TopicService {

  TopicRepository topicRepository;

  TopicMapper topicMapper;

  public TopicResponse create(TopicCreationRequest request) {

    Topic topic = topicMapper.toTopic(request);

    return this.save(topic);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public TopicResponse update(Long id, TopicUpdateRequest request) {

    Topic topic = this.getTopicById(id);
    topicMapper.updateTopic(topic, request);

    return this.save(topic);
  }

  public TopicResponse getTopicResponseByName(FindTopicByNameRequest request) {
    return this.topicMapper.toTopicResponse(this.getTopicByName(request.getName()));
  }

  public TopicResponse getTopicResponseById(Long id) {
    return topicMapper.toTopicResponse(this.getTopicById(id));
  }

  public List<TopicResponse> getAll() {

    return topicRepository.findAll().stream().map(topicMapper::toTopicResponse).toList();
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void delete(Long id) {
    Topic topic = this.getTopicById(id);
    topicRepository.delete(topic);
  }

  private TopicResponse save(Topic topic) {
    try {
      return topicMapper.toTopicResponse(topicRepository.save(topic));
    } catch (DataIntegrityViolationException exception) {
      throw new AppException(ErrorCode.SAVE_TOPIC_FAILED);
    }
  }

  private Topic getTopicById(Long id) {

    return topicRepository
        .findById(id)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.TOPIC_NOT_EXIST);
            });
  }

  private Topic getTopicByName(String name) {
    return topicRepository
        .findByName(name)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.TOPIC_NOT_EXIST);
            });
  }
}
