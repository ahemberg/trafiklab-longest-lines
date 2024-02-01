package eu.alehem.longestlines.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.alehem.longestlines.model.dao.BusLine;
import eu.alehem.longestlines.model.dao.BusStop;
import eu.alehem.longestlines.model.trafiklab.DefaultTransportModeCode;
import eu.alehem.longestlines.model.trafiklab.JourneyPatternPointOnLine;
import eu.alehem.longestlines.model.trafiklab.Line;
import eu.alehem.longestlines.model.trafiklab.StopAreaTypeCode;
import eu.alehem.longestlines.model.trafiklab.StopPoint;
import eu.alehem.longestlines.model.trafiklab.TrafikLabResponse;
import eu.alehem.longestlines.model.trafiklab.ZoneShortName;
import eu.alehem.longestlines.testutil.DateUtil;
import eu.alehem.longestlines.testutil.TrafikLabFixtures;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class TrafikLabLoaderServiceTest {

  @Mock private StopService stopService;

  @Mock private LineService lineService;

  @Mock private TrafiklabService trafiklabService;

  @Captor ArgumentCaptor<List<BusStop>> busStopCaptor;
  @Captor ArgumentCaptor<List<BusLine>> busLineCaptor;

  private TrafikLabLoaderService trafikLabLoaderService;

  @BeforeEach
  void setUp() {
    trafikLabLoaderService = new TrafikLabLoaderService(lineService, stopService, trafiklabService);
  }

  @Test
  void updateDatabaseInsertsExpectedBusStops() {

    final TrafikLabResponse<StopPoint> mockStopResponse =
        TrafikLabFixtures.defaultTrafikLabResponseStopPoint(
            TrafikLabFixtures.defaultResponseDataStopPoint(
                List.of(
                    new StopPoint(
                        10001,
                        "Stadshagsplan",
                        10001,
                        59.3373571967995,
                        18.0214674159693,
                        ZoneShortName.A,
                        StopAreaTypeCode.BUSTERM,
                        DateUtil.dateFromString("2022-10-28 00:00:00.000"),
                        DateUtil.dateFromString("2022-10-28 00:00:00.000")))));

    final TrafikLabResponse<Line> mockLineResponse =
        TrafikLabFixtures.defaultTrafikLabResponseLine();
    final TrafikLabResponse<JourneyPatternPointOnLine> mockJourResponse =
        TrafikLabFixtures.defaultTrafikLabResponseJourneyPatternPointOnLine();

    final List<BusStop> expectedGeneratedStops = List.of(new BusStop(10001, "Stadshagsplan"));

    when(trafiklabService.getStopPoints()).thenReturn(Mono.just(mockStopResponse));
    doNothing().when(stopService).insertStops(any());
    when(trafiklabService.getJourneyPatterns()).thenReturn(Mono.just(mockJourResponse));
    when(trafiklabService.getSLBusLines()).thenReturn(Mono.just(mockLineResponse));
    doNothing().when(lineService).insertLines(any());

    trafikLabLoaderService.updateDatabase();
    verify(stopService).insertStops(busStopCaptor.capture());
    assertEquals(expectedGeneratedStops, busStopCaptor.getValue());
  }

  @Test
  void updateDatabaseInsertsExpectedBusLines() {

    final TrafikLabResponse<StopPoint> mockStopResponse =
        TrafikLabFixtures.defaultTrafikLabResponseStopPoint();

    final TrafikLabResponse<Line> mockLineResponse =
        TrafikLabFixtures.defaultTrafikLabResponseLine(
            TrafikLabFixtures.defaultResponseDataLine(
                List.of(
                    new Line(
                        1,
                        "1",
                        "blåbuss",
                        DefaultTransportModeCode.BUS,
                        DateUtil.dateFromString("2007-08-24 00:00:00.000"),
                        DateUtil.dateFromString("2007-08-24 00:00:00.000")),
                    new Line(
                        101,
                        "101",
                        "",
                        DefaultTransportModeCode.BUS,
                        DateUtil.dateFromString("2023-06-08 00:00:00.000"),
                        DateUtil.dateFromString("2023-06-08 00:00:00.000")))));

    final TrafikLabResponse<JourneyPatternPointOnLine> mockJourResponse =
        TrafikLabFixtures.defaultTrafikLabResponseJourneyPatternPointOnLine(
            TrafikLabFixtures.defaultResponseJourneyPatternPointOnLine(
                List.of(
                    new JourneyPatternPointOnLine(
                        1,
                        1,
                        10008,
                        DateUtil.dateFromString("2022-02-15 00:00:00.000"),
                        DateUtil.dateFromString("2022-02-15 00:00:00.000")),
                    new JourneyPatternPointOnLine(
                        1,
                        1,
                        10012,
                        DateUtil.dateFromString("2023-03-07 00:00:00.000"),
                        DateUtil.dateFromString("2023-03-07 00:00:00.000")),
                    new JourneyPatternPointOnLine(
                        1,
                        2,
                        10008,
                        DateUtil.dateFromString("2022-02-15 00:00:00.000"),
                        DateUtil.dateFromString("2022-02-15 00:00:00.000")),
                    new JourneyPatternPointOnLine(
                        1,
                        2,
                        10012,
                        DateUtil.dateFromString("2023-03-07 00:00:00.000"),
                        DateUtil.dateFromString("2023-03-07 00:00:00.000")),
                    new JourneyPatternPointOnLine(
                        101,
                        1,
                        10025,
                        DateUtil.dateFromString("2022-02-15 00:00:00.000"),
                        DateUtil.dateFromString("2022-02-15 00:00:00.000")),
                    new JourneyPatternPointOnLine(
                        101,
                        1,
                        10026,
                        DateUtil.dateFromString("2023-03-07 00:00:00.000"),
                        DateUtil.dateFromString("2023-03-07 00:00:00.000")))));

    final List<BusLine> expectedGeneratedLines =
        List.of(
            new BusLine(1, List.of(10008, 10012), List.of(10008, 10012)),
            new BusLine(101, List.of(10025, 10026), List.of()));

    when(trafiklabService.getStopPoints()).thenReturn(Mono.just(mockStopResponse));
    doNothing().when(stopService).insertStops(any());
    when(trafiklabService.getJourneyPatterns()).thenReturn(Mono.just(mockJourResponse));
    when(trafiklabService.getSLBusLines()).thenReturn(Mono.just(mockLineResponse));
    doNothing().when(lineService).insertLines(any());

    trafikLabLoaderService.updateDatabase();

    verify(lineService).insertLines(busLineCaptor.capture());
    assertEquals(expectedGeneratedLines, busLineCaptor.getValue());
  }

  @Test
  void updateDatabaseDoesNotInsertBusStopsOnFailedResponseFromStopApi() {
    final TrafikLabResponse<StopPoint> failedResponse = TrafikLabFixtures.trafikLabErrorResponse();
    final TrafikLabResponse<Line> lineResponse = TrafikLabFixtures.defaultTrafikLabResponseLine();
    final TrafikLabResponse<JourneyPatternPointOnLine> journeyPatternResponse =
        TrafikLabFixtures.defaultTrafikLabResponseJourneyPatternPointOnLine();

    when(trafiklabService.getStopPoints()).thenReturn(Mono.just(failedResponse));
    when(trafiklabService.getJourneyPatterns()).thenReturn(Mono.just(journeyPatternResponse));
    when(trafiklabService.getSLBusLines()).thenReturn(Mono.just(lineResponse));
    doNothing().when(lineService).insertLines(any());

    trafikLabLoaderService.updateDatabase();
    verify(stopService, never()).insertStops(any());
  }

  @Test
  void updateDatabaseDoesNotInsertBusStopsOnErrorResponseFromStopApi() {

    final TrafikLabResponse<Line> lineResponse = TrafikLabFixtures.defaultTrafikLabResponseLine();
    final TrafikLabResponse<JourneyPatternPointOnLine> journeyPatternResponse =
        TrafikLabFixtures.defaultTrafikLabResponseJourneyPatternPointOnLine();

    when(trafiklabService.getStopPoints()).thenReturn(Mono.error(RuntimeException::new));
    when(trafiklabService.getJourneyPatterns()).thenReturn(Mono.just(journeyPatternResponse));
    when(trafiklabService.getSLBusLines()).thenReturn(Mono.just(lineResponse));
    doNothing().when(lineService).insertLines(any());

    trafikLabLoaderService.updateDatabase();
    verify(stopService, never()).insertStops(any());
  }

  @Test
  void updateDatabaseDoesNotInsertBusLinesOnFailedResponseFromLineApi() {
    final TrafikLabResponse<Line> failedResponse = TrafikLabFixtures.trafikLabErrorResponse();

    final TrafikLabResponse<StopPoint> stopPointResponse =
        TrafikLabFixtures.defaultTrafikLabResponseStopPoint();
    final TrafikLabResponse<JourneyPatternPointOnLine> journeyPatternResponse =
        TrafikLabFixtures.defaultTrafikLabResponseJourneyPatternPointOnLine();

    when(trafiklabService.getStopPoints()).thenReturn(Mono.just(stopPointResponse));
    doNothing().when(stopService).insertStops(any());
    when(trafiklabService.getJourneyPatterns()).thenReturn(Mono.just(journeyPatternResponse));
    when(trafiklabService.getSLBusLines()).thenReturn(Mono.just(failedResponse));

    trafikLabLoaderService.updateDatabase();
    verify(lineService, never()).insertLines(any());
  }

  @Test
  void updateDatabaseDoesNotInsertBusLinesOnErrorResponseFromLineApi() {
    final TrafikLabResponse<StopPoint> stopPointResponse =
        TrafikLabFixtures.defaultTrafikLabResponseStopPoint();
    final TrafikLabResponse<JourneyPatternPointOnLine> journeyPatternResponse =
        TrafikLabFixtures.defaultTrafikLabResponseJourneyPatternPointOnLine();

    when(trafiklabService.getStopPoints()).thenReturn(Mono.just(stopPointResponse));
    doNothing().when(stopService).insertStops(any());
    when(trafiklabService.getJourneyPatterns()).thenReturn(Mono.just(journeyPatternResponse));
    when(trafiklabService.getSLBusLines()).thenReturn(Mono.error(RuntimeException::new));

    trafikLabLoaderService.updateDatabase();
    verify(lineService, never()).insertLines(any());
  }

  @Test
  void updateDatabaseDoesNotInsertBusLinesOnFailedResponseFromJourneyPointApi() {
    final TrafikLabResponse<JourneyPatternPointOnLine> failedResponse =
        TrafikLabFixtures.trafikLabErrorResponse();

    final TrafikLabResponse<Line> lineResponse = TrafikLabFixtures.defaultTrafikLabResponseLine();
    final TrafikLabResponse<StopPoint> stopPointResponse =
        TrafikLabFixtures.defaultTrafikLabResponseStopPoint();

    when(trafiklabService.getStopPoints()).thenReturn(Mono.just(stopPointResponse));
    doNothing().when(stopService).insertStops(any());
    when(trafiklabService.getJourneyPatterns()).thenReturn(Mono.just(failedResponse));
    when(trafiklabService.getSLBusLines()).thenReturn(Mono.just(lineResponse));

    trafikLabLoaderService.updateDatabase();
    verify(lineService, never()).insertLines(any());
  }

  @Test
  void updateDatabaseDoesNotInsertBusLinesOnErrorResponseFromJourneyPointApi() {
    final TrafikLabResponse<Line> lineResponse = TrafikLabFixtures.defaultTrafikLabResponseLine();
    final TrafikLabResponse<StopPoint> stopPointResponse =
        TrafikLabFixtures.defaultTrafikLabResponseStopPoint();

    when(trafiklabService.getStopPoints()).thenReturn(Mono.just(stopPointResponse));
    doNothing().when(stopService).insertStops(any());
    when(trafiklabService.getJourneyPatterns()).thenReturn(Mono.error(RuntimeException::new));
    when(trafiklabService.getSLBusLines()).thenReturn(Mono.just(lineResponse));

    trafikLabLoaderService.updateDatabase();
    verify(lineService, never()).insertLines(any());
  }
}
