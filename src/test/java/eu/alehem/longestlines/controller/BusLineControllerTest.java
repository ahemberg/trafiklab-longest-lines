package eu.alehem.longestlines.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import eu.alehem.longestlines.model.dto.LineDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BusLineController.class)
class BusLineControllerTest {
  @Autowired private MockMvc mockMvc;

  @Test
  void rootPathShouldReturnLinkToLongestLines() throws Exception {
    this.mockMvc
        .perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("href", is("http://localhost/longest")))
        .andExpect(
            jsonPath("_links.longest/outbound.href", is("http://localhost/longest/outbound")))
        .andExpect(jsonPath("_links.longest/inbound.href", is("http://localhost/longest/inbound")));
  }

  @Test
  void longestPathShouldReturnJsonWithLinkToSelfWhenNoLinesAreAvailable() {
    // Not Implemented
  }

  @Test
  void longestPathShouldReturnJsonWithLinksToLinesInExpectedOrder() throws Exception {
    List<LineDTO> expectedLongest = List.of(new LineDTO(1), new LineDTO(2), new LineDTO(3));

    this.mockMvc
        .perform(get("/longest"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[0].line_number", is(expectedLongest.get(0).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[1].line_number", is(expectedLongest.get(1).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[2].line_number", is(expectedLongest.get(2).lineNumber())))
        .andExpect(jsonPath("_links.self.href", is("http://localhost/longest")))
        .andExpect(
            jsonPath(
                "_links.stops[0].href",
                is("http://localhost/stops/" + expectedLongest.get(0).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[1].href",
                is("http://localhost/stops/" + expectedLongest.get(1).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[2].href",
                is("http://localhost/stops/" + expectedLongest.get(2).lineNumber())));
  }

  @Test
  void longestInboundPathShouldReturnJsonWithLinksToLinesInExpectedOrder() throws Exception {
    List<LineDTO> expectedLongest = List.of(new LineDTO(1), new LineDTO(2), new LineDTO(3));

    this.mockMvc
        .perform(get("/longest/outbound"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[0].line_number", is(expectedLongest.get(0).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[1].line_number", is(expectedLongest.get(1).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[2].line_number", is(expectedLongest.get(2).lineNumber())))
        .andExpect(jsonPath("_links.self.href", is("http://localhost/longest/outbound")))
        .andExpect(
            jsonPath(
                "_links.stops[0].href",
                is("http://localhost/stops/" + expectedLongest.get(0).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[1].href",
                is("http://localhost/stops/" + expectedLongest.get(1).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[2].href",
                is("http://localhost/stops/" + expectedLongest.get(2).lineNumber())));
  }

  @Test
  void longestOutboundPathShouldReturnJsonWithLinksToLinesInExpectedOrder() throws Exception {
    List<LineDTO> expectedLongest = List.of(new LineDTO(1), new LineDTO(2), new LineDTO(3));

    this.mockMvc
        .perform(get("/longest/inbound"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[0].line_number", is(expectedLongest.get(0).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[1].line_number", is(expectedLongest.get(1).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[2].line_number", is(expectedLongest.get(2).lineNumber())))
        .andExpect(jsonPath("_links.self.href", is("http://localhost/longest/inbound")))
        .andExpect(
            jsonPath(
                "_links.stops[0].href",
                is("http://localhost/stops/" + expectedLongest.get(0).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[1].href",
                is("http://localhost/stops/" + expectedLongest.get(1).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[2].href",
                is("http://localhost/stops/" + expectedLongest.get(2).lineNumber())));
  }

  @Test
  void stopsShallReturn404WhenLineDoesNotExist() {
    // NOT Implemented
  }

  @Test
  void stopsShallReturn400OnInvalidInput() {
    // NOT Implemented
  }

  @Test
  void stopsShallReturnExpectedResponseForValidLine() throws Exception {
    this.mockMvc
        .perform(get("/stops/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("line_number", is(1)))
        .andExpect(jsonPath("outbound_stops[0].id", is(1)))
        .andExpect(jsonPath("outbound_stops[0].name", is("1")))
        .andExpect(jsonPath("outbound_stops[1].id", is(2)))
        .andExpect(jsonPath("outbound_stops[1].name", is("2")))
        .andExpect(jsonPath("outbound_stops[2].id", is(3)))
        .andExpect(jsonPath("outbound_stops[2].name", is("3")))
        .andExpect(jsonPath("inbound_stops[0].id", is(1)))
        .andExpect(jsonPath("inbound_stops[0].name", is("1")))
        .andExpect(jsonPath("inbound_stops[1].id", is(2)))
        .andExpect(jsonPath("inbound_stops[1].name", is("2")))
        .andExpect(jsonPath("inbound_stops[2].id", is(3)))
        .andExpect(jsonPath("inbound_stops[2].name", is("3")))
        .andExpect(jsonPath("_links.self.href", is("http://localhost/stops/1")))
        .andExpect(jsonPath("_links.longest.href", is("http://localhost/longest")))
        .andExpect(
            jsonPath("_links.longest/outbound.href", is("http://localhost/longest/outbound")))
        .andExpect(jsonPath("_links.longest/inbound.href", is("http://localhost/longest/inbound")));
  }
}
