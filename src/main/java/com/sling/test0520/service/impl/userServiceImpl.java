package com.sling.test0520.service.impl;

import com.core.epril.service.GenericServiceImpl;
import com.sling.test0520.domain.User;
import com.sling.test0520.repository.UserRepository;
import com.sling.test0520.service.UserService;
import com.sling.test0520.web.dto.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class userServiceImpl extends GenericServiceImpl<User, Long, UserRepository> implements UserService{

    //로그인 할때
    @Override
    public User userLogin(Login user) {
        User userInfo = repository.findByEMail(user.getEmail());
        if(ObjectUtils.isEmpty(userInfo)) return null;
        if (!userInfo.getPassword().equals(user.getPassword())) return null;
        return userInfo;
    }

    //회원가입 시, 정보 변경시
    @Override
    public String saveUser(User user){

        if(!ObjectUtils.isEmpty(user.getId())) {
            User users = repository.findAllById(user.getId());
            user.setCreatedBy(users.getCreatedBy());
            user.setCreated(users.getCreated());
            user.setUpdatedBy(users);
            user.setUpdated(LocalDateTime.now());
            if (!StringUtils.hasText(user.getPassword())){
                user.setPassword(users.getPassword());
            }
        }
        try {
            repository.save(user);
        }catch (Exception e){
            return "중복됩니다.";
        }
        return "성공하셨습니다.";
    }

    //비밀번호 찾기
    @Override
    public User updPassword(Login user){
        if(!StringUtils.hasText(user.getEmail())){
            return null;
        }
        User user2 = repository.findByEMail(user.getEmail());
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
    public User findMyPage(Long id){
        if(ObjectUtils.isEmpty(id))return null;

        return repository.findAllById(id);
    }

    @Override
    public String findMyEmail(String name, String phone) {
        return repository.findEmailByNameAndPhoneNum(name, phone);
    }
}
