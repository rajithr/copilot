package com.example.creditrisk.service;

import com.example.creditrisk.dto.CreditRiskRecordDTO;
import com.example.creditrisk.model.CreditRiskRecord;
import com.example.creditrisk.repository.CreditRiskRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class CreditRiskService {

    private final CreditRiskRecordRepository repo;

    @Autowired
    public CreditRiskService(CreditRiskRecordRepository repo) {
        this.repo = repo;
    }

    public Page<CreditRiskRecordDTO> findAll(Pageable pageable) {
        Page<CreditRiskRecord> page = repo.findAll(pageable);
        return page.map(CreditRiskRecordDTO::fromEntity);
    }
}
