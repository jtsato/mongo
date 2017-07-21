package io.github.jtsato;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Execute {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Execute.class, args);
    }
	
}
