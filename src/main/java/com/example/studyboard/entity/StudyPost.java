package com.example.studyboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class StudyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdDate;
    private LocalDate deadline;
    private Integer duration;
    private String studyType;
    private String courseType;
    private String mbtiType;
    private String status;
    private Boolean closed = false; // 기본값을 false로 설정

    private String weekdayOrWeekend; // 추가된 필드

    @Setter
    @Getter
    @Transient // DB에 매핑하지 않음
    private String statusLabel;

    // 기본 생성자
    public StudyPost() {
    }

    // 필드를 초기화하는 생성자 (필요에 따라 추가)
    public StudyPost(String title, String content, String studyType, String courseType, Integer duration, String weekdayOrWeekend) {
        this.title = title;
        this.content = content;
        this.studyType = studyType;
        this.courseType = courseType;
        this.duration = duration;
        this.weekdayOrWeekend = weekdayOrWeekend;
        this.createdDate = LocalDateTime.now();
        this.deadline = this.createdDate.plusDays(duration).toLocalDate();
        this.writer = "익명 사용자";
        this.status = "ONGOING";
        this.closed = false;
    }

    public String getWeekdayOrWeekend() {
        if ("weekday".equals(this.weekdayOrWeekend)) {
            return "주중";
        } else if ("weekend".equals(this.weekdayOrWeekend)) {
            return "주말";
        }
        return this.weekdayOrWeekend; // 기존 값 반환 (혹시 다른 값이 있을 경우를 대비)
    }
}