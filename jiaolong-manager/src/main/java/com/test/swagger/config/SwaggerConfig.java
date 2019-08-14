package com.test.swagger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Autowired
    Environment environment;

    @Bean
    public Docket getDocket(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.select().apis(RequestHandlerSelectors.basePackage("com.test.controller"))
        .build();
        ;//根据名称匹配的接口展示

        //配置全局的参数start
        List<Parameter> parameterList=new ArrayList<>();
        Parameter parameter=new ParameterBuilder()
                .name("token")
                .parameterType("header")
                .description("请求令牌")
                .modelRef(new ModelRef("string"))
                .build();
        parameterList.add(parameter);
        docket.globalOperationParameters(parameterList);

        //配置全局的参数end

        docket.apiInfo(setApiInfo());
        return docket;
    }

    public ApiInfo setApiInfo(){

        Contact contact=new Contact("王诗雨","http://www.baidu.com","5555@qq.com");
        ApiInfo apiInfo=new ApiInfo(
                "这是文档的标题",
                "这是文档信息的描述信息",
                "v-1.0",
                "http://www.baidu.com",
                contact,"监听信息",
                "http://www.baidu.com",
                new ArrayList<VendorExtension>()
        );

        return apiInfo;
    }

}
