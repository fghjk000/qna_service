package com.sbs.qna_service;

import com.sbs.qna_service.boundedContext.question.Question;
import com.sbs.qna_service.boundedContext.question.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class QnaServiceApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("데티어 저장하기")
    void t001() {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateTime(LocalDateTime.now());
        questionRepository.save(q1);
        // save INSERT query날림

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("Id는 자동으로 생성되나요?");
        q2.setCreateTime(LocalDateTime.now());
        questionRepository.save(q2);
    }

    // findAll() => Select * from question
    @Test
    @DisplayName("findAll")
    void t002() {
        List<Question> all = questionRepository.findAll();
        assertEquals(2, all.size());

        Question q = all.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());

    }

    // Select * from question Where id = 1;
    @Test
    @DisplayName("findById")
    void t003() {
        Optional<Question> oq = questionRepository.findById(1);
        if(oq.isPresent()) {  // isPresent : 값의 존재를 확인 있으면 true 없으면 false
            Question q = oq.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }

    // Select * From question Where subject = 'sbb가 무엇인가요?'
    @Test
    @DisplayName("findBySubject")
    void t004() {
        Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
        assertEquals(1, q.getId());
    }

    // SELECT * From question WHERE subject = 'sbb가 무엇인가요?' AND content = 'sbb에 대해서 알고 싶습니다.';
    @Test
    @DisplayName("findBySubjectAndContent")
    void t005() {
        Question q = questionRepository.findBySubjectAndContent("sbb가 무엇인가요?",
                "sbb에 대해서 알고 싶습니다.");
        assertEquals(1, q.getId());
    }

    // SELECT * From question Where subject LIKE 'sbb%';
    @Test
    @DisplayName("findBySubjectLike")
    void t006() {
        List<Question> qList = questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    // UPDATE question
    // SET content =?,
    // create_time = ?,
    // subject = ?,
    // WHERE id = ?,
    @Test
    @DisplayName("데이터 수정하기")
    void t007() {
        // SELECT * FROM question WHERE id = 1;
        Optional<Question> oq = questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        q.setSubject("수정된 제목");
        questionRepository.save(q);
    }
}
