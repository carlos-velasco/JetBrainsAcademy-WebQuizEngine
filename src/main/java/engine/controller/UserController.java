package engine.controller;

import engine.model.dto.UserDto;
import engine.model.mapper.UserMapper;
import engine.model.user.User;
import engine.service.user.QuizUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class UserController {

    private final QuizUserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserDto registerNewUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(userMapper.toUser(userDto));
        return userMapper.toUserDto(user);
    }
}
