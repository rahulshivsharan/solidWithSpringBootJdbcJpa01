package com.sol.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sol.repo.UserRepository;
import com.sol.vo.UserVO;

@Service
public class UserService {
		
	private final UserRepository repo;
	
	
	public UserService(@Qualifier("jdbcRepo") UserRepository repo) {
		this.repo = repo;
	}
	
	@Transactional
	public List<UserVO> getUsers(){
		return repo.getUsers();
	}
}
