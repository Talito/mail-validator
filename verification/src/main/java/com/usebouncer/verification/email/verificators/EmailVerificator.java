package com.usebouncer.verification.email.verificators;

import com.usebouncer.verification.email.externals.MXRecordLoader;
import com.usebouncer.verification.email.model.EmailVerification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.xbill.DNS.MXRecord;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.usebouncer.verification.email.externals.SMTPLowLevelClient.smtpServersHaveEmail;
import static io.vavr.API.*;
import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class EmailVerificator {

    private static final Pattern VALID_EMAIL_PATTERN =
            Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public EmailVerification.Status verifyEmail(final String email) {
        if (hasInvalidFormat(email)) {
            return EmailVerification.Status.INVALID_FORMAT;
        }
        return Optional.of(email)
                .map(x -> Match(x).of(
                        Case($(this::hasInvalidDomain), () -> EmailVerification.Status.INVALID_DOMAIN),
                        Case($(this::emailDoesNotExistAtDomain), () -> EmailVerification.Status.INVALID_EMAIL),
                        Case($(), () -> EmailVerification.Status.OK)
                )).orElse(null);

    }

    private boolean emailDoesNotExistAtDomain(final String email) {
        final String domain = extractDomain(email);
        final List<MXRecord> mxRecords = MXRecordLoader.loadResults(domain).stream()
                .sorted(Comparator.comparing(MXRecord::getPriority))
                .collect(Collectors.toList());
        return !smtpServersHaveEmail(mxRecords, domain, email);
    }

    private boolean hasInvalidDomain(final String email) {
        final String domain = extractDomain(email);
        return !MXRecordLoader.existMXRecords(domain);
    }

    private static String extractDomain(final String email) {
        return email.substring(email.indexOf("@") + 1);
    }

    private static boolean hasInvalidFormat(final String email) {
        return ofNullable(email)
                .filter(e -> !StringUtils.isEmpty(e))
                .map(EmailVerificator::isValid)
                .orElse(true);
    }

    private static boolean isValid(final String email) {
        Matcher matcher = VALID_EMAIL_PATTERN.matcher(email);
        return !matcher.matches();
    }
}
