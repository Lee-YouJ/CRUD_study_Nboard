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
			// DB가 비어있을 때만 insert 가 실행되도록 하는 코드
			if (studyPostRepository.count() == 0) {
				StudyPost post1 = new StudyPost();
				post1.setTitle("자바 스터디");
				post1.setContent("자바 공부하실 분 모집합니다.");
				post1.setCreatedDate(LocalDateTime.now());
				post1.setDuration(7);
				post1.setDeadline(LocalDateTime.now().plusDays(7).toLocalDate()); // deadline 타입이 LocalDate일 경우 변환
				post1.setClosed(false);
				post1.setMbtiType("I");
				post1.setStudyType("online");
				studyPostRepository.save(post1);

				StudyPost post2 = new StudyPost();
				post2.setTitle("스프링 스터디");
				post2.setContent("스프링 프레임워크 같이 공부해요.");
				post2.setCreatedDate(LocalDateTime.now());
				post2.setDuration(14);
				post2.setDeadline(LocalDateTime.now().plusDays(14).toLocalDate()); // deadline 타입이 LocalDate일 경우 변환
				post2.setClosed(false);
				post2.setMbtiType("E");
				post2.setStudyType("offline");
				studyPostRepository.save(post2);
			}
		};
	}
}