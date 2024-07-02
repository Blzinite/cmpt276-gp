package com.example.SchedulEx.controllers;

import com.example.SchedulEx.models.AccessLevel;
import com.example.SchedulEx.helpers.PasswordHelper;
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
    //TODO: better error handling

    @GetMapping("/user/all")
    public String getAllUsers(Model model, HttpSession session){
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        User curr = (User) session.getAttribute("user");
        if(curr == null){
            return "redirect:/login";
        }
        model.addAttribute("current", curr);
        return "allUsers";
    }

    @GetMapping("/login")
    public String getLogin(Model model){
        return "login";
    }

    @GetMapping("/user/add")
    public String getAddUser(Model model){
        return "addUser";
    }

    @PostMapping("/user/add/new")
    public String addUser(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
//        User requester = (User) session.getAttribute("user");
//        if(requester == null) {
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            return "login"; //how did we get here?
//        }
//        if(requester.getAccessLevel() != AccessLevel.ADMIN){
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            return "homepage"; //how did we get here?
//        }
        //only admin can add users
        User toAdd;
        //TODO: only one user per email
        //TODO: Email new users with random password
        //String pwd = PasswordHelper.generatePassword();
        try {
            toAdd = new User(params.get("email"), params.get("password"), params.get("firstname"),
                    params.get("lastname"), params.get("accesslevel"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
        userRepo.save(toAdd);
        return "redirect:all";
    }

    @PostMapping("/user/update")
    public String updateUser(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) throws Exception {
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
        if (session.getAttribute("user") != null) {
            return "redirect:logout";
        }
        if (toFind.isEmpty()) {
            //TODO: Error Handling (User Not Found)
            return "redirect:login/error";
        }
        User user = toFind.get();
        boolean matches;
        try {
            matches = PasswordHelper.verifyPassword(password, user.getPassword(), user.getSalt());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!matches) {
            //TODO: Error Handling (Incorrect Password)
            return "redirect:login/error";
        }
        //TODO: login the user
        session.setAttribute("user", user);
        model.addAttribute("user", user);
        response.setStatus(HttpServletResponse.SC_OK);
        if(user.isNewUser()){
            return "redirect:newPwd";
        }
        return switch (user.getAccessLevel()) {
            case AccessLevel.ADMIN -> "redirect:all";
            case AccessLevel.PROFESSOR -> "redirect:all";
            case AccessLevel.INVIGILATOR -> "redirect:all";
            default -> null;
        };
    }

    @GetMapping("/user/newPwd")
    public String setNewPassword(Model model, HttpSession session, HttpServletResponse response) {
        return "newPwd";
    }

    @PostMapping("/user/newPwd/update")
    public String updatePassword(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        User requester = (User) session.getAttribute("user");
        if(requester == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:login";
        }
        String password = params.get("password1");
        try {
            requester.setPassword(password);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
        requester.setNewUser(false);
        userRepo.save(requester);
        return "redirect:/user/logout";
    }

    @GetMapping("/user/login/error")
    public String loginError(Model model){
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

}