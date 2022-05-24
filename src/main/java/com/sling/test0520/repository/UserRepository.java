package com.sling.test0520.repository;

import com.core.epril.repository.BaseRepository;
import com.sling.test0520.domain.User;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends BaseRepository<User,Long>{
    @Query("select u from User u where u.eMail = ?1")
    User findByEMail(String eMail);

}
