package com.sol.repo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.sol.entity.UserEntity;
import com.sol.exception.UserRepositoryException;
import com.sol.vo.UserVO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository("jpaRepo")
public class JPAUserRepository implements UserRepository{
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public void saveUser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UserVO> getUsers() {
		try {
			TypedQuery<UserEntity> query = em.createQuery("select u from UserEntity u", UserEntity.class); 
			List<UserEntity> userList = query.getResultList();
			
			List<UserVO> list  = userList.stream().map((entity) -> new UserVO(entity)).collect(Collectors.toList());			
			return list;
		}catch(Exception e) {
			throw new UserRepositoryException("Failed to fetch users", e);
		}
		
	}
}
