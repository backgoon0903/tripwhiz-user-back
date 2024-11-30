package com.tripwhiz.tripwhizuserback.luggage.controller;

import com.tripwhiz.tripwhizuserback.luggage.dto.LuggageDTO;
import com.tripwhiz.tripwhizuserback.luggage.service.LuggageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/luggage")
public class LuggageController {

    @Autowired
    private LuggageService luggageService;

    @PostMapping("/saveLuggage")
    public ResponseEntity<String> saveLuggage(@RequestBody LuggageDTO luggageDTO) {
        luggageService.saveLuggage(luggageDTO.getStartPoint(), luggageDTO.getEndPoint());
        return ResponseEntity.ok("Luggage points saved successfully");
    }
}
