package com.mindoasis.backend.controller;

import com.mindoasis.backend.model.Journal;
import com.mindoasis.backend.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/journals")
@CrossOrigin(origins = "*")
public class JournalController{

    @Autowired
    private JournalRepository journalRepository;

    @PostMapping("/save")
    public Journal createJournal(@RequestBody Journal journal){

        return journalRepository.save(journal);
    }
}