package com.example.creditrisk.dto;

import com.example.creditrisk.model.CreditRiskRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CreditRiskRecordDTOTest {

    private CreditRiskRecordDTO dto;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.now();
        dto = new CreditRiskRecordDTO(
                1L,
                "Test Customer",
                700,
                new BigDecimal("75000.00"),
                "A",
                testDateTime
        );
    }

    @Test
    void testDefaultConstructor() {
        CreditRiskRecordDTO newDto = new CreditRiskRecordDTO();
        assertNotNull(newDto);
        assertNull(newDto.getId());
        assertNull(newDto.getCustomerName());
        assertNull(newDto.getScore());
        assertNull(newDto.getExposure());
        assertNull(newDto.getRating());
        assertNull(newDto.getUpdatedAt());
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals(1L, dto.getId());
        assertEquals("Test Customer", dto.getCustomerName());
        assertEquals(700, dto.getScore());
        assertEquals(new BigDecimal("75000.00"), dto.getExposure());
        assertEquals("A", dto.getRating());
        assertEquals(testDateTime, dto.getUpdatedAt());
    }

    @Test
    void testSetAndGetId() {
        dto.setId(5L);
        assertEquals(5L, dto.getId());

        dto.setId(999L);
        assertEquals(999L, dto.getId());
    }

    @Test
    void testSetAndGetCustomerName() {
        dto.setCustomerName("John Doe");
        assertEquals("John Doe", dto.getCustomerName());

        dto.setCustomerName("Jane Smith");
        assertEquals("Jane Smith", dto.getCustomerName());
    }

    @Test
    void testSetAndGetScore() {
        dto.setScore(800);
        assertEquals(800, dto.getScore());

        dto.setScore(600);
        assertEquals(600, dto.getScore());
    }

    @Test
    void testSetAndGetExposure() {
        BigDecimal exposure = new BigDecimal("150000.00");
        dto.setExposure(exposure);
        assertEquals(exposure, dto.getExposure());

        BigDecimal newExposure = new BigDecimal("50000.50");
        dto.setExposure(newExposure);
        assertEquals(newExposure, dto.getExposure());
    }

    @Test
    void testSetAndGetRating() {
        dto.setRating("B");
        assertEquals("B", dto.getRating());

        dto.setRating("C");
        assertEquals("C", dto.getRating());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime newDateTime = LocalDateTime.now().plusHours(1);
        dto.setUpdatedAt(newDateTime);
        assertEquals(newDateTime, dto.getUpdatedAt());
    }

    @Test
    void testFromEntityBasic() {
        CreditRiskRecord entity = new CreditRiskRecord(
                "Entity Customer",
                750,
                new BigDecimal("100000.00"),
                "A",
                testDateTime
        );
        entity.setId(1L);

        CreditRiskRecordDTO convertedDto = CreditRiskRecordDTO.fromEntity(entity);

        assertNotNull(convertedDto);
        assertEquals(1L, convertedDto.getId());
        assertEquals("Entity Customer", convertedDto.getCustomerName());
        assertEquals(750, convertedDto.getScore());
        assertEquals(new BigDecimal("100000.00"), convertedDto.getExposure());
        assertEquals("A", convertedDto.getRating());
        assertEquals(testDateTime, convertedDto.getUpdatedAt());
    }

    @Test
    void testFromEntityWithNullFields() {
        CreditRiskRecord entity = new CreditRiskRecord();
        entity.setId(1L);

        CreditRiskRecordDTO convertedDto = CreditRiskRecordDTO.fromEntity(entity);

        assertNotNull(convertedDto);
        assertEquals(1L, convertedDto.getId());
        assertNull(convertedDto.getCustomerName());
        assertNull(convertedDto.getScore());
        assertNull(convertedDto.getExposure());
        assertNull(convertedDto.getRating());
        assertNull(convertedDto.getUpdatedAt());
    }

    @Test
    void testFromEntityPreservesAllData() {
        CreditRiskRecord entity = new CreditRiskRecord(
                "Preserve Test",
                650,
                new BigDecimal("75000.75"),
                "B",
                testDateTime
        );
        entity.setId(99L);

        CreditRiskRecordDTO convertedDto = CreditRiskRecordDTO.fromEntity(entity);

        assertEquals(entity.getId(), convertedDto.getId());
        assertEquals(entity.getCustomerName(), convertedDto.getCustomerName());
        assertEquals(entity.getScore(), convertedDto.getScore());
        assertEquals(entity.getExposure(), convertedDto.getExposure());
        assertEquals(entity.getRating(), convertedDto.getRating());
        assertEquals(entity.getUpdatedAt(), convertedDto.getUpdatedAt());
    }

    @Test
    void testFromEntityMultipleConversions() {
        CreditRiskRecord entity1 = new CreditRiskRecord("Customer 1", 700, new BigDecimal("100000.00"), "A", testDateTime);
        entity1.setId(1L);

        CreditRiskRecord entity2 = new CreditRiskRecord("Customer 2", 650, new BigDecimal("50000.00"), "B", testDateTime);
        entity2.setId(2L);

        CreditRiskRecordDTO dto1 = CreditRiskRecordDTO.fromEntity(entity1);
        CreditRiskRecordDTO dto2 = CreditRiskRecordDTO.fromEntity(entity2);

        assertNotEquals(dto1.getId(), dto2.getId());
        assertNotEquals(dto1.getCustomerName(), dto2.getCustomerName());
        assertNotEquals(dto1.getScore(), dto2.getScore());
    }

    @Test
    void testMultipleFieldUpdates() {
        dto.setId(10L);
        dto.setCustomerName("Updated Customer");
        dto.setScore(800);
        dto.setExposure(new BigDecimal("200000.00"));
        dto.setRating("AA");

        assertEquals(10L, dto.getId());
        assertEquals("Updated Customer", dto.getCustomerName());
        assertEquals(800, dto.getScore());
        assertEquals(new BigDecimal("200000.00"), dto.getExposure());
        assertEquals("AA", dto.getRating());
    }

    @Test
    void testNullValues() {
        CreditRiskRecordDTO nullDto = new CreditRiskRecordDTO(null, null, null, null, null, null);
        assertNull(nullDto.getId());
        assertNull(nullDto.getCustomerName());
        assertNull(nullDto.getScore());
        assertNull(nullDto.getExposure());
        assertNull(nullDto.getRating());
        assertNull(nullDto.getUpdatedAt());
    }

    @Test
    void testBigDecimalPrecision() {
        BigDecimal precision = new BigDecimal("999999.99");
        dto.setExposure(precision);
        assertEquals(precision, dto.getExposure());
    }

    @Test
    void testEquityBetweenEntityAndDTO() {
        CreditRiskRecord entity = new CreditRiskRecord(
                "Same Customer",
                700,
                new BigDecimal("75000.00"),
                "A",
                testDateTime
        );
        entity.setId(1L);

        CreditRiskRecordDTO convertedDto = CreditRiskRecordDTO.fromEntity(entity);
        CreditRiskRecordDTO manualDto = new CreditRiskRecordDTO(
                1L,
                "Same Customer",
                700,
                new BigDecimal("75000.00"),
                "A",
                testDateTime
        );

        assertEquals(convertedDto.getId(), manualDto.getId());
        assertEquals(convertedDto.getCustomerName(), manualDto.getCustomerName());
        assertEquals(convertedDto.getScore(), manualDto.getScore());
        assertEquals(convertedDto.getExposure(), manualDto.getExposure());
        assertEquals(convertedDto.getRating(), manualDto.getRating());
        assertEquals(convertedDto.getUpdatedAt(), manualDto.getUpdatedAt());
    }

    @Test
    void testEmptyStringValues() {
        dto.setCustomerName("");
        dto.setRating("");
        assertEquals("", dto.getCustomerName());
        assertEquals("", dto.getRating());
    }

    @Test
    void testZeroScoreAndExposure() {
        dto.setScore(0);
        dto.setExposure(BigDecimal.ZERO);
        assertEquals(0, dto.getScore());
        assertEquals(BigDecimal.ZERO, dto.getExposure());
    }
}
