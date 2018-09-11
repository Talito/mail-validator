package com.usebouncer.verification.email;

import com.usebouncer.verification.email.repository.EmailVerificationData;
import com.usebouncer.verification.email.repository.EmailVerificationDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmailVerificationService {

    private final EmailVerificationDataRepository repository;

    EmailVerification verify(String email) {
        // TODO implement email format verification
        // TODO implement mx dns records verification using dnsjava library
        //   (already in dependencies, missing -> INVALID_DOMAIN)
        EmailVerification.Status status = EmailVerification.Status.OK;
        Instant now = Instant.now();
        repository.insert(EmailVerificationData.builder()
                .email(email)
                .status(status.name())
                .verifiedAt(now)
                .build());
        return EmailVerification.builder()
                .email(email)
                .status(status)
                .verifiedAt(now)
                .build();
    }

    List<EmailVerification> history() {
        // TODO results should be returned in reverse order (newest first)
        return repository.findAll().stream()
                .map(v -> EmailVerification.builder()
                        .email(v.getEmail())
                        .status(EmailVerification.Status.valueOf(v.getStatus()))
                        .verifiedAt(v.getVerifiedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
