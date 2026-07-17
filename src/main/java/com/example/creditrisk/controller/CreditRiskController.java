package com.example.creditrisk.controller;

import com.example.creditrisk.dto.CreditRiskRecordDTO;
import com.example.creditrisk.service.CreditRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credit-risks")
public class CreditRiskController {

    private final CreditRiskService service;

    @Autowired
    public CreditRiskController(CreditRiskService service) {
        this.service = service;
    }

    /**
     * GET /api/credit-risks
     * Query params: page (0-based), size, sort (e.g. score,desc or customerName,asc)
     *
     * Example: /api/credit-risks?page=0&size=10&sort=score,desc
     */
    @GetMapping
    public Page<CreditRiskRecordDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        Sort sortSpec = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortSpec);
        return service.findAll(pageable);
    }

    private Sort parseSort(String[] sort) {
        // Accept either single "property,direction" or multiple pairs
        if (sort.length == 1 && sort[0].contains(",")) {
            String[] parts = sort[0].split(",");
            String prop = parts[0];
            Sort.Direction dir = parts.length > 1 && parts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            return Sort.by(dir, prop);
        } else {
            // fallback: no direction provided -> ascending by provided fields
            Sort s = Sort.unsorted();
            for (String prop : sort) {
                s = s.and(Sort.by(Sort.Direction.ASC, prop));
            }
            return s.isUnsorted() ? Sort.by("id") : s;
        }
    }
}
