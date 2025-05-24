package com.example.studyboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class StudyPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String writer;
    private LocalDate deadline;
    // private String category; // 제거된 필드

    private String status; // 스터디 상태를 나타내는 필드 (예: APPLYING, ONGOING, CLOSED)
    private LocalDateTime createdDate;
    private int duration; // 스터디 기간 (일 수)
    private boolean closed; // 스터디 마감 여부

    private String mbtiType;        // 성향 : I/E
    private String studyType;       // Online / Offline / on/off

    public String getStatusLabel() {
        System.out.println("this.status==> "+this.status);
        if (this.status != null) {
            return switch (this.status) {
                case "APPLYING" -> "신청 가능";
                case "ONGOING" -> "진행 중";
                case "CLOSED" -> "마감됨";
                case "REJECTED" -> "거절됨";
                default -> "알 수 없음";
            };
        }
        return "알 수 없음";
    }
    
    @OneToMany (mappedBy = "studyPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();
}