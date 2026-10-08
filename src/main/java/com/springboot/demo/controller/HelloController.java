package com.springboot.demo.controller;

import com.springboot.demo.annotation.CommonResp;
import com.springboot.demo.config.PersonProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yan.kefei
 * @date 2018/5/13 23:31
 */
@RestController
@RequestMapping("/hello")
@Tag(name = "Hello")
@CommonResp
public class HelloController {

    @Autowired
    private PersonProperties personProperties;

    @RequestMapping(value = "/say", method = RequestMethod.GET)
    @Operation(summary = "say")
    public String say() {
//        return "Hello Spring Boot";
        return "index";
    }

    @GetMapping(value = "/say2")
    @Operation(summary = "say2")
    public String say2() {
        return personProperties.getName() + ";" + personProperties.getAge();
    }

    @GetMapping(value = "/say3/{id}")
    @Operation(summary = "say3")
    public String say3(@PathVariable("id") String id) {
        return "id:" + id;
    }

    @GetMapping(value = "/say4")
    @Operation(summary = "say4")
    public String say4(@RequestParam(value = "id", required = false, defaultValue = "888") String id) {
        return "id:" + id;
    }

}
