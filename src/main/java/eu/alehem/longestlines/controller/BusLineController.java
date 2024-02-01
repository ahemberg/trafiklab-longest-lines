package eu.alehem.longestlines.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import eu.alehem.longestlines.exceptions.BusLineNotFoundException;
import eu.alehem.longestlines.model.dto.LineDTO;
import eu.alehem.longestlines.model.dto.StopDTO;
import eu.alehem.longestlines.model.dto.StopsDTO;
import eu.alehem.longestlines.service.LineService;
import eu.alehem.longestlines.service.StopService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BusLineController {

  @Autowired private LineService lineService;

  @Autowired private StopService stopService;

  @GetMapping("/")
  public EntityModel<Link> root() {
    return EntityModel.of(
        linkTo(methodOn(BusLineController.class).getLongestLines()).withRel("longest"),
        linkTo(methodOn(BusLineController.class).getLongestOutbound()).withRel("longest/outbound"),
        linkTo(methodOn(BusLineController.class).getLongestInbound()).withRel("longest/inbound"),
        linkTo(methodOn(BusLineController.class).root()).withSelfRel());
  }

  @GetMapping("/longest")
  public CollectionModel<LineDTO> getLongestLines() {

    List<LineDTO> longestLines = lineService.getLongestLines(10);
    Stream<Link> links =
        longestLines.stream()
            .map(
                dto ->
                    linkTo(methodOn(BusLineController.class).getStops(dto.lineNumber()))
                        .withRel("stops"));

    return CollectionModel.of(
        longestLines,
        Stream.concat(
                links,
                Stream.of(
                    linkTo(methodOn(BusLineController.class).getLongestLines()).withSelfRel()))
            .toList());
  }

  @GetMapping("/clear-text/longest")
  public String getLongestLinesAsClearText() {
    return lineService.getLongestLines(10).stream()
        .map(LineDTO::lineNumber)
        .map(Object::toString)
        .collect(Collectors.joining(","));
  }

  @GetMapping("/longest/outbound")
  public CollectionModel<LineDTO> getLongestOutbound() {
    List<LineDTO> longestLines = lineService.getLongestOutboundLines(10);
    Stream<Link> links =
        longestLines.stream()
            .map(
                dto ->
                    linkTo(methodOn(BusLineController.class).getStops(dto.lineNumber()))
                        .withRel("stops"));

    return CollectionModel.of(
        longestLines,
        Stream.concat(
                links,
                Stream.of(
                    linkTo(methodOn(BusLineController.class).getLongestOutbound()).withSelfRel()))
            .toList());
  }

  @GetMapping("/clear-text/longest/outbound")
  public String getLongestOutboundAsClearText() {
    return lineService.getLongestOutboundLines(10).stream()
        .map(LineDTO::lineNumber)
        .map(Object::toString)
        .collect(Collectors.joining(","));
  }

  @GetMapping("/longest/inbound")
  public CollectionModel<LineDTO> getLongestInbound() {
    List<LineDTO> longestLines = lineService.getLongestInboundLines(10);
    Stream<Link> links =
        longestLines.stream()
            .map(
                dto ->
                    linkTo(methodOn(BusLineController.class).getStops(dto.lineNumber()))
                        .withRel("stops"));

    return CollectionModel.of(
        longestLines,
        Stream.concat(
                links,
                Stream.of(
                    linkTo(methodOn(BusLineController.class).getLongestInbound()).withSelfRel()))
            .toList());
  }

  @GetMapping("/clear-text/longest/inbound")
  public String getLongestInboundAsClearText() {
    return lineService.getLongestInboundLines(10).stream()
        .map(LineDTO::lineNumber)
        .map(Object::toString)
        .collect(Collectors.joining(","));
  }

  @GetMapping("/stops/{lineNumber}")
  public EntityModel<StopsDTO> getStops(@PathVariable int lineNumber) {

    StopsDTO stopsDTO =
        stopService
            .getAllStopsForLine(lineNumber)
            .orElseThrow(() -> new BusLineNotFoundException(lineNumber));

    return EntityModel.of(
        stopsDTO,
        linkTo(methodOn(BusLineController.class).getStops(lineNumber)).withSelfRel(),
        linkTo(methodOn(BusLineController.class).getLongestLines()).withRel("longest"),
        linkTo(methodOn(BusLineController.class).getLongestOutbound()).withRel("longest/outbound"),
        linkTo(methodOn(BusLineController.class).getLongestInbound()).withRel("longest/inbound"));
  }

  @GetMapping("clear-text/stops/{lineNumber}")
  public String getStopsAsClearText(@PathVariable int lineNumber) {
    StopsDTO stopsDTO =
        stopService
            .getAllStopsForLine(lineNumber)
            .orElseThrow(() -> new BusLineNotFoundException(lineNumber));

    return Stream.concat(stopsDTO.outboundStops().stream(), stopsDTO.inboundStops().stream())
        .map(StopDTO::name)
        .collect(Collectors.joining(","));
  }
}
