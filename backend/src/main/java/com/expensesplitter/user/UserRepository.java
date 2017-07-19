package com.expensesplitter.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

    List<User> findAllByOrderByUsernameAsc();

    @Query("select u from User u left join fetch u.roles r where u.username=:username and u.active = 1")
    public Optional<User> findByUsername(@Param("username") String username);

}