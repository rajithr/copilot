package com.example.creditrisk.service;

import com.example.creditrisk.dto.CreditRiskRecordDTO;
import com.example.creditrisk.model.CreditRiskRecord;
import com.example.creditrisk.repository.CreditRiskRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditRiskServiceTest {

    @Mock
    private CreditRiskRecordRepository repository;

    @InjectMocks
    private CreditRiskService service;

    private CreditRiskRecord testRecord1;
    private CreditRiskRecord testRecord2;

    @BeforeEach
    void setUp() {
        testRecord1 = new CreditRiskRecord(
                "John Doe",
                750,
                new BigDecimal("100000.00"),
                "A",
                LocalDateTime.now()
        );
        testRecord1.setId(1L);

        testRecord2 = new CreditRiskRecord(
                "Jane Smith",
                650,
                new BigDecimal("50000.00"),
                "B",
                LocalDateTime.now()
        );
        testRecord2.setId(2L);
    }

    @Test
    void testFindAllWithPagination() {
        // Arrange
        List<CreditRiskRecord> records = Arrays.asList(testRecord1, testRecord2);
        Page<CreditRiskRecord> recordPage = new PageImpl<>(records, PageRequest.of(0, 10), 2);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(recordPage);

        // Act
        Page<CreditRiskRecordDTO> result = service.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getContent().size());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void testFindAllSingleRecord() {
        // Arrange
        List<CreditRiskRecord> records = Arrays.asList(testRecord1);
        Page<CreditRiskRecord> recordPage = new PageImpl<>(records, PageRequest.of(0, 10), 1);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(recordPage);

        // Act
        Page<CreditRiskRecordDTO> result = service.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).getCustomerName());
    }

    @Test
    void testFindAllEmpty() {
        // Arrange
        Page<CreditRiskRecord> emptyPage = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        Page<CreditRiskRecordDTO> result = service.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void testFindAllConvertsToDTO() {
        // Arrange
        List<CreditRiskRecord> records = Arrays.asList(testRecord1);
        Page<CreditRiskRecord> recordPage = new PageImpl<>(records, PageRequest.of(0, 10), 1);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(recordPage);

        // Act
        Page<CreditRiskRecordDTO> result = service.findAll(pageable);

        // Assert
        CreditRiskRecordDTO dto = result.getContent().get(0);
        assertEquals(testRecord1.getId(), dto.getId());
        assertEquals(testRecord1.getCustomerName(), dto.getCustomerName());
        assertEquals(testRecord1.getScore(), dto.getScore());
        assertEquals(testRecord1.getExposure(), dto.getExposure());
        assertEquals(testRecord1.getRating(), dto.getRating());
        assertEquals(testRecord1.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    void testFindAllWithDifferentPageSizes() {
        // Arrange
        List<CreditRiskRecord> records = Arrays.asList(testRecord1, testRecord2);
        Page<CreditRiskRecord> recordPage = new PageImpl<>(records, PageRequest.of(0, 2), 2);
        Pageable pageable = PageRequest.of(0, 2);

        when(repository.findAll(pageable)).thenReturn(recordPage);

        // Act
        Page<CreditRiskRecordDTO> result = service.findAll(pageable);

        // Assert
        assertEquals(2, result.getPageable().getPageSize());
        assertEquals(2, result.getContent().size());
    }

    @Test
    void testFindAllPreservesRecordProperties() {
        // Arrange
        List<CreditRiskRecord> records = Arrays.asList(testRecord1, testRecord2);
        Page<CreditRiskRecord> recordPage = new PageImpl<>(records, PageRequest.of(0, 10), 2);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(recordPage);

        // Act
        Page<CreditRiskRecordDTO> result = service.findAll(pageable);

        // Assert
        CreditRiskRecordDTO dto1 = result.getContent().get(0);
        CreditRiskRecordDTO dto2 = result.getContent().get(1);

        assertEquals(750, dto1.getScore());
        assertEquals(650, dto2.getScore());
        assertEquals("A", dto1.getRating());
        assertEquals("B", dto2.getRating());
    }

    @Test
    void testFindAllMultiplePages() {
        // Arrange
        List<CreditRiskRecord> page1Records = Arrays.asList(testRecord1);
        Page<CreditRiskRecord> page1 = new PageImpl<>(page1Records, PageRequest.of(0, 1), 2);
        Pageable pageable = PageRequest.of(0, 1);

        when(repository.findAll(pageable)).thenReturn(page1);

        // Act
        Page<CreditRiskRecordDTO> result = service.findAll(pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindAllRepositoryInvoked() {
        // Arrange
        Page<CreditRiskRecord> emptyPage = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        service.findAll(pageable);

        // Assert
        verify(repository, times(1)).findAll(pageable);
    }
}
