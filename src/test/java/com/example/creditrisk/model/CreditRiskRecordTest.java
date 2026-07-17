package com.example.creditrisk.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CreditRiskRecordTest {

    private CreditRiskRecord record;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.now();
        record = new CreditRiskRecord(
                "Test Customer",
                700,
                new BigDecimal("75000.00"),
                "A",
                testDateTime
        );
    }

    @Test
    void testDefaultConstructor() {
        CreditRiskRecord newRecord = new CreditRiskRecord();
        assertNotNull(newRecord);
        assertNull(newRecord.getId());
        assertNull(newRecord.getCustomerName());
        assertNull(newRecord.getScore());
        assertNull(newRecord.getExposure());
        assertNull(newRecord.getRating());
        assertNull(newRecord.getUpdatedAt());
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals("Test Customer", record.getCustomerName());
        assertEquals(700, record.getScore());
        assertEquals(new BigDecimal("75000.00"), record.getExposure());
        assertEquals("A", record.getRating());
        assertEquals(testDateTime, record.getUpdatedAt());
    }

    @Test
    void testSetAndGetId() {
        record.setId(1L);
        assertEquals(1L, record.getId());

        record.setId(999L);
        assertEquals(999L, record.getId());
    }

    @Test
    void testSetAndGetCustomerName() {
        record.setCustomerName("John Doe");
        assertEquals("John Doe", record.getCustomerName());

        record.setCustomerName("Jane Smith");
        assertEquals("Jane Smith", record.getCustomerName());
    }

    @Test
    void testSetAndGetScore() {
        record.setScore(750);
        assertEquals(750, record.getScore());

        record.setScore(650);
        assertEquals(650, record.getScore());
    }

    @Test
    void testSetAndGetExposure() {
        BigDecimal exposure = new BigDecimal("100000.50");
        record.setExposure(exposure);
        assertEquals(exposure, record.getExposure());

        BigDecimal newExposure = new BigDecimal("50000.00");
        record.setExposure(newExposure);
        assertEquals(newExposure, record.getExposure());
    }

    @Test
    void testSetAndGetRating() {
        record.setRating("B");
        assertEquals("B", record.getRating());

        record.setRating("C");
        assertEquals("C", record.getRating());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1);
        record.setUpdatedAt(newDateTime);
        assertEquals(newDateTime, record.getUpdatedAt());
    }

    @Test
    void testMultipleFieldUpdates() {
        record.setId(10L);
        record.setCustomerName("Updated Name");
        record.setScore(800);
        record.setExposure(new BigDecimal("200000.00"));
        record.setRating("AA");

        assertEquals(10L, record.getId());
        assertEquals("Updated Name", record.getCustomerName());
        assertEquals(800, record.getScore());
        assertEquals(new BigDecimal("200000.00"), record.getExposure());
        assertEquals("AA", record.getRating());
    }

    @Test
    void testNullValues() {
        CreditRiskRecord nullRecord = new CreditRiskRecord(null, null, null, null, null);
        assertNull(nullRecord.getCustomerName());
        assertNull(nullRecord.getScore());
        assertNull(nullRecord.getExposure());
        assertNull(nullRecord.getRating());
        assertNull(nullRecord.getUpdatedAt());
    }

    @Test
    void testBigDecimalPrecision() {
        BigDecimal exposure = new BigDecimal("99999.99");
        record.setExposure(exposure);
        assertEquals(new BigDecimal("99999.99"), record.getExposure());
    }

    @Test
    void testScoreEdgeCases() {
        // Test minimum score
        record.setScore(0);
        assertEquals(0, record.getScore());

        // Test maximum score
        record.setScore(1000);
        assertEquals(1000, record.getScore());

        // Test negative score
        record.setScore(-100);
        assertEquals(-100, record.getScore());
    }

    @Test
    void testEmptyStringCustomerName() {
        record.setCustomerName("");
        assertEquals("", record.getCustomerName());
    }

    @Test
    void testLongCustomerNameValue() {
        String longName = "A".repeat(500);
        record.setCustomerName(longName);
        assertEquals(longName, record.getCustomerName());
    }

    @Test
    void testRecordImmutabilityAfterCreation() {
        CreditRiskRecord immutableRecord = new CreditRiskRecord(
                "Immutable Customer",
                700,
                new BigDecimal("75000.00"),
                "A",
                testDateTime
        );

        assertEquals("Immutable Customer", immutableRecord.getCustomerName());

        // Now modify it
        immutableRecord.setCustomerName("Modified");
        assertEquals("Modified", immutableRecord.getCustomerName());
    }

    @Test
    void testZeroExposure() {
        record.setExposure(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, record.getExposure());
    }

    @Test
    void testNegativeExposure() {
        record.setExposure(new BigDecimal("-5000.00"));
        assertEquals(new BigDecimal("-5000.00"), record.getExposure());
    }
}
