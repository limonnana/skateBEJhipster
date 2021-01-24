package com.limonnana.skate.service;

import com.limonnana.skate.domain.Player;
import com.limonnana.skate.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Player}.
 */
@Service
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Save a player.
     *
     * @param player the entity to save.
     * @return the persisted entity.
     */
    public Player save(Player player) {
        log.debug("Request to save Player : {}", player);
        return playerRepository.save(player);
    }

    /**
     * Get all the players.
     *
     * @return the list of entities.
     */
    public List<Player> findAll() {
        log.debug("Request to get all Players");
        return playerRepository.findAll();
    }


    /**
     * Get one player by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Player> findOne(String id) {
        log.debug("Request to get Player : {}", id);
        return playerRepository.findById(id);
    }

    /**
     * Delete the player by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Player : {}", id);
        playerRepository.deleteById(id);
    }
}
