package com.jl.db.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String HEADER_TOKEN = "Authorization";

    @Bean
    public Docket createRestApi() {

        Predicate<RequestHandler> selector1 = RequestHandlerSelectors.basePackage("com.jl.openapi.controller");
//        ParameterBuilder ticketPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<Parameter>();
//        ticketPar.name(HEADER_TOKEN).description("访问TOKEN")
//                .modelRef(new ModelRef("string")).parameterType("header")
//                .required(false).build(); //header中的ticket参数非必填，传空也可以
//        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                //.globalOperationParameters(pars)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(Predicates.or(selector1))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("金龙科技体育API服务V1.0版本--基于RESTful风格API接口文档")
                //描述
                .description("金龙科技体育API服务接口服务（API）V1.0版本")
                //创建人
                .contact(new Contact("Finlay", "", ""))
                //版本号
                .version("1.0")
                .build();
    }

}
