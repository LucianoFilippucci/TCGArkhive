package it.lucianofilippucci.tcgarkhive.API.V1.controller;

import it.lucianofilippucci.tcgarkhive.API.V1.DTO.Cards.CardDTO;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.HttpResponse;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.UserDTO;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.UserRolesEditRequest;
import it.lucianofilippucci.tcgarkhive.entity.RolesEntity;
import it.lucianofilippucci.tcgarkhive.services.AdministrationService;
import it.lucianofilippucci.tcgarkhive.services.OPTCGService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/admin/")
public class AdministrationController {
    private final AdministrationService administrationService;
    private final OPTCGService optcgService;


    public AdministrationController(AdministrationService administrationService, OPTCGService optcgService) {
        this.administrationService = administrationService;
        this.optcgService = optcgService;
    }

    @PostMapping("user/roles/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpResponse<Boolean>> editUserRoles(@RequestBody @Valid UserRolesEditRequest req) {
        return ResponseEntity.ok(
                HttpResponse.<Boolean>builder()
                        .timestamp(LocalDateTime.now())
                        .message("")
                        .data(this.administrationService.EditUserRoles(req))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }


    @GetMapping("user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpResponse<Set<UserDTO>>> fetchUserList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {

        Sort.Direction dir = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, limit, Sort.by(dir, sort));

        return ResponseEntity.ok(
                HttpResponse.<Set<UserDTO>>builder()
                        .timestamp(LocalDateTime.now())
                        .data(this.administrationService.fetchUserList(pageable))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("cards/new/op")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpResponse<CardDTO>> newCard(@RequestBody CardDTO CardDTO) {
        CardDTO card = null;
        card = this.optcgService.createCard(CardDTO);
        return ResponseEntity.ok(
                HttpResponse.<CardDTO>builder()
                        .timestamp(LocalDateTime.now())
                        .data(card)
                        .build()
        );
    }

    @PostMapping("cards/edit/op")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpResponse<CardDTO>> editCard(@RequestBody CardDTO cardDTO) {
        return ResponseEntity.ok(
                HttpResponse.<CardDTO>builder()
                        .timestamp(LocalDateTime.now())
                        .data(this.optcgService.editCard(cardDTO))
                        .build()
        );
    }
}


