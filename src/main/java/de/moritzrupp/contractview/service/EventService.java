package de.moritzrupp.contractview.service;

import de.moritzrupp.contractview.repository.ContractRepository;
import de.moritzrupp.contractview.service.dto.EventDTO;
import de.moritzrupp.contractview.service.mapper.EventMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.Instant;
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
    public List<EventDTO> getAllContractsAsEvents(@NotNull Instant start, @NotNull Instant end) {

        if (start == null || end == null) {
            throw new IllegalArgumentException("start or end cannot be null.");
        }

        return this.contractRepository.findAllByContractEndIsBetween(start, end)
            .stream().map(EventMapper.INSTANCE::contractToEventDTO).collect(Collectors.toList());
    }
}
