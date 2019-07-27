package de.moritzrupp.contractview.service;

import de.moritzrupp.contractview.repository.ContractRepository;
import de.moritzrupp.contractview.service.dto.EventDTO;
import de.moritzrupp.contractview.service.mapper.EventMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.*;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing events.
 */
@Service
@Transactional
public class EventService {

    private final ContractRepository contractRepository;

    public EventService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Transactional(readOnly = true)
    public List<EventDTO> getAllContractsAsEvents(@NotNull Year year, Month month) {

        if(year == null) {
            throw new IllegalArgumentException("Year cannot be null.");
        }

        LocalDate dateStart = LocalDate.of(year.getValue(), (month != null ? month : Month.JANUARY), 1);
        LocalDate dateEnd = LocalDate.of(year.getValue(), (month != null ? month : Month.DECEMBER),
            (month != null ? month.length(year.isLeap()) : Month.DECEMBER.length(year.isLeap())));

        Instant start = dateStart.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant end = dateEnd.atStartOfDay().toInstant(ZoneOffset.UTC);

        return this.contractRepository.findAllByContractEndIsBetween(start, end)
            .stream().map(contract -> EventMapper.INSTANCE.contractToEventDTO(contract)).collect(Collectors.toList());
    }
}
