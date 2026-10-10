package com.fertilizer.recommendation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    @Column(nullable = false, length = 100)
    private String cropName;

    @Column(length = 50)
    private String soilType;

    @Column(nullable = false, length = 150)
    private String fertilizerName;

    @Column(nullable = false, length = 100)
    private String quantity;

    @Column(length = 255)
    private String applicationMethod;

    @Column(columnDefinition = "TEXT")
    private String usageInstructions;

    @Column(length = 50)
    private String nitrogenStatus;

    @Column(length = 50)
    private String phosphorusStatus;

    @Column(length = 50)
    private String potassiumStatus;

    @Column(length = 50)
    private String phStatus;

    @Column(columnDefinition = "TEXT")
    private String organicAdvice;

    private LocalDateTime createdDate = LocalDateTime.now();

    public Recommendation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public String getFertilizerName() {
        return fertilizerName;
    }

    public void setFertilizerName(String fertilizerName) {
        this.fertilizerName = fertilizerName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getApplicationMethod() {
        return applicationMethod;
    }

    public void setApplicationMethod(String applicationMethod) {
        this.applicationMethod = applicationMethod;
    }

    public String getUsageInstructions() {
        return usageInstructions;
    }

    public void setUsageInstructions(String usageInstructions) {
        this.usageInstructions = usageInstructions;
    }

    public String getNitrogenStatus() {
        return nitrogenStatus;
    }

    public void setNitrogenStatus(String nitrogenStatus) {
        this.nitrogenStatus = nitrogenStatus;
    }

    public String getPhosphorusStatus() {
        return phosphorusStatus;
    }

    public void setPhosphorusStatus(String phosphorusStatus) {
        this.phosphorusStatus = phosphorusStatus;
    }

    public String getPotassiumStatus() {
        return potassiumStatus;
    }

    public void setPotassiumStatus(String potassiumStatus) {
        this.potassiumStatus = potassiumStatus;
    }

    public String getPhStatus() {
        return phStatus;
    }

    public void setPhStatus(String phStatus) {
        this.phStatus = phStatus;
    }

    public String getOrganicAdvice() {
        return organicAdvice;
    }

    public void setOrganicAdvice(String organicAdvice) {
        this.organicAdvice = organicAdvice;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
