package com.usebouncer.verification.email.externals;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.xbill.DNS.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MXRecordLoader {

    private static final int THIRTY_SECONDS = 30;

    public static List<MXRecord> loadResults(final String domain) {
        return getMXRecords(domain);
    }

    public static boolean existMXRecords(final String domain) {
        return !getMXRecords(domain).isEmpty();
    }

    private static List<MXRecord> getMXRecords(final String domain) {
        Lookup lookup;
        List<MXRecord> results = new ArrayList<>();
        if (StringUtils.isEmpty(domain)) {
            return results;
        }
        try {
            lookup = new Lookup(domain, Type.MX);
            lookup.setResolver(getConfiguredSimpleResolver(new SimpleResolver()));
            Record[] records = lookup.run();
            if (records != null) {
                for (Record record: records) {
                    MXRecord mxRecord = (MXRecord) record;
                    results.add(mxRecord);
                }
            }
        } catch (TextParseException | UnknownHostException e) {
            log.error("Could not resolve MX records for domain: {}", domain);
            e.printStackTrace();
        }
        return results;
    }

    private static SimpleResolver getConfiguredSimpleResolver(final SimpleResolver simpleResolver) {
        simpleResolver.setTimeout(THIRTY_SECONDS);
        return simpleResolver;
    }
}
