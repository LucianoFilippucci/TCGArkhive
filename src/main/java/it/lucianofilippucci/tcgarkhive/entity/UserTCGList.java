package it.lucianofilippucci.tcgarkhive.entity;

import it.lucianofilippucci.tcgarkhive.helpers.enums.TCGListTypes;
import it.lucianofilippucci.tcgarkhive.helpers.enums.TCGListVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tcg_user_list", schema = "public")
public class UserTCGList {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;


    @Column(name = "list_name", nullable = false, length = 75)
    private String listName;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "list_type", columnDefinition = "tcg_list_type")
    private TCGListTypes listType;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "list_visibility", columnDefinition = "tcg_list_visibility")
    private TCGListVisibility listVisibility;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tcg_id", nullable = false)
    private TCGEntity tcg;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TCGListEntry> entries = new ArrayList<>();




}
