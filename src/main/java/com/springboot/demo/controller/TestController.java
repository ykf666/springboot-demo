package com.springboot.demo.controller;

//import com.aist.uam.userorg.remote.UamUserOrgService;
//import com.aist.uam.userorg.remote.vo.UserVO;
//import com.alibaba.csp.sentinel.annotation.SentinelResource;
//import com.alibaba.fastjson.JSONObject;

import com.springboot.demo.annotation.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yankefei on 2022/8/22.
 */
@RestController
@Slf4j
@RequestMapping("/test")
@CommonResp
public class TestController {

    @RequestMapping(value = "/sentinel", method = RequestMethod.GET)
//    @SentinelResource()
    public String sentinelTest() {
        return "success";
    }

    @GetMapping("/testVoid")
    public void testVoid() {
        log.info("test void");
    }
}
