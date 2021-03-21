package com.limonnana.skate.web.rest;

import com.limonnana.skate.domain.*;
import com.limonnana.skate.repository.EventRepository;
import com.limonnana.skate.repository.PhotoRepository;
import com.limonnana.skate.repository.PlayerRepository;
import com.limonnana.skate.repository.TrickRepository;
import com.limonnana.skate.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
 * REST controller for managing {@link com.limonnana.skate.domain.Event}.
 */
@RestController
@RequestMapping("/api")
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);

    private static final String ENTITY_NAME = "event";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventRepository eventRepository;
    private final TrickRepository trickRepository;
    private final PlayerRepository playerRepository;
    private final PhotoRepository photoRepository;

    public EventResource(EventRepository eventRepository, TrickRepository trickRepository, PlayerRepository playerRepository, PhotoRepository photoRepository) {

        this.eventRepository = eventRepository;
        this.trickRepository = trickRepository;
        this.playerRepository = playerRepository;
        this.photoRepository = photoRepository;
    }

    /**
     * {@code POST  /events} : Create a new event.
     *
     * @param event the event to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new event, or with status {@code 400 (Bad Request)} if the event has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) throws URISyntaxException {
        log.debug("REST request to save Event : {}", event);

        if (event.getId() != null) {
            throw new BadRequestAlertException("A new event cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Event result = eventRepository.save(event);
        return ResponseEntity.created(new URI("/api/events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /events} : Updates an existing event.
     *
     * @param event the event to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated event,
     * or with status {@code 400 (Bad Request)} if the event is not valid,
     * or with status {@code 500 (Internal Server Error)} if the event couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/events")
    public ResponseEntity<Event> updateEvent(@Valid @RequestBody Event event) throws URISyntaxException {
        log.debug("REST request to update Event : {}", event);
        if (event.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Event e = eventRepository.findById(event.getId()).get();
        e.setDay(event.getDay());
        e.setDayString(event.getDayString());
        e.setName(event.getName());
        e.setSpot(event.getSpot());
        e.setActive(event.isActive());
        Event result = eventRepository.save(e);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, event.getId()))
            .body(result);
    }

    @GetMapping("/events/active")
    public ResponseEntity<Event> getActive() throws Exception {
        log.debug("REST request to get Active Event : {}");
        List<Event> events = eventRepository.findAll();
        Event result = null;
        if(events == null){
            throw new Exception();
        }
        for(Event e : events){
            if(e.isActive()){
                result = e;
                break;
            }
        }
        return ResponseUtil.wrapOrNotFound(Optional.of(result));
    }

    @PostMapping("/events/addImage")
    public ResponseEntity<Event> addImage(@RequestPart("title") String title, @RequestPart("idEvent") String idEvent, @RequestPart("file") String file ) throws IOException {

        Event event = eventRepository.findById(idEvent).get();
        Photo p = new Photo();
        p.setImage(file);
        p.setTitle(title);
        p = photoRepository.save(p);
        event.getPhotos().add(p);
        Event result = eventRepository.save(event);

        return ResponseUtil.wrapOrNotFound(Optional.of(result));
    }

    @PostMapping("/events/deleteImage")
    public ResponseEntity<Event> deleteImage(@RequestPart("idImage") String idImage, @RequestPart("idEvent") String idEvent) {
        log.debug("REST request to delete Image : {}", idImage);
        Event event = eventRepository.findById(idEvent).get();
        removeObjectFromSet(event.getPhotos(), idImage);
        Event result = eventRepository.save(event);
        photoRepository.deleteById(idImage);
        return ResponseUtil.wrapOrNotFound(Optional.of(result));
    }

    @PutMapping("/events/addPlayer")
    public ResponseEntity<Event> addPlayer(@Valid @RequestBody AddPlayer addPlayer)throws URISyntaxException{
        log.debug("REST request to addPlayer AddPlayer : {}", addPlayer);
        if(addPlayer.getIdEvent() == null){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "eventIdnull");
        }
        Event event = eventRepository.findById(addPlayer.getIdEvent()).get();
        Player player = playerRepository.findById(addPlayer.getIdPlayer()).get();
        event.addPlayer(player);
        Event result = eventRepository.save(event);

        return ResponseUtil.wrapOrNotFound(Optional.of(result));
    }

    @PutMapping("/events/addTrick")
    public ResponseEntity<Event> addTrick(@Valid @RequestBody AddTrick addTrick)throws URISyntaxException{
        log.debug("REST request to addTrick AddTrick : {}", addTrick);
        if(addTrick.getIdEvent() == null){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "eventIdnull");
        }
        Event event = eventRepository.findById(addTrick.getIdEvent()).get();
        Trick trick = trickRepository.findById(addTrick.getIdTrick()).get();
        event.addTrick(trick);
        Event result = eventRepository.save(event);

        return ResponseUtil.wrapOrNotFound(Optional.of(result));
    }
    /**
     * {@code GET  /events} : get all the events.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of events in body.
     */
    @GetMapping("/events")
    public List<Event> getAllEvents() {
        log.debug("REST request to get all Events");
        return eventRepository.findAll();
    }

    /**
     * {@code GET  /events/:id} : get the "id" event.
     *
     * @param id the id of the event to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the event, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable String id) {
        log.debug("REST request to get Event : {}", id);
        Optional<Event> event = eventRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(event);
    }


    /**
     * {@code DELETE  /events/:id} : delete the "id" event.
     *
     * @param id the id of the event to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        log.debug("REST request to delete Event : {}", id);
        eventRepository.deleteById(id);
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
