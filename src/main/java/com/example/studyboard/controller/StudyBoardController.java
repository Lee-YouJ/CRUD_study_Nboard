package com.example.studyboard.controller;

import java.util.AbstractMap.SimpleEntry;

import com.example.studyboard.entity.Application;
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

    /** 1. 스터디 게시글 목록 */
    @GetMapping
    public String list(Model model) {
        List<StudyPost> posts = studyPostService.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("studyType", "전체");
        return "study/list";
    }

    /** 2. 상세 */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        StudyPost post = studyPostService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("courseType", post.getCourseType());

        // 수업 종류에 따른 플래그 설정 (뷰에서 사용하기 위함)
        String type = post.getStudyType();
        model.addAttribute("isOnline", "Online".equalsIgnoreCase(type));
        model.addAttribute("isOffline", "Offline".equalsIgnoreCase(type));
        model.addAttribute("isOnOff", "On/Off".equalsIgnoreCase(type) || "OnOff".equalsIgnoreCase(type));

        // 수업 요일 정보를 모델에 추가
        model.addAttribute("weekdayOrWeekend", post.getWeekdayOrWeekend());

        // 🔍 디버깅용 출력
        System.out.println("확인 (컨트롤러): post.getWeekdayOrWeekend() = " + post.getWeekdayOrWeekend());

        return "study/detail";
    }

    /** 3. 스터디 생성 폼 */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("studyPost", new StudyPost());
        return "study/createForm";
    }

    /** 4. 스터디 생성 처리 */
    @PostMapping("/new")
    public String create(@ModelAttribute StudyPost post) {
        // 🔍 디버깅용 출력
        System.out.println("요일 구분 (폼에서 받은 값): " + post.getWeekdayOrWeekend());

        post.setCreatedDate(LocalDateTime.now());
        post.setDeadline(post.getCreatedDate().plusDays(post.getDuration()).toLocalDate());
        post.setWriter("익명 사용자");
        post.setStatus("ONGOING"); // 기본 상태
        studyPostService.save(post);
        return "redirect:/study";
    }

    /** 5. 스터디 신청 */
    @PostMapping("/{id}/apply")
    public String apply(@PathVariable Long id, Model model) {
        String applicant = "익명 사용자";
        applicationService.applyToStudy(id, applicant);
        model.addAttribute("studyId", id);
        return "study/applyComplete";
    }

    /** 6. 성향 테스트 폼 */
    @GetMapping("/test")
    public String personalityTestForm() {
        return "study/testForm";
    }

    /** 7. 성향 테스트 결과 처리 */
    @PostMapping("/test")
    public String processTest(@RequestParam Map<String, String> answers, Model model) {
        SimpleEntry<SimpleEntry<String, String>, String> analysisResult = personalityService.analyze(answers);
        SimpleEntry<String, String> personalityAndCourse = analysisResult.getKey();
        String preferredDayResult = analysisResult.getValue();

        String mbtiResult = personalityAndCourse.getKey();
        String courseTypeResult = personalityAndCourse.getValue();

        List<StudyPost> recommendedByMbti = studyPostService.recommendByPersonality(mbtiResult);
        model.addAttribute("mbtiResult", mbtiResult);
        model.addAttribute("recommendedByMbti", recommendedByMbti);
        model.addAttribute("recommendedByMbtiSize", recommendedByMbti.size());


        if (courseTypeResult != null) {
            List<StudyPost> recommendedByCourseType = studyPostService.recommendByCourseType(courseTypeResult);
            model.addAttribute("courseTypeResult", courseTypeResult);
            model.addAttribute("recommendedByCourseType", recommendedByCourseType);
        }

        if (preferredDayResult != null) {
            List<StudyPost> recommendedByPreferredDay = studyPostService.recommendByPreferredDay(preferredDayResult);
            model.addAttribute("preferredDayResult", (preferredDayResult.equals("weekday") ? "주중" : "주말"));
            model.addAttribute("recommendedByPreferredDay", recommendedByPreferredDay);
        }

        return "study/testResult";
    }

    /** 8. 내가 가입한 스터디 목록 */
    @GetMapping("/myStudy")
    public String myStudy(Model model) {
        String username = "익명 사용자";
        List<Application> applications = applicationService.findByUsername(username);

        // 상태 라벨 붙이기
        for (Application app : applications) {
            String label = switch (app.getStatus()) {
                case "APPROVED" -> "승인됨";
                case "REJECTED" -> "거절됨";
                case "PENDING" -> "대기 중";
                default -> "알 수 없음";
            };
            app.setStatusLabel(label);
        }

        model.addAttribute("applications", applications);
        return "study/myStudy";
    }
}
