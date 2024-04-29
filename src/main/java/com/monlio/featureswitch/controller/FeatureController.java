package com.monlio.featureswitch.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monlio.featureswitch.DTO.FeatureDTO;
import com.monlio.featureswitch.service.FeatureService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
//@RequestMapping("api/v1/feature")
public class FeatureController {
        @Autowired
        private FeatureService featureService;

        @GetMapping("/")
        public String test() {
            return "Greetings from Spring Boot";
        }
        
        @PostMapping("/feature")
        public ResponseEntity<String> add_UserWithFeature(@RequestBody FeatureDTO request) {
            if (featureService.addOrUpdate_UserWithFeature(request)){
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        
        @GetMapping("/feature")
        public String getFeature(@RequestParam String email, @RequestParam String featureName) {
            boolean canAccess = featureService.checkAccess(email, featureName);

            String output = "{ \"canAccess\": " + canAccess + " }"; // Construct the response JSON
            return output;
        }
        
}
