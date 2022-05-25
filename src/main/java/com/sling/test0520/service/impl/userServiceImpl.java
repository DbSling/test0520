package com.sling.test0520.service.impl;

import com.core.epril.service.GenericServiceImpl;
import com.sling.test0520.domain.User;
import com.sling.test0520.repository.UserRepository;
import com.sling.test0520.service.UserService;
import com.sling.test0520.web.dto.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class userServiceImpl extends GenericServiceImpl<User, Long, UserRepository> implements UserService{

    //로그인 할때
    @Override
    public User userLogin(Login user) {
        User userInfo = repository.findByEMail(user.getEMail());
        if(userInfo != null) {
            if (userInfo.getPassword().equals(user.getPassword())) return userInfo;
        }
        return null;
    }

    //회원가입 시
    @Override
    public String saveUser(User user){
        User saveUser;
        try {
            saveUser = repository.save(user);
        }catch (Exception e){
            return "중복됩니다.";
        }
        return "가입에 성공하셨습니다.";
    }

    //비밀번호 찾기
    @Override
    public User updUser(Login user){
        if(!StringUtils.hasText(user.getEMail())){
            return null;
        }
        User user2 = repository.findByEMail(user.getEMail());
        try{
            user2.setPassword(user.getPassword());
            update(user2);
        }catch (Exception e){
            return null;
        }
        return user2;
    }

    //마이페이지 진입시 기본 데이터 구축
    @Override
    public User findById(String id){
        if(!StringUtils.hasText(id))return null;

        return repository.findById(id);
    }
}
