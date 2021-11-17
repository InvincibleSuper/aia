package my.jwds.springboot.samples.controller;


import my.jwds.springboot.samples.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/r1")
    public int r1(User user){
        return 1;
    }
}
