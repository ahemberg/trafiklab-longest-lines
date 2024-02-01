package eu.alehem.longestlines.repository;

import eu.alehem.longestlines.model.dao.BusLine;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends JpaRepository<BusLine, Integer> {

  @Query(
      value = "SELECT * FROM bus_lines ORDER BY total_stops desc LIMIT :limit",
      nativeQuery = true)
  List<BusLine> getLongestBusLines(@Param("limit") final int limit);

  @Query(
      value = "SELECT * FROM bus_lines ORDER BY total_outbound desc LIMIT :limit",
      nativeQuery = true)
  List<BusLine> getLongestOutboundBusLines(@Param("limit") final int limit);

  @Query(
      value = "SELECT * FROM bus_lines ORDER BY total_inbound desc LIMIT :limit",
      nativeQuery = true)
  List<BusLine> getLongestInboundBusLines(@Param("limit") final int limit);

  @Query(value = "SELECT * FROM bus_lines WHERE line_number=:lineNumber", nativeQuery = true)
  Optional<BusLine> getLineByNumber(@Param("lineNumber") final int lineNumber);
}
