package com.mindoasis.backend.dto;
import java.util.List;
public record MetSearchResponse(int total, List<Integer> objectIDs) {}