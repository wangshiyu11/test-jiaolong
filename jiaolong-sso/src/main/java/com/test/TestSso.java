package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/5 14:14
 */
@SpringBootApplication
@EnableJpaAuditing
@RestController
@EntityScan(basePackages = "com.test.**")
public class TestSso {
    public static void main(String[] args) {
        SpringApplication.run(TestSso.class,args);
    }

    @RequestMapping("/health")
    public String health(){
        System.out.println("---ok----");
        return "ok";
    }
}
