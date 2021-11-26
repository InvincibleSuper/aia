package my.jwds.springboot.samples.controller;


import my.jwds.core.AiaApiScanner;
import my.jwds.springboot.samples.entity.User;
import my.jwds.springweb.data.AiaController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("aia")
public class TestController {

    @Resource
    private AiaController aiaController;

    @GetMapping(path = "/r1",headers = {"user"})
    public int r1(User user){
        return 1;
    }
    @PostMapping(path = "/r1",headers = {"user"})
    public int r1post(@RequestBody User user,@RequestBody User user1){
        return 1;
    }

    @GetMapping(path = "/r2")
    public String r2(){
        return "index";
    }



}
