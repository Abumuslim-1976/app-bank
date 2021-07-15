package uz.pdp.appbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appbank.payload.ApiResponse;
import uz.pdp.appbank.payload.CardDto;
import uz.pdp.appbank.service.CardService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/card")
public class CardController {


    @Autowired
    CardService cardService;


    //    ---------- userga karta biriktirish -----------
    @PostMapping
    @PreAuthorize(value = "hasRole('CARD_WORKER')")
    public HttpEntity<?> createCard(@Valid @RequestBody CardDto cardDto) {
        ApiResponse apiResponse = cardService.createCardToUser(cardDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    //    -----------kartani blockdan chiqarish -------------
    @GetMapping("/block/{id}")
    @PreAuthorize(value = "hasRole('CARD_WORKER')")
    public HttpEntity<?> unBlock(@Valid @PathVariable UUID id) {
        ApiResponse apiResponse = cardService.unBlock(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }



}
