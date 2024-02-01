package eu.alehem.longestlines.model.trafiklab;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public record StopPoint(
    @JsonProperty("StopPointNumber") int stopPointNumber,
    @JsonProperty("StopPointName") String stopPointName,
    @JsonProperty("StopAreaNumber") int stopAreaNumber,
    @JsonProperty("LocationNorthingCoordinate") double locationNorthingCoordinate,
    @JsonProperty("LocationEastingCoordinate") double locationEastingCoordinate,
    @JsonProperty("ZoneShortName") ZoneShortName zoneShortName,
    @JsonProperty("StopAreaTypeCode") StopAreaTypeCode stopAreaTypeCode,
    @JsonProperty("LastModifiedUtcDateTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        Date lastModifiedUtcDateTime,
    @JsonProperty("ExistsFromDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        Date existsFromDate)
    implements TrafikLabData {}
