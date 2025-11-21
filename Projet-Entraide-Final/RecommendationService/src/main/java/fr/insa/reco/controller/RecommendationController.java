package fr.insa.reco.controller;

import fr.insa.reco.dto.StudentInfo;
import fr.insa.reco.dto.ReviewRequest;
import fr.insa.reco.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") 
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // GET Mmis à jour : Ajout de @RequestParam String targetDate
    // URL : /api/recommendations/search?keywords=java&targetDate=2025-11-24
    @GetMapping("/api/recommendations/search") 
    public ResponseEntity<List<StudentInfo>> getRecommendations(
            @RequestParam List<String> keywords,
            @RequestParam String targetDate) { // Paramètre obligatoire
        
        try {
            List<StudentInfo> recommendedAids = recommendationService.getRecommendedAids(keywords, targetDate);
            
            if (recommendedAids.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(recommendedAids);
            
        } catch (Exception e) {
            // Erreur si le format de date est mauvais
            System.err.println("Erreur date : " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/recommendations/review")
    public ResponseEntity<Void> handleReviewSubmission(@RequestBody ReviewRequest review) {
        try {
            recommendationService.processReview(review);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}