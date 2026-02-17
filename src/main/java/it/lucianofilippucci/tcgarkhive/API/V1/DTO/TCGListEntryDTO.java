package it.lucianofilippucci.tcgarkhive.API.V1.DTO;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import it.lucianofilippucci.tcgarkhive.API.V1.DTO.Cards.CardDTO;
import lombok.Data;

@Data
public class TCGListEntryDTO {

    @JsonProperty("entry-id")
    private Long entryId;
    @JsonGetter
    public Long getEntryId() { return this.entryId; }
    @JsonSetter
    public void setEntryId(Long entryId) { this.entryId = entryId; }

    @JsonProperty("entry-quantity")
    private int entryQuantity;
    @JsonGetter
    public int getEntryQuantity() { return this.entryQuantity; }
    @JsonSetter
    public void setEntryQuantity(int entryQuantity) { this.entryQuantity = entryQuantity; }

    @JsonProperty("list-id")
    private Long listId;
    @JsonGetter
    public Long getListId() { return this.listId; }
    @JsonSetter
    public void setListId(Long listId) { this.listId = listId; }

    @JsonProperty("card")
    private CardDTO card;
    @JsonGetter
    public CardDTO getCard() { return this.card; }
    @JsonSetter
    public void setCard(CardDTO card) { this.card = card; }


    @JsonProperty("tcg-id")
    private Long tcgId;
    @JsonGetter
    public Long getTcgID() { return this.tcgId; }
    @JsonSetter
    public void setTcgID(Long tcgID) { this.tcgId = tcgID; }

}
