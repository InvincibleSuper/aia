package my.jwds.springboot.samples.controller;


import my.jwds.core.AiaApiScanner;
import my.jwds.springboot.samples.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("aia")
public class TestController {

    @GetMapping(path = "/r1",headers = {"user"})
    public int r1(User user){
        return 1;
    }
    @PostMapping(path = "/r1",headers = {"user"})
    public int r1post(@RequestBody User user,@RequestBody User user1){
        return 1;
    }

    @GetMapping(path = "/r2/*/{user}")
    public String r2(@PathVariable(value = "user") String user){
        return user;
    }


    @Resource
    private AiaApiScanner scanner;

    @GetMapping(path = "/r123")
    public void scanner(){
        scanner.startScanner();
    }
}
