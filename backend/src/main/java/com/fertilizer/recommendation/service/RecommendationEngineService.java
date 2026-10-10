package com.fertilizer.recommendation.service;

import com.fertilizer.recommendation.dto.NutrientAnalysisDto;
import com.fertilizer.recommendation.dto.RecommendationRequest;
import com.fertilizer.recommendation.dto.RecommendationResponse;
import com.fertilizer.recommendation.entity.Crop;
import com.fertilizer.recommendation.entity.Recommendation;
import com.fertilizer.recommendation.entity.SoilData;
import com.fertilizer.recommendation.entity.User;
import com.fertilizer.recommendation.repository.CropRepository;
import com.fertilizer.recommendation.repository.RecommendationRepository;
import com.fertilizer.recommendation.repository.SoilDataRepository;
import com.fertilizer.recommendation.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationEngineService {

    private final CropRepository cropRepository;
    private final UserRepository userRepository;
    private final SoilDataRepository soilDataRepository;
    private final RecommendationRepository recommendationRepository;

    public RecommendationEngineService(CropRepository cropRepository,
                                       UserRepository userRepository,
                                       SoilDataRepository soilDataRepository,
                                       RecommendationRepository recommendationRepository) {
        this.cropRepository = cropRepository;
        this.userRepository = userRepository;
        this.soilDataRepository = soilDataRepository;
        this.recommendationRepository = recommendationRepository;
    }

    @Transactional
    public RecommendationResponse generateRecommendation(RecommendationRequest request) {
        // 1. Fetch Crop reference targets
        Crop crop = cropRepository.findByCropNameIgnoreCase(request.getCropName().trim())
                .orElse(new Crop(request.getCropName(), "General", 90.0, 50.0, 50.0, 6.0, 7.5, 60.0, "Standard general crop guidelines."));

        Double targetN = crop.getTargetN();
        Double targetP = crop.getTargetP();
        Double targetK = crop.getTargetK();

        Double soilN = request.getNitrogen();
        Double soilP = request.getPhosphorus();
        Double soilK = request.getPotassium();
        Double ph = request.getPh();

        // 2. Classify Nutrient Status
        String nStatus = classifyNutrient(soilN, targetN);
        String pStatus = classifyNutrient(soilP, targetP);
        String kStatus = classifyNutrient(soilK, targetK);
        String phStatus = classifyPh(ph);

        // 3. Build Nutrient Breakdown DTOs
        List<NutrientAnalysisDto> breakdown = new ArrayList<>();
        breakdown.add(new NutrientAnalysisDto("Nitrogen (N)", soilN, targetN, Math.max(0.0, targetN - soilN), nStatus, "Vegetative growth, chlorophyll synthesis, and protein formation"));
        breakdown.add(new NutrientAnalysisDto("Phosphorus (P)", soilP, targetP, Math.max(0.0, targetP - soilP), pStatus, "Root development, early seedling vigor, and flower initiation"));
        breakdown.add(new NutrientAnalysisDto("Potassium (K)", soilK, targetK, Math.max(0.0, targetK - soilK), kStatus, "Disease resistance, water regulation, and fruit/grain filling"));

        // 4. Recommendation Logic / Rules Engine
        boolean nLow = "Low / Deficient".equals(nStatus);
        boolean pLow = "Low / Deficient".equals(pStatus);
        boolean kLow = "Low / Deficient".equals(kStatus);

        String fertilizerName;
        String quantity;
        String applicationMethod;
        StringBuilder usageInstructions = new StringBuilder();
        StringBuilder organicAdvice = new StringBuilder();

        if (nLow && pLow && kLow) {
            fertilizerName = "NPK Complex (10:26:26 or 12:32:16) + Neem-Coated Urea";
            quantity = "65 kg/acre (160 kg/ha) NPK + 25 kg/acre Urea (Split)";
            applicationMethod = "Basal application at final land preparation + Top dressing at 30 days";
            usageInstructions.append("1. Apply full dose of NPK Complex 10:26:26 at the time of sowing or transplanting into the moist root zone.\n")
                    .append("2. Top-dress with 25 kg/acre Neem-Coated Urea 25-30 days after sowing during active vegetative growth.\n")
                    .append("3. Ensure adequate field moisture during fertilizer application.");
        } else if (nLow && pLow && !kLow) {
            fertilizerName = "DAP (Diammonium Phosphate 18-46-0) + Urea";
            quantity = "50 kg/acre (125 kg/ha) DAP + 30 kg/acre Urea";
            applicationMethod = "Basal placement for DAP + Split top-dressing for Urea";
            usageInstructions.append("1. Band-place DAP 4-5 cm below and to the side of seeds at sowing time.\n")
                    .append("2. Apply 30 kg/acre Urea in two equal splits: at tillering/branching and before flowering.");
        } else if (nLow && !pLow && kLow) {
            fertilizerName = "Neem-Coated Urea (46% N) + MOP (Muriate of Potash 60% K₂O)";
            quantity = "55 kg/acre Urea + 35 kg/acre MOP";
            applicationMethod = "Split application: 50% basal, 50% top-dressing";
            usageInstructions.append("1. Broadcast entire MOP (35 kg/acre) and half Urea (25 kg/acre) during sowing.\n")
                    .append("2. Apply remaining Urea at first irrigation/inter-cultivation.");
        } else if (!nLow && pLow && kLow) {
            fertilizerName = "Single Super Phosphate (SSP 16% P₂O₅) + MOP (60% K₂O)";
            quantity = "75 kg/acre SSP + 35 kg/acre MOP";
            applicationMethod = "100% Basal incorporation";
            usageInstructions.append("1. Incorporate both SSP and MOP thoroughly into the topsoil prior to sowing.\n")
                    .append("2. SSP also provides 11% essential Sulfur which promotes oil and protein synthesis.");
        } else if (nLow && !pLow && !kLow) {
            fertilizerName = "Neem-Coated Urea (46% Nitrogen)";
            quantity = "50 kg/acre (125 kg/ha)";
            applicationMethod = "2 to 3 Split top-dressings";
            usageInstructions.append("1. Apply 30 kg/acre at early vegetative stage (20-25 days after sowing).\n")
                    .append("2. Apply remaining 20 kg/acre at flower bud formation or panicle initiation.");
        } else if (!nLow && pLow && !kLow) {
            fertilizerName = "Single Super Phosphate (SSP 16% P) or DAP (18-46-0)";
            quantity = "70 kg/acre SSP (or 40 kg/acre DAP)";
            applicationMethod = "Deep basal placement near root zone";
            usageInstructions.append("1. Place fertilizer 5 cm below seed level to maximize root interception.\n")
                    .append("2. Avoid surface broadcasting as phosphorus is relatively immobile in soil.");
        } else if (!nLow && !pLow && kLow) {
            fertilizerName = "MOP (Muriate of Potash 0-0-60) / SOP for sensitive crops";
            quantity = "35 - 45 kg/acre (90 - 110 kg/ha)";
            applicationMethod = "Basal application or early top-dressing";
            usageInstructions.append("1. Apply during final land preparation.\n")
                    .append("2. Potash improves drought tolerance, strengthens stems, and prevents lodging.");
        } else {
            // All optimal or high
            fertilizerName = "Bio-NPK Consortium & Well-Rotted Vermicompost";
            quantity = "2 - 3 tons/acre Vermicompost + 2 kg/acre Bio-fertilizers";
            applicationMethod = "Soil incorporation before final harrowing";
            usageInstructions.append("1. Your soil has excellent base N-P-K reserves! Minimal chemical fertilizer is required.\n")
                    .append("2. Apply organic vermicompost enriched with Trichoderma and Bio-NPK to maintain soil microbial ecology.");
        }

        // Soil texture adaptation
        if (request.getSoilType() != null) {
            String soilTypeLower = request.getSoilType().toLowerCase();
            if (soilTypeLower.contains("sand")) {
                usageInstructions.append("\n• Sandy Soil Advisory: Sandy soils have high leaching rates. Split fertilizer dosage into 3-4 smaller applications rather than single large doses.");
            } else if (soilTypeLower.contains("clay") || soilTypeLower.contains("black")) {
                usageInstructions.append("\n• Clay/Black Soil Advisory: Clay soil has high cation exchange capacity. Avoid over-watering immediately after fertilizer application to prevent waterlogging.");
            }
        }

        // Environmental & Weather Advisories
        if (request.getMoisture() != null && request.getMoisture() < 35.0) {
            usageInstructions.append("\n• Low Soil Moisture Warning: Soil moisture is below 35%. Irrigate lightly prior to fertilizer broadcasting to prevent osmotic root scorching.");
        }
        if (request.getTemperature() != null && request.getTemperature() > 34.0) {
            usageInstructions.append("\n• High Temperature Alert: Temperature exceeds 34°C. Broadcast Urea strictly during early morning or late evening hours to eliminate ammonia volatilization loss.");
        }

        // pH Advisory
        if (ph < 5.5) {
            organicAdvice.append("Acidic Soil Detected (pH ").append(String.format("%.1f", ph)).append("): Apply Agricultural Lime (CaCO₃) or Dolomite at 250-350 kg/acre 2-3 weeks before sowing to neutralize acidity and liberate fixed phosphorus. Avoid Ammonium Sulfate.");
        } else if (ph > 8.0) {
            organicAdvice.append("Alkaline Soil Detected (pH ").append(String.format("%.1f", ph)).append("): Apply Agricultural Gypsum (CaSO₄·2H₂O) at 200 kg/acre and incorporate green manure (Dhaincha/Sesbania) to buffer sodium and reduce pH.");
        } else {
            organicAdvice.append("Soil pH is in the optimal range (").append(String.format("%.1f", ph)).append("). Nutrient bioavailability is maximal. Incorporate 5 tons/acre Farm Yard Manure (FYM) to enhance soil humus.");
        }

        // Calculate Soil Health Score (0-100)
        int healthScore = calculateHealthScore(soilN, targetN, soilP, targetP, soilK, targetK, ph);

        // 5. Link User
        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId()).orElse(null);
        }
        if (user == null) {
            // Check default user or first available
            user = userRepository.findAll().stream().findFirst().orElse(null);
        }

        // 6. Persist Soil Data
        SoilData soilData = new SoilData();
        soilData.setUser(user);
        soilData.setCropName(crop.getCropName());
        soilData.setSoilType(request.getSoilType());
        soilData.setNitrogen(soilN);
        soilData.setPhosphorus(soilP);
        soilData.setPotassium(soilK);
        soilData.setPh(ph);
        soilData.setMoisture(request.getMoisture());
        soilData.setTemperature(request.getTemperature());
        soilData.setHumidity(request.getHumidity());
        soilData.setSeason(request.getSeason());
        soilData.setTestedAt(LocalDateTime.now());
        soilDataRepository.save(soilData);

        // 7. Persist Recommendation
        Recommendation rec = new Recommendation();
        rec.setUser(user);
        rec.setCropName(crop.getCropName());
        rec.setSoilType(request.getSoilType());
        rec.setFertilizerName(fertilizerName);
        rec.setQuantity(quantity);
        rec.setApplicationMethod(applicationMethod);
        rec.setUsageInstructions(usageInstructions.toString());
        rec.setOrganicAdvice(organicAdvice.toString());
        rec.setNitrogenStatus(nStatus);
        rec.setPhosphorusStatus(pStatus);
        rec.setPotassiumStatus(kStatus);
        rec.setPhStatus(phStatus);
        rec.setCreatedDate(LocalDateTime.now());
        Recommendation savedRec = recommendationRepository.save(rec);

        // 8. Build Response
        RecommendationResponse response = new RecommendationResponse();
        response.setId(savedRec.getId());
        response.setCropName(savedRec.getCropName());
        response.setSoilType(savedRec.getSoilType());
        response.setFertilizerName(savedRec.getFertilizerName());
        response.setQuantity(savedRec.getQuantity());
        response.setApplicationMethod(savedRec.getApplicationMethod());
        response.setUsageInstructions(savedRec.getUsageInstructions());
        response.setOrganicAdvice(savedRec.getOrganicAdvice());
        response.setNitrogenStatus(nStatus);
        response.setPhosphorusStatus(pStatus);
        response.setPotassiumStatus(kStatus);
        response.setPhStatus(phStatus);
        response.setSoilHealthScore(healthScore);
        response.setCreatedDate(savedRec.getCreatedDate());
        response.setNutrientBreakdown(breakdown);

        return response;
    }

    private String classifyNutrient(Double soil, Double target) {
        if (target <= 0) return "Optimal / Balanced";
        double ratio = soil / target;
        if (ratio < 0.75) {
            return "Low / Deficient";
        } else if (ratio <= 1.25) {
            return "Optimal / Balanced";
        } else {
            return "High / Excess";
        }
    }

    private String classifyPh(Double ph) {
        if (ph < 5.5) {
            return "Strongly Acidic (< 5.5)";
        } else if (ph < 6.5) {
            return "Slightly Acidic (5.5 - 6.5)";
        } else if (ph <= 7.5) {
            return "Optimal / Neutral (6.5 - 7.5)";
        } else if (ph <= 8.5) {
            return "Moderately Alkaline (7.5 - 8.5)";
        } else {
            return "Strongly Alkaline (> 8.5)";
        }
    }

    private int calculateHealthScore(Double n, Double tn, Double p, Double tp, Double k, Double tk, Double ph) {
        double score = 100.0;
        // N penalty
        double nRatio = n / Math.max(1.0, tn);
        if (nRatio < 0.75 || nRatio > 1.3) score -= 15.0;

        // P penalty
        double pRatio = p / Math.max(1.0, tp);
        if (pRatio < 0.75 || pRatio > 1.3) score -= 15.0;

        // K penalty
        double kRatio = k / Math.max(1.0, tk);
        if (kRatio < 0.75 || kRatio > 1.3) score -= 15.0;

        // pH penalty
        if (ph < 6.0 || ph > 7.5) {
            score -= Math.min(25.0, Math.abs(6.8 - ph) * 15.0);
        }

        return Math.max(20, (int) Math.round(score));
    }
}
