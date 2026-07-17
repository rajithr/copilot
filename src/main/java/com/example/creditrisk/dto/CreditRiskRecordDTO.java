package com.example.creditrisk.dto;

import com.example.creditrisk.model.CreditRiskRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreditRiskRecordDTO {
    private Long id;
    private String customerName;
    private Integer score;
    private BigDecimal exposure;
    private String rating;
    private LocalDateTime updatedAt;

    public CreditRiskRecordDTO() {}

    public CreditRiskRecordDTO(Long id, String customerName, Integer score, BigDecimal exposure, String rating, LocalDateTime updatedAt) {
        this.id = id;
        this.customerName = customerName;
        this.score = score;
        this.exposure = exposure;
        this.rating = rating;
        this.updatedAt = updatedAt;
    }

    public static CreditRiskRecordDTO fromEntity(CreditRiskRecord e) {
        return new CreditRiskRecordDTO(e.getId(), e.getCustomerName(), e.getScore(), e.getExposure(), e.getRating(), e.getUpdatedAt());
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public Integer getScore() { return score; }
    public BigDecimal getExposure() { return exposure; }
    public String getRating() { return rating; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setScore(Integer score) { this.score = score; }
    public void setExposure(BigDecimal exposure) { this.exposure = exposure; }
    public void setRating(String rating) { this.rating = rating; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
