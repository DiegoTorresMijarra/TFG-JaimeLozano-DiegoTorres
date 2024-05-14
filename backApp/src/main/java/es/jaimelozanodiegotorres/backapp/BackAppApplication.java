package es.jaimelozanodiegotorres.backapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackAppApplication.class, args);
	}

}
