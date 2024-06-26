package com.example.SchedulEx.controllers;

import com.example.SchedulEx.models.Exam;
import com.example.SchedulEx.models.ExamRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ExamController {
    @Autowired
    private ExamRepository examRepository;

    //TODO: add mappings

    public String addExam(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        //TODO: validate user
        //TODO: actually add an exam
        examRepository.save(new Exam());
        response.setStatus(HttpServletResponse.SC_CREATED);
        return "success";
    }

    public String updateExam(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        //TODO: validate user
        //TODO: call db
        return "success";
    }

    public String deleteExam(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        //TODO: validate user
        //TODO: call db
        return "success";
    }

}
