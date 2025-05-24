package com.example.studyboard.service;
import com.example.studyboard.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import com.example.studyboard.entity.Application;
import com.example.studyboard.entity.StudyPost;
import com.example.studyboard.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final StudyPostRepository studyPostRepository;

    public void applyToStudy(Long postId, String username) {
        System.out.println("postId==>"+postId);
        StudyPost studyPost = studyPostRepository.findById(postId).orElse(null);
        if (studyPost == null) {
            System.out.println("Error: StudyPost with ID " + postId + " not found.");
            return; // 또는 예외를 던질 수 있습니다.
        }
        Application app = new Application();
        app.setStudyPost(studyPost);
        app.setUsername(username);
        applicationRepository.save(app);
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public Boolean isDupCheck(Long appId, String username) {
        System.out.println("appId ==> " + appId);
        Application app = applicationRepository.findById(appId).orElse(null);
        boolean isDupCheck = false;
        if(app != null) {
            isDupCheck = Objects.equals(app.getUsername(), username);
        }
        return isDupCheck;
    }

    public void approve(Long appId) {
        Application app = applicationRepository.findById(appId).orElseThrow();
        app.setStatus("APPROVED");
        applicationRepository.save(app);

        // 관련 StudyPost의 상태를 업데이트
        StudyPost studyPost = app.getStudyPost();
        // 스터디 상태를 "진행 중"으로 변경하는 예시
        if (studyPost != null) {
            studyPost.setStatus("ONGOING");
            studyPostRepository.save(studyPost);
        }
    }

    public void reject(Long appId) {
        Application app = applicationRepository.findById(appId).orElseThrow();
        app.setStatus("REJECTED");
        applicationRepository.save(app);

        // 관련 StudyPost의 상태를 업데이트
        StudyPost studyPost = app.getStudyPost();
        // 스터디 상태를 "진행 중"으로 변경하는 예시
        if (studyPost != null) {
            studyPost.setStatus("REJECTED");
            studyPostRepository.save(studyPost);
        }
    }

    public List<StudyPost> findStudyPostsByUsername(String username) {
        return applicationRepository.findByUsername(username).stream()
                .map(Application::getStudyPost)
                .collect(Collectors.toList());
    }

    public boolean hasApprovedApplicants(StudyPost post) {
        return applicationRepository.findByStudyPost(post).stream()
                .anyMatch(app -> "APPROVED".equals(app.getStatus()));
    }
}