package com.ykf.springbootdemo.service;

import com.ykf.springbootdemo.entity.Person;
import com.ykf.springbootdemo.exception.PersonException;
import com.ykf.springbootdemo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yan.kefei
 * @date 2018/7/4 22:47
 */
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Transactional
    public void insertTwo() {
        Person p1 = new Person();
        p1.setName("1");
        p1.setAge(19);
        p1.setAddr("aaa");
        personRepository.save(p1);

        Person p2 = new Person();
        p2.setName("2");
        p2.setAge(24);
        p2.setAddr("aaaa");
        personRepository.save(p2);
    }

    public void getAge(Integer id) throws Exception {
        Person person = personRepository.getOne(id);
        if (person.getAge() < 10) {
            throw new PersonException(100, "还没上小学吧");
        } else if (person.getAge() >= 10 && person.getAge() < 16) {
            throw new PersonException(101, "在上初中吧");
        } else {
            // 略
        }
    }

    public Person findById(Integer id) {
       return personRepository.getOne(id);
    }

}
