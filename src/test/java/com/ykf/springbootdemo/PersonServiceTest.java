package com.ykf.springbootdemo;

import com.ykf.springbootdemo.entity.Person;
import com.ykf.springbootdemo.service.PersonService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yan.kefei
 * @date 2018/7/6 0:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Test
    public void getOneTest(){
        Person person = personService.findById(2);
        Assert.assertEquals(new Integer(25), person.getAge());
    }
}
