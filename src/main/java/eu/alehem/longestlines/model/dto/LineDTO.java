package eu.alehem.longestlines.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LineDTO(@JsonProperty("line_number") int lineNumber
    /*    @JsonProperty("stops_outbound")
    List<StopDTO> stopsOutbound,
    @JsonProperty("stops_inbound")
    List<StopDTO> stopsInbound*/
    ) {}
