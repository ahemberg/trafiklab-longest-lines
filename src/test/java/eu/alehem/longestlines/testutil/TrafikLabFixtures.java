package eu.alehem.longestlines.testutil;

import eu.alehem.longestlines.model.trafiklab.DefaultTransportModeCode;
import eu.alehem.longestlines.model.trafiklab.JourneyPatternPointOnLine;
import eu.alehem.longestlines.model.trafiklab.Line;
import eu.alehem.longestlines.model.trafiklab.ResponseData;
import eu.alehem.longestlines.model.trafiklab.StopAreaTypeCode;
import eu.alehem.longestlines.model.trafiklab.StopPoint;
import eu.alehem.longestlines.model.trafiklab.TrafikLabData;
import eu.alehem.longestlines.model.trafiklab.TrafikLabResponse;
import eu.alehem.longestlines.model.trafiklab.ZoneShortName;
import java.util.List;
import java.util.Optional;

public class TrafikLabFixtures {

  public static <T extends TrafikLabData> TrafikLabResponse<T> trafikLabErrorResponse() {
    return trafikLabErrorResponse(1002, "Key is invalid");
  }

  public static <T extends TrafikLabData> TrafikLabResponse<T> trafikLabErrorResponse(
      int statusCode, String message) {
    return new TrafikLabResponse<>(
        statusCode, Optional.of(message), Optional.empty(), Optional.empty());
  }

  public static String trafikLabErrorResponseJson() {
    return trafikLabErrorResponseJson(1002, "Key is invalid");
  }

  public static String trafikLabErrorResponseJson(int statusCode, String message) {
    return "{\"StatusCode\":" + statusCode + ",\"Message\":\"" + message + "\"}";
  }

  public static ResponseData<JourneyPatternPointOnLine> defaultResponseJourneyPatternPointOnLine() {
    return defaultResponseJourneyPatternPointOnLine(
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
                1,
                10014,
                DateUtil.dateFromString("2022-08-10 00:00:00.000"),
                DateUtil.dateFromString("2022-08-10 00:00:00.000"))));
  }

  public static ResponseData<JourneyPatternPointOnLine> defaultResponseJourneyPatternPointOnLine(
      List<JourneyPatternPointOnLine> journeyPatterns) {
    return new ResponseData<>(
        DateUtil.dateFromString("2024-01-23 00:13", "yyyy-MM-dd HH:mm"),
        "JourneyPatternPointOnLine",
        journeyPatterns);
  }

  public static ResponseData<Line> defaultResponseDataLine() {
    return defaultResponseDataLine(
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
                DateUtil.dateFromString("2023-06-08 00:00:00.000")),
            new Line(
                112,
                "112",
                "",
                DefaultTransportModeCode.BUS,
                DateUtil.dateFromString("2007-08-24 00:00:00.000"),
                DateUtil.dateFromString("2007-08-24 00:00:00.000"))));
  }

  public static ResponseData<Line> defaultResponseDataLine(List<Line> lines) {
    return new ResponseData<>(
        DateUtil.dateFromString("2024-01-31 00:13", "yyyy-MM-dd HH:mm"), "Line", lines);
  }

  public static ResponseData<StopPoint> defaultResponseDataStopPoint() {
    return defaultResponseDataStopPoint(
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
                DateUtil.dateFromString("2022-10-28 00:00:00.000")),
            new StopPoint(
                10002,
                "John Bergs plan",
                10002,
                59.3361450073188,
                18.0222866342593,
                ZoneShortName.A,
                StopAreaTypeCode.BUSTERM,
                DateUtil.dateFromString("2015-09-24 00:00:00.000"),
                DateUtil.dateFromString("2015-09-24 00:00:00.000")),
            new StopPoint(
                10006,
                "Arbetargatan",
                10006,
                59.3352143599364,
                18.0270636513120,
                ZoneShortName.A,
                StopAreaTypeCode.BUSTERM,
                DateUtil.dateFromString("2022-09-29 00:00:00.000"),
                DateUtil.dateFromString("2022-09-29 00:00:00.000"))));
  }

  public static ResponseData<StopPoint> defaultResponseDataStopPoint(List<StopPoint> stopPoints) {
    return new ResponseData<>(
        DateUtil.dateFromString("2024-01-23 00:13", "yyyy-MM-dd HH:mm"), "StopPoint", stopPoints);
  }

  public static TrafikLabResponse<JourneyPatternPointOnLine>
      defaultTrafikLabResponseJourneyPatternPointOnLine() {
    return defaultTrafikLabResponseJourneyPatternPointOnLine(
        defaultResponseJourneyPatternPointOnLine());
  }

  public static TrafikLabResponse<JourneyPatternPointOnLine>
      defaultTrafikLabResponseJourneyPatternPointOnLine(
          ResponseData<JourneyPatternPointOnLine> customResponseData) {
    return new TrafikLabResponse<>(
        0, Optional.empty(), Optional.of(316), Optional.of(customResponseData));
  }

  public static TrafikLabResponse<Line> defaultTrafikLabResponseLine() {
    return defaultTrafikLabResponseLine(defaultResponseDataLine());
  }

  public static TrafikLabResponse<Line> defaultTrafikLabResponseLine(
      ResponseData<Line> customResponseData) {
    return new TrafikLabResponse<>(
        0, Optional.empty(), Optional.of(402), Optional.of(customResponseData));
  }

  public static TrafikLabResponse<StopPoint> defaultTrafikLabResponseStopPoint() {
    return defaultTrafikLabResponseStopPoint(defaultResponseDataStopPoint());
  }

  public static TrafikLabResponse<StopPoint> defaultTrafikLabResponseStopPoint(
      ResponseData<StopPoint> customResponseData) {
    return new TrafikLabResponse<>(
        0, Optional.empty(), Optional.of(489), Optional.of(customResponseData));
  }

  public static String defaultJourneyPatternPointOnLineResponseJson() {
    return "{"
        + "\"Version\":\"2024-01-23 00:13\","
        + "\"Type\":\"JourneyPatternPointOnLine\","
        + "\"Result\":"
        + "[{"
        + "\"LineNumber\":1,"
        + "\"DirectionCode\":1,"
        + "\"JourneyPatternPointNumber\":10008,"
        + "\"LastModifiedUtcDateTime\":\"2022-02-15 00:00:00.000\","
        + "\"ExistsFromDate\":\"2022-02-15 00:00:00.000\""
        + "},{"
        + "\"LineNumber\":1,"
        + "\"DirectionCode\":1,"
        + "\"JourneyPatternPointNumber\":10012,"
        + "\"LastModifiedUtcDateTime\":\"2023-03-07 00:00:00.000\","
        + "\"ExistsFromDate\":\"2023-03-07 00:00:00.000\""
        + "},{"
        + "\"LineNumber\":1,"
        + "\"DirectionCode\":1,"
        + "\"JourneyPatternPointNumber\":10014,"
        + "\"LastModifiedUtcDateTime\":\"2022-08-10 00:00:00.000\","
        + "\"ExistsFromDate\":\"2022-08-10 00:00:00.000\"}]}";
  }

  public static String defaultLineResponseJson() {
    return "{"
        + "\"Version\":\"2024-01-31 00:13\","
        + "\"Type\":\"Line\","
        + "\"Result\":"
        + "[{"
        + "\"LineNumber\":1,"
        + "\"LineDesignation\":\"1\","
        + "\"DefaultTransportMode\":\"blåbuss\","
        + "\"DefaultTransportModeCode\":\"BUS\","
        + "\"LastModifiedUtcDateTime\":\"2007-08-24 00:00:00.000\","
        + "\"ExistsFromDate\":\"2007-08-24 00:00:00.000\""
        + "},{"
        + "\"LineNumber\":101,"
        + "\"LineDesignation\":\"101\","
        + "\"DefaultTransportMode\":\"\","
        + "\"DefaultTransportModeCode\":\"BUS\","
        + "\"LastModifiedUtcDateTime\":\"2023-06-08 00:00:00.000\","
        + "\"ExistsFromDate\":\"2023-06-08 00:00:00.000\""
        + "},{"
        + "\"LineNumber\":112,"
        + "\"LineDesignation\":\"112\","
        + "\"DefaultTransportMode\":\"\","
        + "\"DefaultTransportModeCode\":\"BUS\","
        + "\"LastModifiedUtcDateTime\":\"2007-08-24 00:00:00.000\","
        + "\"ExistsFromDate\":\"2007-08-24 00:00:00.000\"}]}";
  }

  public static String defaultStopPointResponseJson() {
    return "{"
        + "\"Version\":\"2024-01-23 00:13\","
        + "\"Type\":\"StopPoint\","
        + "\"Result\":"
        + "[{"
        + "\"StopPointNumber\":10001,"
        + "\"StopPointName\":\"Stadshagsplan\","
        + "\"StopAreaNumber\":10001,"
        + "\"LocationNorthingCoordinate\":59.3373571967995,"
        + "\"LocationEastingCoordinate\":18.0214674159693,"
        + "\"ZoneShortName\":\"A\","
        + "\"StopAreaTypeCode\":\"BUSTERM\","
        + "\"LastModifiedUtcDateTime\":\"2022-10-28 00:00:00.000\","
        + "\"ExistsFromDate\":\"2022-10-28 00:00:00.000\""
        + "},{"
        + "\"StopPointNumber\":10002,"
        + "\"StopPointName\":\"John Bergs plan\","
        + "\"StopAreaNumber\":10002,"
        + "\"LocationNorthingCoordinate\":59.3361450073188,"
        + "\"LocationEastingCoordinate\":18.0222866342593,"
        + "\"ZoneShortName\":\"A\","
        + "\"StopAreaTypeCode\":\"BUSTERM\","
        + "\"LastModifiedUtcDateTime\":\"2015-09-24 00:00:00.000\","
        + "\"ExistsFromDate\":\"2015-09-24 00:00:00.000\""
        + "},{"
        + "\"StopPointNumber\":10006,"
        + "\"StopPointName\":\"Arbetargatan\","
        + "\"StopAreaNumber\":10006,"
        + "\"LocationNorthingCoordinate\":59.3352143599364,"
        + "\"LocationEastingCoordinate\":18.027063651312," // TODO. Zeros at the end of string
        // numbers will mess things up
        + "\"ZoneShortName\":\"A\","
        + "\"StopAreaTypeCode\":\"BUSTERM\","
        + "\"LastModifiedUtcDateTime\":\"2022-09-29 00:00:00.000\","
        + "\"ExistsFromDate\":\"2022-09-29 00:00:00.000\"}]}";
  }

  public static String defaultJourneyPatternPointOnLineApiResponse() {
    return "{"
        + "\"StatusCode\":0,"
        + "\"Message\":null,"
        + "\"ExecutionTime\":316,"
        + "\"ResponseData\":"
        + defaultJourneyPatternPointOnLineResponseJson()
        + "}";
  }

  public static String defaultLineApiResponse() {
    return "{"
        + "\"StatusCode\":0,"
        + "\"Message\":null,"
        + "\"ExecutionTime\":402,"
        + "\"ResponseData\":"
        + defaultLineResponseJson()
        + "}";
  }

  public static String defaultStopPointApiResponse() {
    return "{\""
        + "StatusCode\":0,"
        + "\"Message\":null,"
        + "\"ExecutionTime\":489,"
        + "\"ResponseData\":"
        + defaultStopPointResponseJson()
        + "}";
  }
}
