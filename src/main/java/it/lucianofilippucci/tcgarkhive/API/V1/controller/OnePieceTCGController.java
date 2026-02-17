package it.lucianofilippucci.tcgarkhive.API.V1.controller;

import it.lucianofilippucci.tcgarkhive.API.V1.DTO.Cards.CardDTO;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.HttpResponse;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.TCGListDTO;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.TCGListEntryDTO;
import it.lucianofilippucci.tcgarkhive.configuration.security.AuthContext;
import it.lucianofilippucci.tcgarkhive.configuration.security.authentication.JwtUtil;
import it.lucianofilippucci.tcgarkhive.entity.TCGListEntry;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.CardRarityNotFoundException;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.TCGNotFoundException;
import it.lucianofilippucci.tcgarkhive.services.OPTCGService;
import jakarta.validation.Valid;
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
    private final AuthContext authContext;
    private final JwtUtil jwtUtil;


    public OnePieceTCGController(OPTCGService optcgService, AuthContext authContext, JwtUtil jwtUtil) {
        this.optcgService = optcgService;
        this.authContext = authContext;
        this.jwtUtil = jwtUtil;
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

    @PostMapping("user/list/new")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpResponse<TCGListDTO>> newList(@RequestBody @Valid TCGListDTO listDTO) {
        String username = this.jwtUtil.getUsernameFromJwtToken(this.authContext.getJwt());

        return ResponseEntity.ok(
          HttpResponse.<TCGListDTO>builder()
                  .timestamp(LocalDateTime.now())
                  .data(this.optcgService.newList(listDTO, username))
                  .statusCode(HttpStatus.CREATED.value())
                  .build()
        );
    }

    @PostMapping("user/list/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpResponse<TCGListDTO>> addCardToList(@RequestBody @Valid TCGListEntryDTO entryDTO) {
        return ResponseEntity.ok(
                HttpResponse.<TCGListDTO>builder()
                        .timestamp(LocalDateTime.now())
                        .data(this.optcgService.addCardToList(entryDTO, this.jwtUtil.getUsernameFromJwtToken(this.authContext.getJwt())))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("user/list/{list-id}/card/edit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpResponse<TCGListDTO>> removeCardFromList(@PathVariable("list-id") Long listId, @RequestParam("entryID") Long entryID, @RequestParam("quantity") int quantity) {
        return ResponseEntity.ok(
                HttpResponse.<TCGListDTO>builder()
                        .timestamp(LocalDateTime.now())
                        .data(this.optcgService.editCardEntry(entryID, this.jwtUtil.getUsernameFromJwtToken(this.authContext.getJwt()), listId, quantity ))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("user/list/{list-id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpResponse<List<TCGListEntryDTO>>> getList(@PathVariable("list-id") Long listID) {
        return ResponseEntity.ok(
                HttpResponse.<List<TCGListEntryDTO>>builder()
                        .timestamp(LocalDateTime.now())
                        .data(this.optcgService.getListCards(listID, this.jwtUtil.getUsernameFromJwtToken(this.authContext.getJwt())))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

}
