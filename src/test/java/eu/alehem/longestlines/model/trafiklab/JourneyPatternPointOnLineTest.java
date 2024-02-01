package eu.alehem.longestlines.model.trafiklab;

import static eu.alehem.longestlines.testutil.DateUtil.dateFromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.alehem.longestlines.testutil.ObjectMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JourneyPatternPointOnLineTest {

  private static final JourneyPatternPointOnLine JOURNEY_PATTERN_POINT_ON_LINE =
      new JourneyPatternPointOnLine(
          1,
          1,
          10008,
          dateFromString("2022-02-15 00:00:00.000"),
          dateFromString("2022-02-15 00:00:00.000"));

  private static final String JSON =
      "{\"LineNumber\":1,"
          + "\"DirectionCode\":1,"
          + "\"JourneyPatternPointNumber\":10008,"
          + "\"LastModifiedUtcDateTime\":\"2022-02-15 00:00:00.000\","
          + "\"ExistsFromDate\":\"2022-02-15 00:00:00.000\"}";

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = ObjectMapperUtil.createObjectMapper();
  }

  @Test
  void jsonSerializeWorksAsExpected() throws JsonProcessingException {
    assertThat(objectMapper.writeValueAsString(JOURNEY_PATTERN_POINT_ON_LINE), is(JSON));
  }

  @Test
  void jsonDeserializeWorksAsExpected() throws JsonProcessingException {
    assertThat(
        objectMapper.readValue(JSON, JourneyPatternPointOnLine.class),
        is(JOURNEY_PATTERN_POINT_ON_LINE));
  }
}
