package com.sbs.qna_service.boundedContext.answer;

import com.sbs.qna_service.boundedContext.question.Question;
import com.sbs.qna_service.boundedContext.question.QuestionService;
import com.sbs.qna_service.boundedContext.user.SiteUser;
import com.sbs.qna_service.boundedContext.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(
            Model model,
            @PathVariable("id") Integer id,
            @Valid AnswerForm answerForm ,
            BindingResult bindingResult,
            Principal principal) {
        Question question = questionService.getQuestion(id);

        SiteUser siteUser = userService.getUser(principal.getName());

        if(bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }

        Answer answer = answerService.create(question, answerForm.getContent(), siteUser);

//        return "%d번 질문에 대한 답변이 생성되었습니다.(답변 번호 : %d)".formatted(id, answer.getId());

         return "redirect:/question/detail/%s".formatted(id);
    }

}
