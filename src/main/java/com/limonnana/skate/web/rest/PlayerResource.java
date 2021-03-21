package com.limonnana.skate.web.rest;

import com.limonnana.skate.domain.Player;
import com.limonnana.skate.domain.User;
import com.limonnana.skate.repository.PlayerRepository;
import com.limonnana.skate.repository.UserRepository;
import com.limonnana.skate.service.UserService;
import com.limonnana.skate.service.dto.UserDTO;
import com.limonnana.skate.web.rest.errors.BadRequestAlertException;

import com.limonnana.skate.web.rest.errors.LoginAlreadyUsedException;
import com.limonnana.skate.web.rest.errors.PhoneAlreadyUsedException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.limonnana.skate.domain.Player}.
 */
@RestController
@RequestMapping("/api")
public class PlayerResource {

    private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

    private static final String ENTITY_NAME = "player";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayerRepository playerRepository;
    private final UserService userService;
    private final UserRepository userRepository;



    public PlayerResource(PlayerRepository playerRepository, UserService userService, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /players} : Create a new player.
     *
     * @param player the player to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new player, or with status {@code 400 (Bad Request)} if the player has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save Player : {}", userDTO);
        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new player cannot already have an ID", ENTITY_NAME, "idexists");
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        user.setActivated(true);
        user.setLogin(userDTO.getPhone());
        user.setCountry(userDTO.getCountry());

        if (userRepository.findOneByLogin(user.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();

        } else if (userRepository.findOneByLogin(user.getPhone()).isPresent()){
            throw new PhoneAlreadyUsedException();
        }

        user = userService.registerUserFromContribution(user);
        Player p = new Player();
        p.setUser(user);
        Player result = playerRepository.save(p);
        return ResponseEntity.created(new URI("/api/players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /players} : Updates an existing player.
     *
     * @param player the player to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated player,
     * or with status {@code 400 (Bad Request)} if the player is not valid,
     * or with status {@code 500 (Internal Server Error)} if the player couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/players")
    public ResponseEntity<Player> updatePlayer(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to update Player : {}", userDTO);
        if (userDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Player player = playerRepository.findById(userDTO.getId()).get();
        User user = player.getUser();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        user.setActivated(true);
        user.setLogin(userDTO.getPhone());
        user.setCountry(userDTO.getCountry());
        userRepository.save(user);
        player.setUser(user);
        Player result = playerRepository.save(player);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code GET  /players} : get all the players.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of players in body.
     */
    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        log.debug("REST request to get all Players");
        return playerRepository.findAll();
    }

    /**
     * {@code GET  /players/:id} : get the "id" player.
     *
     * @param id the id of the player to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the player, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable String id) {
        log.debug("REST request to get Player : {}", id);
        Optional<Player> player = playerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(player);
    }

    /**
     * {@code DELETE  /players/:id} : delete the "id" player.
     *
     * @param id the id of the player to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String id) {
        log.debug("REST request to delete Player : {}", id);
        playerRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
