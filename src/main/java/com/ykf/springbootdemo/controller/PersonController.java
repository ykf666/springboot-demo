package com.ykf.springbootdemo.controller;

import com.ykf.springbootdemo.entity.Person;
import com.ykf.springbootdemo.entity.Result;
import com.ykf.springbootdemo.repository.PersonRepository;
import com.ykf.springbootdemo.service.PersonService;
import com.ykf.springbootdemo.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yan.kefei
 * @date 2018/6/18 16:11
 */
@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @GetMapping(value = "/persons")
    public List<Person> listPerson() {
        return personRepository.findAll();
    }

    @PostMapping(value = "/persons")
    public Result<Person> add(@Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
        }
        return ResultUtil.success(personRepository.save(person));
    }

    @GetMapping(value = "/persons/{id}")
    public Person get(@PathVariable("id") Integer id) {
        return personRepository.getOne(id);
    }

    @PutMapping(value = "/persons/{id}")
    public Person put(@PathVariable("id") Integer id, @RequestParam("age") Integer age,
                      @RequestParam("name") String name) {
        Person person = new Person();
        person.setId(id);
        person.setAge(age);
        person.setName(name);
        return personRepository.save(person);
    }

    @DeleteMapping(value = "/persons/{id}")
    public void delete(@PathVariable("id") Integer id){
        Person person = new Person();
        person.setId(id);
        personRepository.delete(person);
    }

    @GetMapping(value = "/persons/insert2")
    public void insertTwo(){
        personService.insertTwo();
    }

    @GetMapping(value = "/persons/age/{age}")
    public List<Person> findByAge(@PathVariable("age") Integer age){
        return personRepository.findByAge(age);
    }

    @GetMapping(value = "/persons/getAge/{id}")
    public void getAge(@PathVariable Integer id) throws Exception {
        personService.getAge(id);
    }
}
