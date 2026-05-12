package com.washinggod.remkey.controller;

import com.washinggod.remkey.dto.request.CardReportCreationRequest;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.CardReportResponse;
import com.washinggod.remkey.service.CardReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private CardReportService cardReportService;


    @GetMapping("/cards")
    public ApiResponse<List<CardReportResponse>> getAllCardReports(){

        ApiResponse<List<CardReportResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setBody(cardReportService.getAll());
        return apiResponse;
    }


    @PostMapping("/cards")
    public ApiResponse<CardReportResponse> createCardReport(@RequestBody CardReportCreationRequest request){

        ApiResponse<CardReportResponse> apiResponse = new ApiResponse<>();
        apiResponse.setBody(cardReportService.create(request));
        return apiResponse;
    }

    @PostMapping("/cards/{id}")
    public ApiResponse<Void> acceptHandlingCardReport(@PathVariable("id") Long cardReportId){
        cardReportService.acceptHandlingReport(cardReportId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Report has been handled.");
        return apiResponse;
    }

    @DeleteMapping("/cards/{id}")
    public ApiResponse<Void> deleteCardReport(@PathVariable Long id){

        cardReportService.delete(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Delete report successfully.");
        return apiResponse;
    }
}
