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

    // 🔐 관리자 로그인 페이지
    @GetMapping("/login")
    public String adminLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "아이디 와 비밀번호를 재입력 해주세요");
        }
        return "admin/adminLogin";
    }

    // 🔐 로그인 처리
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

    // 📄 관리자 전용 목록 및 스터디 상세 페이지
    @GetMapping("/list")
    public String list(Model model) {
        List<StudyPost> posts = studyPostService.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("studyPost", "관리자용 목록");
        return "admin/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model,
                         @ModelAttribute("msg") String msg, RedirectAttributes redirectAttributes) {
        StudyPost post = studyPostService.findById(id);

        if (post == null) {
            // 해당 ID의 스터디가 없으면 메시지를 담고 목록 페이지로 리다이렉트
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
            studyDayKo = ""; // 또는 다른 기본값
        }
        model.addAttribute("studyDayKo", studyDayKo);

        // msg가 null이거나 빈 문자열인 경우 model에서 제거
        if (msg == null || msg.trim().isEmpty()) {
            model.addAttribute("msg", null); // 또는 model.remove("msg");
        }

        return "admin/detail";
    }

    @PostMapping("/{id}/delete")
    public String deleteStudy(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        StudyPost post = studyPostService.findById(id);

        // ✅ 삭제 제한 조건 추가
        boolean hasApproved = applicationService.hasApprovedApplicants(post);
        if (hasApproved) {
            redirectAttributes.addFlashAttribute("msg", "스터디에 가입한 인원이 있어 삭제가 불가능 합니다.");
            return "redirect:/admin/" + id;
        }

        studyPostService.delete(id);
        redirectAttributes.addFlashAttribute("msg", "스터디가 삭제되었습니다.");
        return "redirect:/admin/list";
    }

    // 새로운 스터디 생성
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("studyPost", new StudyPost());
        return "admin/createForm";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute StudyPost post) {
        System.out.println("생성 시 수업 요일: " + post.getWeekdayOrWeekend()); // 👈 이 줄 추가
        post.setCreatedDate(LocalDateTime.now());
        post.setDeadline(post.getCreatedDate().plusDays(post.getDuration()).toLocalDate());
        post.setWriter("익명 사용자");
        studyPostService.save(post);
        return "redirect:/admin/list";
    }

    // 스터디 수정
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        StudyPost post = studyPostService.findById(id);
        model.addAttribute("studyPost", post);

        // 수업 요일 정보를 템플릿에서 사용하기 위한 플래그 추가
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
            post.setClosed(existingPost.getClosed()); // 기존 closed 값 유지
            // duration을 기반으로 deadline을 새롭게 설정
            post.setDeadline(post.getCreatedDate().plusDays(post.getDuration()).toLocalDate());
            studyPostService.save(post);
            return "redirect:/admin/" + id;
        } else {
            // 해당 ID의 스터디가 없을 경우 처리 (예: 오류 메시지, 리다이렉트)
            return "redirect:/admin/list?error=notfound";
        }
    }

    // 📄 관리자 스터디 신청자 목록 처리
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