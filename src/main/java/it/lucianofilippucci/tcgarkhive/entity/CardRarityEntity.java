package it.lucianofilippucci.tcgarkhive.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "card_rarity")
public class CardRarityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code; // C, R, SR, SAR etc..

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tcg_id")
    private TCGEntity tcg;

    private String name; // Full name: Common, Rare, Super Rare etc...
}
