package com.github.aia.springboot.samples.controller;


import com.github.aia.springboot.samples.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用模板链测试
 */
@RestController
@RequestMapping("chain")
public class ChainTestController {



    @RequestMapping(path = "/r1")
    public Object r1(){
        Map<String,Object> map = new HashMap<>();
        map.put("user",new User("123456","user",null));
        map.put("arr", Arrays.asList(1,2,3));
        map.put("data","test");
        return map;
    }

    @RequestMapping(path = "/r2")
    public Object r2(@RequestBody Map map){
        if (map.isEmpty())return r1();
        return map;
    }



}
