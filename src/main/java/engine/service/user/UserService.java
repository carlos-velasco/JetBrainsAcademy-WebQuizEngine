package engine.service.user;

import engine.model.user.User;

import javax.validation.Valid;

public interface UserService {
    User createUser(@Valid User user);
}
