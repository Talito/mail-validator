package com.usebouncer.verification.email.repository;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Builder
@Value
public class EmailVerificationData {

    private String email;
    private String status;
    private Instant verifiedAt;
}
