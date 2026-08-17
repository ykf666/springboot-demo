package com.springboot.demo.controller;

//import com.aist.uam.userorg.remote.UamUserOrgService;
//import com.aist.uam.userorg.remote.vo.UserVO;
//import com.alibaba.csp.sentinel.annotation.SentinelResource;
//import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yankefei on 2022/8/22.
 */
@Controller
@Slf4j
@RequestMapping("/nh")
public class TestController {

    @RequestMapping(value = "/sentinel", method = RequestMethod.GET)
    @ResponseBody
//    @SentinelResource()
    public String sentinelTest() {
        return "success";
    }

}
