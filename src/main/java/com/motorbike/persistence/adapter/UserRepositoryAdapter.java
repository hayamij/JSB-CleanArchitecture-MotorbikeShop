package com.motorbike.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.motorbike.business.entity.User;
import com.motorbike.business.repository.UserRepository;
import com.motorbike.persistence.entity.UserJpaEntity;
import com.motorbike.persistence.mapper.UserEntityMapper;
import com.motorbike.persistence.repository.UserJpaRepository;

/**
 * Adapter implementation of UserRepository
 * Bridges the domain repository interface with Spring Data JPA
 */
@Component
public class UserRepositoryAdapter implements UserRepository {
    
    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;
    
    public UserRepositoryAdapter(UserJpaRepository jpaRepository, 
                                UserEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public User save(User user) {
        UserJpaEntity jpaEntity = mapper.toJpaEntity(user);
        UserJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean insert(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean update(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<User> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
}
