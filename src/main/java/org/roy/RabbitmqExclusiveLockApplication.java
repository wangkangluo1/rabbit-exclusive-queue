package org.roy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqExclusiveLockApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqExclusiveLockApplication.class, args);
	}
}
