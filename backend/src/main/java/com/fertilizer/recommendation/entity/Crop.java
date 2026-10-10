package com.fertilizer.recommendation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "crops")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String cropName;

    @Column(length = 50)
    private String category;

    @Column(nullable = false)
    private Double targetN;

    @Column(nullable = false)
    private Double targetP;

    @Column(nullable = false)
    private Double targetK;

    @Column(nullable = false)
    private Double minPh;

    @Column(nullable = false)
    private Double maxPh;

    private Double idealMoisture;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Crop() {
    }

    public Crop(String cropName, String category, Double targetN, Double targetP, Double targetK,
                Double minPh, Double maxPh, Double idealMoisture, String description) {
        this.cropName = cropName;
        this.category = category;
        this.targetN = targetN;
        this.targetP = targetP;
        this.targetK = targetK;
        this.minPh = minPh;
        this.maxPh = maxPh;
        this.idealMoisture = idealMoisture;
        this.description = description;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getTargetN() {
        return targetN;
    }

    public void setTargetN(Double targetN) {
        this.targetN = targetN;
    }

    public Double getTargetP() {
        return targetP;
    }

    public void setTargetP(Double targetP) {
        this.targetP = targetP;
    }

    public Double getTargetK() {
        return targetK;
    }

    public void setTargetK(Double targetK) {
        this.targetK = targetK;
    }

    public Double getMinPh() {
        return minPh;
    }

    public void setMinPh(Double minPh) {
        this.minPh = minPh;
    }

    public Double getMaxPh() {
        return maxPh;
    }

    public void setMaxPh(Double maxPh) {
        this.maxPh = maxPh;
    }

    public Double getIdealMoisture() {
        return idealMoisture;
    }

    public void setIdealMoisture(Double idealMoisture) {
        this.idealMoisture = idealMoisture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
