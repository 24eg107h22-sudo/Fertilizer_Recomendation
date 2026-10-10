package com.fertilizer.recommendation.dto;

public class NutrientAnalysisDto {
    private String nutrient;
    private Double soilLevel;
    private Double targetLevel;
    private Double deficit;
    private String status; // Low, Optimal, High
    private String role;

    public NutrientAnalysisDto() {
    }

    public NutrientAnalysisDto(String nutrient, Double soilLevel, Double targetLevel, Double deficit, String status, String role) {
        this.nutrient = nutrient;
        this.soilLevel = soilLevel;
        this.targetLevel = targetLevel;
        this.deficit = deficit;
        this.status = status;
        this.role = role;
    }

    public String getNutrient() {
        return nutrient;
    }

    public void setNutrient(String nutrient) {
        this.nutrient = nutrient;
    }

    public Double getSoilLevel() {
        return soilLevel;
    }

    public void setSoilLevel(Double soilLevel) {
        this.soilLevel = soilLevel;
    }

    public Double getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(Double targetLevel) {
        this.targetLevel = targetLevel;
    }

    public Double getDeficit() {
        return deficit;
    }

    public void setDeficit(Double deficit) {
        this.deficit = deficit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
