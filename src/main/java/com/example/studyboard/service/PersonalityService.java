package com.example.studyboard.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PersonalityService {
    public String analyze(Map<String, String> answers) {
        int score = 0;
        for (String key : answers.keySet()) {
            score += Integer.parseInt(answers.get(key));
        }
        return score > 10 ? "E" : "I";  // E: 외향형, I: 내향형
    }
}

