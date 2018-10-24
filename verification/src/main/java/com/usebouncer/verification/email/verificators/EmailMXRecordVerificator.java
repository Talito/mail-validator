package com.usebouncer.verification.email.verificators;

import com.usebouncer.verification.email.EmailVerification;
import com.usebouncer.verification.email.externals.MXRecordLoader;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;

import java.util.Arrays;
import java.util.Optional;

import static io.vavr.API.*;

@Slf4j
public class EmailMXRecordVerificator {

    public static EmailVerification.Status verifyEmail(final String email) {
        if (EmailFormatVerificator.isInvalid(email)) {
            return EmailVerification.Status.INVALID_FORMAT;
        }
        return Optional.of(email)
                .map(EmailMXRecordVerificator::extractDomain)
                .map(MXRecordLoader::loadResults)
                .map(x -> Match(x).of(
                       Case($(EmailMXRecordVerificator::hasNoMXRecord), () -> EmailVerification.Status.INVALID_EMAIL),
                       Case($(EmailMXRecordVerificator::hasInvalidDomain), () -> EmailVerification.Status.INVALID_DOMAIN),
                       Case($(), () -> EmailVerification.Status.OK)
                )).orElse(null);

    }


    private static boolean hasNoMXRecord(final Tuple2<Record[], Integer> recordsAndLookupStatus) {
        final Record[] records = recordsAndLookupStatus._1();
        if (records != null && records.length == 0) {
            log.info("Records result: " + Arrays.toString(records));
            return true;
        }
        return false;
    }

    private static boolean hasInvalidDomain(final Tuple2<Record[], Integer> recordsAndLookupStatus) {
        final int lookupStatus = recordsAndLookupStatus._2();
            if (lookupStatus == Lookup.HOST_NOT_FOUND) {
                log.info("Lookup result: " + lookupStatus);
                return true;
            }
            return false;
    }

    private static String extractDomain(final String email) {
        return email.substring(email.indexOf("@") + 1);
    }
}
