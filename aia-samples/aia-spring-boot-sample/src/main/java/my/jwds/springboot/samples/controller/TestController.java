package my.jwds.springboot.samples.controller;


import my.jwds.springboot.samples.entity.User;
import my.jwds.springweb.data.AiaController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 测试接口
 * aia测试
 */
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
    @ResponseBody
    public User r2(@RequestBody User user){

        return  user;
    }

    /**
     * 请求3
     * 作为测试使用
     * @param user
     * @param user1
     * @return
     */
    @GetMapping(path = "/r3")
    public String r3(User user,User user1){
        return "index";
    }

    /**
     * 请求4
     * 作为测试使用
     * @param user 用户名
     * @return 用户信息
     */
    @GetMapping(path = "/r4/{user}")
    @ResponseBody
    public String r4(@PathVariable("user") String user){
        return user;
    }


    /**
     * 文件上传
     * 作为测试使用
     * @return 用户信息
     */
    @PostMapping(path = "/fileUp")
    @ResponseBody
    public Object fileUp(MultipartFile file){
        return file.getName();
    }
}
