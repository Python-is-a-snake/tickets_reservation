package com.trs.tickets.controller;

import com.trs.tickets.model.dto.RatingDto;
import com.trs.tickets.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    @PostMapping("/rate")
    public ResponseEntity<String> rate(@RequestBody RatingDto ratingDto) {
        ratingService.saveRating(ratingDto);
        return ResponseEntity.ok("Rating saved successfully");
    }
}
