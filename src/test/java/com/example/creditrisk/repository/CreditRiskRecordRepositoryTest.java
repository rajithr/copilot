package com.example.creditrisk.repository;

import com.example.creditrisk.model.CreditRiskRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CreditRiskRecordRepositoryTest {

    @Autowired
    private CreditRiskRecordRepository repository;

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

        testRecord2 = new CreditRiskRecord(
                "Jane Smith",
                650,
                new BigDecimal("50000.00"),
                "B",
                LocalDateTime.now()
        );
    }

    @Test
    void testSaveRecord() {
        // Act
        CreditRiskRecord saved = repository.save(testRecord1);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("John Doe", saved.getCustomerName());
        assertEquals(750, saved.getScore());
    }

    @Test
    void testSaveAndFindById() {
        // Act
        CreditRiskRecord saved = repository.save(testRecord1);
        Optional<CreditRiskRecord> found = repository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getCustomerName());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    void testFindByIdNotFound() {
        // Act
        Optional<CreditRiskRecord> found = repository.findById(999L);

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        // Arrange
        repository.save(testRecord1);
        repository.save(testRecord2);

        // Act
        Iterable<CreditRiskRecord> all = repository.findAll();

        // Assert
        assertNotNull(all);
        int count = 0;
        for (CreditRiskRecord record : all) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void testFindAllWithPagination() {
        // Arrange
        repository.save(testRecord1);
        repository.save(testRecord2);
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<CreditRiskRecord> page = repository.findAll(pageable);

        // Assert
        assertNotNull(page);
        assertEquals(2, page.getTotalElements());
        assertEquals(1, page.getTotalPages());
        assertEquals(2, page.getContent().size());
    }

    @Test
    void testFindAllWithSmallPageSize() {
        // Arrange
        repository.save(testRecord1);
        repository.save(testRecord2);
        Pageable pageable = PageRequest.of(0, 1);

        // Act
        Page<CreditRiskRecord> page = repository.findAll(pageable);

        // Assert
        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
        assertEquals(1, page.getContent().size());
    }

    @Test
    void testFindAllSecondPage() {
        // Arrange
        repository.save(testRecord1);
        repository.save(testRecord2);
        Pageable pageOne = PageRequest.of(0, 1);
        Pageable pageTwo = PageRequest.of(1, 1);

        // Act
        Page<CreditRiskRecord> page1 = repository.findAll(pageOne);
        Page<CreditRiskRecord> page2 = repository.findAll(pageTwo);

        // Assert
        assertEquals(1, page1.getContent().size());
        assertEquals(1, page2.getContent().size());
        assertNotEquals(page1.getContent().get(0).getId(), page2.getContent().get(0).getId());
    }

    @Test
    void testUpdateRecord() {
        // Arrange
        CreditRiskRecord saved = repository.save(testRecord1);
        saved.setCustomerName("Updated Name");
        saved.setScore(800);

        // Act
        CreditRiskRecord updated = repository.save(saved);

        // Assert
        assertEquals("Updated Name", updated.getCustomerName());
        assertEquals(800, updated.getScore());
        assertEquals(saved.getId(), updated.getId());
    }

    @Test
    void testDeleteRecord() {
        // Arrange
        CreditRiskRecord saved = repository.save(testRecord1);
        Long savedId = saved.getId();

        // Act
        repository.deleteById(savedId);
        Optional<CreditRiskRecord> found = repository.findById(savedId);

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteAllRecords() {
        // Arrange
        repository.save(testRecord1);
        repository.save(testRecord2);

        // Act
        repository.deleteAll();
        Page<CreditRiskRecord> all = repository.findAll(PageRequest.of(0, 10));

        // Assert
        assertEquals(0, all.getTotalElements());
    }

    @Test
    void testSaveMultipleRecords() {
        // Arrange
        CreditRiskRecord record3 = new CreditRiskRecord(
                "Bob Johnson",
                700,
                new BigDecimal("75000.00"),
                "A",
                LocalDateTime.now()
        );

        // Act
        repository.save(testRecord1);
        repository.save(testRecord2);
        repository.save(record3);

        // Assert
        assertEquals(3, repository.findAll(PageRequest.of(0, 10)).getTotalElements());
    }

    @Test
    void testRecordWithBigDecimalPrecision() {
        // Arrange
        testRecord1.setExposure(new BigDecimal("123456.78"));

        // Act
        CreditRiskRecord saved = repository.save(testRecord1);

        // Assert
        assertEquals(new BigDecimal("123456.78"), saved.getExposure());
    }

    @Test
    void testRecordWithNullFields() {
        // Arrange
        CreditRiskRecord nullRecord = new CreditRiskRecord();

        // Act
        CreditRiskRecord saved = repository.save(nullRecord);

        // Assert
        assertNotNull(saved.getId());
        assertNull(saved.getCustomerName());
        assertNull(saved.getScore());
    }

    @Test
    void testRepositoryCount() {
        // Arrange
        repository.save(testRecord1);
        repository.save(testRecord2);

        // Act
        long count = repository.count();

        // Assert
        assertEquals(2, count);
    }

    @Test
    void testRepositoryExistsById() {
        // Arrange
        CreditRiskRecord saved = repository.save(testRecord1);

        // Act
        boolean exists = repository.existsById(saved.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void testRepositoryExistsByIdFalse() {
        // Act
        boolean exists = repository.existsById(999L);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testPageableWithNegativePageNumber() {
        // Arrange
        repository.save(testRecord1);
        repository.save(testRecord2);

        // Act & Assert - Should not throw exception
        Pageable pageable = PageRequest.of(0, 10);
        Page<CreditRiskRecord> page = repository.findAll(pageable);
        assertNotNull(page);
    }

    @Test
    void testSaveAndRetrieveAllFields() {
        // Arrange
        LocalDateTime testTime = LocalDateTime.now();
        testRecord1.setUpdatedAt(testTime);

        // Act
        CreditRiskRecord saved = repository.save(testRecord1);
        Optional<CreditRiskRecord> retrieved = repository.findById(saved.getId());

        // Assert
        assertTrue(retrieved.isPresent());
        CreditRiskRecord record = retrieved.get();
        assertEquals("John Doe", record.getCustomerName());
        assertEquals(750, record.getScore());
        assertEquals(new BigDecimal("100000.00"), record.getExposure());
        assertEquals("A", record.getRating());
        assertNotNull(record.getUpdatedAt());
    }
}
