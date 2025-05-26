package com.example.studyboard.repository;

import com.example.studyboard.entity.StudyPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {
    List<StudyPost> findByMbtiType(String mbtiType);
    List<StudyPost> findByCourseType(String courseType);

    @Query("SELECT p FROM StudyPost p WHERE p.weekdayOrWeekend = :day")
    List<StudyPost> findByWeekdayOrWeekend(@Param("day") String weekdayOrWeekend);

    // 마감일(Deadline) 오름차순 정렬로 모든 스터디글 조회
    List<StudyPost> findAllByOrderByDeadlineAsc();
}
