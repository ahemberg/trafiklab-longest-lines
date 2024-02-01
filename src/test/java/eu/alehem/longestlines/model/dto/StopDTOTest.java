package eu.alehem.longestlines.model.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.alehem.longestlines.testutil.ObjectMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StopDTOTest {
  private static final StopDTO STOP_DTO = new StopDTO(1, "First Stop");

  private static final String JSON = "{\"id\":1,\"name\":\"First Stop\"}";

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = ObjectMapperUtil.createObjectMapper();
  }

  @Test
  void jsonSerializeWorksAsExpected() throws JsonProcessingException {
    assertThat(objectMapper.writeValueAsString(STOP_DTO), is(JSON));
  }

  @Test
  void jsonDeserializeWorksAsExpected() throws JsonProcessingException {
    assertThat(objectMapper.readValue(JSON, StopDTO.class), is(STOP_DTO));
  }
}
