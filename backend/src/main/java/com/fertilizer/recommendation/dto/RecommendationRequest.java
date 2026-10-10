package com.fertilizer.recommendation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RecommendationRequest {

    private Long userId;

    @NotBlank(message = "Crop name is required")
    private String cropName;

    @NotBlank(message = "Soil type is required")
    private String soilType;

    @NotNull(message = "Nitrogen level is required")
    @Min(value = 0, message = "Nitrogen must be non-negative")
    private Double nitrogen;

    @NotNull(message = "Phosphorus level is required")
    @Min(value = 0, message = "Phosphorus must be non-negative")
    private Double phosphorus;

    @NotNull(message = "Potassium level is required")
    @Min(value = 0, message = "Potassium must be non-negative")
    private Double potassium;

    @NotNull(message = "Soil pH is required")
    @Min(value = 0, message = "pH must be between 0 and 14")
    @Max(value = 14, message = "pH must be between 0 and 14")
    private Double ph;

    private Double moisture;
    private Double temperature;
    private Double humidity;
    private String season;

    public RecommendationRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Double getNitrogen() {
        return nitrogen;
    }

    public void setNitrogen(Double nitrogen) {
        this.nitrogen = nitrogen;
    }

    public Double getPhosphorus() {
        return phosphorus;
    }

    public void setPhosphorus(Double phosphorus) {
        this.phosphorus = phosphorus;
    }

    public Double getPotassium() {
        return potassium;
    }

    public void setPotassium(Double potassium) {
        this.potassium = potassium;
    }

    public Double getPh() {
        return ph;
    }

    public void setPh(Double ph) {
        this.ph = ph;
    }

    public Double getMoisture() {
        return moisture;
    }

    public void setMoisture(Double moisture) {
        this.moisture = moisture;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
