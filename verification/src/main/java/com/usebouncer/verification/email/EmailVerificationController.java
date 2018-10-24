package com.usebouncer.verification.email;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/email")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @GetMapping("/verify")
    public EmailVerification verify(@RequestParam("email") String email) {
        return ofNullable(email)
                .filter(e -> !StringUtils.isEmpty(e))
                .map(emailVerificationService::verify)
                .orElse(null);
    }

    @GetMapping("/verify/history")
    public List<EmailVerification> verify() {
        return emailVerificationService.history();
    }
}
