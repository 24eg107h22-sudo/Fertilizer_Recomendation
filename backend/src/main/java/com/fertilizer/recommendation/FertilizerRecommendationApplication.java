package com.fertilizer.recommendation;

import com.fertilizer.recommendation.entity.Crop;
import com.fertilizer.recommendation.entity.User;
import com.fertilizer.recommendation.repository.CropRepository;
import com.fertilizer.recommendation.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class FertilizerRecommendationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FertilizerRecommendationApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedInitialData(CropRepository cropRepository, UserRepository userRepository) {
        return args -> {
            if (cropRepository.count() == 0) {
                cropRepository.saveAll(List.of(
                    new Crop("Rice", "Cereal", 80.0, 40.0, 40.0, 5.5, 7.0, 80.0, "Paddy requires high nitrogen and standing water during vegetative stages."),
                    new Crop("Wheat", "Cereal", 120.0, 60.0, 40.0, 6.0, 7.5, 60.0, "Wheat thrives in well-drained loamy soil with balanced phosphorus for root growth."),
                    new Crop("Maize", "Cereal", 120.0, 60.0, 50.0, 5.8, 7.2, 65.0, "Corn is a heavy feeder requiring high nitrogen and balanced potash."),
                    new Crop("Cotton", "Fiber / Cash Crop", 100.0, 50.0, 50.0, 6.0, 7.5, 55.0, "Deep-rooted crop needing phosphorus for boll formation and potassium for fiber quality."),
                    new Crop("Sugarcane", "Cash Crop", 150.0, 60.0, 90.0, 6.0, 7.5, 70.0, "Heavy feeder requiring prolonged nutrient supply across multiple tillering phases."),
                    new Crop("Potato", "Tuber / Vegetable", 120.0, 100.0, 120.0, 5.2, 6.5, 70.0, "Requires substantial phosphorus and high potassium for tuber development and starch."),
                    new Crop("Tomato", "Vegetable", 100.0, 60.0, 80.0, 6.0, 7.0, 65.0, "Needs consistent potassium for fruit firmness and calcium/pH balance to prevent blossom rot."),
                    new Crop("Soybean", "Legume / Oilseed", 30.0, 60.0, 40.0, 6.0, 7.0, 60.0, "Fixes atmospheric nitrogen; requires high phosphorus for root nodulation."),
                    new Crop("Groundnut", "Legume / Oilseed", 25.0, 50.0, 40.0, 5.8, 6.5, 50.0, "Requires phosphorus and calcium for pegging and pod filling."),
                    new Crop("Chickpea (Gram)", "Pulse", 20.0, 40.0, 20.0, 6.0, 7.5, 45.0, "Pulse crop requiring minimal nitrogen, relying heavily on phosphorus for root nodule bacteria.")
                ));
            }

            if (userRepository.count() == 0) {
                User demoUser = new User();
                demoUser.setName("Ramesh Kumar");
                demoUser.setEmail("farmer@example.com");
                demoUser.setPassword("password123");
                demoUser.setPhone("9876543210");
                demoUser.setLocation("Punjab, India");
                demoUser.setCreatedAt(LocalDateTime.now());
                userRepository.save(demoUser);
            }
        };
    }
}
