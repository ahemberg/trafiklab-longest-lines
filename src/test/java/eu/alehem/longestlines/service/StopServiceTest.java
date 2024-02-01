package eu.alehem.longestlines.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.alehem.longestlines.model.dao.BusLine;
import eu.alehem.longestlines.model.dao.BusStop;
import eu.alehem.longestlines.model.dto.StopDTO;
import eu.alehem.longestlines.model.dto.StopsDTO;
import eu.alehem.longestlines.repository.LineRepository;
import eu.alehem.longestlines.repository.StopRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StopServiceTest {

  private StopService stopService;

  @Mock private StopRepository stopRepository;

  @Mock private LineRepository lineRepository;

  @Captor ArgumentCaptor<List<BusStop>> busstopCaptor;

  @BeforeEach
  void setUp() {
    stopService = new StopService(stopRepository, lineRepository);
  }

  @Test
  void insertStopsInvokesSaveAllWithExpectedParameters() {
    final List<BusStop> busStops =
        List.of(
            new BusStop(1, "First Stop"),
            new BusStop(2, "Second Stop"),
            new BusStop(3, "Third Stop"),
            new BusStop(4, "Fourth Stop"));

    stopService.insertStops(busStops);

    verify(stopRepository).saveAll(busstopCaptor.capture());
    assertEquals(busStops, busstopCaptor.getValue());
  }

  @Test
  void getAllStopsForLineReturnsEmptyWhenLineNotFound() {
    when(lineRepository.getLineByNumber(anyInt())).thenReturn(Optional.empty());
    assertThat(stopService.getAllStopsForLine(1), is(Optional.empty()));
  }

  @Test
  void getAllStopsForLineReturnsExpectedObjectWhenObjectFound() {
    final BusLine busLine = new BusLine(1, List.of(1, 2, 3, 4), List.of(4, 2, 1));

    final List<BusStop> outboundStops =
        List.of(
            new BusStop(1, "First Stop"),
            new BusStop(2, "Second Stop"),
            new BusStop(3, "Third Stop"),
            new BusStop(4, "Fourth Stop"));

    final List<BusStop> inBoundStops =
        List.of(
            new BusStop(1, "First Stop"),
            new BusStop(2, "Second Stop"),
            new BusStop(4, "Fourth Stop"));

    final StopsDTO expected =
        new StopsDTO(
            1,
            List.of(
                new StopDTO(1, "First Stop"),
                new StopDTO(2, "Second Stop"),
                new StopDTO(3, "Third Stop"),
                new StopDTO(4, "Fourth Stop")),
            List.of(
                new StopDTO(1, "First Stop"),
                new StopDTO(2, "Second Stop"),
                new StopDTO(4, "Fourth Stop")));

    when(lineRepository.getLineByNumber(1)).thenReturn(Optional.of(busLine));
    when(stopRepository.getAllStopsInList(List.of(1, 2, 3, 4))).thenReturn(outboundStops);
    when(stopRepository.getAllStopsInList(List.of(4, 2, 1))).thenReturn(inBoundStops);
    assertThat(stopService.getAllStopsForLine(1), is(Optional.of(expected)));
  }

  @Test
  void getAllStopsForLineReturnsDtoWithoutOutgoingBusStopsWhenNoOutGoingBusStopsMatch() {
    final BusLine busLine = new BusLine(1, List.of(5, 6, 7), List.of(4, 2, 1));

    final List<BusStop> inBoundStops =
        List.of(
            new BusStop(1, "First Stop"),
            new BusStop(2, "Second Stop"),
            new BusStop(4, "Fourth Stop"));

    final StopsDTO expected =
        new StopsDTO(
            1,
            List.of(),
            List.of(
                new StopDTO(1, "First Stop"),
                new StopDTO(2, "Second Stop"),
                new StopDTO(4, "Fourth Stop")));

    when(lineRepository.getLineByNumber(1)).thenReturn(Optional.of(busLine));
    when(stopRepository.getAllStopsInList(List.of(5, 6, 7))).thenReturn(List.of());
    when(stopRepository.getAllStopsInList(List.of(4, 2, 1))).thenReturn(inBoundStops);
    assertThat(stopService.getAllStopsForLine(1), is(Optional.of(expected)));
  }

  @Test
  void getAllStopsForLineReturnsDtoWithoutBusStopsWhenNoBusInboundStopsMatch() {
    final BusLine busLine = new BusLine(1, List.of(5, 6, 7), List.of(4, 2, 1));

    final List<BusStop> outBoundStops =
        List.of(
            new BusStop(5, "Fifth Stop"),
            new BusStop(6, "Sixth Stop"),
            new BusStop(7, "Seventh Stop"));

    final StopsDTO expected =
        new StopsDTO(
            1,
            List.of(
                new StopDTO(5, "Fifth Stop"),
                new StopDTO(6, "Sixth Stop"),
                new StopDTO(7, "Seventh Stop")),
            List.of());

    when(lineRepository.getLineByNumber(1)).thenReturn(Optional.of(busLine));
    when(stopRepository.getAllStopsInList(List.of(5, 6, 7))).thenReturn(outBoundStops);
    when(stopRepository.getAllStopsInList(List.of(4, 2, 1))).thenReturn(List.of());
    assertThat(stopService.getAllStopsForLine(1), is(Optional.of(expected)));
  }

  @Test
  void getAllStopsForLineReturnsDtoWithoutBusStopsWhenNoBusStopsMatch() {
    final BusLine busLine = new BusLine(1, List.of(1, 2, 3, 4), List.of(4, 2, 1));

    final StopsDTO expected = new StopsDTO(1, List.of(), List.of());

    when(lineRepository.getLineByNumber(1)).thenReturn(Optional.of(busLine));
    when(stopRepository.getAllStopsInList(List.of(1, 2, 3, 4))).thenReturn(List.of());
    when(stopRepository.getAllStopsInList(List.of(4, 2, 1))).thenReturn(List.of());
    assertThat(stopService.getAllStopsForLine(1), is(Optional.of(expected)));
  }

  @Test
  void getAllStopsReturnsAllStops() {
    final List<BusStop> busStops =
        List.of(
            new BusStop(1, "First Stop"),
            new BusStop(2, "Second Stop"),
            new BusStop(3, "Third Stop"),
            new BusStop(4, "Fourth Stop"));

    final List<StopDTO> expected =
        List.of(
            new StopDTO(1, "First Stop"),
            new StopDTO(2, "Second Stop"),
            new StopDTO(3, "Third Stop"),
            new StopDTO(4, "Fourth Stop"));

    when(stopRepository.findAll()).thenReturn(busStops);
    assertThat(stopService.getAllStops(), is(expected));
  }
}
