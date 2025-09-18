package org.bigseven;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
@MapperScan("org.bigseven.mapper")
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }

    /// 关闭spring security方便测试,注释掉就是不关闭
    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
        ).csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    */

}
