package eu.alehem.longestlines.model.trafiklab;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public record Line(
    @JsonProperty("LineNumber") int lineNumber,
    @JsonProperty("LineDesignation") String lineDesignation,
    @JsonProperty("DefaultTransportMode") String defaultTransportMode,
    @JsonProperty("DefaultTransportModeCode") DefaultTransportModeCode defaultTransportModeCode,
    @JsonProperty("LastModifiedUtcDateTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        Date lastModifiedUtcDateTime,
    @JsonProperty("ExistsFromDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        Date existsFromDate)
    implements TrafikLabData {}
