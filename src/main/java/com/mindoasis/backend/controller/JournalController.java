package com.mindoasis.backend.controller;

import com.mindoasis.backend.dto.JournalRequest;
import com.mindoasis.backend.model.Journal;
import com.mindoasis.backend.service.JournalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/journals")
@CrossOrigin(origins = "*")
public class JournalController {

    private final JournalService journalService;

    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    /**
     * achiever selected_theme - call Met api - storage in DB
     */
    @PostMapping
    public ResponseEntity<Journal> createJournal(@RequestBody JournalRequest request) {
        Journal savedJournal = journalService.createJournalWithArt(
                request.getTheme(),
                request.getContent()
        );

        return ResponseEntity.ok(savedJournal);
    }
}