package com.mindoasis.backend.controller;

import com.mindoasis.backend.dto.MetArtworkDTO;
import com.mindoasis.backend.service.MetApiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/art")
//@CrossOrigin(origins = "${cors.allowed.origins:http://localhost:3000}")
public class ArtController {

    private final MetApiService metApiService;

    public ArtController(MetApiService metApiService) {
        this.metApiService = metApiService;
    }

    @GetMapping("/recommend")
    public MetArtworkDTO getRecommendation(@RequestParam String theme) {
        return metApiService.getArtworkByTheme(theme);
    }
}