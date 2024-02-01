package eu.alehem.longestlines.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import eu.alehem.longestlines.model.dto.LineDTO;
import eu.alehem.longestlines.model.dto.StopDTO;
import eu.alehem.longestlines.model.dto.StopsDTO;
import eu.alehem.longestlines.service.LineService;
import eu.alehem.longestlines.service.StopService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BusLineController.class)
class BusLineControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean
  LineService lineService;

  @MockBean
  StopService stopService;

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
  // TODO: Shouldnt we have a 404 here?
  void longestPathShouldReturnJsonWithLinkToSelfWhenNoLinesAreAvailable() throws Exception {
    when(lineService.getLongestLines(anyInt())).thenReturn(List.of());
    this.mockMvc
        .perform(get("/longest"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("_links.self.href", is("http://localhost/longest")))
        .andExpect(jsonPath("_embedded").doesNotExist())
        .andExpect(jsonPath("_links.stops").doesNotExist())
        .andExpect(
            content().json("{\"_links\":{\"self\":{\"href\":\"http://localhost/longest\"}}}"));
  }

  @Test
  void longestPathShouldReturnJsonWithLinksToLinesInExpectedOrder() throws Exception {
    List<LineDTO> expectedLongest = new ArrayList<>(10);
    expectedLongest.add(new LineDTO(1));
    expectedLongest.add(new LineDTO(2));
    expectedLongest.add(new LineDTO(3));
    expectedLongest.add(new LineDTO(4));
    expectedLongest.add(new LineDTO(5));
    expectedLongest.add(new LineDTO(6));
    expectedLongest.add(new LineDTO(7));
    expectedLongest.add(new LineDTO(8));
    expectedLongest.add(new LineDTO(9));
    expectedLongest.add(new LineDTO(10));

    when(lineService.getLongestLines(anyInt())).thenReturn(expectedLongest);
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
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[3].line_number", is(expectedLongest.get(3).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[4].line_number", is(expectedLongest.get(4).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[5].line_number", is(expectedLongest.get(5).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[6].line_number", is(expectedLongest.get(6).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[7].line_number", is(expectedLongest.get(7).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[8].line_number", is(expectedLongest.get(8).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[9].line_number", is(expectedLongest.get(9).lineNumber())))
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
                is("http://localhost/stops/" + expectedLongest.get(2).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[3].href",
                is("http://localhost/stops/" + expectedLongest.get(3).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[4].href",
                is("http://localhost/stops/" + expectedLongest.get(4).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[5].href",
                is("http://localhost/stops/" + expectedLongest.get(5).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[6].href",
                is("http://localhost/stops/" + expectedLongest.get(6).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[7].href",
                is("http://localhost/stops/" + expectedLongest.get(7).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[8].href",
                is("http://localhost/stops/" + expectedLongest.get(8).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[9].href",
                is("http://localhost/stops/" + expectedLongest.get(9).lineNumber())));
  }

  @Test
  void longestInboundPathShouldReturnJsonWithLinksToLinesInExpectedOrder() throws Exception {
    List<LineDTO> expectedLongest = new ArrayList<>(10);
    expectedLongest.add(new LineDTO(1));
    expectedLongest.add(new LineDTO(2));
    expectedLongest.add(new LineDTO(3));
    expectedLongest.add(new LineDTO(4));
    expectedLongest.add(new LineDTO(5));
    expectedLongest.add(new LineDTO(6));
    expectedLongest.add(new LineDTO(7));
    expectedLongest.add(new LineDTO(8));
    expectedLongest.add(new LineDTO(9));
    expectedLongest.add(new LineDTO(10));

    when(lineService.getLongestOutboundLines(anyInt())).thenReturn(expectedLongest);
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
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[3].line_number", is(expectedLongest.get(3).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[4].line_number", is(expectedLongest.get(4).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[5].line_number", is(expectedLongest.get(5).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[6].line_number", is(expectedLongest.get(6).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[7].line_number", is(expectedLongest.get(7).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[8].line_number", is(expectedLongest.get(8).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[9].line_number", is(expectedLongest.get(9).lineNumber())))
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
                is("http://localhost/stops/" + expectedLongest.get(2).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[3].href",
                is("http://localhost/stops/" + expectedLongest.get(3).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[4].href",
                is("http://localhost/stops/" + expectedLongest.get(4).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[5].href",
                is("http://localhost/stops/" + expectedLongest.get(5).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[6].href",
                is("http://localhost/stops/" + expectedLongest.get(6).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[7].href",
                is("http://localhost/stops/" + expectedLongest.get(7).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[8].href",
                is("http://localhost/stops/" + expectedLongest.get(8).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[9].href",
                is("http://localhost/stops/" + expectedLongest.get(9).lineNumber())));
  }

  @Test
  void longestOutboundPathShouldReturnJsonWithLinksToLinesInExpectedOrder() throws Exception {
    List<LineDTO> expectedLongest = new ArrayList<>(10);
    expectedLongest.add(new LineDTO(1));
    expectedLongest.add(new LineDTO(2));
    expectedLongest.add(new LineDTO(3));
    expectedLongest.add(new LineDTO(4));
    expectedLongest.add(new LineDTO(5));
    expectedLongest.add(new LineDTO(6));
    expectedLongest.add(new LineDTO(7));
    expectedLongest.add(new LineDTO(8));
    expectedLongest.add(new LineDTO(9));
    expectedLongest.add(new LineDTO(10));

    when(lineService.getLongestInboundLines(anyInt())).thenReturn(expectedLongest);
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
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[3].line_number", is(expectedLongest.get(3).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[4].line_number", is(expectedLongest.get(4).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[5].line_number", is(expectedLongest.get(5).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[6].line_number", is(expectedLongest.get(6).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[7].line_number", is(expectedLongest.get(7).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[8].line_number", is(expectedLongest.get(8).lineNumber())))
        .andExpect(
            jsonPath(
                "_embedded.lineDTOList[9].line_number", is(expectedLongest.get(9).lineNumber())))
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
                is("http://localhost/stops/" + expectedLongest.get(2).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[3].href",
                is("http://localhost/stops/" + expectedLongest.get(3).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[4].href",
                is("http://localhost/stops/" + expectedLongest.get(4).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[5].href",
                is("http://localhost/stops/" + expectedLongest.get(5).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[6].href",
                is("http://localhost/stops/" + expectedLongest.get(6).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[7].href",
                is("http://localhost/stops/" + expectedLongest.get(7).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[8].href",
                is("http://localhost/stops/" + expectedLongest.get(8).lineNumber())))
        .andExpect(
            jsonPath(
                "_links.stops[9].href",
                is("http://localhost/stops/" + expectedLongest.get(9).lineNumber())));
  }

  @Test
  void stopsShallReturn404WhenLineDoesNotExist() throws Exception {
    when(stopService.getAllStopsForLine(anyInt())).thenReturn(Optional.empty());
    this.mockMvc.perform(get("/stops/0")).andExpect(status().isNotFound());
  }

  @Test
  void stopsShallReturn400OnInvalidInput() throws Exception {
    when(stopService.getAllStopsForLine(anyInt())).thenReturn(Optional.empty());
    this.mockMvc.perform(get("/stops/invalid")).andExpect(status().isBadRequest());
  }

  @Test
  void stopsShallReturnExpectedResponseForValidLine() throws Exception {
    final StopsDTO expectedStops =
        new StopsDTO(
            1,
            List.of(
                new StopDTO(1, "First Stop"),
                new StopDTO(2, "Second Stop"),
                new StopDTO(3, "Third Stop"),
                new StopDTO(4, "Fourth Stop")),
            List.of(
                new StopDTO(4, "Fourth Stop"),
                new StopDTO(2, "Second Stop"),
                new StopDTO(1, "First Stop")));

    when(stopService.getAllStopsForLine(1)).thenReturn(Optional.of(expectedStops));
    this.mockMvc
        .perform(get("/stops/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("line_number", is(1)))
        .andExpect(jsonPath("outbound_stops[0].id", is(1)))
        .andExpect(jsonPath("outbound_stops[0].name", is("First Stop")))
        .andExpect(jsonPath("outbound_stops[1].id", is(2)))
        .andExpect(jsonPath("outbound_stops[1].name", is("Second Stop")))
        .andExpect(jsonPath("outbound_stops[2].id", is(3)))
        .andExpect(jsonPath("outbound_stops[2].name", is("Third Stop")))
        .andExpect(jsonPath("outbound_stops[3].id", is(4)))
        .andExpect(jsonPath("outbound_stops[3].name", is("Fourth Stop")))
        .andExpect(jsonPath("inbound_stops[0].id", is(4)))
        .andExpect(jsonPath("inbound_stops[0].name", is("Fourth Stop")))
        .andExpect(jsonPath("inbound_stops[1].id", is(2)))
        .andExpect(jsonPath("inbound_stops[1].name", is("Second Stop")))
        .andExpect(jsonPath("inbound_stops[2].id", is(1)))
        .andExpect(jsonPath("inbound_stops[2].name", is("First Stop")))
        .andExpect(jsonPath("_links.self.href", is("http://localhost/stops/1")))
        .andExpect(jsonPath("_links.longest.href", is("http://localhost/longest")))
        .andExpect(
            jsonPath("_links.longest/outbound.href", is("http://localhost/longest/outbound")))
        .andExpect(jsonPath("_links.longest/inbound.href", is("http://localhost/longest/inbound")));
  }
}
