package uz.pdp.appbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.appbank.payload.ApiResponse;
import uz.pdp.appbank.payload.TransferDto;
import uz.pdp.appbank.service.TransferService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {


    @Autowired
    TransferService transferService;


    @PostMapping
    @PreAuthorize(value = "hasRole('CLIENT')")
    public HttpEntity<?> transfer(@Valid @RequestBody TransferDto transferDto) {
        ApiResponse apiResponse = transferService.transferMoney(transferDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


}
