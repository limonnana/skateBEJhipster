package com.limonnana.skate.web.rest;

import com.limonnana.skate.domain.Event;
import com.limonnana.skate.repository.EventRepository;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class OpenResource {

    private final EventRepository eventRepository;
    private final Logger log = LoggerFactory.getLogger(OpenResource.class);


    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public OpenResource( EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    @GetMapping("/event/active")
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



    @GetMapping("/hola")
    public ResponseEntity<String> getAllFans() {

        return ResponseEntity.ok().body("Hola ");
    }


}
