package com.mindoasis.backend.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class ThemeMapperService {

    private static final Map<String, List<String>> THEME_KEYWORDS = Map.of(
            "Chaos", List.of(
                    "storm", "tempest", "abstract expressionism", "battle", "fragment",
                    "whirlpool", "explosion", "fire", "collision", "vortex"
            ),
            "Loneliness", List.of(
                    "solitude", "desert", "monochrome", "empty", "shadow", "moon",
                    "ruins", "hermit", "winter", "mist", "abandoned"
            ),
            "Hope", List.of(
                    "sunrise", "dawn", "blossom", "light", "rainbow", "horizon",
                    "angel", "resurrection", "glimmer", "springtime", "rebirth"
            ),
            "Stress", List.of(
                    "crowd", "tension", "dark", "heavy", "clutter", "iron",
                    "thunder", "pressure", "constraint", "labyrinth", "maze"
            ),
            "Calm", List.of(
                    "lake", "zen", "horizon", "still life", "buddha", "meadow",
                    "pastel", "evening", "reflection", "blue", "waterfall"
            ),
            "Growth", List.of(
                    "garden", "seedling", "spring", "forest", "vine", "evolution",
                    "sprout", "greenery", "nurture", "life cycle", "abundance"
            )
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