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
    //TODO: resolve redirects

    @GetMapping("/user/all")
    public String getAllUsers(Model model, HttpSession session){
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        User curr = (User) session.getAttribute("user");
        if(curr == null){
            return "redirect:../login";
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


    //POST should include:
    //User Email - named "email"
    //User Password - temporarily named "password"
    //Password will be randomly generated in production
    //User First Name - named "firstname"
    //User Surname - named "lastname"
    //User Account Type - named "accesslevel"
    //Upon success a new user will be created in the db
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
        return "redirect:../action-panel";
    }

    //POST should include
    //New Email - named "email"
    //New Password - named "password"
    //New First Name - named "firstname"
    //New Surname - named "lastname"
    //New Access Level - named "accesslevel"
    //Form validation should be done on FE
    //Upon success "toEdit" will be updated in the db
    //Upon success redirect the user to wherever they need to go
    @PostMapping("/user/update")
    public String updateUser(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) throws Exception {
        User requester = (User) session.getAttribute("user");
        if(requester == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:/login";
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

    //POST should include
    //User Email - named "email"
    //User Password - named "password"
    //Input validation should be done on the FE
    //
    //If this is the user's first login, they will be redirected to change their password
    //Else they will be directed to the homepage with the required data stored in the session
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
        return "redirect:../action-panel";
    }

    @GetMapping("/user/newPwd")
    public String setNewPassword() {
        return "newPwd";
    }

    //POST should include
    //New User Password - named "password1"
    //Input validation is done on the FE
    //User will be logged out after successfully updating their password
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

    @GetMapping("action-panel")
    public String getActionPanel(Model model, HttpSession session){
        User curr = (User) session.getAttribute("user");
        if(curr == null){
            return "login";
        }
        switch(curr.getAccessLevel()){
            case AccessLevel.ADMIN -> {
                model.addAttribute("currentUser", curr);
                model.addAttribute("users", userRepo.findAll());
                return "admin";
            }
            case AccessLevel.INVIGILATOR -> {
                model.addAttribute("currentUser", curr);
                return "invigilator";
            }
            case AccessLevel.PROFESSOR -> {
                model.addAttribute("currentUser", curr);
                return "professor";
            }
            default -> {
                return "login";
            }
        }
    }

}