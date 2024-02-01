package eu.alehem.longestlines.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import eu.alehem.longestlines.model.dto.LineDTO;
import eu.alehem.longestlines.model.dto.StopDTO;
import eu.alehem.longestlines.model.dto.StopsDTO;
import java.util.List;
import java.util.stream.Stream;
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
    List<LineDTO> longestLines = List.of(new LineDTO(1), new LineDTO(2), new LineDTO(3));
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

  @GetMapping("/longest/outbound")
  public CollectionModel<LineDTO> getLongestOutbound() {
    List<LineDTO> longestLines = List.of(new LineDTO(1), new LineDTO(2), new LineDTO(3));
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

  @GetMapping("/longest/inbound")
  public CollectionModel<LineDTO> getLongestInbound() {
    List<LineDTO> longestLines = List.of(new LineDTO(1), new LineDTO(2), new LineDTO(3));
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

  @GetMapping("/stops/{lineNumber}")
  public EntityModel<StopsDTO> getStops(@PathVariable int lineNumber) {

    StopsDTO stopsDTO =
        new StopsDTO(
            lineNumber,
            List.of(new StopDTO(1, "1"), new StopDTO(2, "2"), new StopDTO(3, "3")),
            List.of(new StopDTO(1, "1"), new StopDTO(2, "2"), new StopDTO(3, "3")));

    return EntityModel.of(
        stopsDTO,
        linkTo(methodOn(BusLineController.class).getStops(lineNumber)).withSelfRel(),
        linkTo(methodOn(BusLineController.class).getLongestLines()).withRel("longest"),
        linkTo(methodOn(BusLineController.class).getLongestOutbound()).withRel("longest/outbound"),
        linkTo(methodOn(BusLineController.class).getLongestInbound()).withRel("longest/inbound"));
  }
}
