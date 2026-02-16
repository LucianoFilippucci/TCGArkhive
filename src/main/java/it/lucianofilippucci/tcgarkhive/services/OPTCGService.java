package it.lucianofilippucci.tcgarkhive.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.Cards.CardDTO;
import it.lucianofilippucci.tcgarkhive.entity.CardEntity;
import it.lucianofilippucci.tcgarkhive.entity.CardRarityEntity;
import it.lucianofilippucci.tcgarkhive.entity.TCGEntity;
import it.lucianofilippucci.tcgarkhive.helpers.CardDetails.CardDetails;
import it.lucianofilippucci.tcgarkhive.helpers.CardDetails.OnePieceTCG;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.CardNotFoundException;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.CardRarityNotFoundException;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.InternalErrorException;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.TCGNotFoundException;
import it.lucianofilippucci.tcgarkhive.repository.CardRarityRepository;
import it.lucianofilippucci.tcgarkhive.repository.TCGRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class OPTCGService {
    private final CardService cardService;
    private final TCGRepository tcgRepository;
    private final CardRarityRepository cardRarityRepository;
    private final ObjectMapper objectMapper;

    public OPTCGService(CardService cardService, TCGRepository tcgRepository, CardRarityRepository cardRarityRepository, ObjectMapper objectMapper) {
        this.cardService = cardService;
        this.tcgRepository = tcgRepository;
        this.cardRarityRepository = cardRarityRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public CardDTO createCard(CardDTO card) throws TCGNotFoundException, CardRarityNotFoundException, RuntimeException {
        Optional<TCGEntity> tmp = this.tcgRepository.findById(card.getTcgId());
        if(tmp.isEmpty()) throw new TCGNotFoundException("TCG not found");
        TCGEntity tcg = tmp.get();
        Optional<CardRarityEntity> tmp2 = this.cardRarityRepository.findById(card.getCardRarityId());
        if(tmp2.isEmpty()) throw new CardRarityNotFoundException("Rarity not found");
        CardRarityEntity cardRarity = tmp2.get();

        CardEntity cardEntity = new CardEntity();
        cardEntity.setCardName(card.getCardName());
        cardEntity.setExternalId(card.getExternalId());
        cardEntity.setRarity(cardRarity);
        cardEntity.setSetCode(card.getSetCode());
        cardEntity.setReleaseDate(card.getReleaseDate());
        cardEntity.setCardSecondaryName(card.getSecondaryCardName());
        cardEntity.setTcg(tcg);
        cardEntity.setImageURL(card.getImageUrl());

        cardEntity.setCardDetails(this.FromDetailsToString(card.getCardDetails()));

        return this.fromEntity(this.cardService.createCard(cardEntity, tcg));
    }

    @Transactional
    public CardDTO editCard(CardDTO dto) {
        Optional<TCGEntity> tmp = this.tcgRepository.findById(dto.getTcgId());
        if(tmp.isEmpty()) throw new TCGNotFoundException("");
        Optional<CardEntity> tmp2 = this.cardService.getCard(dto.getCardId(), tmp.get());
        if(tmp2.isEmpty()) throw new CardNotFoundException("");

        Optional<CardRarityEntity> tmp3 = this.cardRarityRepository.findById(dto.getCardRarityId());
        if(tmp3.isEmpty()) throw new CardRarityNotFoundException("");

        CardEntity card = tmp2.get();

        card.setCardDetails(this.FromDetailsToString(dto.getCardDetails()));
        card.setCardName(dto.getCardName());
        card.setCardSecondaryName(dto.getSecondaryCardName());
        card.setRarity(tmp3.get());
        card.setExternalId(dto.getExternalId());
        card.setImageURL(dto.getImageUrl());
        card.setReleaseDate(dto.getReleaseDate());
        card.setSetCode(dto.getSetCode());

        return this.fromEntity(this.cardService.saveCard(card));
    }


    @Transactional
    public List<CardDTO> getCardsFromName(String name) {
        Optional<TCGEntity> temp = this.tcgRepository.findByCode("OPTCG");
        if(temp.isEmpty()) throw new TCGNotFoundException("TCG not found");
        TCGEntity tcg = temp.get();

        List<CardDTO> cards = new ArrayList<>();

        for(CardEntity entity : this.cardService.getFromName(name, tcg)) {
            cards.add(this.fromEntity(entity));
        }

        return cards;

    }

    @Transactional
    public CardDTO getCardFromId(Long cardId) {
        Optional<TCGEntity> temp = this.tcgRepository.findByCode("OPTCG");
        if(temp.isEmpty()) throw new TCGNotFoundException("TCG not found");
        TCGEntity tcg = temp.get();

        Optional<CardEntity> tmp = this.cardService.getCard(cardId, tcg);
        if(tmp.isEmpty()) throw new CardNotFoundException("");

        return this.fromEntity(tmp.get());

    }

    private String FromDetailsToString(CardDetails details) {
        try {
            return this.objectMapper.writeValueAsString(details);
        } catch (Exception e ) {
            throw new InternalErrorException(Arrays.toString(e.getStackTrace()));
        }
    }

    private CardDTO fromEntity(CardEntity cardEntity) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setCardName(cardEntity.getCardName());
        cardDTO.setExternalId(cardEntity.getExternalId());
        cardDTO.setSetCode(cardEntity.getSetCode());
        cardDTO.setReleaseDate(cardEntity.getReleaseDate());
        cardDTO.setImageUrl(cardEntity.getImageURL());
        cardDTO.setCardRarityId(cardEntity.getRarity().getId());
        cardDTO.setSecondaryCardName(cardEntity.getCardSecondaryName());
        cardDTO.setTcgId(cardEntity.getTcg().getId());

        try {
            cardDTO.setCardDetails(this.objectMapper.readValue(cardEntity.getCardDetails(), OnePieceTCG.class));
        } catch (Exception e) {
            throw new RuntimeException("Error while writing card details");
        }
        return cardDTO;
    }
}
