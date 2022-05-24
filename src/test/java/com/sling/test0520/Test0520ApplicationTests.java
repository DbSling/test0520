package com.sling.test0520;

import com.mysema.commons.lang.Assert;
import com.sling.test0520.domain.User;
import com.sling.test0520.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class Test0520ApplicationTests {

    UserRepository repository;

    @Test
    void contextLoads() throws Exception{

    }

}
