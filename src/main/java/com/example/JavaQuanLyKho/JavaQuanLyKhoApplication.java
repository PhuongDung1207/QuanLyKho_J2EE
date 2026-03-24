package com.example.JavaQuanLyKho;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

@SpringBootApplication
public class JavaQuanLyKhoApplication {

	public static void main(String[] args) {
		// Load .env variables into System properties
		try {
			Dotenv dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.load();
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		} catch (Exception e) {
			System.err.println("Warning: Could not load .env file: " + e.getMessage());
		}

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(JavaQuanLyKhoApplication.class, args);
	}

}
