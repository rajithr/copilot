package com.example.creditrisk.repository;

import com.example.creditrisk.model.CreditRiskRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRiskRecordRepository extends JpaRepository<CreditRiskRecord, Long> {
}
