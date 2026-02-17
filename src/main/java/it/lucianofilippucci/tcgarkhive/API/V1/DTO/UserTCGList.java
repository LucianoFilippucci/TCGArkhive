package it.lucianofilippucci.tcgarkhive.API.V1.DTO;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import it.lucianofilippucci.tcgarkhive.helpers.enums.TCGListTypes;
import it.lucianofilippucci.tcgarkhive.helpers.enums.TCGListVisibility;
import lombok.Data;

@Data
public class UserTCGList {
    @JsonProperty("list-name")
    private String listName;
    @JsonGetter
    public String getListName() { return this.listName; }
    @JsonSetter
    public void setListName(String listName) { this.listName = listName; }

    @JsonProperty("list-type")
    private TCGListTypes listType;
    @JsonGetter
    public TCGListTypes getListType() { return this.listType; }
    @JsonSetter
    public void setListType(TCGListTypes listType) { this.listType = listType; }

    @JsonProperty("list-visibility")
    public TCGListVisibility visibility;
    @JsonGetter
    public TCGListVisibility getVisibility() { return this.visibility; }
    @JsonSetter
    public void setVisibility(TCGListVisibility visibility) { this.visibility = visibility; }

    @JsonProperty("tcg-id")
    public Long tcgId;
    @JsonGetter
    public Long getTcgId() { return this.tcgId; }
    @JsonSetter
    public void setTcgId(Long tcgId) { this.tcgId = tcgId; }
}
