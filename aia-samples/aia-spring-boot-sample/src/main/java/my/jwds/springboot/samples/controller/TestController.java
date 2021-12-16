package my.jwds.springboot.samples.controller;


import my.jwds.core.AiaApiScanner;
import my.jwds.springboot.samples.entity.User;
import my.jwds.springweb.data.AiaController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("aia")
public class TestController {

    @Resource
    private AiaController aiaController;

    @GetMapping(path = "/r1",headers = {"user"})
    public int r1(User user){
        return 1;
    }
    @PostMapping(path = "/r1")
    public int r1post(@ModelAttribute("user") User user,@ModelAttribute("user1")User user1){
        return 1;
    }

    @PostMapping(path = "/r2")
    public String r2(User[] user){
        new User();
        return "index";
    }


    @GetMapping(path = "/r3")
    public String r3(User user,User user1){
        return "index";
    }
}
