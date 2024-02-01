package eu.alehem.longestlines.model.trafiklab;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public record JourneyPatternPointOnLine (
    @JsonProperty("LineNumber")
    int lineNumber,
    @JsonProperty("DirectionCode")
    int directionCode,
    @JsonProperty("JourneyPatternPointNumber")
    int journeyPatternPointNumber,
    @JsonProperty("LastModifiedUtcDateTime")
    @JsonFormat
        (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone="UTC")
    Date lastModifiedUtcDateTime,
    @JsonProperty("ExistsFromDate")
    @JsonFormat
        (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone="UTC")
    Date existsFromDate) implements TrafikLabData {}
