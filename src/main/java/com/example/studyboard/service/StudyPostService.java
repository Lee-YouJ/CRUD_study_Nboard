package com.example.studyboard.service;

import com.example.studyboard.entity.StudyPost;
import com.example.studyboard.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyPostService {

    private final StudyPostRepository studyPostRepository;

    // 모든 스터디글 조회
    public List<StudyPost> findAll() {
        return studyPostRepository.findAll();
    }

    // 마감일 순 정렬된 모든 스터디글 조회
    public List<StudyPost> findAllOrderByDeadline() {
        return studyPostRepository.findAllByOrderByDeadlineAsc();
    }

    // ID로 스터디글 조회
    public StudyPost findById(Long id) {
        return studyPostRepository.findById(id).orElse(null);
    }

    // 스터디글 저장 또는 수정
    public void save(StudyPost post) {
        studyPostRepository.save(post);
    }

    // 스터디글 삭제
    public void delete(Long id) {
        studyPostRepository.deleteById(id);
    }

    // MBTI 유형에 따른 추천 스터디글 조회
    public List<StudyPost> recommendByPersonality(String personalityType) {
        return studyPostRepository.findByMbtiType(personalityType);
    }

    // 수업 종류에 따른 추천 스터디글 조회
    public List<StudyPost> recommendByCourseType(String courseType) {
        return studyPostRepository.findByCourseType(courseType);
    }

    // 선호 요일에 따른 스터디글 조회
    public List<StudyPost> recommendByPreferredDay(String preferredDay) {
        return studyPostRepository.findByWeekdayOrWeekend(preferredDay);
    }
}
