package it.lucianofilippucci.tcgarkhive.helpers.CardDetails;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tcg"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OnePieceTCG.class, name = "OPTCG")
})
public interface CardDetails {
}
