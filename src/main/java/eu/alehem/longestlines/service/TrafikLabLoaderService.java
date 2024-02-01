package eu.alehem.longestlines.service;

import eu.alehem.longestlines.model.dao.BusLine;
import eu.alehem.longestlines.model.dao.BusStop;
import eu.alehem.longestlines.model.trafiklab.JourneyPatternPointOnLine;
import eu.alehem.longestlines.model.trafiklab.Line;
import eu.alehem.longestlines.model.trafiklab.ResponseData;
import eu.alehem.longestlines.model.trafiklab.StopPoint;
import eu.alehem.longestlines.model.trafiklab.TrafikLabData;
import eu.alehem.longestlines.model.trafiklab.TrafikLabResponse;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

// TODO Refactor this if there is time!
@Service
public class TrafikLabLoaderService {
  private final LineService lineService;
  private final StopService stopService;
  private final TrafiklabService trafiklabService;
  private final Logger logger = LoggerFactory.getLogger(TrafikLabLoaderService.class);

  @Autowired
  public TrafikLabLoaderService(
      LineService lineService, StopService stopService, TrafiklabService trafiklabService) {
    this.lineService = lineService;
    this.stopService = stopService;
    this.trafiklabService = trafiklabService;
  }

  //TODO, what if the first request fails? In that case we might want to retry instead of waiting 1hr
  @Scheduled(fixedRate = 3600000) //Refresh database once every hour
  public void updateDatabase() {
    logger.info("Refreshing database");
    loadStopsInDatabase();
    loadLinesInDatabase();
    logger.info("Done refreshing database");
  }

  private void loadLinesInDatabase() {
    retrieveBusLines()
        .thenCombine(
            retrieveJourneyPatterns(),
            (linesOpt, journeyPatternsOpt) ->
                linesOpt.map(
                    lines ->
                        journeyPatternsOpt.map(
                            journeyPatterns ->
                                mergeLinesAndJourneyPatterns(lines, journeyPatterns))))
        .thenApply(opt -> opt.orElse(Optional.empty()))
        .thenAccept(merged -> merged.ifPresent(lineService::insertLines));
    // TODO handle exceptions while saving data.
  }

  private static List<BusLine> mergeLinesAndJourneyPatterns(
      List<Line> lines, List<JourneyPatternPointOnLine> journeyPatterns) {
    final Map<Integer, List<Integer>> outboundStopsByLine = keyStopsByLine(journeyPatterns, true);
    final Map<Integer, List<Integer>> inboundStopsByLine = keyStopsByLine(journeyPatterns, false);

    return lines.stream()
        .map(
            line ->
                new BusLine(
                    line.lineNumber(),
                    outboundStopsByLine.getOrDefault(line.lineNumber(), List.of()),
                    inboundStopsByLine.getOrDefault(line.lineNumber(), List.of())))
        .toList();
  }

  private static Map<Integer, List<Integer>> keyStopsByLine(
      List<JourneyPatternPointOnLine> journeyPatterns, boolean outBound) {

    final int directionCode = outBound ? 1 : 2;

    return journeyPatterns.stream()
        .filter(jp -> jp.directionCode() == directionCode)
        .map(jp -> Map.entry(jp.lineNumber(), jp.journeyPatternPointNumber()))
        .collect(
            Collectors.toMap(
                Entry::getKey,
                e -> List.of(e.getValue()),
                (v1, v2) -> Stream.concat(v1.stream(), v2.stream()).toList()));
  }

  private void loadStopsInDatabase() {
    retrieveStopPoints()
        .thenApply(
            stopPointsOpt ->
                stopPointsOpt.map(
                    stopPoints ->
                        stopPoints.stream()
                            .map(sp -> new BusStop(sp.stopPointNumber(), sp.stopPointName()))
                            .toList()))
        .thenAccept(a -> a.ifPresent(stopService::insertStops));
    // TODO handle exceptions while saving data.
  }

  private CompletableFuture<Optional<List<Line>>> retrieveBusLines() {
    return handleTrafikLabResponse(
        trafiklabService.getSLBusLines(),
        (msg) -> logger.error("Failed to retrieve lines: " + msg));
  }

  private CompletableFuture<Optional<List<JourneyPatternPointOnLine>>> retrieveJourneyPatterns() {
    return handleTrafikLabResponse(
        trafiklabService.getJourneyPatterns(),
        (msg) -> logger.error("Failed to retrieve journeyPatterns: " + msg));
  }

  private CompletableFuture<Optional<List<StopPoint>>> retrieveStopPoints() {
    return handleTrafikLabResponse(
        trafiklabService.getStopPoints(),
        (msg) -> logger.error("Failed to retrieve stopPoints: " + msg));
  }

  private static <T extends TrafikLabData> TrafikLabResponse<T> verifyTrafikLabResponse(
      TrafikLabResponse<T> trafikLabResponse, Consumer<String> loggerFunction) {
    if (trafikLabResponse.statusCode() != 0) {
      loggerFunction.accept("API returned non zero exit code: " + trafikLabResponse.statusCode());
      loggerFunction.accept(trafikLabResponse.message().orElse("API did not specify message"));
    }
    return trafikLabResponse;
  }

  private static <T extends TrafikLabData>
      CompletableFuture<Optional<List<T>>> handleTrafikLabResponse(
          Mono<TrafikLabResponse<T>> response, Consumer<String> loggerFunction) {
    return response
        .doOnError(ex -> loggerFunction.accept(ex.getMessage()))
        .onErrorReturn(TrafikLabResponse.failedResponse("Failed to retrieve"))
        .map(tlresponse -> verifyTrafikLabResponse(tlresponse, loggerFunction))
        .map(TrafikLabResponse::responseData)
        .map(opt -> opt.map(ResponseData::responseData))
        .toFuture();
  }
}
