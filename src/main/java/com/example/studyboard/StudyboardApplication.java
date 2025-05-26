package com.example.studyboard;

import com.example.studyboard.entity.StudyPost;
import com.example.studyboard.repository.StudyPostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class StudyboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyboardApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(StudyPostRepository studyPostRepository) {
		return args -> {
			if (studyPostRepository.count() == 0) {
				StudyPost post1 = new StudyPost();
				post1.setTitle("자바 스터디");
				post1.setContent("자바 공부하실 분 모집합니다.");
				post1.setCreatedDate(LocalDateTime.now());
				post1.setDuration(7);
				post1.setDeadline(LocalDateTime.now().plusDays(7).toLocalDate());
				post1.setClosed(false);
				post1.setMbtiType("I");
				post1.setStudyType("online");
				post1.setCourseType("전공");
				post1.setWeekdayOrWeekend("주중");
				studyPostRepository.save(post1);

				StudyPost post2 = new StudyPost();
				post2.setTitle("스프링 스터디");
				post2.setContent("스프링 프레임워크 같이 공부해요.");
				post2.setCreatedDate(LocalDateTime.now());
				post2.setDuration(14);
				post2.setDeadline(LocalDateTime.now().plusDays(14).toLocalDate());
				post2.setClosed(false);
				post2.setMbtiType("E");
				post2.setStudyType("offline");
				post2.setCourseType("전공");
				post2.setWeekdayOrWeekend("주말");
				studyPostRepository.save(post2);

				StudyPost post3 = new StudyPost();
				post3.setTitle("SQLD");
				post3.setContent("SQLD 자격증 공부 같이해요!");
				post3.setCreatedDate(LocalDateTime.now());
				post3.setDuration(14);
				post3.setDeadline(LocalDateTime.now().plusDays(14).toLocalDate());
				post3.setClosed(false);
				post3.setMbtiType("E");
				post3.setStudyType("offline");
				post3.setCourseType("자격증");
				post3.setWeekdayOrWeekend("주말");
				studyPostRepository.save(post3);

				StudyPost post4 = new StudyPost();
				post4.setTitle("리더십");
				post4.setContent("같이 리더십을 키워봐요!");
				post4.setCreatedDate(LocalDateTime.now());
				post4.setDuration(20);
				post4.setDeadline(LocalDateTime.now().plusDays(20).toLocalDate());
				post4.setClosed(false);
				post4.setMbtiType("E");
				post4.setStudyType("offline");
				post4.setCourseType("교양");
				post4.setWeekdayOrWeekend("주중");
				studyPostRepository.save(post4);

				StudyPost post5 = new StudyPost();
				post5.setTitle("컴퓨터활용능력시험");
				post5.setContent("컴퓨터활용능력시험 자격증 공부 같이해요!");
				post5.setCreatedDate(LocalDateTime.now());
				post5.setDuration(7);
				post5.setDeadline(LocalDateTime.now().plusDays(7).toLocalDate());
				post5.setClosed(false);
				post5.setMbtiType("I");
				post5.setStudyType("offline");
				post5.setCourseType("자격증");
				post5.setWeekdayOrWeekend("주말");
				studyPostRepository.save(post5);

				StudyPost post6= new StudyPost();
				post6.setTitle("철학 입문");
				post6.setContent("철학 공부 같이해요!");
				post6.setCreatedDate(LocalDateTime.now());
				post6.setDuration(7);
				post6.setDeadline(LocalDateTime.now().plusDays(7).toLocalDate());
				post6.setClosed(false);
				post6.setMbtiType("I");
				post6.setStudyType("offline");
				post6.setCourseType("기타");
				post6.setWeekdayOrWeekend("주중");
				studyPostRepository.save(post6);
			}
		};
	}
}
