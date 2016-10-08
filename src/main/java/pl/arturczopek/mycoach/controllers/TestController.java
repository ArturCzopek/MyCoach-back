package pl.arturczopek.mycoach.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author arturczopek
 * @Date 10/8/16
 */

@RestController
public class TestController {

    @RequestMapping("/")
    public String hi() {
        return "Hello, my coach works";
    }
}
