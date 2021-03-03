package com.limonnana.skate.web.rest;

import com.limonnana.skate.domain.ContributionForm;
import com.limonnana.skate.domain.Fan;
import com.limonnana.skate.domain.Trick;
import com.limonnana.skate.repository.FanRepository;
import com.limonnana.skate.repository.TrickRepository;
import com.limonnana.skate.web.rest.errors.BadRequestAlertException;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.limonnana.skate.domain.Fan}.
 */
@RestController
@RequestMapping("/api")
public class FanResource {

    private final Logger log = LoggerFactory.getLogger(FanResource.class);

    private static final String ENTITY_NAME = "fan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FanRepository fanRepository;
    private final TrickRepository trickRepository;

    public FanResource(
        FanRepository fanRepository,
        TrickRepository trickRepository
    ) {
        this.fanRepository = fanRepository;
        this.trickRepository = trickRepository;
    }

    /**
     * {@code POST  /fans} : Create a new fan.
     *
     * @param fan the fan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fan, or with status {@code 400 (Bad Request)} if the fan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fans")
    public ResponseEntity<Fan> createFan(@Valid @RequestBody Fan fan) throws URISyntaxException {
        log.debug("REST request to save Fan : {}", fan);
        if (fan.getId() != null) {
            throw new BadRequestAlertException("A new fan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fan result = fanRepository.save(fan);
        return ResponseEntity.created(new URI("/api/fans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }


    @PostMapping("/fans/contribution")
    public ResponseEntity<Fan> createFanContribution(@Valid @RequestBody ContributionForm contributionForm) throws URISyntaxException {
        log.debug("REST request to save Fan Contribution : {}", contributionForm);

        Fan fan = new Fan();

        if(contributionForm.getFanId() != null){
            fan = fanRepository.findById(contributionForm.getFanId()).get();
        }else{
            fan.setFullName(contributionForm.getFanFullName());
            fan.setPhone(contributionForm.getPhone());
            fan = fanRepository.save(fan);
        }

        Trick trick = trickRepository.findById(contributionForm.getTrick().getId()).get();
        int ca = trick.getCurrentAmount().intValue();
        String amount = contributionForm.getAmount();
        int toAdd = Integer.parseInt(amount);
        int amountTotal =  ca + toAdd;
        trick.setCurrentAmount(amountTotal);
        trickRepository.save(trick);


        return ResponseEntity.created(new URI("/api/fans/" + fan.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, fan.getId()))
            .body(fan);
    }

    /**
     * {@code PUT  /fans} : Updates an existing fan.
     *
     * @param fan the fan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fan,
     * or with status {@code 400 (Bad Request)} if the fan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fans")
    public ResponseEntity<Fan> updateFan(@Valid @RequestBody Fan fan) throws URISyntaxException {
        log.debug("REST request to update Fan : {}", fan);
        if (fan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Fan result = fanRepository.save(fan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fan.getId()))
            .body(result);
    }

    /**
     * {@code GET  /fans} : get all the fans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fans in body.
     */
    @GetMapping("/fans")
    public ResponseEntity<List<Fan>> getAllFans(Pageable pageable) {
        log.debug("REST request to get a page of Fans");
        Page<Fan> page = fanRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fans/:id} : get the "id" fan.
     *
     * @param id the id of the fan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fans/{id}")
    public ResponseEntity<Fan> getFan(@PathVariable String id) {
        log.debug("REST request to get Fan : {}", id);
        Optional<Fan> fan = fanRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fan);
    }

    /**
     * {@code DELETE  /fans/:id} : delete the "id" fan.
     *
     * @param id the id of the fan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fans/{id}")
    public ResponseEntity<Void> deleteFan(@PathVariable String id) {
        log.debug("REST request to delete Fan : {}", id);
        fanRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }


}
