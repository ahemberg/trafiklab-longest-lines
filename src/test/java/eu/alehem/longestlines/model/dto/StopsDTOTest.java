package eu.alehem.longestlines.model.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.alehem.longestlines.testutil.ObjectMapperUtil;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StopsDTOTest {

  private static final StopsDTO STOPS_DTO =
      new StopsDTO(
          1,
          List.of(new StopDTO(1, "First Stop"), new StopDTO(2, "Second Stop")),
          List.of(
              new StopDTO(3, "Third Stop"),
              new StopDTO(2, "Second Stop"),
              new StopDTO(1, "First Stop")));

  private static final String JSON =
      "{"
          + "\"line_number\":1,"
          + "\"outbound_stops\":["
          + "{\"id\":1,\"name\":\"First Stop\"},"
          + "{\"id\":2,\"name\":\"Second Stop\"}],"
          + "\"inbound_stops\":["
          + "{\"id\":3,\"name\":\"Third Stop\"},"
          + "{\"id\":2,\"name\":\"Second Stop\"},"
          + "{\"id\":1,\"name\":\"First Stop\"}]}";

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = ObjectMapperUtil.createObjectMapper();
  }

  @Test
  void jsonSerializeWorksAsExpected() throws JsonProcessingException {
    assertThat(objectMapper.writeValueAsString(STOPS_DTO), is(JSON));
  }

  @Test
  void jsonDeserializeWorksAsExpected() throws JsonProcessingException {
    assertThat(objectMapper.readValue(JSON, StopsDTO.class), is(STOPS_DTO));
  }
}
