package it.lucianofilippucci.tcgarkhive.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.lucianofilippucci.tcgarkhive.entity.CardEntity;
import it.lucianofilippucci.tcgarkhive.entity.TCGEntity;
import it.lucianofilippucci.tcgarkhive.helpers.CardDetails.OnePieceTCG;
import it.lucianofilippucci.tcgarkhive.helpers.enums.TCGCode;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.TCGNotFoundException;
import it.lucianofilippucci.tcgarkhive.repository.CardRarityRepository;
import it.lucianofilippucci.tcgarkhive.repository.CardRepository;
import it.lucianofilippucci.tcgarkhive.repository.TCGRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardRarityRepository cardRarityRepository;
    private final TCGRepository tcgRepository;
    private final ObjectMapper objectMapper;


    public CardService(CardRepository cardRepository, CardRarityRepository cardRarityRepository, TCGRepository tcgRepository, ObjectMapper objectMapper) {
        this.cardRepository = cardRepository;
        this.cardRarityRepository = cardRarityRepository;
        this.tcgRepository = tcgRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public CardEntity createCard(CardEntity card, TCGEntity tcg) throws TCGNotFoundException {
        return this.cardRepository.save(card);
    }

    @Transactional
    public List<CardEntity> getFromName(String name, TCGEntity tcg) throws TCGNotFoundException {
        return this.cardRepository.findByCardNameContainsAndTcgIs(name, tcg);
    }

    @Transactional
    public List<CardEntity> getCard(Long cardId, TCGEntity tcg)  {
        return this.cardRepository.findByIdIsAndTcgIs(cardId, tcg);
    }
}
