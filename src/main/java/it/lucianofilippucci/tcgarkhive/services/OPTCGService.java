package it.lucianofilippucci.tcgarkhive.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.Cards.CardDTO;
import it.lucianofilippucci.tcgarkhive.entity.CardEntity;
import it.lucianofilippucci.tcgarkhive.entity.CardRarityEntity;
import it.lucianofilippucci.tcgarkhive.entity.TCGEntity;
import it.lucianofilippucci.tcgarkhive.helpers.CardDetails.OnePieceTCG;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.CardRarityNotFoundException;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.TCGNotFoundException;
import it.lucianofilippucci.tcgarkhive.repository.CardRarityRepository;
import it.lucianofilippucci.tcgarkhive.repository.TCGRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

        try {
            cardEntity.setCardDetails(this.objectMapper.writeValueAsString(card.getCardDetails()));
        } catch (Exception e) {
            throw new RuntimeException("Error while writing card details");
        }


        return this.fromEntity(this.cardService.createCard(cardEntity, tcg));
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
    public List<CardDTO> getCardFromId(Long cardId) {
        Optional<TCGEntity> temp = this.tcgRepository.findByCode("OPTCG");
        if(temp.isEmpty()) throw new TCGNotFoundException("TCG not found");
        TCGEntity tcg = temp.get();

        List<CardDTO> cards = new ArrayList<>();
        for(CardEntity entity: this.cardService.getCard(cardId, tcg)) {
            cards.add(this.fromEntity(entity));
        }
        return cards;

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
