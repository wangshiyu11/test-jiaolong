package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/5 11:43
 */
@SpringBootApplication
@RestController
public class TestGateway {
    public static void main(String[] args) {
        SpringApplication.run(TestGateway.class,args);
    }

    @RequestMapping("/serverhealth")
    public String serverhealth(){
        System.out.println("---ok----");
        return "ok";
    }
}
