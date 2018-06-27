package com.ykf.springbootdemo.controller;

import com.ykf.springbootdemo.PersonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author yan.kefei
 * @date 2018/5/13 23:31
 */
//@RestController
@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private PersonProperties personProperties;

    @RequestMapping(value = "/say", method = RequestMethod.GET)
    public String say() {
//        return "Hello Spring Boot";
        return "index";
    }

    @GetMapping(value = "/say2")
    @ResponseBody
    public String say2() {
        return personProperties.getName() + ";" + personProperties.getAge();
    }

    @GetMapping(value = "/say3/{id}")
    @ResponseBody
    public String say3(@PathVariable("id") String id) {
        return "id:" + id;
    }

    @GetMapping(value = "/say4")
    @ResponseBody
    public String say4(@RequestParam(value = "id", required = false, defaultValue = "888") String id) {
        return "id:" + id;
    }


}
