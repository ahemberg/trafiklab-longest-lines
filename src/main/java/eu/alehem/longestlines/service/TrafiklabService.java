package eu.alehem.longestlines.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.alehem.longestlines.model.trafiklab.JourneyPatternPointOnLine;
import eu.alehem.longestlines.model.trafiklab.Line;
import eu.alehem.longestlines.model.trafiklab.StopPoint;
import eu.alehem.longestlines.model.trafiklab.TrafikLabResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TrafiklabService {

  private final WebClient webClient;
  private final String apiKey;

  @Autowired
  public TrafiklabService(String baseUrl, String apiKey) {
    this.webClient = createWebclientBuilder().baseUrl(baseUrl).build();
    this.apiKey = apiKey;
  }

  public Mono<TrafikLabResponse<Line>> getSLBusLines() {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("LineData.json")
                    .queryParam("model", "line")
                    .queryParam("DefaultTransportModeCode", "BUS")
                    .queryParam("key", apiKey)
                    .build())
        .retrieve()
        .onStatus(
            httpStatusCode -> !httpStatusCode.is2xxSuccessful(),
            clientResponse -> handleErrorResponse(clientResponse.statusCode(), "bus lines"))
        .bodyToMono(new ParameterizedTypeReference<>() {});
  }

  public Mono<TrafikLabResponse<JourneyPatternPointOnLine>> getJourneyPatterns() {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("LineData.json")
                    .queryParam("model", "jour")
                    .queryParam("DefaultTransportModeCode", "BUS")
                    .queryParam("key", apiKey)
                    .build())
        .retrieve()
        .onStatus(
            httpStatusCode -> !httpStatusCode.is2xxSuccessful(),
            clientResponse -> handleErrorResponse(clientResponse.statusCode(), "journeyPatterns"))
        .bodyToMono(new ParameterizedTypeReference<>() {});
  }

  public Mono<TrafikLabResponse<StopPoint>> getStopPoints() {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("LineData.json")
                    .queryParam("model", "stop")
                    .queryParam("DefaultTransportModeCode", "BUS")
                    .queryParam("key", apiKey)
                    .build())
        .retrieve()
        .onStatus(
            httpStatusCode -> !httpStatusCode.is2xxSuccessful(),
            clientResponse -> handleErrorResponse(clientResponse.statusCode(), "stopPoints"))
        .bodyToMono(new ParameterizedTypeReference<>() {});
  }

  private static Mono<? extends Throwable> handleErrorResponse(
      HttpStatusCode statusCode, String msg) {
    return Mono.error(
        new RuntimeException("Failed to fetch " + msg + ". Status code: " + statusCode));
  }

  private static WebClient.Builder createWebclientBuilder() {
    final ObjectMapper mapper =
        new ObjectMapper()
            .findAndRegisterModules()
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
    final ExchangeStrategies exchangeStrategies =
        ExchangeStrategies.builder()
            .codecs(
                configurer ->
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper)))
            .build();

    return WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
        .exchangeStrategies(exchangeStrategies);
  }
}
