package it.lucianofilippucci.tcgarkhive.API.V1.DTO.Cards;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import it.lucianofilippucci.tcgarkhive.helpers.CardDetails.CardDetails;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CardDTO {
    @JsonProperty("tcg")
    private Long tcgId;
    @JsonGetter
    public Long getTcgId() {return tcgId;}
    @JsonSetter
    public void setTcgId(Long tcgId) {this.tcgId = tcgId;}

    @JsonProperty("card-name")
    private String cardName;
    @JsonGetter
    public String getCardName() {return cardName;}
    @JsonSetter
    public void setCardName(String cardName) {this.cardName = cardName;}

    @JsonProperty("secondary-card-name")
    private String secondaryCardName;
    @JsonGetter
    public String getSecondaryCardName() {return secondaryCardName;}
    @JsonSetter
    public void setSecondaryCardName(String secondaryCardName) {this.secondaryCardName = secondaryCardName;}


    @JsonProperty("external-id")
    private String externalId;
    @JsonGetter
    public String getExternalId() {return externalId;}
    @JsonSetter
    public void setExternalId(String externalId) {this.externalId = externalId;}

    @JsonProperty("set-code")
    private String setCode;
    @JsonGetter
    public String getSetCode() {return setCode;}
    @JsonSetter
    public void setSetCode(String setCode) {this.setCode = setCode;}

    @JsonProperty("image-url")
    private String imageUrl;
    @JsonGetter
    public String getImageUrl() {return imageUrl;}
    @JsonSetter
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    @JsonProperty("release-date")
    private LocalDate releaseDate;
    @JsonGetter
    public LocalDate getReleaseDate() {return releaseDate;}
    @JsonSetter
    public void setReleaseDate(LocalDate releaseDate) {this.releaseDate = releaseDate;}

    @JsonProperty("card-rarity")
    private Long cardRarityId;
    @JsonGetter
    public Long getCardRarityId() {return cardRarityId;}
    @JsonSetter
    public void setCardRarityId(Long cardRarityId) {this.cardRarityId = cardRarityId;}

    @JsonProperty("card-details")
    private CardDetails cardDetails;
    @JsonGetter
    public CardDetails getCardDetails() {return cardDetails;}
    @JsonSetter
    public void setCardDetails(CardDetails cardDetails) {this.cardDetails = cardDetails;}


}
