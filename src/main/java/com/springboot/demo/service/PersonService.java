package com.springboot.demo.service;

import com.springboot.demo.entity.Person;
import com.springboot.demo.exception.ServiceException;
import com.springboot.demo.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author yan.kefei
 * @date 2018/7/4 22:47
 */
@Slf4j
@Component
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Transactional(rollbackFor = Exception.class)
    public void insertTwo() {
        PersonService personService = (PersonService) AopContext.currentProxy();
        Person p1 = new Person();
        p1.setName("1");
        p1.setAge(19);
        p1.setAddr("aaa");
        personService.insert(p1);

        Person p2 = new Person();
        p2.setName("2");
        p2.setAge(24);
        p2.setAddr("aaaa");
        personService.insertFail(p2);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            log.error("", e);
        }
//        throw new RuntimeException("插入两条数据异常");
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void insertFail(Person person) {
        log.info("插入person: {}", person.toString());
        personRepository.save(person);

        Person person1 = new Person();
        person1.setName("000");
        person1.setAge(30);
        person1.setAddr("xxx");
        log.info("插入person: {}", person1.toString());
        personRepository.save(person1);
        throw new RuntimeException("插入person失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(Person person) {
        log.info("插入person: {}", person.toString());
        personRepository.save(person);
    }

    public void getAge(Integer id) {
        Person person = personRepository.getById(id);
        if (person.getAge() < 10) {
            throw new ServiceException(100, "还没上小学吧");
        } else if (person.getAge() >= 10 && person.getAge() < 16) {
            throw new ServiceException(101, "在上初中吧");
        } else {
            // 略
        }
    }

    public Person findById(Integer id) {
        return personRepository.getOne(id);
    }

}
