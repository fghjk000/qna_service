package com.sbs.qna_service.boundedContext.question;


import com.sbs.qna_service.boundedContext.answer.AnswerRepository;
import com.sbs.qna_service.excption.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public List<Question> findAll(){
        return questionRepository.findAll();
    }

    public Question getQuestion(Integer id) {
        Optional<Question> oq = questionRepository.findById(id);

        if(oq.isEmpty())
            throw new DataNotFoundException("question not found");
        return oq.get();
    }



    public Question create(String subject, String content) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateTime(LocalDateTime.now());
        questionRepository.save(q);
        return q;
    }

    public Page<Question> getList(int page) {
        // Pageable : 페이징 정보를 담는 객체
        Pageable pageable = PageRequest.of(page, 10);
        return questionRepository.findAll(pageable);
    }
}
