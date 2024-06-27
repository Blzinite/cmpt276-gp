package com.example.SchedulEx.controllers;

import com.example.SchedulEx.models.AccessLevel;
import com.example.SchedulEx.models.User;
import com.example.SchedulEx.models.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepo;

    //TODO: resolve mappings

    @GetMapping("/user/all")
    public String getAllUsers(Model model, HttpSession session){
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        User curr = (User) session.getAttribute("user");
        model.addAttribute("current", curr);
        return "allUsers";
    }

    @GetMapping("/login")
    public String getLogin(Model model){
        return "login";
    }

    @PostMapping("/user/add")
    public String addUser(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        User requester = (User) session.getAttribute("user");
//        if(requester == null) {
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            return "login"; //how did we get here?
//        }
//        if(requester.getAccessLevel() != AccessLevel.ADMIN){
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            return "homepage"; //how did we get here?
//        }
        //only admin can add users
        userRepo.save(new User(params.get("email"), params.get("password"), params.get("firstname"), params.get("lastname"), params.get("accesslevel")));
        return "redirect:all";
    }

    @PostMapping("/user/update")
    public String updateUser(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        User requester = (User) session.getAttribute("user");
        if(requester == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect: /login";
        }
        User toEdit = userRepo.findById(Integer.parseInt(params.get("id"))).get();
        boolean selfEdit = toEdit.getUid() == requester.getUid();
        if(!selfEdit && requester.getAccessLevel() != AccessLevel.ADMIN){
            //only admins can edit other accounts
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:login";
        }

        toEdit.setEmail(params.get("email"));
        toEdit.setPassword(params.get("password"));
        toEdit.setFirstName(params.get("firstname"));
        toEdit.setLastName(params.get("lastname"));
        toEdit.setAccessLevel(params.get("accesslevel"));

        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        return selfEdit ? "userSettings" : "redirect:allUsers";
    }

    @PostMapping("/user/login")
    public String loginUser(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        String email = params.get("email");
        String password = params.get("password");
        Optional<User> toFind = userRepo.findByEmail(email);
        if(toFind.isEmpty()) {
            //TODO: Error Handling (User Not Found)
            return "redirect:false";
        }
        User user = toFind.get();
        if(!password.equals(user.getPassword())) {
            //TODO: Error Handling (Incorrect Password)
            return "redirect:false";
        }
        //TODO: login the user
        session.setAttribute("user", user);
        model.addAttribute("user", user);
        response.setStatus(HttpServletResponse.SC_OK);
        return switch(user.getAccessLevel()){
            case AccessLevel.ADMIN -> "redirect:all";
            case AccessLevel.PROFESSOR -> "redirect:all";
            case AccessLevel.INVIGILATOR -> "redirect:all";
            default -> null;
        };
    }
}