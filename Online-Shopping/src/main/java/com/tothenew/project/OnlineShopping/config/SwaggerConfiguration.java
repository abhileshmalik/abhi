package com.tothenew.project.OnlineShopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {


    public static final Contact DEFAULT_CONTACT = new Contact(
            "Abhilesh Malik", "http://www.procart.com", "abhilesh.malik@tothenew.com");

    public static final ApiInfo DEFAULT = new ApiInfo(
            "Pro Cart", "It gives description about all APIs ", "1.0",
            "urn:tos", DEFAULT_CONTACT,
            "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",

            new ArrayList<VendorExtension>());



    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.tothenew.project.OnlineShopping.controller"))
                .paths(PathSelectors.regex("/.*"))
                .build().apiInfo(DEFAULT);
    }

/*    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(DEFAULT);
    }*/

}