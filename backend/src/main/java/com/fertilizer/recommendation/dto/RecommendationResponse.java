package com.fertilizer.recommendation.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RecommendationResponse {

    private Long id;
    private String cropName;
    private String soilType;
    private String fertilizerName;
    private String quantity;
    private String applicationMethod;
    private String usageInstructions;
    private String organicAdvice;
    private String nitrogenStatus;
    private String phosphorusStatus;
    private String potassiumStatus;
    private String phStatus;
    private Integer soilHealthScore;
    private LocalDateTime createdDate;
    private List<NutrientAnalysisDto> nutrientBreakdown;

    public RecommendationResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOrganicAdvice() {
        return organicAdvice;
    }

    public void setOrganicAdvice(String organicAdvice) {
        this.organicAdvice = organicAdvice;
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

    public Integer getSoilHealthScore() {
        return soilHealthScore;
    }

    public void setSoilHealthScore(Integer soilHealthScore) {
        this.soilHealthScore = soilHealthScore;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<NutrientAnalysisDto> getNutrientBreakdown() {
        return nutrientBreakdown;
    }

    public void setNutrientBreakdown(List<NutrientAnalysisDto> nutrientBreakdown) {
        this.nutrientBreakdown = nutrientBreakdown;
    }
}
