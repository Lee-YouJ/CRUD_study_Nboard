package com.example.studyboard.service;

import com.example.studyboard.entity.StudyPost;
import com.example.studyboard.repository.StudyPostRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyPostService {
    private final StudyPostRepository studyPostRepository;

    public List<StudyPost> findAll() {
        return studyPostRepository.findAll();
    }

    public StudyPost findById(Long id) {
        return studyPostRepository.findById(id).orElseThrow();
    }

    public void save(StudyPost post) {
        studyPostRepository.save(post);
    }

    public List<StudyPost> recommendByPersonality(String type) {
        if (type.equals("E")) return studyPostRepository.findByClosedFalse();
        else return studyPostRepository.findByClosedFalse();
    }

    public void delete(Long id) {
        studyPostRepository.deleteById(id);
    }
}