package com.usebouncer.verification.email.verificators;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.ofNullable;

public class EmailFormatVerificator {

    private static final Pattern VALID_EMAIL_PATTERN =
            Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public static boolean isInvalid(final String email) {
        return ofNullable(email)
                .filter(e -> !StringUtils.isEmpty(e))
                .map(EmailFormatVerificator::isValid)
                .orElse(true);
    }

    private static boolean isValid(final String email) {
        Matcher matcher= VALID_EMAIL_PATTERN.matcher(email);
        return !matcher.matches();
    }

}
