package com.sbs.qna_service.boundedContext.answer;

import com.sbs.qna_service.boundedContext.question.Question;
import com.sbs.qna_service.boundedContext.question.QuestionRepository;
import com.sbs.qna_service.boundedContext.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam(value = "content") String content) {
        Question question = questionService.getQuestion(id);

        Answer answer = answerService.create(question, content);

//        return "%d번 질문에 대한 답변이 생성되었습니다.(답변 번호 : %d)".formatted(id, answer.getId());

         return "redirect:/question/detail/%s".formatted(id);
    }

}
