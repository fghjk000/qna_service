package com.sbs.qna_service;

import com.sbs.qna_service.boundedContext.answer.Answer;
import com.sbs.qna_service.boundedContext.answer.AnswerRepository;
import com.sbs.qna_service.boundedContext.question.Question;
import com.sbs.qna_service.boundedContext.question.QuestionRepository;
import com.sbs.qna_service.boundedContext.question.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class QnaServiceApplicationTests {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void beforeEach() {
        // 모든 데이터 삭제
        questionRepository.deleteAll();
        // 흔적삭제 (다음번 INSERT 때 id가 1번으로 설정되도록)
        questionRepository.clearAutoIncrement();

        answerRepository.deleteAll();

        answerRepository.clearAutoIncrement();

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

        // 답변 1개 생성하기
        Answer a1 = new Answer();
        a1.setContent("네 자동으로 생성됩니다.");
        q2.addAnswer(a1);
        a1.setCreateDate(LocalDateTime.now());
        answerRepository.save(a1);


    }

    @Test
    @DisplayName("데티어 저장하기")
    void t001() {
        Question q = new Question();
        q.setSubject("겨울 제철 음식으로는 무엇을 먹어야 하나요?");
        q.setContent("겨울 제철 음식을 알려주세요.");
        q.setCreateTime(LocalDateTime.now());
        questionRepository.save(q);
        assertEquals("겨울 제철 음식으로는 무엇을 먹어야 하나요?", questionRepository.findById(3).get().getSubject());
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

    // DELETE FROM question WHERE id = ?
    @Test
    @DisplayName("데이터 삭제하기")
    void t008() {
        assertEquals(2, questionRepository.count());
        Optional<Question> oq = questionRepository.findById(1);
        Optional<Question> oq2 = questionRepository.findById(2);
        assertTrue(oq.isPresent());
        assertTrue(oq2.isPresent());
        Question q = oq.get();
        Question q2 = oq2.get();
        questionRepository.delete(q);
        questionRepository.delete(q2);
        assertEquals(0, questionRepository.count());
    }


    // 특정 질문가져오기
    // SELECT * FROM question WHERE id = ?
    // INSERT INTO answer SET create_date =
    // NOW(),
    // content = ?,
    // question_id =?;
    @Test
    @DisplayName("답변 데이터 생성 후 저장")
    void t009() {
        Optional<Question> oq = questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        /*
        // v1
        Optional<Question> oq = questionRepository.findById(2);
        Question q = oq.get();
        */

        /*
        // v2
        Question q = questionRepository.findById(2).orElse(null);
         */
        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(q); // 어떤 질문의 답변인 알기위해서 Question 객체가 필요하다.
        a.setCreateDate(LocalDateTime.now());
        answerRepository.save(a);
    }

    /*
    SELECT A.*, Q.*
    FROM answer AS A
    LEFT JOIN question As Q
    on Q.id = A.question_id
    WHERE A.id = ?;
    */
    @Test
    @DisplayName("답변 데이터 조회")
    void t010() {
        Optional<Answer> oa = answerRepository.findById(1);
        assertTrue(oa.isPresent());
        Answer a = oa.get();
        assertEquals(2, a.getQuestion().getId());
    }

    // 테스트 코드에서는 Transactiona을 붙여줘야 한다.
    // findById 메서드를 실행하고 나면 DB가 끊어지기 때문
    // Transactional 어노테이션을 사용하면 메서드가 종료될 때까지 DB연결이 유지된다..
    @Transactional
    @Test
    @DisplayName("질문을 통해 답변 찾기")
    @Rollback(false) // 테스트 메서드가 끝난 후에도 트랜잭션이 롤백되지 않고 커밋된다.
    void t011() {
        // SELECT * FROM question WHERE id =2;
        Optional<Question> oq = questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        // 테스트 환경에서는 get해서 가져온뒤 DB연결을 끊음

        //SQL : SELECT * FROM answer WHERE question_id = 2;
        List<Answer> answerList = q.getAnswerList(); // DB 통신이 끊어졌기때문에 answer 를 사져오지 못해 => 에러 발생

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    }

    @Test
    @DisplayName("대량의 테스트 데이터 만들기")
    void t012() {
        IntStream.rangeClosed(3,300)
                .forEach(
                        no -> questionService.create("테스트 제목입니다.%d".formatted(no), "테스트 내용입니다.%d".formatted(no)
                        ));



    }
}
