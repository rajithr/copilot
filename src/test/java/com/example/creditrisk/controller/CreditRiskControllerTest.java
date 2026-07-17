package com.example.creditrisk.controller;

import com.example.creditrisk.dto.CreditRiskRecordDTO;
import com.example.creditrisk.service.CreditRiskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreditRiskController.class)
class CreditRiskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditRiskService service;

    private CreditRiskRecordDTO testDTO1;
    private CreditRiskRecordDTO testDTO2;

    @BeforeEach
    void setUp() {
        testDTO1 = new CreditRiskRecordDTO(
                1L,
                "John Doe",
                750,
                new BigDecimal("100000.00"),
                "A",
                LocalDateTime.now()
        );

        testDTO2 = new CreditRiskRecordDTO(
                2L,
                "Jane Smith",
                650,
                new BigDecimal("50000.00"),
                "B",
                LocalDateTime.now()
        );
    }

    @Test
    void testListWithDefaultParameters() throws Exception {
        List<CreditRiskRecordDTO> content = Arrays.asList(testDTO1, testDTO2);
        Page<CreditRiskRecordDTO> page = new PageImpl<>(content, PageRequest.of(0, 10), 2);

        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/credit-risks")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].customerName", is("John Doe")))
                .andExpect(jsonPath("$.content[0].score", is(750)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].customerName", is("Jane Smith")))
                .andExpect(jsonPath("$.pageable.pageNumber", is(0)))
                .andExpect(jsonPath("$.pageable.pageSize", is(10)))
                .andExpect(jsonPath("$.totalElements", is(2)));

        verify(service, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testListWithCustomPageSize() throws Exception {
        List<CreditRiskRecordDTO> content = Arrays.asList(testDTO1);
        Page<CreditRiskRecordDTO> page = new PageImpl<>(content, PageRequest.of(0, 5), 1);

        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/credit-risks")
                .param("page", "0")
                .param("size", "5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.pageable.pageSize", is(5)));

        verify(service, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testListWithSortByScoreDescending() throws Exception {
        List<CreditRiskRecordDTO> content = Arrays.asList(testDTO1, testDTO2);
        Page<CreditRiskRecordDTO> page = new PageImpl<>(content, PageRequest.of(0, 10), 2);

        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/credit-risks")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "score,desc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].score", is(750)));

        verify(service, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testListWithSortByCustomerNameAscending() throws Exception {
        List<CreditRiskRecordDTO> content = Arrays.asList(testDTO2, testDTO1);
        Page<CreditRiskRecordDTO> page = new PageImpl<>(content, PageRequest.of(0, 10), 2);

        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/credit-risks")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "customerName,asc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        verify(service, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testListWithPagination() throws Exception {
        List<CreditRiskRecordDTO> content = Arrays.asList(testDTO1);
        Page<CreditRiskRecordDTO> page = new PageImpl<>(content, PageRequest.of(1, 1), 2);

        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/credit-risks")
                .param("page", "1")
                .param("size", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.pageable.pageNumber", is(1)));

        verify(service, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testListEmptyResult() throws Exception {
        Page<CreditRiskRecordDTO> emptyPage = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);

        when(service.findAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/credit-risks")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));

        verify(service, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testListWithAllFields() throws Exception {
        List<CreditRiskRecordDTO> content = Arrays.asList(testDTO1);
        Page<CreditRiskRecordDTO> page = new PageImpl<>(content, PageRequest.of(0, 10), 1);

        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/credit-risks")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", notNullValue()))
                .andExpect(jsonPath("$.content[0].customerName", notNullValue()))
                .andExpect(jsonPath("$.content[0].score", notNullValue()))
                .andExpect(jsonPath("$.content[0].exposure", notNullValue()))
                .andExpect(jsonPath("$.content[0].rating", notNullValue()))
                .andExpect(jsonPath("$.content[0].updatedAt", notNullValue()));
    }

    @Test
    void testListResponseContentType() throws Exception {
        Page<CreditRiskRecordDTO> page = new PageImpl<>(Arrays.asList(testDTO1), PageRequest.of(0, 10), 1);

        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/credit-risks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
