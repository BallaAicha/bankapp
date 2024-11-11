package org.etutoria.accounts.service.client;

import org.etutoria.accounts.dto.LoansDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallback implements LoansFeignClient{

    @Override
    public ResponseEntity<LoansDto> fetchLoanDetails(String mobileNumber) {
        return null;
    }
}
