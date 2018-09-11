package com.usebouncer.verification.email.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailVerificationDataRepository extends MongoRepository<EmailVerificationData, String> {
}
