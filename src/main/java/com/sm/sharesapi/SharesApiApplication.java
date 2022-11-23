package com.sm.sharesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SharesApiApplication {

	public static void main(String[] args) {
		
		try
		{
			System.setProperty("spring.devtools.restart.enabled", "false");

		SpringApplication.run(SharesApiApplication.class, args);
		}
		
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
