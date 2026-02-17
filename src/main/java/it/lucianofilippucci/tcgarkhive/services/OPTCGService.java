package it.lucianofilippucci.tcgarkhive.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.Cards.CardDTO;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.TCGListDTO;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.TCGListEntryDTO;
import it.lucianofilippucci.tcgarkhive.entity.*;
import it.lucianofilippucci.tcgarkhive.helpers.CardDetails.CardDetails;
import it.lucianofilippucci.tcgarkhive.helpers.CardDetails.OnePieceTCG;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.*;
import it.lucianofilippucci.tcgarkhive.repository.CardRarityRepository;
import it.lucianofilippucci.tcgarkhive.repository.TCGRepository;
import it.lucianofilippucci.tcgarkhive.repository.UserRepository;
import it.lucianofilippucci.tcgarkhive.repository.UserTCGListRepository;
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

    private final UserTCGListRepository userTCGListRepository;
    private final UserRepository userRepository;


    private final TCGService tcgService;


    public OPTCGService(CardService cardService, TCGRepository tcgRepository, CardRarityRepository cardRarityRepository, ObjectMapper objectMapper, UserTCGListRepository userTCGListRepository, UserRepository userRepository, TCGService tcgService) {
        this.cardService = cardService;
        this.tcgRepository = tcgRepository;
        this.cardRarityRepository = cardRarityRepository;
        this.objectMapper = objectMapper;
        this.userTCGListRepository = userTCGListRepository;
        this.userRepository = userRepository;
        this.tcgService = tcgService;
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

    private TCGListEntryDTO fromEntity(TCGListEntry entry) {
        TCGListEntryDTO entryDTO = new TCGListEntryDTO();

        entryDTO.setEntryQuantity(entry.getQuantity());
        entryDTO.setEntryId(entry.getId());
        entryDTO.setListId(entry.getList().getId());
        entryDTO.setTcgID(entryDTO.getTcgID());
        entryDTO.setCard(this.fromEntity(entry.getCard()));

        return entryDTO;
    }


    @Transactional
    public TCGListDTO newList(TCGListDTO listDTO, String username) {
        Optional<UserEntity> temp = this.userRepository.findByUsername(username);
        if(temp.isEmpty()) throw new UserNotExistsException("User {"+ username +"} not found");
        UserEntity user = temp.get();

        Optional<TCGEntity> temp2 = this.tcgRepository.findById(listDTO.getTcgId());
        if(temp2.isEmpty()) throw new TCGNotFoundException("TCG Id {" + listDTO.getTcgId() + "} not found");
        TCGEntity tcg = temp2.get();

        UserTCGList tcgList = new UserTCGList();

        tcgList.setListName(listDTO.getListName());
        tcgList.setListType(listDTO.getType());
        tcgList.setListVisibility(listDTO.getVisibility());
        tcgList.setTcg(tcg);
        tcgList.setUser(user);

        return this.tcgService.createUserTCGList(tcgList);

    }



    @Transactional
    public TCGListDTO addCardToList(TCGListEntryDTO entryDTO, String username) {
        Optional<TCGEntity> temp = this.tcgRepository.findById(entryDTO.getTcgID());
        if(temp.isEmpty()) throw new TCGNotFoundException("TCG with ID {" + entryDTO.getTcgID() + "} not found.");
        Optional<CardEntity> temp2 = this.cardService.getCard(entryDTO.getCard().getCardId(), temp.get());
        if(temp2.isEmpty()) throw new CardNotFoundException("Card " + entryDTO.getCard().toString() + "/nNot Found");

        Optional<UserTCGList> temp3 = this.userTCGListRepository.findById(entryDTO.getListId());
        if(temp3.isEmpty()) throw new UserTCGListNotFoundException("TCG List with ID {" + entryDTO.getListId() + "} not found");
        UserTCGList list = temp3.get();
        if(!list.getUser().getUsername().equals(username)) throw new UnauthorizedException("Unauthorized Access to List with ID {" + list.getId() + "} from user {" + username + "}");

        return this.tcgService.addCardToList(temp2.get(), list.getId(), username, entryDTO.getEntryQuantity());
    }


    @Transactional
    public TCGListDTO editCardEntry(Long entryId, String username, Long userListID, int quantity) {
        return this.tcgService.editCardEntry(entryId, userListID, username, quantity);
    }

    @Transactional
    public List<TCGListEntryDTO> getListCards(Long listID, String username) {
        List<TCGListEntry> listEntry =  this.tcgService.getListCards(listID, username);

        List<TCGListEntryDTO> entryDTO = new ArrayList<>();

        for(TCGListEntry entry : listEntry) {
            entryDTO.add(this.fromEntity(entry));
        }

        return entryDTO;
    }


}
