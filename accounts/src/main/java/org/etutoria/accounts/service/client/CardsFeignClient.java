package org.etutoria.accounts.service.client;

import org.etutoria.accounts.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards", url = "${cards.service.url}",fallback = CardsFallback.class)
public interface CardsFeignClient {
    @GetMapping(value = "/api/v1/cards/card/fetch", consumes = "application/json")
    ResponseEntity<CardsDto> fetchCardDetails(@RequestParam String mobileNumber);
}