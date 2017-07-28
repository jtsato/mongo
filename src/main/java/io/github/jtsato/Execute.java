package io.github.jtsato;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Jorge Takeshi Sato on 20/07/2017.
 */

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class })
public class Execute {

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(Execute.class, args);
	}

}
