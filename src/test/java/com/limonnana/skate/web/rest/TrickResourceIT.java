package com.limonnana.skate.web.rest;

import com.limonnana.skate.Skate03App;
import com.limonnana.skate.domain.Trick;
import com.limonnana.skate.repository.TrickRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TrickResource} REST controller.
 */
@SpringBootTest(classes = Skate03App.class)
@AutoConfigureMockMvc
@WithMockUser
public class TrickResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_OBJECTIVE_AMOUNT = 1;
    private static final Integer UPDATED_OBJECTIVE_AMOUNT = 2;

    private static final Integer DEFAULT_CURRENT_AMOUNT = 1;
    private static final Integer UPDATED_CURRENT_AMOUNT = 2;

    @Autowired
    private TrickRepository trickRepository;

    @Autowired
    private MockMvc restTrickMockMvc;

    private Trick trick;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trick createEntity() {
        Trick trick = new Trick()
            .name(DEFAULT_NAME)
            .objectiveAmount(DEFAULT_OBJECTIVE_AMOUNT)
            .currentAmount(DEFAULT_CURRENT_AMOUNT);
        return trick;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trick createUpdatedEntity() {
        Trick trick = new Trick()
            .name(UPDATED_NAME)
            .objectiveAmount(UPDATED_OBJECTIVE_AMOUNT)
            .currentAmount(UPDATED_CURRENT_AMOUNT);
        return trick;
    }

    @BeforeEach
    public void initTest() {
        trickRepository.deleteAll();
        trick = createEntity();
    }

    @Test
    public void createTrick() throws Exception {
        int databaseSizeBeforeCreate = trickRepository.findAll().size();
        // Create the Trick
        restTrickMockMvc.perform(post("/api/tricks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(trick)))
            .andExpect(status().isCreated());

        // Validate the Trick in the database
        List<Trick> trickList = trickRepository.findAll();
        assertThat(trickList).hasSize(databaseSizeBeforeCreate + 1);
        Trick testTrick = trickList.get(trickList.size() - 1);
        assertThat(testTrick.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrick.getObjectiveAmount()).isEqualTo(DEFAULT_OBJECTIVE_AMOUNT);
        assertThat(testTrick.getCurrentAmount()).isEqualTo(DEFAULT_CURRENT_AMOUNT);
    }

    @Test
    public void createTrickWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trickRepository.findAll().size();

        // Create the Trick with an existing ID
        trick.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrickMockMvc.perform(post("/api/tricks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(trick)))
            .andExpect(status().isBadRequest());

        // Validate the Trick in the database
        List<Trick> trickList = trickRepository.findAll();
        assertThat(trickList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trickRepository.findAll().size();
        // set the field null
        trick.setName(null);

        // Create the Trick, which fails.


        restTrickMockMvc.perform(post("/api/tricks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(trick)))
            .andExpect(status().isBadRequest());

        List<Trick> trickList = trickRepository.findAll();
        assertThat(trickList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTricks() throws Exception {
        // Initialize the database
        trickRepository.save(trick);

        // Get all the trickList
        restTrickMockMvc.perform(get("/api/tricks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trick.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].objectiveAmount").value(hasItem(DEFAULT_OBJECTIVE_AMOUNT)))
            .andExpect(jsonPath("$.[*].currentAmount").value(hasItem(DEFAULT_CURRENT_AMOUNT)));
    }
    
    @Test
    public void getTrick() throws Exception {
        // Initialize the database
        trickRepository.save(trick);

        // Get the trick
        restTrickMockMvc.perform(get("/api/tricks/{id}", trick.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trick.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.objectiveAmount").value(DEFAULT_OBJECTIVE_AMOUNT))
            .andExpect(jsonPath("$.currentAmount").value(DEFAULT_CURRENT_AMOUNT));
    }
    @Test
    public void getNonExistingTrick() throws Exception {
        // Get the trick
        restTrickMockMvc.perform(get("/api/tricks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTrick() throws Exception {
        // Initialize the database
        trickRepository.save(trick);

        int databaseSizeBeforeUpdate = trickRepository.findAll().size();

        // Update the trick
        Trick updatedTrick = trickRepository.findById(trick.getId()).get();
        updatedTrick
            .name(UPDATED_NAME)
            .objectiveAmount(UPDATED_OBJECTIVE_AMOUNT)
            .currentAmount(UPDATED_CURRENT_AMOUNT);

        restTrickMockMvc.perform(put("/api/tricks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrick)))
            .andExpect(status().isOk());

        // Validate the Trick in the database
        List<Trick> trickList = trickRepository.findAll();
        assertThat(trickList).hasSize(databaseSizeBeforeUpdate);
        Trick testTrick = trickList.get(trickList.size() - 1);
        assertThat(testTrick.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrick.getObjectiveAmount()).isEqualTo(UPDATED_OBJECTIVE_AMOUNT);
        assertThat(testTrick.getCurrentAmount()).isEqualTo(UPDATED_CURRENT_AMOUNT);
    }

    @Test
    public void updateNonExistingTrick() throws Exception {
        int databaseSizeBeforeUpdate = trickRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrickMockMvc.perform(put("/api/tricks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(trick)))
            .andExpect(status().isBadRequest());

        // Validate the Trick in the database
        List<Trick> trickList = trickRepository.findAll();
        assertThat(trickList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTrick() throws Exception {
        // Initialize the database
        trickRepository.save(trick);

        int databaseSizeBeforeDelete = trickRepository.findAll().size();

        // Delete the trick
        restTrickMockMvc.perform(delete("/api/tricks/{id}", trick.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trick> trickList = trickRepository.findAll();
        assertThat(trickList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
