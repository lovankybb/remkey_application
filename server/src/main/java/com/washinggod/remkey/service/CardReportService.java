package com.washinggod.remkey.service;


import com.washinggod.remkey.dto.request.CardReportCreationRequest;
import com.washinggod.remkey.dto.response.CardReportResponse;
import com.washinggod.remkey.entity.Card;
import com.washinggod.remkey.entity.CardReport;
import com.washinggod.remkey.enums.FileType;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.CardReportMapper;
import com.washinggod.remkey.repository.CardReportRepository;
import com.washinggod.remkey.repository.CardRepository;
import com.washinggod.remkey.util.StorageFiles;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class CardReportService {


    CardReportRepository cardReportRepository;

    CardReportMapper cardReportMapper;

    CardRepository cardRepository;

    StorageFiles storageFiles;

    public List<CardReportResponse> getAll() {

        return this.cardReportRepository.findAll()
                .stream().map(this.cardReportMapper::toCardReportResponse).toList();
    }

    public CardReportResponse create(CardReportCreationRequest request) {

        CardReport cardReport = cardReportMapper.toCardReport(request);

        cardReport.setCreatedAt(LocalDateTime.now());
        cardReport = this.cardReportRepository.save(cardReport);
        return this.cardReportMapper.toCardReportResponse(cardReport);
    }


    public void acceptHandlingReport(Long id) {

        CardReport cardReport = this.getCardReportById(id);
        cardReportRepository.delete(cardReport);
        Optional<Card> cardOptional = cardRepository.findById(cardReport.getCardId());
        cardOptional.ifPresent(card -> {

            if(card.getCardImages().isEmpty())log.info("cardImage is Empty =============");
            card.getCardImages().forEach(image -> {
                log.info("Image: === {}", image.getUrl());
                storageFiles.deleteFile(image.getUrl(), FileType.IMAGE);
            });
            cardRepository.delete(card);
        });

    }

    public void delete(Long id) {
        cardReportRepository.delete(this.getCardReportById(id));
    }

    private CardReport getCardReportById(Long id) {
        return this.cardReportRepository.findById(id).orElseThrow(() -> {
            return new AppException(ErrorCode.REPORT_NOT_EXIST);
        });
    }

}
