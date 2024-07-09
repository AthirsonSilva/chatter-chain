package com.azilzor.chatterchain.repository

import com.azilzor.chatterchain.entity.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String>
