package de.moritzrupp.contractview.web.rest;

import de.moritzrupp.contractview.ContractviewApp;
import de.moritzrupp.contractview.domain.Contract;
import de.moritzrupp.contractview.repository.ContractRepository;
import de.moritzrupp.contractview.service.EventService;
import de.moritzrupp.contractview.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.temporal.ChronoUnit;

import static de.moritzrupp.contractview.web.rest.TestUtil.createFormattingConversionService;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EventResource} REST controller.
 */
@SpringBootTest(classes = ContractviewApp.class)
public class EventResourceIntTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventMockMvc;

    private Contract contract;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventResource eventResource = new EventResource(eventService);
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        return ContractResourceIT.createEntity(em);
    }

    @BeforeEach
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {

        contractRepository.saveAndFlush(contract);

        restEventMockMvc.perform(get("/api/events?from={from}&to={to}", contract.getContractStart().minus(1, ChronoUnit.DAYS),
            contract.getContractEnd().plus(1, ChronoUnit.DAYS)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    @Transactional
    public void failWithMissingFromParameter() throws Exception {

        contractRepository.saveAndFlush(contract);

        restEventMockMvc.perform(get("/api/events?to={to}", contract.getContractEnd().plus(1, ChronoUnit.DAYS)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void failWithMissingToParameter() throws Exception {

        contractRepository.saveAndFlush(contract);

        restEventMockMvc.perform(get("/api/events?from={from}", contract.getContractStart().minus(1, ChronoUnit.DAYS)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void failWithMissingParameters() throws Exception {

        contractRepository.saveAndFlush(contract);

        restEventMockMvc.perform(get("/api/events"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void checkEventDTO() throws Exception {

        contractRepository.saveAndFlush(contract);

        restEventMockMvc.perform(get("/api/events?from={from}&to={to}", contract.getContractStart().minus(1, ChronoUnit.DAYS),
            contract.getContractEnd().plus(1, ChronoUnit.DAYS)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(contract.getName())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(contract.getProvider().getName())))
            .andExpect(jsonPath("$.[*].contractStart").value(hasItem(contract.getContractStart().toString())))
            .andExpect(jsonPath("$.[*].contractEnd").value(hasItem(contract.getContractEnd().toString())));
    }
}
