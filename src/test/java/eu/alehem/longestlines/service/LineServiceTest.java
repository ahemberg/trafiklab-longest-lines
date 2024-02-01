package eu.alehem.longestlines.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.alehem.longestlines.model.dao.BusLine;
import eu.alehem.longestlines.model.dto.LineDTO;
import eu.alehem.longestlines.repository.LineRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {

  private LineService lineService;

  @Mock private LineRepository lineRepository;

  @Captor ArgumentCaptor<List<BusLine>> lineCaptor;

  @BeforeEach
  void setUp() {
    lineService = new LineService(lineRepository);
  }

  @Test
  void insertLinesInvokesSaveAllWithExpectedParameters() {
    final List<BusLine> busLines =
        List.of(
            new BusLine(1, List.of(1), List.of(1)),
            new BusLine(2, List.of(1, 2, 3, 4), List.of()),
            new BusLine(3, List.of(1, 2, 3, 4), List.of(4, 2, 1)));
    lineService.insertLines(busLines);
    verify(lineRepository).saveAll(lineCaptor.capture());
    assertEquals(busLines, lineCaptor.getValue());
  }

  /* Save this for theJPA integration test!
  @Test
  void getLongestLinesReturnsNLongestLines() {
    final List<BusLine> linesInDb = List.of(
        new BusLine(1, List.of(1), List.of(1)), //TOTAL 2, O 1, I 1
        new BusLine(2, List.of(1, 2, 3, 4), List.of()), //TOTAL 4, O 4, I 0
        new BusLine(3, List.of(1, 2, 3, 4), List.of(4, 2, 1)), //TOTAL 7, O 4, I 3
        new BusLine(4, List.of(1, 2, 3), List.of(3, 2, 1)), //TOTAL 6, O 3, I 3
        new BusLine(5, List.of(1, 2, 3, 4), List.of(4, 2, 1))//TOTAL 6, O 4, I 3
    );
  } */

  @Test
  void getLongestLinesReturnsReturnsExpectedObject() {
    final List<BusLine> busLines =
        List.of(
            new BusLine(1, List.of(1), List.of(1)),
            new BusLine(2, List.of(1, 2, 3, 4), List.of()),
            new BusLine(3, List.of(1, 2, 3, 4), List.of(4, 2, 1)));

    final List<LineDTO> expected = List.of(new LineDTO(1), new LineDTO(2), new LineDTO(3));
    when(lineRepository.getLongestBusLines(10)).thenReturn(busLines);
    assertThat(lineService.getLongestLines(10), is(expected));
  }

  @Test
  void getLongestOutboundLinesReturnsReturnsExpectedObject() {
    final List<BusLine> busLines =
        List.of(
            new BusLine(1, List.of(1), List.of(1)),
            new BusLine(2, List.of(1, 2, 3, 4), List.of()),
            new BusLine(3, List.of(1, 2, 3, 4), List.of(4, 2, 1)));

    final List<LineDTO> expected = List.of(new LineDTO(1), new LineDTO(2), new LineDTO(3));
    when(lineRepository.getLongestOutboundBusLines(10)).thenReturn(busLines);
    assertThat(lineService.getLongestOutboundLines(10), is(expected));
  }

  @Test
  void getLongestInboundLinesReturnsReturnsExpectedObject() {
    final List<BusLine> busLines =
        List.of(
            new BusLine(1, List.of(1), List.of(1)),
            new BusLine(2, List.of(1, 2, 3, 4), List.of()),
            new BusLine(3, List.of(1, 2, 3, 4), List.of(4, 2, 1)));

    final List<LineDTO> expected = List.of(new LineDTO(1), new LineDTO(2), new LineDTO(3));
    when(lineRepository.getLongestInboundBusLines(10)).thenReturn(busLines);
    assertThat(lineService.getLongestInboundLines(10), is(expected));
  }

  @Test
  void getAllLinesInvokesFindAll() {
    lineService.getAllLines();
    verify(lineRepository, times(1)).findAll();
  }
}



/*
ublic void insertLines(List<BusLine> lines) {
    // TODO: Make sure that this actually updates
    lineRepository.saveAll(lines);
  }

  public List<LineDTO> getLongestLines(int limit) {
    return lineRepository.getLongestBusLines(10).stream()
        .map(bl -> new LineDTO(bl.getLineNumber()))
        .collect(Collectors.toList());
  }

  public List<LineDTO> getLongestOutboundLines(int limit) {
    return lineRepository.getLongestOutboundBusLines(10).stream()
        .map(bl -> new LineDTO(bl.getLineNumber()))
        .collect(Collectors.toList());
  }

  public List<LineDTO> getLongestInboundLines(int limit) {
    return lineRepository.getLongestInboundBusLines(10).stream()
        .map(bl -> new LineDTO(bl.getLineNumber()))
        .collect(Collectors.toList());
  }

  public List<LineDTO> getAllLines() {
    return lineRepository.findAll().stream()
        .map(bl -> new LineDTO(bl.getLineNumber()))
        .collect(Collectors.toList());
  }
 */
