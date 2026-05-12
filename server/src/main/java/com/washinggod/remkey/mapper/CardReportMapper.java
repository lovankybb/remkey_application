package com.washinggod.remkey.mapper;


import com.washinggod.remkey.dto.request.CardReportCreationRequest;
import com.washinggod.remkey.dto.response.CardReportResponse;
import com.washinggod.remkey.entity.CardReport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardReportMapper {

    public CardReport toCardReport(CardReportCreationRequest request);

    public CardReportResponse toCardReportResponse(CardReport cardReport);
}
