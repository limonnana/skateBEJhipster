package com.limonnana.skate.web.rest;

import com.limonnana.skate.Skate03App;
import com.limonnana.skate.domain.Spot;
import com.limonnana.skate.repository.SpotRepository;

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
 * Integration tests for the {@link SpotResource} REST controller.
 */
@SpringBootTest(classes = Skate03App.class)
@AutoConfigureMockMvc
@WithMockUser
public class SpotResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMG_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private MockMvc restSpotMockMvc;

    private Spot spot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spot createEntity() {
        Spot spot = new Spot()
            .name(DEFAULT_NAME)
            .imgPath(DEFAULT_IMG_PATH)
            .description(DEFAULT_DESCRIPTION);
        return spot;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spot createUpdatedEntity() {
        Spot spot = new Spot()
            .name(UPDATED_NAME)
            .imgPath(UPDATED_IMG_PATH)
            .description(UPDATED_DESCRIPTION);
        return spot;
    }

    @BeforeEach
    public void initTest() {
        spotRepository.deleteAll();
        spot = createEntity();
    }

    @Test
    public void createSpot() throws Exception {
        int databaseSizeBeforeCreate = spotRepository.findAll().size();
        // Create the Spot
        restSpotMockMvc.perform(post("/api/spots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spot)))
            .andExpect(status().isCreated());

        // Validate the Spot in the database
        List<Spot> spotList = spotRepository.findAll();
        assertThat(spotList).hasSize(databaseSizeBeforeCreate + 1);
        Spot testSpot = spotList.get(spotList.size() - 1);
        assertThat(testSpot.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpot.getImgPath()).isEqualTo(DEFAULT_IMG_PATH);
        assertThat(testSpot.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    public void createSpotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = spotRepository.findAll().size();

        // Create the Spot with an existing ID
        spot.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpotMockMvc.perform(post("/api/spots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spot)))
            .andExpect(status().isBadRequest());

        // Validate the Spot in the database
        List<Spot> spotList = spotRepository.findAll();
        assertThat(spotList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = spotRepository.findAll().size();
        // set the field null
        spot.setName(null);

        // Create the Spot, which fails.


        restSpotMockMvc.perform(post("/api/spots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spot)))
            .andExpect(status().isBadRequest());

        List<Spot> spotList = spotRepository.findAll();
        assertThat(spotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSpots() throws Exception {
        // Initialize the database
        spotRepository.save(spot);

        // Get all the spotList
        restSpotMockMvc.perform(get("/api/spots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spot.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imgPath").value(hasItem(DEFAULT_IMG_PATH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    public void getSpot() throws Exception {
        // Initialize the database
        spotRepository.save(spot);

        // Get the spot
        restSpotMockMvc.perform(get("/api/spots/{id}", spot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spot.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imgPath").value(DEFAULT_IMG_PATH))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    public void getNonExistingSpot() throws Exception {
        // Get the spot
        restSpotMockMvc.perform(get("/api/spots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSpot() throws Exception {
        // Initialize the database
        spotRepository.save(spot);

        int databaseSizeBeforeUpdate = spotRepository.findAll().size();

        // Update the spot
        Spot updatedSpot = spotRepository.findById(spot.getId()).get();
        updatedSpot
            .name(UPDATED_NAME)
            .imgPath(UPDATED_IMG_PATH)
            .description(UPDATED_DESCRIPTION);

        restSpotMockMvc.perform(put("/api/spots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpot)))
            .andExpect(status().isOk());

        // Validate the Spot in the database
        List<Spot> spotList = spotRepository.findAll();
        assertThat(spotList).hasSize(databaseSizeBeforeUpdate);
        Spot testSpot = spotList.get(spotList.size() - 1);
        assertThat(testSpot.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpot.getImgPath()).isEqualTo(UPDATED_IMG_PATH);
        assertThat(testSpot.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    public void updateNonExistingSpot() throws Exception {
        int databaseSizeBeforeUpdate = spotRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpotMockMvc.perform(put("/api/spots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(spot)))
            .andExpect(status().isBadRequest());

        // Validate the Spot in the database
        List<Spot> spotList = spotRepository.findAll();
        assertThat(spotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSpot() throws Exception {
        // Initialize the database
        spotRepository.save(spot);

        int databaseSizeBeforeDelete = spotRepository.findAll().size();

        // Delete the spot
        restSpotMockMvc.perform(delete("/api/spots/{id}", spot.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Spot> spotList = spotRepository.findAll();
        assertThat(spotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
