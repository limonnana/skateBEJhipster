package com.limonnana.skate.web.rest;

import com.limonnana.skate.Skate03App;
import com.limonnana.skate.domain.Fan;
import com.limonnana.skate.repository.FanRepository;

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
 * Integration tests for the {@link FanResource} REST controller.
 */
@SpringBootTest(classes = Skate03App.class)
@AutoConfigureMockMvc
@WithMockUser
public class FanResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_PATH = "AAAAAAAAAA";


    @Autowired
    private FanRepository fanRepository;

    @Autowired
    private MockMvc restFanMockMvc;

    private Fan fan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fan createEntity() {
        Fan fan = new Fan();
            //.fullName(DEFAULT_FULL_NAME)
            //.email(DEFAULT_EMAIL)
           // .phone(DEFAULT_PHONE);
        return fan;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fan createUpdatedEntity() {
        Fan fan = new Fan();
            //.fullName(UPDATED_FULL_NAME)
           // .email(UPDATED_EMAIL)
          //  .phone(UPDATED_PHONE);

        return fan;
    }

    @BeforeEach
    public void initTest() {
        fanRepository.deleteAll();
        fan = createEntity();
    }

    @Test
    public void createFan() throws Exception {
        int databaseSizeBeforeCreate = fanRepository.findAll().size();
        // Create the Fan
        restFanMockMvc.perform(post("/api/fans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fan)))
            .andExpect(status().isCreated());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeCreate + 1);
        Fan testFan = fanList.get(fanList.size() - 1);
       // assertThat(testFan.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
       // assertThat(testFan.getEmail()).isEqualTo(DEFAULT_EMAIL);
      //  assertThat(testFan.getPhone()).isEqualTo(DEFAULT_PHONE);

    }

    @Test
    public void createFanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fanRepository.findAll().size();

        // Create the Fan with an existing ID
        fan.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restFanMockMvc.perform(post("/api/fans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fan)))
            .andExpect(status().isBadRequest());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fanRepository.findAll().size();
        // set the field null
     //   fan.setFullName(null);

        // Create the Fan, which fails.


        restFanMockMvc.perform(post("/api/fans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fan)))
            .andExpect(status().isBadRequest());

        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllFans() throws Exception {
        // Initialize the database
        fanRepository.save(fan);

        // Get all the fanList
        restFanMockMvc.perform(get("/api/fans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fan.getId())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].imgPath").value(hasItem(DEFAULT_IMG_PATH)));
    }

    @Test
    public void getFan() throws Exception {
        // Initialize the database
        fanRepository.save(fan);

        // Get the fan
        restFanMockMvc.perform(get("/api/fans/{id}", fan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fan.getId()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.imgPath").value(DEFAULT_IMG_PATH));
    }
    @Test
    public void getNonExistingFan() throws Exception {
        // Get the fan
        restFanMockMvc.perform(get("/api/fans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateFan() throws Exception {
        // Initialize the database
        fanRepository.save(fan);

        int databaseSizeBeforeUpdate = fanRepository.findAll().size();

        // Update the fan
        Fan updatedFan = fanRepository.findById(fan.getId()).get();
      //  updatedFan
            //.fullName(UPDATED_FULL_NAME)
          // .email(UPDATED_EMAIL);
          //  .phone(UPDATED_PHONE);


        restFanMockMvc.perform(put("/api/fans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFan)))
            .andExpect(status().isOk());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeUpdate);
        Fan testFan = fanList.get(fanList.size() - 1);
       // assertThat(testFan.getFullName()).isEqualTo(UPDATED_FULL_NAME);
       // assertThat(testFan.getEmail()).isEqualTo(UPDATED_EMAIL);
       // assertThat(testFan.getPhone()).isEqualTo(UPDATED_PHONE);

    }

    @Test
    public void updateNonExistingFan() throws Exception {
        int databaseSizeBeforeUpdate = fanRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFanMockMvc.perform(put("/api/fans")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fan)))
            .andExpect(status().isBadRequest());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteFan() throws Exception {
        // Initialize the database
        fanRepository.save(fan);

        int databaseSizeBeforeDelete = fanRepository.findAll().size();

        // Delete the fan
        restFanMockMvc.perform(delete("/api/fans/{id}", fan.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
