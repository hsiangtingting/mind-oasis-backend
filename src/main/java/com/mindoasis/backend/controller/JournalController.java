package com.mindoasis.backend.controller;

import com.mindoasis.backend.dto.JournalRequest;
import com.mindoasis.backend.model.Journal;
import com.mindoasis.backend.service.JournalService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journals")
//replace * in production
//@CrossOrigin(origins = "${cors.allowed.origins:http://localhost:3000}")
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
        try {
            Journal savedJournal = journalService.createJournalWithArt(
                    request.getTheme(),
                    request.getContent()
            );

            return new ResponseEntity<>(savedJournal, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Error creating journal: " + e.getMessage());
            return new ResponseEntity<Journal>((Journal) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * get all journals with artworks
     */
    @GetMapping
    public ResponseEntity<List<Journal>> getAllJournals() {
        List<Journal> journals = journalService.getAllJournals();
        return ResponseEntity.ok(journals);
    }
}
