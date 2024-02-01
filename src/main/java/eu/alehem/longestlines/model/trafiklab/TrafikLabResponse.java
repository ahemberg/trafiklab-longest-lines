package eu.alehem.longestlines.model.trafiklab;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;

public record TrafikLabResponse<T extends TrafikLabData>(
    @JsonProperty("StatusCode") int statusCode,
    @JsonProperty("Message") Optional<String> message,
    @JsonInclude(JsonInclude.Include.NON_ABSENT) @JsonProperty("ExecutionTime")
        Optional<Integer> executionTime,
    @JsonInclude(JsonInclude.Include.NON_ABSENT) @JsonProperty("ResponseData")
        Optional<ResponseData<T>> responseData) {

  public static <T extends TrafikLabData> TrafikLabResponse<T> failedResponse(String msg) {
    return new TrafikLabResponse<>(-1, Optional.of(msg), Optional.empty(), Optional.empty());
  }
}
