package com.example.studyboard.repository;

import com.example.studyboard.entity.StudyPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {
    List<StudyPost> findByClosedFalse();
}

