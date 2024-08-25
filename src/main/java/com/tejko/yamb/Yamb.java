package com.tejko.yamb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class Yamb {

	public static void main(String[] args) {
		SpringApplication.run(Yamb.class, args);
	}

}
