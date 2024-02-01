package eu.alehem.longestlines.model.trafiklab;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.alehem.longestlines.testutil.ObjectMapperUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineTest {

  private static final Line LINE =
      new Line(
          1,
          "1",
          "blåbuss",
          DefaultTransportModeCode.BUS,
          dateFromString("2007-08-24 00:00:00.000"),
          dateFromString("2007-08-24 00:00:00.000"));

  private static final String JSON =
      "{\"LineNumber\":1,"
          + "\"LineDesignation\":\"1\","
          + "\"DefaultTransportMode\":\"blåbuss\","
          + "\"DefaultTransportModeCode\":\"BUS\","
          + "\"LastModifiedUtcDateTime\":\"2007-08-24 00:00:00.000\","
          + "\"ExistsFromDate\":\"2007-08-24 00:00:00.000\"}";

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = ObjectMapperUtil.createObjectMapper();
  }

  @Test
  void jsonSerializeWorksAsExpected() throws JsonProcessingException {
    assertThat(objectMapper.writeValueAsString(LINE), is(JSON));
  }

  @Test
  void jsonDeserializeWorksAsExpected() throws JsonProcessingException {
    assertThat(objectMapper.readValue(JSON, Line.class), is(LINE));
  }

  private static Date dateFromString(String dateString) {
    try {
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      format.setTimeZone(TimeZone.getTimeZone("UTC"));
      return format.parse(dateString);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
