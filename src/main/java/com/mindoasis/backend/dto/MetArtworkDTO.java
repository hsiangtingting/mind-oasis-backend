package com.mindoasis.backend.dto;

public record MetArtworkDTO(
        Integer objectID,
        String title,
        String primaryImage,
        String primaryImageSmall,
        String artistDisplayName,
        String objectDate,
        String medium,
        String culture,
        String creditLine,
        String objectURL
) {}