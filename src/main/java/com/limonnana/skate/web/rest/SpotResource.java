package com.limonnana.skate.web.rest;

import com.limonnana.skate.domain.Event;
import com.limonnana.skate.domain.Photo;
import com.limonnana.skate.domain.Spot;
import com.limonnana.skate.repository.PhotoRepository;
import com.limonnana.skate.repository.SpotRepository;
import com.limonnana.skate.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing {@link com.limonnana.skate.domain.Spot}.
 */
@RestController
@RequestMapping("/api")
public class SpotResource {

    private final Logger log = LoggerFactory.getLogger(SpotResource.class);

    private static final String ENTITY_NAME = "spot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpotRepository spotRepository;
    private final PhotoRepository photoRepository;

    public SpotResource(
        SpotRepository spotRepository,
        PhotoRepository photoRepository
    ) {
        this.spotRepository = spotRepository;
        this.photoRepository = photoRepository;
    }

    /**
     * {@code POST  /spots} : Create a new spot.
     *
     * @param spot the spot to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spot, or with status {@code 400 (Bad Request)} if the spot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spots")
    public ResponseEntity<Spot> createSpot(@Valid @RequestBody Spot spot) throws URISyntaxException {
        log.debug("REST request to save Spot : {}", spot);
        if (spot.getId() != null) {
            throw new BadRequestAlertException("A new spot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Spot result = spotRepository.save(spot);
        return ResponseEntity.created(new URI("/api/spots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /spots} : Updates an existing spot.
     *
     * @param spot the spot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spot,
     * or with status {@code 400 (Bad Request)} if the spot is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spots")
    public ResponseEntity<Spot> updateSpot(@Valid @RequestBody Spot spot) throws URISyntaxException {
        log.debug("REST request to update Spot : {}", spot);
        if (spot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Spot result = spotRepository.save(spot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spot.getId()))
            .body(result);
    }

    @PostMapping("/spots/addImage")
    public ResponseEntity<Spot> addImage(@RequestPart("title") String title, @RequestPart("idSpot") String idSpot, @RequestPart("file") String file ) throws IOException {

        Spot spot = spotRepository.findById(idSpot).get();
        Photo p = new Photo();
        p.setImage(file);
        p.setTitle(title);
        p = photoRepository.save(p);
        spot.getPhotos().add(p);
        Spot result = spotRepository.save(spot);
        return ResponseUtil.wrapOrNotFound(Optional.of(result));
    }

    @PostMapping("/spots/deleteImage")
    public ResponseEntity<Spot> deleteImage(@RequestPart("idImage") String idImage, @RequestPart("idSpot") String idSpot) {
        log.debug("REST request to delete Image from spot: {}", idImage);
        Spot spot = spotRepository.findById(idSpot).get();
        removeObjectFromSet(spot.getPhotos(), idImage);
        Spot result = spotRepository.save(spot);
        photoRepository.deleteById(idImage);
        return ResponseUtil.wrapOrNotFound(Optional.of(result));
    }

    /**
     * {@code GET  /spots} : get all the spots.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spots in body.
     */
    @GetMapping("/spots")
    public List<Spot> getAllSpots() {
        log.debug("REST request to get all Spots");
        return spotRepository.findAll();
    }

    /**
     * {@code GET  /spots/:id} : get the "id" spot.
     *
     * @param id the id of the spot to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spot, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spots/{id}")
    public ResponseEntity<Spot> getSpot(@PathVariable String id) {
        log.debug("REST request to get Spot : {}", id);
        Optional<Spot> spot = spotRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(spot);
    }

    /**
     * {@code DELETE  /spots/:id} : delete the "id" spot.
     *
     * @param id the id of the spot to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spots/{id}")
    public ResponseEntity<Void> deleteSpot(@PathVariable String id) {
        log.debug("REST request to delete Spot : {}", id);
        spotRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    private void removeObjectFromSet(Set<Photo> s, String photoId){
        Iterator<Photo> iterator = s.iterator();
        while(iterator.hasNext())
        {
            if(iterator.next().getId().equals(photoId))
                iterator.remove();
        }

    }
}
