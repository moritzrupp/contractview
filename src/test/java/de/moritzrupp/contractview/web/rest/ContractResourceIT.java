package de.moritzrupp.contractview.web.rest;

import de.moritzrupp.contractview.ContractviewApp;
import de.moritzrupp.contractview.domain.Contract;
import de.moritzrupp.contractview.domain.Provider;
import de.moritzrupp.contractview.domain.User;
import de.moritzrupp.contractview.repository.ContractRepository;
import de.moritzrupp.contractview.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static de.moritzrupp.contractview.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ContractResource} REST controller.
 */
@SpringBootTest(classes = ContractviewApp.class)
public class ContractResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;
    private static final Double SMALLER_PRICE = 0D - 1D;

    private static final Integer DEFAULT_BILLING_PERIOD_DAYS = 0;
    private static final Integer UPDATED_BILLING_PERIOD_DAYS = 1;
    private static final Integer SMALLER_BILLING_PERIOD_DAYS = 0 - 1;

    private static final Instant DEFAULT_CONTRACT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONTRACT_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CONTRACT_START = Instant.ofEpochMilli(-1L);

    private static final Instant DEFAULT_CONTRACT_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONTRACT_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_CONTRACT_END = Instant.ofEpochMilli(-1L);

    private static final Boolean DEFAULT_AUTOMATIC_EXTENSION = false;
    private static final Boolean UPDATED_AUTOMATIC_EXTENSION = true;

    private static final Integer DEFAULT_EXTENSION_PERIOD_DAYS = 0;
    private static final Integer UPDATED_EXTENSION_PERIOD_DAYS = 1;
    private static final Integer SMALLER_EXTENSION_PERIOD_DAYS = 0 - 1;

    private static final Boolean DEFAULT_EXTENSION_REMINDER = false;
    private static final Boolean UPDATED_EXTENSION_REMINDER = true;

    private static final Integer DEFAULT_EXTENSION_REMINDER_PERIOD_DAYS = 0;
    private static final Integer UPDATED_EXTENSION_REMINDER_PERIOD_DAYS = 1;
    private static final Integer SMALLER_EXTENSION_REMINDER_PERIOD_DAYS = 0 - 1;

    @Autowired
    private ContractRepository contractRepository;

    @Mock
    private ContractRepository contractRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restContractMockMvc;

    private Contract contract;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContractResource contractResource = new ContractResource(contractRepository);
        this.restContractMockMvc = MockMvcBuilders.standaloneSetup(contractResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .name(DEFAULT_NAME)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .price(DEFAULT_PRICE)
            .billingPeriodDays(DEFAULT_BILLING_PERIOD_DAYS)
            .contractStart(DEFAULT_CONTRACT_START)
            .contractEnd(DEFAULT_CONTRACT_END)
            .automaticExtension(DEFAULT_AUTOMATIC_EXTENSION)
            .extensionPeriodDays(DEFAULT_EXTENSION_PERIOD_DAYS)
            .extensionReminder(DEFAULT_EXTENSION_REMINDER)
            .extensionReminderPeriodDays(DEFAULT_EXTENSION_REMINDER_PERIOD_DAYS);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        contract.setProvider(provider);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        contract.setOwner(user);
        return contract;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity(EntityManager em) {
        Contract contract = new Contract()
            .name(UPDATED_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .price(UPDATED_PRICE)
            .billingPeriodDays(UPDATED_BILLING_PERIOD_DAYS)
            .contractStart(UPDATED_CONTRACT_START)
            .contractEnd(UPDATED_CONTRACT_END)
            .automaticExtension(UPDATED_AUTOMATIC_EXTENSION)
            .extensionPeriodDays(UPDATED_EXTENSION_PERIOD_DAYS)
            .extensionReminder(UPDATED_EXTENSION_REMINDER)
            .extensionReminderPeriodDays(UPDATED_EXTENSION_REMINDER_PERIOD_DAYS);
        // Add required entity
        Provider provider;
        if (TestUtil.findAll(em, Provider.class).isEmpty()) {
            provider = ProviderResourceIT.createUpdatedEntity(em);
            em.persist(provider);
            em.flush();
        } else {
            provider = TestUtil.findAll(em, Provider.class).get(0);
        }
        contract.setProvider(provider);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        contract.setOwner(user);
        return contract;
    }

    @BeforeEach
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    public void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // Create the Contract
        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContract.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testContract.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testContract.getBillingPeriodDays()).isEqualTo(DEFAULT_BILLING_PERIOD_DAYS);
        assertThat(testContract.getContractStart()).isEqualTo(DEFAULT_CONTRACT_START);
        assertThat(testContract.getContractEnd()).isEqualTo(DEFAULT_CONTRACT_END);
        assertThat(testContract.isAutomaticExtension()).isEqualTo(DEFAULT_AUTOMATIC_EXTENSION);
        assertThat(testContract.getExtensionPeriodDays()).isEqualTo(DEFAULT_EXTENSION_PERIOD_DAYS);
        assertThat(testContract.isExtensionReminder()).isEqualTo(DEFAULT_EXTENSION_REMINDER);
        assertThat(testContract.getExtensionReminderPeriodDays()).isEqualTo(DEFAULT_EXTENSION_REMINDER_PERIOD_DAYS);
    }

    @Test
    @Transactional
    public void createContractWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // Create the Contract with an existing ID
        contract.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setName(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setPrice(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBillingPeriodDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setBillingPeriodDays(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContractStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setContractStart(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAutomaticExtensionIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setAutomaticExtension(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExtensionPeriodDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setExtensionPeriodDays(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExtensionReminderIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setExtensionReminder(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExtensionReminderPeriodDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setExtensionReminderPeriodDays(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].billingPeriodDays").value(hasItem(DEFAULT_BILLING_PERIOD_DAYS)))
            .andExpect(jsonPath("$.[*].contractStart").value(hasItem(DEFAULT_CONTRACT_START.toString())))
            .andExpect(jsonPath("$.[*].contractEnd").value(hasItem(DEFAULT_CONTRACT_END.toString())))
            .andExpect(jsonPath("$.[*].automaticExtension").value(hasItem(DEFAULT_AUTOMATIC_EXTENSION.booleanValue())))
            .andExpect(jsonPath("$.[*].extensionPeriodDays").value(hasItem(DEFAULT_EXTENSION_PERIOD_DAYS)))
            .andExpect(jsonPath("$.[*].extensionReminder").value(hasItem(DEFAULT_EXTENSION_REMINDER.booleanValue())))
            .andExpect(jsonPath("$.[*].extensionReminderPeriodDays").value(hasItem(DEFAULT_EXTENSION_REMINDER_PERIOD_DAYS)));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllContractsWithEagerRelationshipsIsEnabled() throws Exception {
        ContractResource contractResource = new ContractResource(contractRepositoryMock);
        when(contractRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restContractMockMvc = MockMvcBuilders.standaloneSetup(contractResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restContractMockMvc.perform(get("/api/contracts?eagerload=true"))
        .andExpect(status().isOk());

        verify(contractRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllContractsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ContractResource contractResource = new ContractResource(contractRepositoryMock);
            when(contractRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restContractMockMvc = MockMvcBuilders.standaloneSetup(contractResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restContractMockMvc.perform(get("/api/contracts?eagerload=true"))
        .andExpect(status().isOk());

            verify(contractRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.billingPeriodDays").value(DEFAULT_BILLING_PERIOD_DAYS))
            .andExpect(jsonPath("$.contractStart").value(DEFAULT_CONTRACT_START.toString()))
            .andExpect(jsonPath("$.contractEnd").value(DEFAULT_CONTRACT_END.toString()))
            .andExpect(jsonPath("$.automaticExtension").value(DEFAULT_AUTOMATIC_EXTENSION.booleanValue()))
            .andExpect(jsonPath("$.extensionPeriodDays").value(DEFAULT_EXTENSION_PERIOD_DAYS))
            .andExpect(jsonPath("$.extensionReminder").value(DEFAULT_EXTENSION_REMINDER.booleanValue()))
            .andExpect(jsonPath("$.extensionReminderPeriodDays").value(DEFAULT_EXTENSION_REMINDER_PERIOD_DAYS));
    }

    @Test
    @Transactional
    public void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .name(UPDATED_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .price(UPDATED_PRICE)
            .billingPeriodDays(UPDATED_BILLING_PERIOD_DAYS)
            .contractStart(UPDATED_CONTRACT_START)
            .contractEnd(UPDATED_CONTRACT_END)
            .automaticExtension(UPDATED_AUTOMATIC_EXTENSION)
            .extensionPeriodDays(UPDATED_EXTENSION_PERIOD_DAYS)
            .extensionReminder(UPDATED_EXTENSION_REMINDER)
            .extensionReminderPeriodDays(UPDATED_EXTENSION_REMINDER_PERIOD_DAYS);

        restContractMockMvc.perform(put("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContract)))
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContract.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testContract.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testContract.getBillingPeriodDays()).isEqualTo(UPDATED_BILLING_PERIOD_DAYS);
        assertThat(testContract.getContractStart()).isEqualTo(UPDATED_CONTRACT_START);
        assertThat(testContract.getContractEnd()).isEqualTo(UPDATED_CONTRACT_END);
        assertThat(testContract.isAutomaticExtension()).isEqualTo(UPDATED_AUTOMATIC_EXTENSION);
        assertThat(testContract.getExtensionPeriodDays()).isEqualTo(UPDATED_EXTENSION_PERIOD_DAYS);
        assertThat(testContract.isExtensionReminder()).isEqualTo(UPDATED_EXTENSION_REMINDER);
        assertThat(testContract.getExtensionReminderPeriodDays()).isEqualTo(UPDATED_EXTENSION_REMINDER_PERIOD_DAYS);
    }

    @Test
    @Transactional
    public void updateNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Create the Contract

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc.perform(put("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Delete the contract
        restContractMockMvc.perform(delete("/api/contracts/{id}", contract.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = new Contract();
        contract1.setId(1L);
        Contract contract2 = new Contract();
        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);
        contract2.setId(2L);
        assertThat(contract1).isNotEqualTo(contract2);
        contract1.setId(null);
        assertThat(contract1).isNotEqualTo(contract2);
    }
}
