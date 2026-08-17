package com.springboot.demo.controller;

import com.springboot.demo.entity.Person;
import com.springboot.demo.entity.Result;
import com.springboot.demo.repository.PersonRepository;
import com.springboot.demo.service.PersonService;
import com.springboot.demo.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
@Tag(name = "人员管理")
@Slf4j
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @GetMapping(value = "/persons")
    @Operation(summary = "查询所有用户")
    public List<Person> listPerson() {
        return personRepository.findAll();
    }

    @PostMapping(value = "/persons")
    @Operation(summary = "新增用户")
    public Result<Person> add(@Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
        }
        return ResultUtil.success(personRepository.save(person));
    }

    @GetMapping(value = "/persons/{id}")
    @Operation(summary = "根据用户id查询用户")
    public Person get(@PathVariable("id") Integer id) {
        return personRepository.getById(id);
    }

    @PutMapping(value = "/persons/{id}")
    @Operation(summary = "根据id修改用户")
    public Person put(@PathVariable("id") Integer id, @RequestParam("age") Integer age,
                      @RequestParam("name") String name) {
        Person person = new Person();
        person.setId(id);
        person.setAge(age);
        person.setName(name);
        return personRepository.save(person);
    }

    @DeleteMapping(value = "/persons/{id}")
    @Operation(summary = "根据id删除用户")
    public void delete(@PathVariable("id") Integer id){
        Person person = new Person();
        person.setId(id);
        personRepository.delete(person);
    }

    @GetMapping(value = "/persons/insert2")
    @Operation(summary = "新增多个用户（事务操作）")
    public void insertTwo(){
        try {
            personService.insertTwo();
        } catch (Exception e) {
            log.warn("接收到事务方法异常", e);
        }
    }

    @GetMapping(value = "/persons/age/{age}")
    @Operation(summary = "根据年龄查询用户")
    public List<Person> findByAge(@PathVariable("age") Integer age){
        return personRepository.findByAge(age);
    }

    @GetMapping(value = "/persons/getAge/{id}")
    @Operation(summary = "根据id查询用户（验证统一异常处理）)")
    public void getAge(@PathVariable Integer id) {
        personService.getAge(id);
    }

    @PostMapping(value = "/persons/add2")
    @Operation(summary = "验证异步调用事务问题")
    public void addPerson2() {
        personService.insertTwo();
    }
}
