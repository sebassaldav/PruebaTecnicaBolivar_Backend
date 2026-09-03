package com.jssv.globalinvoice.controller;

import com.jssv.globalinvoice.service.integration.NumberToWordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/test/soap")
@RequiredArgsConstructor
public class SoapTestController {

    private final NumberToWordsService numberToWordsService;

    @GetMapping("/number-to-words")
    public ResponseEntity<String> numberToWords(
            @RequestParam BigDecimal value) {

        String result =
                numberToWordsService.convert(value);

        return ResponseEntity.ok(result);
    }
}