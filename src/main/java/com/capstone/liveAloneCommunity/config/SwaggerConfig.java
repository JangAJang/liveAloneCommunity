package com.capstone.liveAloneCommunity.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title="Live Alone Community",
                description = "2023년 1학기 캡스톤 프로젝트 - 자취생들의 커뮤니티 L.A.N입니다.",
                version = "v0.0.1",
                license = @License(
                        name = "Apache License Version 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"),
                contact = @Contact(
                        name = "janghee",
                        email = "janghee5395@gmail.com"
                )
        ),
        tags = {
                @Tag(name = "Auth API", description = "회원 가입, 로그인, 토큰 재발행"),
                @Tag(name = "Member API", description = "회원 기능"),
                @Tag(name = "Post API", description = "게시물 기능")
        },
        security = {
                @SecurityRequirement(name = "Authorization")
        }
)
@SecuritySchemes({
        @SecurityScheme(type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, paramName = "Authorization")
})
@Import(BeanValidatorPluginsConfiguration.class)
@Configuration
// http://localhost:8080/swagger-ui/index.html
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("project")
                .pathsToMatch("/api/**")
                .build();
    }
}

