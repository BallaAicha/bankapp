package org.etutoria.accounts.service.client;

import org.etutoria.accounts.dto.CardsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallback implements CardsFeignClient{

    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String mobileNumber) {
        return null;
    }

}