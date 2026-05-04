package com.pagoEnCombo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PagoEnComboApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagoEnComboApplication.class, args);
	}

}
