package com.mindoasis.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable =true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AppUser appUser;

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
