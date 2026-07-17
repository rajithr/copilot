package com.example.creditrisk.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_risk_record")
public class CreditRiskRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private Integer score;

    private BigDecimal exposure;

    private String rating;

    private LocalDateTime updatedAt;

    public CreditRiskRecord() {}

    public CreditRiskRecord(String customerName, Integer score, BigDecimal exposure, String rating, LocalDateTime updatedAt) {
        this.customerName = customerName;
        this.score = score;
        this.exposure = exposure;
        this.rating = rating;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public BigDecimal getExposure() { return exposure; }
    public void setExposure(BigDecimal exposure) { this.exposure = exposure; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
