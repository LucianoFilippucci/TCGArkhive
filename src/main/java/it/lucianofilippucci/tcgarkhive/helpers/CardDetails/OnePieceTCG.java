package it.lucianofilippucci.tcgarkhive.helpers.CardDetails;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import it.lucianofilippucci.tcgarkhive.helpers.enums.OnePieceCardType;
import it.lucianofilippucci.tcgarkhive.helpers.enums.OnePieceColors;
import lombok.Data;

import java.util.Set;

@Data
public class OnePieceTCG implements CardDetails {
    @JsonProperty("set")
    private String set;
    @JsonGetter
    public String getSet() {return set;}
    @JsonSetter
    public void setSet(String set) {this.set = set;}

    @JsonProperty("cost")
    private int cost;
    @JsonGetter
    public int getCost() {return cost;}
    @JsonSetter
    public void setCost(int cost) {this.cost = cost;}

    @JsonProperty("counter")
    private int counter;
    @JsonGetter
    public int getCounter() {return counter;}
    @JsonSetter
    public void setCounter(int counter) {this.counter = counter;}

    @JsonProperty("power")
    private int power;
    @JsonGetter
    public int getPower() {return power;}
    @JsonSetter
    public void setPower(int power) {this.power = power;}

    @JsonProperty("effect")
    private String effect;
    @JsonGetter
    public String getEffect() {return effect;}
    @JsonSetter
    public void setEffect(String effect) {this.effect = effect;}

    @JsonProperty("type") // Straw Hat, Neptunian etc....
    private String type;
    @JsonGetter
    public String getType() {return type;}
    @JsonSetter
    public void setType(String type) {this.type = type;}

    @JsonProperty("attribute")
    private String attribute;
    @JsonGetter
    public String getAttribute() {return attribute;}
    @JsonSetter
    public void setAttribute(String attribute) {this.attribute = attribute;}

    @JsonProperty("block")
    private int block;
    @JsonGetter
    public int getBlock() {return block;}
    @JsonSetter
    public void setBlock(int block) {this.block = block;}

    @JsonProperty("color")
    private Set<OnePieceColors> color;

    @JsonProperty("card-type")
    private Set<OnePieceCardType> cardType;
}
