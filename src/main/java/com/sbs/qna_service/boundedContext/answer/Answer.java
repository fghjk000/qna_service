package com.sbs.qna_service.boundedContext.answer;

import com.sbs.qna_service.boundedContext.question.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;


    public Answer() {
        this.createDate = LocalDateTime.now();
        this.question = new Question();
    }
}
