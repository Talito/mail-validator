package com.usebouncer.verification.email.verificators;

import com.usebouncer.verification.email.EmailVerification;
import com.usebouncer.verification.email.externals.MXRecordLoader;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.smtp.SMTPClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

import java.io.*;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vavr.API.*;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class EmailVerificator {

    private static final Pattern VALID_EMAIL_PATTERN =
            Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private SMTPClient smtpClient;

    public EmailVerificator() {
        smtpClient = new SMTPClient();
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
        Optional<Record[]> records = MXRecordLoader.loadResults(domain);
        boolean result = false;
        final Optional<MXRecord> highestPriorityRecord = records.map(Stream::of).orElse(Stream.empty())
                .map(x -> (MXRecord) x)
                .sorted()
                .findFirst();
        try {
            smtpClient.setDefaultTimeout(5);
            smtpClient.setConnectTimeout(10);
            smtpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            // TODO: research about library
            smtpClient.connect(highestPriorityRecord.get().getAdditionalName().toString());
            result = smtpClient.verify(email);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                smtpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
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
