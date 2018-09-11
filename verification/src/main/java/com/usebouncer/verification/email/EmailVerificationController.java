package com.usebouncer.verification.email;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @GetMapping("/verify")
    public EmailVerification verify(@RequestParam("email") String email) {
        return emailVerificationService.verify(email);
    }

    @GetMapping("/verify/history")
    public List<EmailVerification> verify() {
        return emailVerificationService.history();
    }
}
