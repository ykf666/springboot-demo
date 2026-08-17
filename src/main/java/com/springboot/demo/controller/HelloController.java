package com.springboot.demo.controller;

import com.springboot.demo.config.PersonProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author yan.kefei
 * @date 2018/5/13 23:31
 */
@Controller
@RequestMapping("/hello")
@Tag(name = "Hello")
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
    @ResponseBody
    public String say2() {
        return personProperties.getName() + ";" + personProperties.getAge();
    }

    @GetMapping(value = "/say3/{id}")
    @Operation(summary = "say3")
    @ResponseBody
    public String say3(@PathVariable("id") String id) {
        return "id:" + id;
    }

    @GetMapping(value = "/say4")
    @Operation(summary = "say4")
    @ResponseBody
    public String say4(@RequestParam(value = "id", required = false, defaultValue = "888") String id) {
        return "id:" + id;
    }


}
