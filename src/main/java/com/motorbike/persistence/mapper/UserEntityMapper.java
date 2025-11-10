package com.motorbike.persistence.mapper;

import com.motorbike.business.entity.User;
import com.motorbike.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between Domain Entity and JPA Entity
 */
@Component
public class UserEntityMapper {
    
    /**
     * Convert from JPA Entity to Domain Entity
     * @param jpaEntity JPA entity from database
     * @return Domain entity
     */
    public User toDomain(UserJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        
        User user = new User();
        user.setId(jpaEntity.getId());
        user.setEmail(jpaEntity.getEmail());
        user.setUsername(jpaEntity.getUsername());
        user.setPassword(jpaEntity.getPassword());
        user.setPhoneNumber(jpaEntity.getPhoneNumber());
        user.setRole(jpaEntity.getRole());
        user.setActive(jpaEntity.isActive());
        user.setCreatedAt(jpaEntity.getCreatedAt());
        user.setUpdatedAt(jpaEntity.getUpdatedAt());
        user.setLastLoginAt(jpaEntity.getLastLoginAt());
        
        return user;
    }
    
    /**
     * Convert from Domain Entity to JPA Entity
     * @param domain Domain entity
     * @return JPA entity for database
     */
    public UserJpaEntity toJpaEntity(User domain) {
        if (domain == null) {
            return null;
        }
        
        UserJpaEntity jpaEntity = new UserJpaEntity();
        jpaEntity.setId(domain.getId());
        jpaEntity.setEmail(domain.getEmail());
        jpaEntity.setUsername(domain.getUsername());
        jpaEntity.setPassword(domain.getPassword());
        jpaEntity.setPhoneNumber(domain.getPhoneNumber());
        jpaEntity.setRole(domain.getRole());
        jpaEntity.setActive(domain.isActive());
        jpaEntity.setCreatedAt(domain.getCreatedAt());
        jpaEntity.setUpdatedAt(domain.getUpdatedAt());
        jpaEntity.setLastLoginAt(domain.getLastLoginAt());
        
        return jpaEntity;
    }
}
