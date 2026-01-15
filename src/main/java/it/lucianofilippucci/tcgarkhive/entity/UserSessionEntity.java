package it.lucianofilippucci.tcgarkhive.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Service
@Table(name = "user_sessions", schema = "public")
public class UserSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id", nullable = false, unique = true, updatable = false)
    private Long sessionId;

    @Column(name = "token", nullable = false, columnDefinition = "TEXT")
    private String token;

    @Column(name = "refresh_token", nullable =false)
    private String refreshToken;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
