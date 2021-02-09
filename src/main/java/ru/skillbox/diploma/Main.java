package ru.skillbox.diploma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD

@SpringBootApplication
public class Main {
	public static void main(String[] args) {
=======
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class Main {
	public static void main(String[] args) throws Exception {
>>>>>>> dev
		SpringApplication.run(Main.class, args);
	}

}
