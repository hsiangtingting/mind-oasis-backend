package com.mindoasis.backend.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class ThemeMapperService {

    private static final Map<String, List<String>> THEME_KEYWORDS = Map.of(
            "Chaos", List.of("storm", "abstract", "battle", "fragment"),
            "Loneliness", List.of("solitude", "desert", "monochrome"),
            "Hope", List.of("sunrise", "dawn", "blossom", "light"),
            "Stress", List.of("crowd", "tension", "dark", "heavy"),
            "Calm", List.of("lake", "zen", "horizon", "still life"),
            "Growth", List.of("garden", "seedling", "spring")
    );

    private final Random random = new Random();

    public String getKeyword(String theme) {
        List<String> keywords = THEME_KEYWORDS.get(theme);
        if (keywords == null || keywords.isEmpty()) {
            return "art";
        }
        return keywords.get(random.nextInt(keywords.size()));
    }
}