package com.limonnana.skate.web.rest;

import com.limonnana.skate.domain.Event;
import com.limonnana.skate.domain.Trick;
import com.limonnana.skate.repository.EventRepository;
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
 * REST controller for managing {@link com.limonnana.skate.domain.Trick}.
 */
@RestController
@RequestMapping("/api")
public class TrickResource {

    private final Logger log = LoggerFactory.getLogger(TrickResource.class);

    private static final String ENTITY_NAME = "trick";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrickRepository trickRepository;

    private final EventResource eventResource;

    private final EventRepository eventRepository;

    public TrickResource(TrickRepository trickRepository, EventResource eventResource, EventRepository eventRepository) {
        this.trickRepository = trickRepository;
        this.eventResource = eventResource;
        this.eventRepository = eventRepository;
    }

    /**
     * {@code POST  /tricks} : Create a new trick.
     *
     * @param trick the trick to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trick, or with status {@code 400 (Bad Request)} if the trick has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tricks")
    public ResponseEntity<Trick> createTrick(@Valid @RequestBody Trick trick) throws Exception {
        log.debug("REST request to save Trick : {}", trick);
        if (trick.getId() != null) {
            throw new BadRequestAlertException("A new trick cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Trick result = trickRepository.save(trick);
        Iterable<Event> activeIterable = eventRepository.findByActiveTrue();
        Event active = activeIterable.iterator().next();
        active.getTricks().add(result);
        eventRepository.save(active);
        return ResponseEntity.created(new URI("/api/tricks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /tricks} : Updates an existing trick.
     *
     * @param trick the trick to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trick,
     * or with status {@code 400 (Bad Request)} if the trick is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trick couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tricks")
    public ResponseEntity<Trick> updateTrick(@Valid @RequestBody Trick trick) throws URISyntaxException {
        log.debug("REST request to update Trick : {}", trick);
        if (trick.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Trick result = trickRepository.save(trick);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trick.getId()))
            .body(result);
    }

    /**
     * {@code GET  /tricks} : get all the tricks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tricks in body.
     */
    @GetMapping("/tricks")
    public ResponseEntity<List<Trick>> getAllTricks(Pageable pageable) {
        log.debug("REST request to get a page of Tricks");
        Page<Trick> page = trickRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tricks/:id} : get the "id" trick.
     *
     * @param id the id of the trick to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trick, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tricks/{id}")
    public ResponseEntity<Trick> getTrick(@PathVariable String id) {
        log.debug("REST request to get Trick : {}", id);
        Optional<Trick> trick = trickRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trick);
    }

    /**
     * {@code DELETE  /tricks/:id} : delete the "id" trick.
     *
     * @param id the id of the trick to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tricks/{id}")
    public ResponseEntity<Void> deleteTrick(@PathVariable String id) {
        log.debug("REST request to delete Trick : {}", id);
        trickRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
