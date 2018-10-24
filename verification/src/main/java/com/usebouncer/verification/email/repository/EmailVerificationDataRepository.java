package com.usebouncer.verification.email.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationDataRepository extends MongoRepository<EmailVerificationData, String> {
}
