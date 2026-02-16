package it.lucianofilippucci.tcgarkhive.API.V1.controller;

import it.lucianofilippucci.tcgarkhive.API.V1.DTO.Cards.CardDTO;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.HttpResponse;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.CardRarityNotFoundException;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.TCGNotFoundException;
import it.lucianofilippucci.tcgarkhive.services.OPTCGService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.smartcardio.Card;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/optcg/")
public class OnePieceTCGController {

    private final OPTCGService optcgService;

    public OnePieceTCGController(OPTCGService optcgService) {
        this.optcgService = optcgService;
    }

    @GetMapping("card/{card-id}")
    public ResponseEntity<HttpResponse<CardDTO>> getCard(@PathVariable("card-id") Long cardId) {
        return ResponseEntity.ok(
                HttpResponse.<CardDTO>builder()
                        .timestamp(LocalDateTime.now())
                        .data(this.optcgService.getCardFromId(cardId))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
