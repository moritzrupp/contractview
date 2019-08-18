package de.moritzrupp.contractview.web.rest;

import de.moritzrupp.contractview.domain.Contract;
import de.moritzrupp.contractview.repository.ContractRepository;
import de.moritzrupp.contractview.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link de.moritzrupp.contractview.domain.Contract}.
 */
@RestController
@RequestMapping("/api")
public class ContractResource {

    private final Logger log = LoggerFactory.getLogger(ContractResource.class);

    private static final String ENTITY_NAME = "contract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractRepository contractRepository;

    public ContractResource(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    /**
     * {@code POST  /contracts} : Create a new contract.
     *
     * @param contract the contract to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contract, or with status {@code 400 (Bad Request)} if the contract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contracts")
    public ResponseEntity<Contract> createContract(@Valid @RequestBody Contract contract) throws URISyntaxException {
        log.debug("REST request to save Contract : {}", contract);
        if (contract.getId() != null) {
            throw new BadRequestAlertException("A new contract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contract result = contractRepository.save(contract);
        return ResponseEntity.created(new URI("/api/contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contracts} : Updates an existing contract.
     *
     * @param contract the contract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contract,
     * or with status {@code 400 (Bad Request)} if the contract is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contracts")
    public ResponseEntity<Contract> updateContract(@Valid @RequestBody Contract contract) throws URISyntaxException {
        log.debug("REST request to update Contract : {}", contract);
        if (contract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Contract result = contractRepository.save(contract);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contract.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contracts} : get all the contracts.
     *

     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contracts in body.
     */
    @GetMapping("/contracts")
    public ResponseEntity<List<Contract>> getAllContracts(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {

        log.debug("REST request to get a page of Contracts");
        Page<Contract> page;
        if (eagerload) {
            page = contractRepository.findAllWithEagerRelationships(pageable);
        }
        else {
            page = contractRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contracts/:id} : get the "id" contract.
     *
     * @param id the id of the contract to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contract, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contracts/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable Long id) {
        log.debug("REST request to get Contract : {}", id);
        Optional<Contract> contract = contractRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(contract);
    }

    /**
     * {@code DELETE  /contracts/:id} : delete the "id" contract.
     *
     * @param id the id of the contract to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contracts/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        log.debug("REST request to delete Contract : {}", id);
        contractRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
