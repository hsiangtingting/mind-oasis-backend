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
     * api calling counter : retryCount
     */
    public MetArtworkDTO getArtworkByTheme(String theme) {
        return getValidArtworkWithRetry(theme, 0);
    }

    /**
     * retry limit & return image url for sure
     */
    private MetArtworkDTO getValidArtworkWithRetry(String theme, int retryCount) {
        if (retryCount >= 5) {
            return getFallbackArtwork();
        }

        String keyword = themeMapperService.getKeyword(theme);

        // no famous ones then return non-famous
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
                    .body(MetArtworkDTO.class);

            // Check: image url
            if (art == null || art.primaryImage() == null || art.primaryImage().isBlank()) {
                System.out.println("No image url (ID: " + randomId + ")，is trying " + (retryCount + 1) + " times retry");
                return getValidArtworkWithRetry(theme, retryCount + 1);
            }
            return art;
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
     * fall back for no images
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