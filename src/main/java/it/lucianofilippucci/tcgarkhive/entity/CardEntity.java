package it.lucianofilippucci.tcgarkhive.entity;

import it.lucianofilippucci.tcgarkhive.helpers.CardDetailsConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "card")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String CardName;

    String CardSecondaryName;

    String ExternalId; // Official card id: op11-119; DSK 099 etc...

    String SetCode; // OP14, OP13, FB03 etc...

    String ImageURL;

    LocalDate ReleaseDate;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String cardDetails;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tcg_id", nullable = false)
    private TCGEntity tcg;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rarity_id", nullable = false)
    private CardRarityEntity rarity;

}
