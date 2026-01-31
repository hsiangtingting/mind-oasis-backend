package com.mindoasis.backend.service;

import com.mindoasis.backend.dto.MetArtworkDTO;
import com.mindoasis.backend.model.Journal;
import com.mindoasis.backend.repository.JournalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import com.mindoasis.backend.model.Journal;


@Service
public class JournalService{

    private final JournalRepository journalRepository;
    private final MetApiService metApiService;

    public JournalService(JournalRepository journalRepository, MetApiService metApiService){
        this.journalRepository = journalRepository;
        this.metApiService = metApiService;
    }

    @Transactional
    public Journal createJournalWithArt(String theme, String userContent){
        System.out.println("Service Ready! Target theme: " + theme);
        MetArtworkDTO artDTO = metApiService.getArtworkByTheme(theme);

        Journal journal = new Journal();
        journal.setSelectedTheme(theme);
        journal.setJournalContent(userContent);

        if (artDTO != null){
            journal.setMetObjectId(artDTO.objectID());
            journal.setArtTitle(artDTO.title());
            journal.setArtImageUrl(artDTO.primaryImage());
            journal.setArtImageUrlSmall(artDTO.primaryImageSmall());
            journal.setArtistName(artDTO.artistDisplayName());
            journal.setMedium(artDTO.medium());
        }

        return journalRepository.save(journal);
    }
    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }
}