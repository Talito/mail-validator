package com.usebouncer.verification.email.verificators;

import com.usebouncer.verification.email.EmailVerification;
import com.usebouncer.verification.email.externals.MXRecordLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.smtp.SMTPClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.xbill.DNS.MXRecord;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.vavr.API.*;
import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class EmailVerificator {

    private static final List<Integer> INVALID_EMAIL_SMTP_RESPONSE_CODES = List(501, 510, 511, 512, 513, 551, 553).asJava();
    private static final Pattern VALID_EMAIL_PATTERN =
            Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private static final int TEN_SECONDS = 10000;
    private SMTPClient smtpClient;

    public EmailVerificator() {
        smtpClient = new SMTPClient();
        smtpClient.setDefaultTimeout(TEN_SECONDS);
    }

    public EmailVerification.Status verifyEmail(final String email) {
        if (hasInvalidFormat(email)) {
            return EmailVerification.Status.INVALID_FORMAT;
        }
        return Optional.of(email)
                .map(x -> Match(x).of(
                        Case($(this::emailDoesNotExistAtDomain), () -> EmailVerification.Status.INVALID_EMAIL),
                        Case($(this::hasInvalidDomain), () -> EmailVerification.Status.INVALID_DOMAIN),
                        Case($(), () -> EmailVerification.Status.OK)
                )).orElse(null);

    }

    private boolean emailDoesNotExistAtDomain(final String email) {
        final String domain = extractDomain(email);
        final List<MXRecord> mxRecords = MXRecordLoader.loadResults(domain).stream()
                .sorted(Comparator.comparing(MXRecord::getPriority))
                .collect(Collectors.toList());
        for (MXRecord record: mxRecords) {
            try {
                smtpClient.connect(record.getAdditionalName().toString());
                final int exists = smtpClient.vrfy(email);
                if (INVALID_EMAIL_SMTP_RESPONSE_CODES.contains(exists)) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    smtpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
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
