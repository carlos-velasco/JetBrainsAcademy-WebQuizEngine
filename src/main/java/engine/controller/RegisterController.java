package engine.controller;

import engine.model.user.User;
import engine.service.user.QuizUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private QuizUserService userService;

    @PostMapping
    public User registerNewUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
