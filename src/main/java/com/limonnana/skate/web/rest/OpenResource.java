package com.limonnana.skate.web.rest;

import com.limonnana.skate.domain.Fan;
import com.limonnana.skate.repository.FanRepository;
import com.limonnana.skate.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class OpenResource {

    private final FanRepository fanRepository;
    private final Logger log = LoggerFactory.getLogger(OpenResource.class);


    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public OpenResource( FanRepository fanRepository){
        this.fanRepository = fanRepository;
    }

    /**
     * {@code POST  /fans} : Create a new fan.
     *
     * @param fan the fan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fan, or with status {@code 400 (Bad Request)} if the fan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/openfans")
    public ResponseEntity<Fan> createFan(@Valid @RequestBody Fan fan) throws URISyntaxException {
        log.debug("REST request to save Fan : {}", fan);
        if (fan.getId() != null) {
            throw new BadRequestAlertException("A new fan cannot already have an ID", "Fan", "idexists");
        }
        Fan result = fanRepository.save(fan);
        return ResponseEntity.created(new URI("/open/fans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, "Fan", result.getId()))
            .body(result);
    }

    @GetMapping("/openfans")
    public ResponseEntity<String> getAllFans() {

        return ResponseEntity.ok().body("Hola ");
    }


}
