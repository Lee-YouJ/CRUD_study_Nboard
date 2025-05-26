package com.example.studyboard.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
public class Application {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private StudyPost studyPost;

    @Transient
    private String statusLabel;

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

