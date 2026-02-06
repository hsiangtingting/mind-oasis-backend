package com.mindoasis.backend.controller;

import com.mindoasis.backend.dto.JournalRequest;
import com.mindoasis.backend.model.Journal;
import com.mindoasis.backend.repository.AppUserRepository;
import com.mindoasis.backend.service.JournalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/journals")
//replace * in production
//@CrossOrigin(origins = "${cors.allowed.origins:http://localhost:3000}")
public class JournalController {

    private final JournalService journalService;

    public JournalController(JournalService journalService, AppUserRepository appUserRepository) {
        this.journalService = journalService;
    }

    @PostMapping
    public ResponseEntity<Journal> createJournal(
            @RequestBody JournalRequest request,
            @RequestParam(required = false) String userUuid) {

        try {
            Journal savedJournal = journalService.createJournalWithArt(
                    request.getTheme(),
                    request.getContent(),
                    userUuid
            );

            return new ResponseEntity<>(savedJournal, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating journal entry: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Journal>> getMyJournals(
            @RequestParam(required = false) String userUuid) {
        if (userUuid == null || userUuid.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(journalService.getJournalsByUserUuid(userUuid));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJournal(@PathVariable Long id) {
        log.info("Delete request，journal ID: {}", id);
        try {
            boolean deleted = journalService.deleteJournalById(id);

            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

