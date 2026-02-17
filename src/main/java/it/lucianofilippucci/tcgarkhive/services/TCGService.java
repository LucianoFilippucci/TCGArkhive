package it.lucianofilippucci.tcgarkhive.services;

import it.lucianofilippucci.tcgarkhive.API.V1.DTO.TCGListDTO;
import it.lucianofilippucci.tcgarkhive.entity.CardEntity;
import it.lucianofilippucci.tcgarkhive.entity.TCGEntity;
import it.lucianofilippucci.tcgarkhive.entity.TCGListEntry;
import it.lucianofilippucci.tcgarkhive.entity.UserTCGList;
import it.lucianofilippucci.tcgarkhive.helpers.enums.TCGListVisibility;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.TCGAlreadyExistsException;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.TCGListentryNotFound;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.UnauthorizedException;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.UserTCGListNotFoundException;
import it.lucianofilippucci.tcgarkhive.repository.TCGListEntryRepository;
import it.lucianofilippucci.tcgarkhive.repository.TCGRepository;
import it.lucianofilippucci.tcgarkhive.repository.UserTCGListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TCGService {

    private final TCGRepository tcgRepository;
    private final UserTCGListRepository userTCGListRepository;
    private final TCGListEntryRepository TCGListEntryRepository;

    public TCGService(TCGRepository tcgRepository, UserTCGListRepository userTCGList, TCGListEntryRepository tcgListEntry) {
        this.tcgRepository = tcgRepository;
        this.userTCGListRepository = userTCGList;
        TCGListEntryRepository = tcgListEntry;
    }

    @Transactional
    public void CreateTcg(String tcgName, String tcgCode) throws TCGAlreadyExistsException {
        Optional<TCGEntity> tmp = this.tcgRepository.findByCode(tcgCode);
        if(tmp.isPresent()) throw new TCGAlreadyExistsException(tcgName);

        TCGEntity newTCG = new TCGEntity();
        newTCG.setCode(tcgCode);
        newTCG.setName(tcgName);
        this.tcgRepository.save(newTCG);
    }



    public TCGListDTO createUserTCGList(UserTCGList tcgList) {
        return this.listDTOFromEntity(this.userTCGListRepository.save(tcgList));
    }

    private TCGListDTO listDTOFromEntity(UserTCGList listEntity) {
        TCGListDTO dto = new TCGListDTO();

        dto.setListId(listEntity.getId());
        dto.setListName(listEntity.getListName());
        dto.setType(listEntity.getListType());
        dto.setVisibility(listEntity.getListVisibility());
        dto.setTcgId(listEntity.getTcg().getId());

        return dto;
    }

    @Transactional
    public TCGListDTO addCardToList(CardEntity card, Long userList, String username, int quantity) {
        Optional<UserTCGList> temp = this.userTCGListRepository.findById(userList);
        if(temp.isEmpty()) throw new UserTCGListNotFoundException("List with ID {" + userList + "} Not found.");
        UserTCGList list = temp.get();

        if(!list.getUser().getUsername().equals(username)) throw new UnauthorizedException("Unauthorized Access to List with ID {" + list.getId() + "} from user {" + username +"}.");

        TCGListEntry entry = new TCGListEntry();
        entry.setCard(card);
        entry.setList(list);
        entry.setQuantity(quantity);

        entry = this.TCGListEntryRepository.save(entry);

        list.getEntries().add(entry);

        return this.listDTOFromEntity(this.userTCGListRepository.save(list));
    }

    @Transactional
    public TCGListDTO editCardEntry(Long entryID, Long userList, String username, int Quantity) {
        Optional<UserTCGList> temp = this.userTCGListRepository.findById(userList);
        if(temp.isEmpty()) throw new UserTCGListNotFoundException("List with ID {" + userList + "} Not found.");
        UserTCGList list = temp.get();

        if(!list.getUser().getUsername().equals(username)) throw new UnauthorizedException("Unauthorized Access to List with ID {" + list.getId() + "} from user {" + username +"}.");

        for(TCGListEntry entry : list.getEntries()) {
            if(entry.getId().equals(entryID)) {
                Optional<TCGListEntry> temp2 = this.TCGListEntryRepository.findById(entryID);
                if(temp2.isEmpty()) throw new TCGListentryNotFound("Entry with ID {" + entryID + "} not found");

                int newQty = temp2.get().getQuantity() + Quantity;

                if( newQty <= 0) {
                    // WE're removing it.
                    this.TCGListEntryRepository.delete(temp2.get());
                    list.getEntries().remove(entry);
                } else {
                    // we're modifying the qta by adding or removing.
                    temp2.get().setQuantity(newQty);
                    entry.setQuantity(newQty);
                    this.TCGListEntryRepository.save(entry);
                }


                break;
            }
        }
        return this.listDTOFromEntity(this.userTCGListRepository.save(list));
    }


    @Transactional
    public List<TCGListEntry> getListCards(Long listId, String username) {
        Optional<UserTCGList> temp = this.userTCGListRepository.findById(listId);
        if(temp.isEmpty()) throw new UserTCGListNotFoundException("");

        if(temp.get().getListVisibility().equals(TCGListVisibility.PRIVATE) && !temp.get().getUser().getUsername().equals(username))
            throw new UnauthorizedException("This list is Private and you have no access to it.");

        return temp.get().getEntries();
    }

}
