package com.example.studyboard.controller;

import com.example.studyboard.entity.StudyPost;
import com.example.studyboard.service.ApplicationService;
import com.example.studyboard.service.StudyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ApplicationService applicationService;
    private final StudyPostService studyPostService;

    // 관리자 로그인 페이지
    @GetMapping("/login")
    public String adminLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "아이디 와 비밀번호를 재입력 해주세요");
        }
        return "admin/adminLogin";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String adminLogin(@RequestParam String adminId,
                             @RequestParam String adminPw,
                             RedirectAttributes redirectAttributes) {

        if ("admin".equals(adminId) && "password".equals(adminPw)) {
            return "redirect:/admin/list";  // 로그인 성공 시 관리자 페이지로
        } else {
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/admin/login"; // 실패 시 로그인 페이지로 리다이렉트
        }
    }

    // 관리자 전용 목록 및 스터디 상세 페이지 (마감일 기준 오름차순 정렬 적용)
    @GetMapping("/list")
    public String list(Model model) {
        List<StudyPost> posts = studyPostService.findAllOrderByDeadline(); // 수정된 부분
        model.addAttribute("posts", posts);
        model.addAttribute("studyPost", "관리자용 목록");
        return "admin/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model,
                         @ModelAttribute("msg") String msg, RedirectAttributes redirectAttributes) {
        StudyPost post = studyPostService.findById(id);

        if (post == null) {
            redirectAttributes.addFlashAttribute("error", "해당 ID의 스터디를 찾을 수 없습니다.");
            return "redirect:/admin/list";
        }

        model.addAttribute("post", post);
        model.addAttribute("courseType", post.getCourseType());

        String studyDayKo;
        if ("weekday".equals(post.getWeekdayOrWeekend())) {
            studyDayKo = "주중";
        } else if ("weekend".equals(post.getWeekdayOrWeekend())) {
            studyDayKo = "주말";
        } else {
            studyDayKo = "";
        }
        model.addAttribute("studyDayKo", studyDayKo);

        if (msg == null || msg.trim().isEmpty()) {
            model.addAttribute("msg", null);
        }

        return "admin/detail";
    }

    @PostMapping("/{id}/delete")
    public String deleteStudy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        StudyPost post = studyPostService.findById(id);

        boolean hasApproved = applicationService.hasApprovedApplicants(post);
        if (hasApproved) {
            redirectAttributes.addFlashAttribute("msg", "스터디에 가입한 인원이 있어 삭제가 불가능 합니다.");
            return "redirect:/admin/" + id;
        }

        studyPostService.delete(id);
        redirectAttributes.addFlashAttribute("msg", "스터디가 삭제되었습니다.");
        return "redirect:/admin/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("studyPost", new StudyPost());
        return "admin/createForm";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute StudyPost post) {
        System.out.println("생성 시 수업 요일: " + post.getWeekdayOrWeekend());
        post.setCreatedDate(LocalDateTime.now());
        post.setDeadline(post.getCreatedDate().plusDays(post.getDuration()).toLocalDate());
        post.setWriter("익명 사용자");
        studyPostService.save(post);
        return "redirect:/admin/list";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        StudyPost post = studyPostService.findById(id);
        model.addAttribute("studyPost", post);

        if (post.getWeekdayOrWeekend() != null) {
            model.addAttribute("isWeekday", post.getWeekdayOrWeekend().equals("weekday"));
            model.addAttribute("isWeekend", post.getWeekdayOrWeekend().equals("weekend"));
        } else {
            model.addAttribute("isWeekday", false);
            model.addAttribute("isWeekend", false);
        }

        return "admin/editForm";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @ModelAttribute StudyPost post) {
        StudyPost existingPost = studyPostService.findById(id);
        if (existingPost != null) {
            post.setCreatedDate(existingPost.getCreatedDate());
            post.setClosed(existingPost.getClosed());
            post.setDeadline(post.getCreatedDate().plusDays(post.getDuration()).toLocalDate());
            studyPostService.save(post);
            return "redirect:/admin/" + id;
        } else {
            return "redirect:/admin/list?error=notfound";
        }
    }

    // 관리자 스터디 신청자 목록 처리
    @GetMapping("/applications")
    public String applicationList(Model model) {
        model.addAttribute("applications", applicationService.findAll());
        return "admin/applications";
    }

    @PostMapping("/applications/{appId}/approve")
    public String approve(@PathVariable Long appId) {
        applicationService.approve(appId);
        return "redirect:/admin/applications";
    }

    @PostMapping("/applications/{appId}/reject")
    public String reject(@PathVariable Long appId) {
        applicationService.reject(appId);
        return "redirect:/admin/applications";
    }
}
