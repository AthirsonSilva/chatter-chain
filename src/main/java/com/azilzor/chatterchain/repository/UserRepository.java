package com.azilzor.chatterchain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.azilzor.chatterchain.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
