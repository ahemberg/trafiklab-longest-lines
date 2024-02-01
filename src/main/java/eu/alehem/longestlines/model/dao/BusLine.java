package eu.alehem.longestlines.model.dao;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "bus_lines")
@Access(AccessType.FIELD)
public class BusLine {
  @Id
  @Column(name = "line_number")
  private int lineNumber;

  @Column(name = "total_outbound")
  private int totalOutbound;

  @Column(name = "total_inbound")
  private int totalInbound;

  @Column(name = "total_stops")
  private int totalStops;

  @Column(name = "outbound_stops")
  private List<Integer> outboundStops;

  @Column(name = "inbound_stops")
  private List<Integer> inboundStops;

  public BusLine(int lineNumber, List<Integer> outboundStops, List<Integer> inboundStops) {
    this.lineNumber = lineNumber;
    this.totalInbound = inboundStops.size();
    this.totalOutbound = outboundStops.size();
    this.totalStops = totalInbound + totalOutbound;
    this.inboundStops = inboundStops;
    this.outboundStops = outboundStops;
  }

  public BusLine() {}

  public int getLineNumber() {
    return lineNumber;
  }

  public int getTotalInbound() {
    return totalInbound;
  }

  public int getTotalOutbound() {
    return totalOutbound;
  }

  public int getTotalStops() {
    return totalStops;
  }

  public List<Integer> getInboundStops() {
    return inboundStops;
  }

  public List<Integer> getOutboundStops() {
    return outboundStops;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    BusLine busLine = (BusLine) object;
    return lineNumber == busLine.lineNumber
        && totalOutbound == busLine.totalOutbound
        && totalInbound == busLine.totalInbound
        && totalStops == busLine.totalStops
        && Objects.equals(outboundStops, busLine.outboundStops)
        && Objects.equals(inboundStops, busLine.inboundStops);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        lineNumber, totalOutbound, totalInbound, totalStops, outboundStops, inboundStops);
  }
}
