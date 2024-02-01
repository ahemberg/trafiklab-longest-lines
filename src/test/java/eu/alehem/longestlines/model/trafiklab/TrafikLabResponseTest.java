package eu.alehem.longestlines.model.trafiklab;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.alehem.longestlines.testutil.ObjectMapperUtil;
import eu.alehem.longestlines.testutil.TrafikLabFixtures;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrafikLabResponseTest {

  private static final TrafikLabResponse<JourneyPatternPointOnLine> TRAFIKLABRESPONSE_JOUR =
      TrafikLabFixtures.defaultTrafikLabResponseJourneyPatternPointOnLine();

  private static final TrafikLabResponse<Line> TRAFIKLABRESPONSE_LINE =
      TrafikLabFixtures.defaultTrafikLabResponseLine();

  private static final TrafikLabResponse<StopPoint> TRAFIKLABRESPONSE_STOP_POINT =
      TrafikLabFixtures.defaultTrafikLabResponseStopPoint();

  private static final String JSON_JOUR =
      TrafikLabFixtures.defaultJourneyPatternPointOnLineApiResponse();
  private static final String JSON_LINE = TrafikLabFixtures.defaultLineApiResponse();
  private static final String JSON_STOP_POINT = TrafikLabFixtures.defaultStopPointApiResponse();
  private static final String JSON_FAILED_REQUEST = TrafikLabFixtures.trafikLabErrorResponseJson();

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = ObjectMapperUtil.createObjectMapper();
  }

  @Test
  void jsonSerializeWorksAsExpectedForTypeJourneyPoint() throws JsonProcessingException {

    assertThat(objectMapper.writeValueAsString(TRAFIKLABRESPONSE_JOUR), is(JSON_JOUR));
  }

  @Test
  void jsonDeserializeWorksAsExpectedForTypeJourneyPoint() throws JsonProcessingException {
    TypeReference<TrafikLabResponse<JourneyPatternPointOnLine>> stopRef = new TypeReference<>() {};
    assertThat(objectMapper.readValue(JSON_JOUR, stopRef), is(TRAFIKLABRESPONSE_JOUR));
  }

  @Test
  void jsonSerializeWorksAsExpectedForTypeLine() throws JsonProcessingException {

    assertThat(objectMapper.writeValueAsString(TRAFIKLABRESPONSE_LINE), is(JSON_LINE));
  }

  @Test
  void jsonDeserializeWorksAsExpectedForTypeLine() throws JsonProcessingException {
    TypeReference<TrafikLabResponse<Line>> stopRef = new TypeReference<>() {};
    assertThat(objectMapper.readValue(JSON_LINE, stopRef), is(TRAFIKLABRESPONSE_LINE));
  }

  @Test
  void jsonSerializeWorksAsExpectedForTypeStopPoint() throws JsonProcessingException {
    assertThat(objectMapper.writeValueAsString(TRAFIKLABRESPONSE_STOP_POINT), is(JSON_STOP_POINT));
  }

  @Test
  void jsonDeserializeWorksAsExpectedForTypeStopPoint() throws JsonProcessingException {
    TypeReference<TrafikLabResponse<StopPoint>> stopRef = new TypeReference<>() {};
    assertThat(objectMapper.readValue(JSON_STOP_POINT, stopRef), is(TRAFIKLABRESPONSE_STOP_POINT));
  }

  @Test
  void jsonSerializeWorksAsExpectedForFailedResponseJourneyPoint() throws JsonProcessingException {
    TrafikLabResponse<JourneyPatternPointOnLine> expected =
        TrafikLabFixtures.trafikLabErrorResponse();
    assertThat(objectMapper.writeValueAsString(expected), is(JSON_FAILED_REQUEST));
  }

  @Test
  void jsonDeserializeWorksAsExpectedForFailedResponseJourneyPoint()
      throws JsonProcessingException {
    TrafikLabResponse<JourneyPatternPointOnLine> expected =
        new TrafikLabResponse<>(
            1002, Optional.of("Key is invalid"), Optional.empty(), Optional.empty());
    TypeReference<TrafikLabResponse<StopPoint>> stopRef = new TypeReference<>() {};
    assertThat(objectMapper.readValue(JSON_FAILED_REQUEST, stopRef), is(expected));
  }

  @Test
  void jsonSerializeWorksAsExpectedForFailedResponseLine() throws JsonProcessingException {
    TrafikLabResponse<Line> expected = TrafikLabFixtures.trafikLabErrorResponse();

    assertThat(objectMapper.writeValueAsString(expected), is(JSON_FAILED_REQUEST));
  }

  @Test
  void jsonDeserializeWorksAsExpectedForFailedResponseLine() throws JsonProcessingException {
    TrafikLabResponse<Line> expected = TrafikLabFixtures.trafikLabErrorResponse();
    TypeReference<TrafikLabResponse<StopPoint>> stopRef = new TypeReference<>() {};
    assertThat(objectMapper.readValue(JSON_FAILED_REQUEST, stopRef), is(expected));
  }

  @Test
  void jsonSerializeWorksAsExpectedForFailedResponseStopPoint() throws JsonProcessingException {
    TrafikLabResponse<Line> expected = TrafikLabFixtures.trafikLabErrorResponse();

    assertThat(objectMapper.writeValueAsString(expected), is(JSON_FAILED_REQUEST));
  }

  @Test
  void jsonDeserializeWorksAsExpectedForFailedResponseStopPoint() throws JsonProcessingException {
    TrafikLabResponse<Line> expected = TrafikLabFixtures.trafikLabErrorResponse();
    TypeReference<TrafikLabResponse<StopPoint>> stopRef = new TypeReference<>() {};
    assertThat(objectMapper.readValue(JSON_FAILED_REQUEST, stopRef), is(expected));
  }

  @Test
  void failedResponseReturnsExpectedObject() {
    assertThat(
        TrafikLabResponse.failedResponse("failed"),
        is(TrafikLabFixtures.trafikLabErrorResponse(-1, "failed")));
  }
}
