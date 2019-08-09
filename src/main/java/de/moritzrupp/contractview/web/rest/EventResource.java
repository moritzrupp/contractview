package de.moritzrupp.contractview.web.rest;

import de.moritzrupp.contractview.service.EventService;
import de.moritzrupp.contractview.service.dto.EventDTO;
import de.moritzrupp.contractview.web.rest.errors.BadRequestAlertException;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * REST controller for managing Events (different view on {@link de.moritzrupp.contractview.domain.Contract Contracts}).
 */
@RestController
@RequestMapping("/api")
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);

    private static final String ENTITY_NAME = "contract";

    private final EventService eventService;

    public EventResource(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * GET /events : get all events of the current year. An event is a contract with a subset of attributes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of events of the current year in body.
     */
    @GetMapping("/events")
    @Timed
    public ResponseEntity<List<EventDTO>> getEvents(@RequestParam String from, @RequestParam String to) {
        try {

            Instant start = Instant.parse(from);
            Instant end = Instant.parse(to);

            log.debug("REST request to get the events between " + start.toString() + " and " + end.toString());

            return ResponseEntity.ok(eventService.getAllContractsAsEvents(start, end));
        } catch (DateTimeParseException ex) {
            throw new BadRequestAlertException("The dates " + from + "/" + to + " must represent " +
                "valid instants in UTC and are parsed using DateTimeFormatter.ISO_INSTANT", ENTITY_NAME,
                "dateformat");
        }
    }
}
