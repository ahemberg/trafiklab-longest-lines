package eu.alehem.longestlines.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StopDTO(@JsonProperty("id") int id, @JsonProperty("name") String name) {}
