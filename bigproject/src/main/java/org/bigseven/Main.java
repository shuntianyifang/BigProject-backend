package org.bigseven;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shuntianyifang
 * &#064;date  2025/9/15
 */
@SpringBootApplication
@MapperScan("org.bigseven.mapper")
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }

}
