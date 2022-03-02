package cn.lyk.smartled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SmartLedApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartLedApplication.class, args);
	}

}
