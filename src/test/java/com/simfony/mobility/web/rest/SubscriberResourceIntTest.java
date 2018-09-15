package com.simfony.mobility.web.rest;

import com.simfony.mobility.Es2App;

import com.simfony.mobility.domain.Subscriber;
import com.simfony.mobility.repository.SubscriberRepository;
import com.simfony.mobility.repository.search.SubscriberSearchRepository;
import com.simfony.mobility.service.SubscriberService;
import com.simfony.mobility.service.dto.SubscriberDTO;
import com.simfony.mobility.service.mapper.SubscriberMapper;
import com.simfony.mobility.web.rest.errors.ExceptionTranslator;
import com.simfony.mobility.service.dto.SubscriberCriteria;
import com.simfony.mobility.service.SubscriberQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.simfony.mobility.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SubscriberResource REST controller.
 *
 * @see SubscriberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Es2App.class)
public class SubscriberResourceIntTest {

    private static final String DEFAULT_IMSI = "AAAAAAAAAAAAAAA";
    private static final String UPDATED_IMSI = "BBBBBBBBBBBBBBB";

    private static final String DEFAULT_MSISDN = "AAAAAAAAAAAAAAA";
    private static final String UPDATED_MSISDN = "BBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_NAM = 0;
    private static final Integer UPDATED_NAM = 1;

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final Integer DEFAULT_CATEGORY = 10;
    private static final Integer UPDATED_CATEGORY = 11;

    private static final Integer DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS = 4;
    private static final Integer UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS = 5;

    private static final String DEFAULT_ICCID = "AAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ICCID = "BBBBBBBBBBBBBBBBBBB";

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SubscriberMapper subscriberMapper;
    
    @Autowired
    private SubscriberService subscriberService;

    /**
     * This repository is mocked in the com.simfony.mobility.repository.search test package.
     *
     * @see com.simfony.mobility.repository.search.SubscriberSearchRepositoryMockConfiguration
     */
    @Autowired
    private SubscriberSearchRepository mockSubscriberSearchRepository;

    @Autowired
    private SubscriberQueryService subscriberQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubscriberMockMvc;

    private Subscriber subscriber;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubscriberResource subscriberResource = new SubscriberResource(subscriberService, subscriberQueryService);
        this.restSubscriberMockMvc = MockMvcBuilders.standaloneSetup(subscriberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subscriber createEntity(EntityManager em) {
        Subscriber subscriber = new Subscriber()
            .imsi(DEFAULT_IMSI)
            .msisdn(DEFAULT_MSISDN)
            .nam(DEFAULT_NAM)
            .state(DEFAULT_STATE)
            .category(DEFAULT_CATEGORY)
            .subscriberChargingCharacteristics(DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS)
            .iccid(DEFAULT_ICCID);
        return subscriber;
    }

    @Before
    public void initTest() {
        subscriber = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubscriber() throws Exception {
        int databaseSizeBeforeCreate = subscriberRepository.findAll().size();

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);
        restSubscriberMockMvc.perform(post("/api/subscribers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isCreated());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeCreate + 1);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getImsi()).isEqualTo(DEFAULT_IMSI);
        assertThat(testSubscriber.getMsisdn()).isEqualTo(DEFAULT_MSISDN);
        assertThat(testSubscriber.getNam()).isEqualTo(DEFAULT_NAM);
        assertThat(testSubscriber.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSubscriber.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testSubscriber.getSubscriberChargingCharacteristics()).isEqualTo(DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS);
        assertThat(testSubscriber.getIccid()).isEqualTo(DEFAULT_ICCID);

        // Validate the Subscriber in Elasticsearch
        verify(mockSubscriberSearchRepository, times(1)).save(testSubscriber);
    }

    @Test
    @Transactional
    public void createSubscriberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subscriberRepository.findAll().size();

        // Create the Subscriber with an existing ID
        subscriber.setId(1L);
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriberMockMvc.perform(post("/api/subscribers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeCreate);

        // Validate the Subscriber in Elasticsearch
        verify(mockSubscriberSearchRepository, times(0)).save(subscriber);
    }

    @Test
    @Transactional
    public void checkImsiIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setImsi(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        restSubscriberMockMvc.perform(post("/api/subscribers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMsisdnIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setMsisdn(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        restSubscriberMockMvc.perform(post("/api/subscribers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNamIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setNam(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        restSubscriberMockMvc.perform(post("/api/subscribers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setState(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        restSubscriberMockMvc.perform(post("/api/subscribers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setCategory(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        restSubscriberMockMvc.perform(post("/api/subscribers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubscriberChargingCharacteristicsIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setSubscriberChargingCharacteristics(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        restSubscriberMockMvc.perform(post("/api/subscribers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIccidIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setIccid(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        restSubscriberMockMvc.perform(post("/api/subscribers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubscribers() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList
        restSubscriberMockMvc.perform(get("/api/subscribers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriber.getId().intValue())))
            .andExpect(jsonPath("$.[*].imsi").value(hasItem(DEFAULT_IMSI.toString())))
            .andExpect(jsonPath("$.[*].msisdn").value(hasItem(DEFAULT_MSISDN.toString())))
            .andExpect(jsonPath("$.[*].nam").value(hasItem(DEFAULT_NAM)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].subscriberChargingCharacteristics").value(hasItem(DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS)))
            .andExpect(jsonPath("$.[*].iccid").value(hasItem(DEFAULT_ICCID.toString())));
    }
    
    @Test
    @Transactional
    public void getSubscriber() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get the subscriber
        restSubscriberMockMvc.perform(get("/api/subscribers/{id}", subscriber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subscriber.getId().intValue()))
            .andExpect(jsonPath("$.imsi").value(DEFAULT_IMSI.toString()))
            .andExpect(jsonPath("$.msisdn").value(DEFAULT_MSISDN.toString()))
            .andExpect(jsonPath("$.nam").value(DEFAULT_NAM))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.subscriberChargingCharacteristics").value(DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS))
            .andExpect(jsonPath("$.iccid").value(DEFAULT_ICCID.toString()));
    }

    @Test
    @Transactional
    public void getAllSubscribersByImsiIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where imsi equals to DEFAULT_IMSI
        defaultSubscriberShouldBeFound("imsi.equals=" + DEFAULT_IMSI);

        // Get all the subscriberList where imsi equals to UPDATED_IMSI
        defaultSubscriberShouldNotBeFound("imsi.equals=" + UPDATED_IMSI);
    }

    @Test
    @Transactional
    public void getAllSubscribersByImsiIsInShouldWork() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where imsi in DEFAULT_IMSI or UPDATED_IMSI
        defaultSubscriberShouldBeFound("imsi.in=" + DEFAULT_IMSI + "," + UPDATED_IMSI);

        // Get all the subscriberList where imsi equals to UPDATED_IMSI
        defaultSubscriberShouldNotBeFound("imsi.in=" + UPDATED_IMSI);
    }

    @Test
    @Transactional
    public void getAllSubscribersByImsiIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where imsi is not null
        defaultSubscriberShouldBeFound("imsi.specified=true");

        // Get all the subscriberList where imsi is null
        defaultSubscriberShouldNotBeFound("imsi.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubscribersByMsisdnIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where msisdn equals to DEFAULT_MSISDN
        defaultSubscriberShouldBeFound("msisdn.equals=" + DEFAULT_MSISDN);

        // Get all the subscriberList where msisdn equals to UPDATED_MSISDN
        defaultSubscriberShouldNotBeFound("msisdn.equals=" + UPDATED_MSISDN);
    }

    @Test
    @Transactional
    public void getAllSubscribersByMsisdnIsInShouldWork() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where msisdn in DEFAULT_MSISDN or UPDATED_MSISDN
        defaultSubscriberShouldBeFound("msisdn.in=" + DEFAULT_MSISDN + "," + UPDATED_MSISDN);

        // Get all the subscriberList where msisdn equals to UPDATED_MSISDN
        defaultSubscriberShouldNotBeFound("msisdn.in=" + UPDATED_MSISDN);
    }

    @Test
    @Transactional
    public void getAllSubscribersByMsisdnIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where msisdn is not null
        defaultSubscriberShouldBeFound("msisdn.specified=true");

        // Get all the subscriberList where msisdn is null
        defaultSubscriberShouldNotBeFound("msisdn.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubscribersByNamIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where nam equals to DEFAULT_NAM
        defaultSubscriberShouldBeFound("nam.equals=" + DEFAULT_NAM);

        // Get all the subscriberList where nam equals to UPDATED_NAM
        defaultSubscriberShouldNotBeFound("nam.equals=" + UPDATED_NAM);
    }

    @Test
    @Transactional
    public void getAllSubscribersByNamIsInShouldWork() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where nam in DEFAULT_NAM or UPDATED_NAM
        defaultSubscriberShouldBeFound("nam.in=" + DEFAULT_NAM + "," + UPDATED_NAM);

        // Get all the subscriberList where nam equals to UPDATED_NAM
        defaultSubscriberShouldNotBeFound("nam.in=" + UPDATED_NAM);
    }

    @Test
    @Transactional
    public void getAllSubscribersByNamIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where nam is not null
        defaultSubscriberShouldBeFound("nam.specified=true");

        // Get all the subscriberList where nam is null
        defaultSubscriberShouldNotBeFound("nam.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubscribersByNamIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where nam greater than or equals to DEFAULT_NAM
        defaultSubscriberShouldBeFound("nam.greaterOrEqualThan=" + DEFAULT_NAM);

        // Get all the subscriberList where nam greater than or equals to (DEFAULT_NAM + 1)
        defaultSubscriberShouldNotBeFound("nam.greaterOrEqualThan=" + (DEFAULT_NAM + 1));
    }

    @Test
    @Transactional
    public void getAllSubscribersByNamIsLessThanSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where nam less than or equals to DEFAULT_NAM
        defaultSubscriberShouldNotBeFound("nam.lessThan=" + DEFAULT_NAM);

        // Get all the subscriberList where nam less than or equals to (DEFAULT_NAM + 1)
        defaultSubscriberShouldBeFound("nam.lessThan=" + (DEFAULT_NAM + 1));
    }


    @Test
    @Transactional
    public void getAllSubscribersByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where state equals to DEFAULT_STATE
        defaultSubscriberShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the subscriberList where state equals to UPDATED_STATE
        defaultSubscriberShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllSubscribersByStateIsInShouldWork() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where state in DEFAULT_STATE or UPDATED_STATE
        defaultSubscriberShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the subscriberList where state equals to UPDATED_STATE
        defaultSubscriberShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllSubscribersByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where state is not null
        defaultSubscriberShouldBeFound("state.specified=true");

        // Get all the subscriberList where state is null
        defaultSubscriberShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubscribersByStateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where state greater than or equals to DEFAULT_STATE
        defaultSubscriberShouldBeFound("state.greaterOrEqualThan=" + DEFAULT_STATE);

        // Get all the subscriberList where state greater than or equals to (DEFAULT_STATE + 1)
        defaultSubscriberShouldNotBeFound("state.greaterOrEqualThan=" + (DEFAULT_STATE + 1));
    }

    @Test
    @Transactional
    public void getAllSubscribersByStateIsLessThanSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where state less than or equals to DEFAULT_STATE
        defaultSubscriberShouldNotBeFound("state.lessThan=" + DEFAULT_STATE);

        // Get all the subscriberList where state less than or equals to (DEFAULT_STATE + 1)
        defaultSubscriberShouldBeFound("state.lessThan=" + (DEFAULT_STATE + 1));
    }


    @Test
    @Transactional
    public void getAllSubscribersByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where category equals to DEFAULT_CATEGORY
        defaultSubscriberShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the subscriberList where category equals to UPDATED_CATEGORY
        defaultSubscriberShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllSubscribersByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultSubscriberShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the subscriberList where category equals to UPDATED_CATEGORY
        defaultSubscriberShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllSubscribersByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where category is not null
        defaultSubscriberShouldBeFound("category.specified=true");

        // Get all the subscriberList where category is null
        defaultSubscriberShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubscribersByCategoryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where category greater than or equals to DEFAULT_CATEGORY
        defaultSubscriberShouldBeFound("category.greaterOrEqualThan=" + DEFAULT_CATEGORY);

        // Get all the subscriberList where category greater than or equals to (DEFAULT_CATEGORY + 1)
        defaultSubscriberShouldNotBeFound("category.greaterOrEqualThan=" + (DEFAULT_CATEGORY + 1));
    }

    @Test
    @Transactional
    public void getAllSubscribersByCategoryIsLessThanSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where category less than or equals to DEFAULT_CATEGORY
        defaultSubscriberShouldNotBeFound("category.lessThan=" + DEFAULT_CATEGORY);

        // Get all the subscriberList where category less than or equals to (DEFAULT_CATEGORY + 1)
        defaultSubscriberShouldBeFound("category.lessThan=" + (DEFAULT_CATEGORY + 1));
    }


    @Test
    @Transactional
    public void getAllSubscribersBySubscriberChargingCharacteristicsIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where subscriberChargingCharacteristics equals to DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS
        defaultSubscriberShouldBeFound("subscriberChargingCharacteristics.equals=" + DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS);

        // Get all the subscriberList where subscriberChargingCharacteristics equals to UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS
        defaultSubscriberShouldNotBeFound("subscriberChargingCharacteristics.equals=" + UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS);
    }

    @Test
    @Transactional
    public void getAllSubscribersBySubscriberChargingCharacteristicsIsInShouldWork() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where subscriberChargingCharacteristics in DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS or UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS
        defaultSubscriberShouldBeFound("subscriberChargingCharacteristics.in=" + DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS + "," + UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS);

        // Get all the subscriberList where subscriberChargingCharacteristics equals to UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS
        defaultSubscriberShouldNotBeFound("subscriberChargingCharacteristics.in=" + UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS);
    }

    @Test
    @Transactional
    public void getAllSubscribersBySubscriberChargingCharacteristicsIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where subscriberChargingCharacteristics is not null
        defaultSubscriberShouldBeFound("subscriberChargingCharacteristics.specified=true");

        // Get all the subscriberList where subscriberChargingCharacteristics is null
        defaultSubscriberShouldNotBeFound("subscriberChargingCharacteristics.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubscribersBySubscriberChargingCharacteristicsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where subscriberChargingCharacteristics greater than or equals to DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS
        defaultSubscriberShouldBeFound("subscriberChargingCharacteristics.greaterOrEqualThan=" + DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS);

        // Get all the subscriberList where subscriberChargingCharacteristics greater than or equals to UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS
        defaultSubscriberShouldNotBeFound("subscriberChargingCharacteristics.greaterOrEqualThan=" + UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS);
    }

    @Test
    @Transactional
    public void getAllSubscribersBySubscriberChargingCharacteristicsIsLessThanSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where subscriberChargingCharacteristics less than or equals to DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS
        defaultSubscriberShouldNotBeFound("subscriberChargingCharacteristics.lessThan=" + DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS);

        // Get all the subscriberList where subscriberChargingCharacteristics less than or equals to UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS
        defaultSubscriberShouldBeFound("subscriberChargingCharacteristics.lessThan=" + UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS);
    }


    @Test
    @Transactional
    public void getAllSubscribersByIccidIsEqualToSomething() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where iccid equals to DEFAULT_ICCID
        defaultSubscriberShouldBeFound("iccid.equals=" + DEFAULT_ICCID);

        // Get all the subscriberList where iccid equals to UPDATED_ICCID
        defaultSubscriberShouldNotBeFound("iccid.equals=" + UPDATED_ICCID);
    }

    @Test
    @Transactional
    public void getAllSubscribersByIccidIsInShouldWork() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where iccid in DEFAULT_ICCID or UPDATED_ICCID
        defaultSubscriberShouldBeFound("iccid.in=" + DEFAULT_ICCID + "," + UPDATED_ICCID);

        // Get all the subscriberList where iccid equals to UPDATED_ICCID
        defaultSubscriberShouldNotBeFound("iccid.in=" + UPDATED_ICCID);
    }

    @Test
    @Transactional
    public void getAllSubscribersByIccidIsNullOrNotNull() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList where iccid is not null
        defaultSubscriberShouldBeFound("iccid.specified=true");

        // Get all the subscriberList where iccid is null
        defaultSubscriberShouldNotBeFound("iccid.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSubscriberShouldBeFound(String filter) throws Exception {
        restSubscriberMockMvc.perform(get("/api/subscribers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriber.getId().intValue())))
            .andExpect(jsonPath("$.[*].imsi").value(hasItem(DEFAULT_IMSI.toString())))
            .andExpect(jsonPath("$.[*].msisdn").value(hasItem(DEFAULT_MSISDN.toString())))
            .andExpect(jsonPath("$.[*].nam").value(hasItem(DEFAULT_NAM)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].subscriberChargingCharacteristics").value(hasItem(DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS)))
            .andExpect(jsonPath("$.[*].iccid").value(hasItem(DEFAULT_ICCID.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSubscriberShouldNotBeFound(String filter) throws Exception {
        restSubscriberMockMvc.perform(get("/api/subscribers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSubscriber() throws Exception {
        // Get the subscriber
        restSubscriberMockMvc.perform(get("/api/subscribers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscriber() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();

        // Update the subscriber
        Subscriber updatedSubscriber = subscriberRepository.findById(subscriber.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriber are not directly saved in db
        em.detach(updatedSubscriber);
        updatedSubscriber
            .imsi(UPDATED_IMSI)
            .msisdn(UPDATED_MSISDN)
            .nam(UPDATED_NAM)
            .state(UPDATED_STATE)
            .category(UPDATED_CATEGORY)
            .subscriberChargingCharacteristics(UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS)
            .iccid(UPDATED_ICCID);
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(updatedSubscriber);

        restSubscriberMockMvc.perform(put("/api/subscribers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isOk());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getImsi()).isEqualTo(UPDATED_IMSI);
        assertThat(testSubscriber.getMsisdn()).isEqualTo(UPDATED_MSISDN);
        assertThat(testSubscriber.getNam()).isEqualTo(UPDATED_NAM);
        assertThat(testSubscriber.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSubscriber.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testSubscriber.getSubscriberChargingCharacteristics()).isEqualTo(UPDATED_SUBSCRIBER_CHARGING_CHARACTERISTICS);
        assertThat(testSubscriber.getIccid()).isEqualTo(UPDATED_ICCID);

        // Validate the Subscriber in Elasticsearch
        verify(mockSubscriberSearchRepository, times(1)).save(testSubscriber);
    }

    @Test
    @Transactional
    public void updateNonExistingSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriberMockMvc.perform(put("/api/subscribers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Subscriber in Elasticsearch
        verify(mockSubscriberSearchRepository, times(0)).save(subscriber);
    }

    @Test
    @Transactional
    public void deleteSubscriber() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        int databaseSizeBeforeDelete = subscriberRepository.findAll().size();

        // Get the subscriber
        restSubscriberMockMvc.perform(delete("/api/subscribers/{id}", subscriber.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Subscriber in Elasticsearch
        verify(mockSubscriberSearchRepository, times(1)).deleteById(subscriber.getId());
    }

    @Test
    @Transactional
    public void searchSubscriber() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);
        when(mockSubscriberSearchRepository.search(queryStringQuery("id:" + subscriber.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(subscriber), PageRequest.of(0, 1), 1));
        // Search the subscriber
        restSubscriberMockMvc.perform(get("/api/_search/subscribers?query=id:" + subscriber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriber.getId().intValue())))
            .andExpect(jsonPath("$.[*].imsi").value(hasItem(DEFAULT_IMSI.toString())))
            .andExpect(jsonPath("$.[*].msisdn").value(hasItem(DEFAULT_MSISDN.toString())))
            .andExpect(jsonPath("$.[*].nam").value(hasItem(DEFAULT_NAM)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].subscriberChargingCharacteristics").value(hasItem(DEFAULT_SUBSCRIBER_CHARGING_CHARACTERISTICS)))
            .andExpect(jsonPath("$.[*].iccid").value(hasItem(DEFAULT_ICCID.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subscriber.class);
        Subscriber subscriber1 = new Subscriber();
        subscriber1.setId(1L);
        Subscriber subscriber2 = new Subscriber();
        subscriber2.setId(subscriber1.getId());
        assertThat(subscriber1).isEqualTo(subscriber2);
        subscriber2.setId(2L);
        assertThat(subscriber1).isNotEqualTo(subscriber2);
        subscriber1.setId(null);
        assertThat(subscriber1).isNotEqualTo(subscriber2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriberDTO.class);
        SubscriberDTO subscriberDTO1 = new SubscriberDTO();
        subscriberDTO1.setId(1L);
        SubscriberDTO subscriberDTO2 = new SubscriberDTO();
        assertThat(subscriberDTO1).isNotEqualTo(subscriberDTO2);
        subscriberDTO2.setId(subscriberDTO1.getId());
        assertThat(subscriberDTO1).isEqualTo(subscriberDTO2);
        subscriberDTO2.setId(2L);
        assertThat(subscriberDTO1).isNotEqualTo(subscriberDTO2);
        subscriberDTO1.setId(null);
        assertThat(subscriberDTO1).isNotEqualTo(subscriberDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(subscriberMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(subscriberMapper.fromId(null)).isNull();
    }
}
