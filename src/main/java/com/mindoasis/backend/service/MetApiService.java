package com.mindoasis.backend.service;

import com.mindoasis.backend.dto.MetArtworkDTO;
import com.mindoasis.backend.dto.MetSearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Random;

@Service
public class MetApiService {

    private final RestClient restClient;
    private final ThemeMapperService themeMapperService;
    private final Random random = new Random();

    public MetApiService(ThemeMapperService themeMapperService) {
        this.themeMapperService = themeMapperService;
        this.restClient = RestClient.builder()
                .baseUrl("https://collectionapi.metmuseum.org/public/collection/v1")
                .build();
    }

    /**
     * theme-match artwork
     */
    public MetArtworkDTO getArtworkByTheme(String theme) {
        return getValidArtworkWithRetry(theme, 0);
    }

    /**
     * try-catch logic
     */
    private MetArtworkDTO getValidArtworkWithRetry(String theme, int retryCount) {
        // retry limit & return fall back
        if (retryCount >= 5) {
            System.out.println("beyond limited retry, use fallback artwork");
            return getFallbackArtwork();
        }

        try {
            String keyword = themeMapperService.getKeyword(theme);
            if (keyword == null) keyword = "Hope";

            // no famous ones return non-famous
            MetSearchResponse searchResult = fetchFromMet(keyword, true);
            if (isResultEmpty(searchResult)) {
                searchResult = fetchFromMet(keyword, false);
            }

            // random pick
            if (!isResultEmpty(searchResult)) {
                Integer randomId = searchResult.objectIDs().get(random.nextInt(searchResult.objectIDs().size()));

                MetArtworkDTO art = restClient.get()
                        .uri("/objects/{id}", randomId)
                        .retrieve()
                        .onStatus(status -> status.isError(), (request, response) -> {
                            throw new RuntimeException("Met API error status");
                        })
                        .body(MetArtworkDTO.class);

                // check image url
                if (art == null || art.primaryImage() == null || art.primaryImage().isBlank()) {
                    System.out.println("No image url (ID: " + randomId + ")，is trying " + (retryCount + 1) + " times retry");
                    return getValidArtworkWithRetry(theme, retryCount + 1);
                }
                return art;
            }
        } catch (Exception e) {
            System.out.println("API bad request: " + e.getMessage());
            // time out
            return getValidArtworkWithRetry(theme, retryCount + 1);
        }

        // change searching term
        return getValidArtworkWithRetry(theme, retryCount + 1);
    }

    private MetSearchResponse fetchFromMet(String keyword, boolean isHighlight) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("hasImages", "true")
                        .queryParam("isHighlight", String.valueOf(isHighlight))
                        .queryParam("q", keyword)
                        .build())
                .retrieve()
                .body(MetSearchResponse.class);
    }

    private boolean isResultEmpty(MetSearchResponse response) {
        return response == null || response.objectIDs() == null || response.objectIDs().isEmpty();
    }

    /**
     * fall back for no image
     */
    private MetArtworkDTO getFallbackArtwork() {
        return new MetArtworkDTO(
                436535,
                "Wheat Field with Cypresses",
                "https://images.metmuseum.org/CRDImages/ep/original/DT1567.jpg",
                "https://images.metmuseum.org/CRDImages/ep/web-large/DT1567.jpg",
                "Vincent van Gogh",
                "1889",
                "Oil on canvas",
                "Dutch",
                "Purchase, Rogers Fund, 1993",
                "https://www.metmuseum.org/art/collection/search/436535"
        );
    }
}