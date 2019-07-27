package de.moritzrupp.contractview.web.rest;

import de.moritzrupp.contractview.repository.ContractRepository;
import de.moritzrupp.contractview.service.EventService;
import de.moritzrupp.contractview.service.dto.EventDTO;
import de.moritzrupp.contractview.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * REST controller for managing Events (different view on {@link de.moritzrupp.contractview.domain.Contract Contracts}).
 */
@RestController
@RequestMapping("/api")
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(ContractResource.class);

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
    public ResponseEntity<List<EventDTO>> getEvents() {
        return this.getEvents(Year.now().toString());
    }

    @GetMapping("/events/{year}")
    @Timed
    public ResponseEntity<List<EventDTO>> getEvents(@PathVariable String year) {
        return this.getEvents(year, null);
    }

    @GetMapping("/events/{year}/{month}")
    @Timed
    public ResponseEntity<List<EventDTO>> getEvents(@PathVariable String year, @PathVariable String month) {
        try {
            if(month != null) {
                int monthInt = 0;

                monthInt = Integer.parseInt(month);
                if (!(1 <= monthInt && monthInt <= 12)) {
                    throw new NumberFormatException();
                }

            }
            Month m = month != null ? Month.of(Integer.parseInt(month)) : null;
            Year y = Year.parse(year);

            log.debug("REST request to get the events of " + year + (m != null ? "-" + m.getValue() : ""));

            return ResponseEntity.ok(eventService.getAllContractsAsEvents(y, m));
        } catch (NumberFormatException nfe) {
            throw new BadRequestAlertException("The format of the month must be a valid (optionally zero-leading) integer from 1-12", ENTITY_NAME, "monthformat");
        } catch (DateTimeParseException dtpe) {
            throw new BadRequestAlertException("The format of the year must be a valid year", ENTITY_NAME, "yearformat");
        }
    }
}
