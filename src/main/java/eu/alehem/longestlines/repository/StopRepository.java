package eu.alehem.longestlines.repository;

import eu.alehem.longestlines.model.dao.BusStop;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StopRepository extends JpaRepository<BusStop, Integer> {

  @Query(value = "SELECT * FROM bus_stops WHERE bus_stop_id IN(:stops)", nativeQuery = true)
  List<BusStop> getAllStopsInList(@Param("stops") final List<Integer> stops);
}
