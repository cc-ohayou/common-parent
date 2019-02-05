package com.ccspace.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ccspace.*"})
public class CommonServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonServerApplication.class, args);
	}


	/*	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 注意这里要指向原先用main方法执行的Application启动类 EtfTaskApplication
		return builder.sources(CommonServerApplication.class);
	}*/
}

