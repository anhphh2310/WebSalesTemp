package tma.datraining;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;

import tma.datraining.converter.DateToTimestampConverter;

@SpringBootApplication
public class WebSalesApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSalesApplication.class, args);
	}

	
}
