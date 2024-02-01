package eu.alehem.longestlines.service;

import eu.alehem.longestlines.model.dao.BusLine;
import eu.alehem.longestlines.model.dao.BusStop;
import eu.alehem.longestlines.model.dto.StopDTO;
import eu.alehem.longestlines.model.dto.StopsDTO;
import eu.alehem.longestlines.repository.LineRepository;
import eu.alehem.longestlines.repository.StopRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StopService {

  private StopRepository stopRepository;

  private LineRepository lineRepository;

  @Autowired
  public StopService(StopRepository stopRepository, LineRepository lineRepository) {
    this.stopRepository = stopRepository;
    this.lineRepository = lineRepository;
  }

  public void insertStops(List<BusStop> stops) {
    // TODO make sure that this actually updates
    stopRepository.saveAll(stops);
  }

  public Optional<StopsDTO> getAllStopsForLine(int lineNumber) {

    final Optional<BusLine> buslineOpt = lineRepository.getLineByNumber(lineNumber);

    if (buslineOpt.isEmpty()) {
      return Optional.empty();
    }

    final BusLine busline = buslineOpt.get();

    final List<BusStop> outboundStops =
        stopRepository.getAllStopsInList(busline.getOutboundStops());
    final List<BusStop> inboundStops = stopRepository.getAllStopsInList(busline.getInboundStops());

    return Optional.of(
        new StopsDTO(
            lineNumber,
            outboundStops.stream()
                .map(bs -> new StopDTO(bs.getBusStopId(), bs.getName()))
                .collect(Collectors.toList()),
            inboundStops.stream()
                .map(bs -> new StopDTO(bs.getBusStopId(), bs.getName()))
                .collect(Collectors.toList())));
  }

  public List<StopDTO> getAllStops() {
    return stopRepository.findAll().stream()
        .map(busStop -> new StopDTO(busStop.getBusStopId(), busStop.getName()))
        .collect(Collectors.toList());
  }
}
