package com.sol.repo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sol.exception.UserRepositoryException;
import com.sol.mapper.UserRowMapper;
import com.sol.vo.UserVO;

@Repository
@Qualifier("jdbcRepo")
public class JDBCUserRepository implements UserRepository{
	
	private final JdbcTemplate jdbcTemplate;
	
	public JDBCUserRepository(JdbcTemplate jdbcTemplate) {		
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	@Override
	public void saveUser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UserVO> getUsers() {
		Connection con = null;
		try {
			List<UserVO> userList = new ArrayList<UserVO>();
			String sql = "select id, username, password from userstbl";
			userList = jdbcTemplate.query(sql, new UserRowMapper());
			
			return userList;
		} catch(Exception e) {
			throw new UserRepositoryException("Failed to fetch users", e);
		}	
		
		
	}

}
