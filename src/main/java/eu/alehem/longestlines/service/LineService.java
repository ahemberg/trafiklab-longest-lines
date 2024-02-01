package eu.alehem.longestlines.service;

import eu.alehem.longestlines.model.dao.BusLine;
import eu.alehem.longestlines.model.dto.LineDTO;
import eu.alehem.longestlines.repository.LineRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LineService {

  private final LineRepository lineRepository;

  @Autowired
  public LineService(LineRepository lineRepository) {
    this.lineRepository = lineRepository;
  }

  public void insertLines(List<BusLine> lines) {
    lineRepository.saveAll(lines);
  }

  public List<LineDTO> getLongestLines(int limit) {
    return lineRepository.getLongestBusLines(limit).stream()
        .map(bl -> new LineDTO(bl.getLineNumber()))
        .collect(Collectors.toList());
  }

  public List<LineDTO> getLongestOutboundLines(int limit) {
    return lineRepository.getLongestOutboundBusLines(limit).stream()
        .map(bl -> new LineDTO(bl.getLineNumber()))
        .collect(Collectors.toList());
  }

  public List<LineDTO> getLongestInboundLines(int limit) {
    return lineRepository.getLongestInboundBusLines(limit).stream()
        .map(bl -> new LineDTO(bl.getLineNumber()))
        .collect(Collectors.toList());
  }

  public List<LineDTO> getAllLines() {
    return lineRepository.findAll().stream()
        .map(bl -> new LineDTO(bl.getLineNumber()))
        .collect(Collectors.toList());
  }
}
