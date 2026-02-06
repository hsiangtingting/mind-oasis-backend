package com.mindoasis.backend.service;

import com.mindoasis.backend.dto.MetArtworkDTO;
import com.mindoasis.backend.model.Journal;
import com.mindoasis.backend.model.AppUser;

import com.mindoasis.backend.repository.JournalRepository;
import com.mindoasis.backend.repository.AppUserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collections;

@Slf4j
@Service
public class JournalService {

    private final JournalRepository journalRepository;
    private final MetApiService metApiService;
    private final AppUserRepository appUserRepository;

    public JournalService(JournalRepository journalRepository,
                          MetApiService metApiService,
                          AppUserRepository appUserRepository) {
        this.journalRepository = journalRepository;
        this.metApiService = metApiService;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public Journal createJournalWithArt(String theme, String userContent, String userUuid) {

        AppUser user = null;
        if (userUuid != null && !userUuid.isBlank()) {
            user = appUserRepository.findByUuid(userUuid);
            if (user != null) {
                log.info("Creating journal for registered user: {}", user.getEmail());
            } else {
                log.warn("UUID {} provided but no user found. Saving as guest.", userUuid);
            }
        } else {
            log.info("Creating journal for guest user.");
        }

        MetArtworkDTO artDTO = metApiService.getArtworkByTheme(theme);

        Journal journal = new Journal();
        journal.setSelectedTheme(theme);
        journal.setJournalContent(userContent);
        journal.setAppUser(user); // 這裡可能是 null，對應資料庫 nullable = true

        if (artDTO != null) {
            journal.setMetObjectId(artDTO.objectID());
            journal.setArtTitle(artDTO.title());
            journal.setArtImageUrl(artDTO.primaryImage());
            journal.setArtImageUrlSmall(artDTO.primaryImageSmall());
            journal.setArtistName(artDTO.artistDisplayName());
            journal.setMedium(artDTO.medium());
        }

        return journalRepository.save(journal);
    }


    public List<Journal> getJournalsByUserUuid(String userUuid) {
        if (userUuid == null || userUuid.isBlank()) {
            return Collections.emptyList();
        }
        return journalRepository.findByAppUserUuid(userUuid);
    }


    public boolean deleteJournalById(Long id) {
        if (journalRepository.existsById(id)) {
            journalRepository.deleteById(id);
            return true;
        }
        return false;
    }
//    public List<Journal> getAllJournals() {
//        return journalRepository.findAll();
//    }
}