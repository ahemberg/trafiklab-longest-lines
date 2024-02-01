package eu.alehem.longestlines.model.trafiklab;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class DefaultTransportModeCodeTest {

  @Test
  void busTermConvertedCorrectly() {
    assertThat(
        DefaultTransportModeCode.fromStopAreaTypeCode(StopAreaTypeCode.BUSTERM),
        is(DefaultTransportModeCode.BUS));
  }

  @Test
  void metroStnConvertedCorrectly() {
    assertThat(
        DefaultTransportModeCode.fromStopAreaTypeCode(StopAreaTypeCode.METROSTN),
        is(DefaultTransportModeCode.METRO));
  }

  @Test
  void tramStnConvertedCorrectly() {
    assertThat(
        DefaultTransportModeCode.fromStopAreaTypeCode(StopAreaTypeCode.TRAMSTN),
        is(DefaultTransportModeCode.TRAM));
  }

  @Test
  void railWayStnConvertedCorrectly() {
    assertThat(
        DefaultTransportModeCode.fromStopAreaTypeCode(StopAreaTypeCode.RAILWSTN),
        is(DefaultTransportModeCode.TRAIN));
  }

  @Test
  void shipBerConvertedCorrectly() {
    assertThat(
        DefaultTransportModeCode.fromStopAreaTypeCode(StopAreaTypeCode.SHIPBER),
        is(DefaultTransportModeCode.SHIP));
  }

  @Test
  void ferryBerConvertedCorrectly() {
    assertThat(
        DefaultTransportModeCode.fromStopAreaTypeCode(StopAreaTypeCode.FERRYBER),
        is(DefaultTransportModeCode.FERRY));
  }

  @Test
  void UnknownConvertedCorrectly() {
    assertThat(
        DefaultTransportModeCode.fromStopAreaTypeCode(StopAreaTypeCode.UNKNOWN),
        is(DefaultTransportModeCode.UNKNOWN));
  }
}
