package com.sling.test0520.service;

import com.core.epril.service.GenericService;
import com.sling.test0520.domain.User;
import com.sling.test0520.repository.UserRepository;
import com.sling.test0520.web.dto.Login;

public interface UserService extends GenericService<User, Long, UserRepository> {
    User userLogin(Login user);

    String saveUser(User user);

    User updUser(User user);
}
