package com.example.studyboard.controller;

import com.example.studyboard.entity.StudyPost;
import com.example.studyboard.service.ApplicationService;
import com.example.studyboard.service.PersonalityService;
import com.example.studyboard.service.StudyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyBoardController {

    private final StudyPostService studyPostService;
    private final ApplicationService applicationService;
    private final PersonalityService personalityService;

    /** ----------------------
     * 📌 1. 스터디 게시글 목록 및 상세
     * ---------------------- */
    @GetMapping
    public String list(Model model) {
        List<StudyPost> posts = studyPostService.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("studyType", "전체");
        return "study/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        StudyPost post = studyPostService.findById(id);
        model.addAttribute("post", post);
        return "study/detail";
    }

    /** ----------------------
     * 📌 2. 글 작성 & 등록
     * ---------------------- */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("studyPost", new StudyPost());
        return "study/createForm";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute StudyPost post) {
        post.setCreatedDate(LocalDateTime.now());
        post.setDeadline(post.getCreatedDate().plusDays(post.getDuration()).toLocalDate());
        post.setWriter("익명 사용자"); // 👈 이 줄을 추가
        studyPostService.save(post);
        return "redirect:/study";
    }

    /** ----------------------
     * 📌 3. 스터디 신청
     * ---------------------- */
    @PostMapping("/{id}/apply")
    public String apply(@PathVariable Long id, Model model) {
        String applicant = "익명 사용자"; // 또는 다른 임의의 이름
        applicationService.applyToStudy(id, applicant);
        model.addAttribute("studyId", id);
        return "study/applyComplete";
    }

    /** ----------------------
     * 📌 5. 성향 테스트
     * ---------------------- */
    @GetMapping("/test")
    public String personalityTestForm() {
        return "study/testForm";
    }

    @PostMapping("/test")
    public String processTest(@RequestParam Map<String, String> answers, Model model) {
        String result = personalityService.analyze(answers);
        List<StudyPost> recommended = studyPostService.recommendByPersonality(result);
        model.addAttribute("result", result);
        model.addAttribute("recommended", recommended);
        return "study/testResult";
    }

    /** ----------------------
     * 📌 6. 내가 가입한 스터디
     * ---------------------- */
    @GetMapping("/myStudy")
    public String myStudy(Model model) {
        String username = "익명 사용자";
        List<StudyPost> myStudyPosts = applicationService.findStudyPostsByUsername(username);

        // 리스트가 비어있지 않을 때만 로그 출력
        if (!myStudyPosts.isEmpty()) {
            System.out.println("myStudyPosts ==> " + myStudyPosts.get(0).getStatusLabel());
        } else {
            System.out.println("myStudyPosts가 비어 있습니다.");
        }

        model.addAttribute("myStudyPosts", myStudyPosts);
        return "study/myStudy";
    }

}