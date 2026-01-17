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

    @PostMapping("new-card")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpResponse<CardDTO>> newCard(@RequestBody CardDTO CardDTO) {
        String message = "";
        HttpStatus status = HttpStatus.CREATED;
        int errorCode = 0;

        CardDTO card = null;

        try {
            card = this.optcgService.createCard(CardDTO);
        } catch (TCGNotFoundException e) {
            message = e.getMessage();
            status = HttpStatus.NOT_FOUND;
            errorCode = 100;
        }  catch (CardRarityNotFoundException e) {
            message = e.getMessage();
            status = HttpStatus.NOT_FOUND;
            errorCode = 101;
        } catch (RuntimeException e) {
            message = "Server Error.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorCode = status.value();
        }

        return ResponseEntity.ok(
                HttpResponse.<CardDTO>builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .message(message)
                        .data(card)
                        .build()
        );
    }

    @GetMapping("card/{card-id}")
    public ResponseEntity<HttpResponse<CardDTO>> getCard(@PathVariable Long cardId) {
        String message = "";
        HttpStatus status = HttpStatus.OK;
        List<CardDTO> card = this.optcgService.getCardFromId(cardId);

        return ResponseEntity.ok(
                HttpResponse.<CardDTO>builder()
                        .timestamp(LocalDateTime.now())
                        .data(card.getFirst())
                        .statusCode(HttpStatus.OK.value())
                        .message(card.isEmpty() ? "No Card Found" : "")
                        .errorCode(card.isEmpty() ? 110 : 0)
                        .build()
        );


    }
}
