package com.sling.test0520.service;

import com.core.epril.service.GenericService;
import com.sling.test0520.domain.User;
import com.sling.test0520.repository.UserRepository;
import com.sling.test0520.web.dto.Login;

public interface UserService extends GenericService<User, Long, UserRepository> {

    //입력값이 DB에 있는지 확인
    User userLogin(Login user);

    //회원가입시 실행
    String saveUser(User user);

    //비밀번호 변경페이지에서 사용
    User updUser(Login user);

    //마이페이지 들어갈때 사용할 기본 데이터
    User findById(String id);
}
