package com.sbs.qna_service.boundedContext.question;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> findAll(){
        return questionRepository.findAll();
    }
}
