package eu.alehem.longestlines.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import eu.alehem.longestlines.model.trafiklab.JourneyPatternPointOnLine;
import eu.alehem.longestlines.model.trafiklab.Line;
import eu.alehem.longestlines.model.trafiklab.StopPoint;
import eu.alehem.longestlines.model.trafiklab.TrafikLabResponse;
import eu.alehem.longestlines.testutil.TrafikLabFixtures;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class TrafiklabServiceTest {

  public static final String JSON_JOUR =
      TrafikLabFixtures.defaultJourneyPatternPointOnLineApiResponse();
  public static final String JSON_LINE = TrafikLabFixtures.defaultLineApiResponse();
  public static final String JSON_STOP_POINT = TrafikLabFixtures.defaultStopPointApiResponse();

  private static final TrafikLabResponse<Line> TRAFIKLAB_RESPONSE_LINE =
      TrafikLabFixtures.defaultTrafikLabResponseLine();

  private static final TrafikLabResponse<StopPoint> TRAFIKLABRESPONSE_STOP_POINT =
      TrafikLabFixtures.defaultTrafikLabResponseStopPoint();

  private static final TrafikLabResponse<JourneyPatternPointOnLine> TRAFIKLABRESPONSE_JOUR =
      TrafikLabFixtures.defaultTrafikLabResponseJourneyPatternPointOnLine();

  private static final String API_KEY = "testkey";

  private static MockWebServer mockWebServer;
  private TrafiklabService trafiklabService;

  @BeforeAll
  static void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
  }

  @AfterAll
  static void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @BeforeEach
  void initialize() {
    String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
    trafiklabService = new TrafiklabService(baseUrl, API_KEY);
  }

  @Test
  void getSlBusLinesReturnsTrafikLabResponseLine() throws InterruptedException {
    mockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody(JSON_LINE)
            .addHeader("Content-Type", "application/json"));

    Mono<TrafikLabResponse<Line>> response = trafiklabService.getSLBusLines();

    StepVerifier.create(response)
        .expectNextMatches(trafiklabResponse -> trafiklabResponse.equals(TRAFIKLAB_RESPONSE_LINE))
        .verifyComplete();

    // TODO this assert should be in a separate test case
    RecordedRequest recordedRequest = mockWebServer.takeRequest();
    assertEquals("GET", recordedRequest.getMethod());
    assertEquals(
        "/LineData.json?model=line&DefaultTransportModeCode=BUS&key=" + API_KEY,
        recordedRequest.getPath());
  }

  @Test
  void getSLBusLinesHandles404Error() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(404));
    Mono<TrafikLabResponse<Line>> response = trafiklabService.getSLBusLines();
    StepVerifier.create(response)
        .expectErrorMessage("Failed to fetch bus lines. Status code: 404 NOT_FOUND")
        .verify();
  }

  @Test
  void getSLBusLinesHandles500Error() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));
    Mono<TrafikLabResponse<Line>> response = trafiklabService.getSLBusLines();
    StepVerifier.create(response)
        .expectErrorMessage("Failed to fetch bus lines. Status code: 500 INTERNAL_SERVER_ERROR")
        .verify();
  }

  @Test
  void getJourneyPatternsReturnTrafikLabResponseJourneyPattern() throws InterruptedException {
    mockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody(JSON_JOUR)
            .addHeader("Content-Type", "application/json"));

    Mono<TrafikLabResponse<JourneyPatternPointOnLine>> response =
        trafiklabService.getJourneyPatterns();

    StepVerifier.create(response)
        .expectNextMatches(trafiklabResponse -> trafiklabResponse.equals(TRAFIKLABRESPONSE_JOUR))
        .verifyComplete();

    // TODO this assert should be in a separate test case
    RecordedRequest recordedRequest = mockWebServer.takeRequest();
    assertEquals("GET", recordedRequest.getMethod());
    assertEquals(
        "/LineData.json?model=jour&DefaultTransportModeCode=BUS&key=" + API_KEY,
        recordedRequest.getPath());
  }

  @Test
  void getJourneyPatternsHandles404Error() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(404));
    Mono<TrafikLabResponse<JourneyPatternPointOnLine>> response =
        trafiklabService.getJourneyPatterns();
    StepVerifier.create(response)
        .expectErrorMessage("Failed to fetch journeyPatterns. Status code: 404 NOT_FOUND")
        .verify();
  }

  @Test
  void getJourneyPatternsHandles500Error() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));
    Mono<TrafikLabResponse<JourneyPatternPointOnLine>> response =
        trafiklabService.getJourneyPatterns();
    StepVerifier.create(response)
        .expectErrorMessage(
            "Failed to fetch journeyPatterns. Status code: 500 INTERNAL_SERVER_ERROR")
        .verify();
  }

  @Test
  void getStopPointsReturnTrafikLabResponseStopPoint() throws InterruptedException {
    mockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody(JSON_STOP_POINT)
            .addHeader("Content-Type", "application/json"));

    Mono<TrafikLabResponse<StopPoint>> response = trafiklabService.getStopPoints();

    StepVerifier.create(response)
        .expectNextMatches(
            trafiklabResponse -> trafiklabResponse.equals(TRAFIKLABRESPONSE_STOP_POINT))
        .verifyComplete();

    // TODO this assert should be in a separate test case
    RecordedRequest recordedRequest = mockWebServer.takeRequest();
    assertEquals("GET", recordedRequest.getMethod());
    assertEquals(
        "/LineData.json?model=stop&DefaultTransportModeCode=BUS&key=" + API_KEY,
        recordedRequest.getPath());
  }

  @Test
  void getStopPointsHandles404Error() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(404));
    Mono<TrafikLabResponse<StopPoint>> response = trafiklabService.getStopPoints();
    StepVerifier.create(response)
        .expectErrorMessage("Failed to fetch stopPoints. Status code: 404 NOT_FOUND")
        .verify();
  }

  @Test
  void getStopPointsHandles500Error() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));
    Mono<TrafikLabResponse<StopPoint>> response = trafiklabService.getStopPoints();
    StepVerifier.create(response)
        .expectErrorMessage("Failed to fetch stopPoints. Status code: 500 INTERNAL_SERVER_ERROR")
        .verify();
  }
}
