package eu.alehem.longestlines.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import eu.alehem.longestlines.model.dao.BusStop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusStopTest {

  private BusStop busStop;

  @Test
  void getBusStopId() {
    assertThat(busStop.getBusStopId(), is(1));
  }

  @Test
  void getName() {
    assertThat(busStop.getName(), is("Teststop"));
  }

  @BeforeEach
  void setUp() {
    busStop = new BusStop(1, "Teststop");
  }
}
