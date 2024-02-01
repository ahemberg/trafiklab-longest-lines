package eu.alehem.longestlines.model.trafiklab;

import static eu.alehem.longestlines.testutil.DateUtil.dateFromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.alehem.longestlines.testutil.ObjectMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StopPointTest {

  private static final StopPoint STOP_POINT =
      new StopPoint(
          10001,
          "Stadshagsplan",
          10001,
          59.3373571967995,
          18.0214674159693,
          ZoneShortName.A,
          StopAreaTypeCode.BUSTERM,
          dateFromString("2022-10-28 00:00:00.000"),
          dateFromString("2022-10-28 00:00:00.000"));

  private static final String JSON =
      "{"
          + "\"StopPointNumber\":10001,"
          + "\"StopPointName\":\"Stadshagsplan\","
          + "\"StopAreaNumber\":10001,"
          + "\"LocationNorthingCoordinate\":59.3373571967995,"
          + "\"LocationEastingCoordinate\":18.0214674159693,"
          + "\"ZoneShortName\":\"A\","
          + "\"StopAreaTypeCode\":\"BUSTERM\","
          + "\"LastModifiedUtcDateTime\":\"2022-10-28 00:00:00.000\","
          + "\"ExistsFromDate\":\"2022-10-28 00:00:00.000\"}";

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = ObjectMapperUtil.createObjectMapper();
  }

  @Test
  void jsonSerializeWorksAsExpected() throws JsonProcessingException {
    assertThat(objectMapper.writeValueAsString(STOP_POINT), is(JSON));
  }

  @Test
  void jsonDeserializeWorksAsExpected() throws JsonProcessingException {
    assertThat(objectMapper.readValue(JSON, StopPoint.class), is(STOP_POINT));
  }
}
