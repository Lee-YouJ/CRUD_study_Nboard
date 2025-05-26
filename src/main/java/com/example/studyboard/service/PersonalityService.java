package com.example.studyboard.service;

import org.springframework.stereotype.Service;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

@Service
public class PersonalityService {

    public SimpleEntry<SimpleEntry<String, String>, String> analyze(Map<String, String> answers) {
        // 1번 질문 분석 (MBTI - E/I)
        String q1Answer = answers.get("q1");
        String mbtiPrefix = "I"; // 기본값
        if (q1Answer != null && q1Answer.equals("2")) {
            mbtiPrefix = "E";
        }
        String mbtiResult = mbtiPrefix;

        String courseType = null;
        // 2번 질문 분석 (수업 종류)
        String q2Answer = answers.get("q2");
        if (q2Answer != null) {
            courseType = switch (q2Answer) {
                case "1" -> "전공";
                case "2" -> "교양";
                case "3" -> "자격증";
                case "4" -> "기타";
                default -> null;
            };
        }

        String preferredDay = null;
        // 새로운 질문: 선호 요일 분석
        String q3Answer = answers.get("q3");
        if (q3Answer != null) {
            preferredDay = switch (q3Answer) {
                case "1" -> "주중"; // 주중 선호
                case "2" -> "주말"; // 주말 선호
                default -> null;
            };
        }

        return new SimpleEntry<>(new SimpleEntry<>(mbtiResult, courseType), preferredDay);
    }
}