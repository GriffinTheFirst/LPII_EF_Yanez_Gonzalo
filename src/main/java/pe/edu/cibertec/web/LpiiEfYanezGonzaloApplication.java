package pe.edu.cibertec.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"pe.edu.cibertec.thymeleaf.controller","pe.edu.cibertec.web.service"})
@PropertySource("classpath:application.properties")

public class LpiiEfYanezGonzaloApplication {

	public static void main(String[] args) {
		SpringApplication.run(LpiiEfYanezGonzaloApplication.class, args);
	}

}
