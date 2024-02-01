package eu.alehem.longestlines.model.trafiklab;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.alehem.longestlines.testutil.ObjectMapperUtil;
import eu.alehem.longestlines.testutil.TrafikLabFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResponseDataTest {
  private static final ResponseData<JourneyPatternPointOnLine> RESPONSEDATA_JOUR =
      TrafikLabFixtures.defaultResponseJourneyPatternPointOnLine();

  private static final ResponseData<Line> RESPONSEDATA_LINE =
      TrafikLabFixtures.defaultResponseDataLine();

  private static final ResponseData<StopPoint> RESPONSEDATA_STOP_POINT =
      TrafikLabFixtures.defaultResponseDataStopPoint();

  public static final String JSON_JOUR =
      TrafikLabFixtures.defaultJourneyPatternPointOnLineResponseJson();
  public static final String JSON_LINE = TrafikLabFixtures.defaultLineResponseJson();
  public static final String JSON_STOP_POINT = TrafikLabFixtures.defaultStopPointResponseJson();

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = ObjectMapperUtil.createObjectMapper();
  }

  @Test
  void jsonSerializeWorksAsExpectedForTypeJourneyPoint() throws JsonProcessingException {
    assertThat(objectMapper.writeValueAsString(RESPONSEDATA_JOUR), is(JSON_JOUR));
  }

  @Test
  void jsonDeserializeWorksAsExpectedForTypeJourneyPoint() throws JsonProcessingException {
    TypeReference<ResponseData<JourneyPatternPointOnLine>> stopRef = new TypeReference<>() {};
    assertThat(objectMapper.readValue(JSON_JOUR, stopRef), is(RESPONSEDATA_JOUR));
  }

  @Test
  void jsonSerializeWorksAsExpectedForTypeLine() throws JsonProcessingException {
    assertThat(objectMapper.writeValueAsString(RESPONSEDATA_LINE), is(JSON_LINE));
  }

  @Test
  void jsonDeserializeWorksAsExpectedForTypeLine() throws JsonProcessingException {
    TypeReference<ResponseData<Line>> stopRef = new TypeReference<>() {};
    assertThat(objectMapper.readValue(JSON_LINE, stopRef), is(RESPONSEDATA_LINE));
  }

  @Test
  void jsonSerializeWorksAsExpectedForTypeStopPoint() throws JsonProcessingException {
    assertThat(objectMapper.writeValueAsString(RESPONSEDATA_STOP_POINT), is(JSON_STOP_POINT));
  }

  @Test
  void jsonDeserializeWorksAsExpectedForTypeStopPoint() throws JsonProcessingException {
    TypeReference<ResponseData<StopPoint>> stopRef = new TypeReference<>() {};
    assertThat(objectMapper.readValue(JSON_STOP_POINT, stopRef), is(RESPONSEDATA_STOP_POINT));
  }
}
