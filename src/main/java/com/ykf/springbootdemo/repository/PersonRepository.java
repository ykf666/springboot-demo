package com.ykf.springbootdemo.repository;

import com.ykf.springbootdemo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yan.kefei
 * @date 2018/6/18 16:13
 */
public interface PersonRepository extends JpaRepository<Person, Integer> {

    public List<Person> findByAge(Integer age);
}
