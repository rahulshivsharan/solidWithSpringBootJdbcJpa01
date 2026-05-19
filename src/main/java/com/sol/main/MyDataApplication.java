package com.sol.main;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import com.sol.service.UserService;
import com.sol.vo.UserVO;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sol"})
@EntityScan(basePackages = {"com.sol.entity"})
public class MyDataApplication implements CommandLineRunner{
	
	
	private final UserService service;
	
	public MyDataApplication(UserService service) {
		this.service = service;
	}

	public void loadUsers() {
		System.out.println("---- Fetcing uder data -----");
		List<UserVO> list = this.service.getUsers();
		list.stream().forEach(System.out::println);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MyDataApplication.class, args);		
	}

	@Override
	public void run(String... args) throws Exception {
		loadUsers();
	}

}
