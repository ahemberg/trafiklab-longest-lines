package eu.alehem.longestlines.model.trafiklab;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

public record ResponseData<T extends TrafikLabData>(
    @JsonProperty("Version")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        Date version,
    @JsonProperty("Type") String type, // TODO: make me enum
    @JsonProperty("Result") List<T> responseData) implements TrafikLabData {}
