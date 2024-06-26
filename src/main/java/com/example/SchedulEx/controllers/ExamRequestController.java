package com.example.SchedulEx.controllers;

import com.example.SchedulEx.models.ExamRequest;
import com.example.SchedulEx.models.ExamRequestRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ExamRequestController {
    @Autowired
    private ExamRequestRepository examRequestRepository;

    //TODO: add mappings

    public String addExamRequest(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        //TODO: validate user
        //TODO: call db
        return "success";
    }

    public String deleteExamRequest(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        //TODO: validate user
        //TODO: call db
        return "success";
    }

    public String updateExamRequest(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        //TODO: validate user
        //TODO call db
        return "success";
    }



}
