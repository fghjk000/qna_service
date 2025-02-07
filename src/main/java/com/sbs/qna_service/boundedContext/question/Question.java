package com.sbs.qna_service.boundedContext.question;

import com.sbs.qna_service.boundedContext.answer.Answer;
import com.sbs.qna_service.boundedContext.user.SiteUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createTime;

    @ManyToOne
    private SiteUser author;

    // @OneToMany 자바세상에서의 편의를 위해 필드 생성
    // 이 녀석은 실에 DB 테이블에 컬럼이 생성되지 않는다.
    // DB는 리스트나 배열을 만들 수 없다.
    // answerList : 만들어도 되고 만들지 않아도 된다. (만들면 질문을 통해 답변을 조회 가능)

    // CascadeType.REMOVE 질문이 삭제되면 답변도 같이 삭제된다.
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList = new ArrayList<>();

    public void addAnswer(Answer a) {
        a.setQuestion(this); // Question 객체에 Answer 추가
        answerList.add(a); // Answer 객체에 Question 설정
    }
}
