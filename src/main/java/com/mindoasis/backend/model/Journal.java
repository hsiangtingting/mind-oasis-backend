package com.mindoasis.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "journals")
@Data
@NoArgsConstructor


public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    private String selectedTheme;

    @Column(columnDefinition = "TEXT")
    private String journalContent;

    private Integer metObjectId;

    private String artTitle;

    @Column(columnDefinition = "TEXT")
    private String artImageUrl;

    @Column(columnDefinition = "TEXT")
    private String artImageUrlSmall;

    private String artistName;

    private String medium;

    private LocalDateTime createdAt = LocalDateTime.now();

}
