package com.example.studyboard.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;


@Entity
@Getter @Setter
public class Application {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private StudyPost studyPost;

    private String username;
    private String status = "PENDING";  // PENDING, APPROVED, REJECTED

    public boolean getIsApprovalCheck() {
        if (this.status != null) {
            return switch (this.status) {
                case "APPROVED" -> true;
                case "REJECTED" -> true;
                default -> false;
            };
        }
        return false;
    }
}

