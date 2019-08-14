package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/5 14:09
 */
@SpringBootApplication
@RestController
@EntityScan(basePackages = "com.test.**")
public class TestManager {
    public static void main(String[] args) {
        SpringApplication.run(TestManager.class,args);
    }

    @RequestMapping("/serverhealth")
    public String serverhealth(){
        System.out.println("---ok---");
        return "ok";
    }
}
