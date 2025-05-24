package com.example.studyboard.repository;

import com.example.studyboard.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUsername(String username);

    // ✅ 스터디에 속한 모든 신청서 조회
    List<Application> findByStudyPost(com.example.studyboard.entity.StudyPost studyPost);

}
