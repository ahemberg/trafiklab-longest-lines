package eu.alehem.longestlines.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LineDTO(@JsonProperty("line_number") int lineNumber) {}
