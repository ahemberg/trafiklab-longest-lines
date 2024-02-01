package eu.alehem.longestlines.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import eu.alehem.longestlines.model.dao.BusLine;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusLineTest {

  private BusLine busLine;

  @BeforeEach
  void setUp() {
    busLine = new BusLine(1, List.of(4, 3, 2, 1), List.of(1, 2, 3, 4));
  }

  @Test
  void getLineNumber() {
    assertThat(busLine.getLineNumber(), is(1));
  }

  @Test
  void getTotalInbound() {
    assertThat(busLine.getTotalInbound(), is(4));
  }

  @Test
  void getTotalOutbound() {
    assertThat(busLine.getTotalOutbound(), is(4));
  }

  @Test
  void getTotalStops() {
    assertThat(busLine.getTotalStops(), is(8));
  }

  @Test
  void getInboundStops() {
    assertThat(busLine.getInboundStops(), is(List.of(1, 2, 3, 4)));
  }

  @Test
  void getOutboundStops() {
    assertThat(busLine.getOutboundStops(), is(List.of(4, 3, 2, 1)));
  }
}
