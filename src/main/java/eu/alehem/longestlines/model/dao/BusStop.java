package eu.alehem.longestlines.model.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "bus_stops")
public class BusStop {

  @Id
  @Column(name = "bus_stop_id")
  private int busStopId;

  @Column(name = "name")
  private String name;

  public BusStop() {}

  public BusStop(int busStopId, String name) {
    this.busStopId = busStopId;
    this.name = name;
  }

  public int getBusStopId() {
    return busStopId;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object)
      return true;
    if (object == null || getClass() != object.getClass())
      return false;
    BusStop busStop = (BusStop) object;
    return busStopId == busStop.busStopId && Objects.equals(name, busStop.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(busStopId, name);
  }
}
