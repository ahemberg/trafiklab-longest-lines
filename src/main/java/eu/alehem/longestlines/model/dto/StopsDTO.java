package eu.alehem.longestlines.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StopsDTO(
    @JsonProperty("line_number") int lineNumber,
    @JsonProperty("outbound_stops") List<StopDTO> outboundStops,
    @JsonProperty("inbound_stops") List<StopDTO> inboundStops) {}
