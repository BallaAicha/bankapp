package org.etutoria.accounts.service.client;

import org.etutoria.accounts.dto.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loans", url = "${loans.service.url}", fallback = LoansFallback.class)
public interface LoansFeignClient {
    @GetMapping(value = "/api/v1/loans/loan/fetch", consumes = "application/json")
    ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam String mobileNumber);
}