package com.expensesplitter.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends CrudRepository<UserRole, String> {
    
    @Query("select ur from UserRole ur where ur.id.userId=:userId")
    public List<UserRole> findByUserId(@Param("userId") String userId);
}