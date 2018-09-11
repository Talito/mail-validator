package com.usebouncer.verification.email;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class EmailVerification {

    private String email;
    private Status status;
    private Instant verifiedAt;

    enum Status {
        OK, INVALID_FORMAT, INVALID_DOMAIN, INVALID_EMAIL
    }
}
