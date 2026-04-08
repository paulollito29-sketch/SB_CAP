package com.example.iam.repository;

import com.example.iam.entity.UserEntity;

public interface UserRepository extends GenericRepository<UserEntity, Integer> {

    UserEntity findOneByUsername(String username);


}
