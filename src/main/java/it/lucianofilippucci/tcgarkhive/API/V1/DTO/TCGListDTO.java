package it.lucianofilippucci.tcgarkhive.API.V1.DTO;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.Cards.CardDTO;
import it.lucianofilippucci.tcgarkhive.helpers.enums.TCGListTypes;
import it.lucianofilippucci.tcgarkhive.helpers.enums.TCGListVisibility;
import lombok.Data;

import java.util.List;

@Data
public class TCGListDTO {

    @JsonProperty("list-name")
    private String listName;
    @JsonGetter
    public String getListName() { return this.listName; }
    @JsonSetter
    public void setListName(String listName) { this.listName = listName; }

    @JsonProperty("list-id")
    private Long listId;
    @JsonGetter
    public Long getListId() { return this.listId; }
    @JsonSetter
    public void setListId(Long listId) { this.listId = listId; }

    @JsonProperty("tcg-id")
    private Long tcgId;
    @JsonGetter
    public Long getTcgId() { return this.tcgId; }
    @JsonSetter
    public void setTcgId(Long tcgId) { this.tcgId = tcgId; }

    @JsonProperty("visibility")
    private TCGListVisibility visibility;
    @JsonGetter
    public TCGListVisibility getVisibility() { return this.visibility; }
    @JsonSetter
    public void setVisibility(TCGListVisibility visibility) { this.visibility = visibility; }

    @JsonProperty("type")
    private TCGListTypes type;
    @JsonGetter
    public TCGListTypes getType() { return this.type; }
    @JsonSetter
    public void setType(TCGListTypes type) { this.type = type; }


    @JsonProperty("cards")
    private List<TCGListEntryDTO> cards;
    @JsonGetter
    public List<TCGListEntryDTO> getCards() { return this.cards; }
    @JsonSetter
    public void setCards(List<TCGListEntryDTO> cards) { this.cards = cards; }

}
