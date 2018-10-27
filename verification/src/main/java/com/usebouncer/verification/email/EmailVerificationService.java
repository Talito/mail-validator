package com.usebouncer.verification.email;

import com.usebouncer.verification.email.repository.EmailVerificationData;
import com.usebouncer.verification.email.repository.EmailVerificationDataRepository;
import com.usebouncer.verification.email.verificators.EmailVerificator;
import io.vavr.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
public class EmailVerificationService {
    private final EmailVerificationDataRepository repository;
    private final EmailVerificator verificator;

    @Autowired
    public EmailVerificationService(final EmailVerificationDataRepository repository,
                                    final EmailVerificator emailVerificator) {
        this.repository = repository;
        this.verificator = emailVerificator;
    }

    EmailVerification verify(final String email) {
        return ofNullable(email)
                .map(verificator::verifyEmail)
                .map(emailStatus ->
                        Tuple.of(emailStatus,
                                saveEmailVerificationToDatabase(email, emailStatus.name(), Instant.now())))
                .map(statusAndVerificationDto ->
                        mapToEmailVerification(statusAndVerificationDto._1(), statusAndVerificationDto._2()))
                .orElse(null);
    }

    List<EmailVerification> history() {
        return repository.findAll(new Sort(Sort.Direction.DESC, "verifiedAt")).stream()
                .map(v -> EmailVerification.builder()
                        .email(v.getEmail())
                        .status(EmailVerification.Status.valueOf(v.getStatus()))
                        .verifiedAt(v.getVerifiedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private EmailVerificationData saveEmailVerificationToDatabase(final String email,
                                                                  final String status,
                                                                  final Instant verifiedTime) {
        return repository.insert(EmailVerificationData.builder()
                .email(email)
                .status(status)
                .verifiedAt(verifiedTime)
                .build());
    }

    private EmailVerification mapToEmailVerification(final EmailVerification.Status status,
                                                     final EmailVerificationData emailVerificationData) {
        return EmailVerification.builder()
                .email(emailVerificationData.getEmail())
                .status(status)
                .verifiedAt(emailVerificationData.getVerifiedAt())
                .build();
    }
}
