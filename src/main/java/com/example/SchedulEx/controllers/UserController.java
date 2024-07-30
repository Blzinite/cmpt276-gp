package com.example.SchedulEx.controllers;

import com.example.SchedulEx.helpers.EmailHelper;
import com.example.SchedulEx.models.AccessLevel;
import com.example.SchedulEx.helpers.PasswordHelper;
import com.example.SchedulEx.models.User;
import com.example.SchedulEx.repositories.UserRepository;
import com.example.SchedulEx.services.CourseService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CourseService courseService;

    //TODO: resolve mappings
    //TODO: better error handling
    //TODO: resolve redirects


    @GetMapping("/login")
    public String getLogin(Model model){
        return "login";
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable Integer id, Model model){
        User get = userRepo.findById(id).orElse(null);
        if(get == null){
            return "redirect:../login";
        }
        model.addAttribute("user", get);
        return "viewUser";
    }

    @GetMapping("/user/self")
    public String getSelf(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            return "redirect:../login";
        }
        model.addAttribute("user", user);
        return "viewSelf";
    }

    //POST should include:
    //User Email - named "email"
    //User Password - temporarily named "password"
    //Password will be randomly generated in production
    //User First Name - named "firstname"
    //User Surname - named "lastname"
    //User Account Type - named "accesslevel"
    //Upon success a new user will be created in the db
    @PostMapping("/user/add")
    public String addUser(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        User requester = (User) session.getAttribute("user");
        if(requester == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:../login"; //how did we get here?
        }
        if(requester.getAccessLevel() != AccessLevel.ADMIN){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:../action-panel"; //how did we get here?
        }
        //only admin can add users
        User toAdd;
        //TODO: only one user per email
        if(userRepo.findByEmail(params.get("email")).isPresent()){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return "redirect:../action-panel";
        }
        //TODO: Email new users with random password
        String pwd = PasswordHelper.generatePassword();
        try {
            toAdd = new User(params.get("email"), pwd, params.get("firstname"),
                    params.get("lastname"), params.get("accesslevel"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
        try {
            EmailHelper.sendEmail(params.get("email"), "Welcome to SchedulEx", String.format(EmailHelper.NEW_USER_EMAIL, pwd) + EmailHelper.EMAIL_SIGN_OFF);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
        userRepo.save(toAdd);
        return "redirect:../action-panel";
    }

    //POST should include
    //User Email - named "email"
    //Email address should not be changed ever ! (used for user identification)
    //New Password - named "password"
    //New First Name - named "firstname"
    //New Surname - named "lastname"
    //New Access Level - named "accesslevel"
    //Form validation should be done on FE
    //Upon success "toEdit" will be updated in the db
    //Upon success redirect the user to wherever they need to go
    @PostMapping("/user/updateSelf")
    public String updateSelf(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        User requester = (User) session.getAttribute("user");
        if(requester == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:../login";
        }
        String email = params.get("email");
        if(!Objects.equals(requester.getEmail(), email)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:../login";
        }
        User toEdit = userRepo.findByEmail(email).orElse(null);
        if(toEdit == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "redirect:../login";
        }
        String pwd = params.get("password");
        if(pwd != null){
            try{
                toEdit.setPassword(pwd);
            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(e);
            }
        }
        toEdit.setFirstName(params.get("firstname"));
        toEdit.setLastName(params.get("lastname"));
        response.setStatus(HttpServletResponse.SC_OK);
        return "redirect:../action-panel";
    }

    //POST should include
    //User Email - named "email"
    //Email address should not  be changed ! (used for user identification)
    //New Password - named "password"
    //New First Name - named "firstname"
    //New Surname - named "lastname"
    //New Access Level - named "accesslevel"
    //Form validation should be done on FE
    //Upon success "toEdit" will be updated in the db
    //Upon success redirect the user to wherever they need to go
    @PostMapping("/user/update")
    public String updateUser(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response) {
        User requester = (User) session.getAttribute("user");
        if(requester == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:../login";
        }
        if(requester.getAccessLevel() != AccessLevel.ADMIN){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:../action-panel";
        }
        String email = params.get("email");
        User toEdit = userRepo.findByEmail(email).orElse(null);
        if(toEdit == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "redirect:../action-panel";
        }
        String pwd = params.get("password");
        if(!Objects.equals(pwd, "")){
            try{
                toEdit.setPassword(pwd);
            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(e);
            }
        }
        toEdit.setFirstName(params.get("firstname"));
        toEdit.setLastName(params.get("lastname"));
        toEdit.setAccessLevel(AccessLevel.parse(params.get("accesslevel")));
        userRepo.save(toEdit);
        response.setStatus(HttpServletResponse.SC_OK);
        return "redirect:../action-panel";
    }

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam Map<String, String> params, Model model, HttpSession session, HttpServletResponse response){
        User requester = (User) session.getAttribute("user");
        if(requester == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:../login";
        }
        String email = params.get("email");
        if(requester.getAccessLevel() != AccessLevel.ADMIN && !Objects.equals(requester.getEmail(), email)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:../login";
        }
        userRepo.delete(userRepo.findByEmail(email).get());
        response.setStatus(HttpServletResponse.SC_OK);
        return "redirect:../action-panel";
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
            session.removeAttribute("user");
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
        return "redirect:/user/login/newPwd";
    }

    @GetMapping("/user/login/newPwd")
    public String loginNewPwd(Model model){
        model.addAttribute("msg", "Password successfully updated.");
        return "login";
    }

    @GetMapping("/user/login/error")
    public String loginError(Model model){
        model.addAttribute("msg", "Invalid email or password");
        return "login";
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("action-panel")
    public String getActionPanel(Model model, HttpSession session) {
        return courseService.getActionPanel(model, session);
    }
}
