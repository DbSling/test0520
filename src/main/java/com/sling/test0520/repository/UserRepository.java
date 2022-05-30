package com.sling.test0520.repository;

import com.core.epril.repository.BaseRepository;
import com.sling.test0520.domain.User;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends BaseRepository<User,Long>{

    @Query("select u from User u where u.email = ?1")
    User findByEMail(String email);

    User findAllById(Long id);

    @Query("select u.email from User u where u.name = ?1 and u.phoneNum =?2")
    String findEmailByNameAndPhoneNum(String name, String phone);
}
