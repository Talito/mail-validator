package com.usebouncer.verification.email.externals;

import org.springframework.util.StringUtils;
import org.xbill.DNS.*;

import java.net.UnknownHostException;
import java.util.Optional;

public class MXRecordLoader {

    private static final int THIRTY_SECONDS = 30;

    public static Optional<Record[]> loadResults(final String domain) {
        Lookup lookup;
        Record[] records = new Record[]{};
        if (StringUtils.isEmpty(domain)) {
            return Optional.empty();
        }
        try {
            lookup = new Lookup(domain, Type.MX);
            lookup.setResolver(getConfiguredSimpleResolver(new SimpleResolver()));
            records = lookup.run();
        } catch (TextParseException | UnknownHostException e) {
            e.printStackTrace();
        }
        if (records != null && records.length == 0) {
            try {
                lookup = new Lookup(domain, Type.A);
                lookup.setResolver(getConfiguredSimpleResolver(new SimpleResolver()));
                records = lookup.run();
            } catch (TextParseException | UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return Optional.ofNullable(records);
    }

    public static boolean existMXRecords(final String domain) {
        Lookup lookup;
        Record[] records = new Record[]{};
        if (StringUtils.isEmpty(domain)) {
            return false;
        }
        try {
            lookup = new Lookup(domain, Type.MX);
            lookup.setResolver(getConfiguredSimpleResolver(new SimpleResolver()));
            records = lookup.run();
        } catch (TextParseException | UnknownHostException e) {
            e.printStackTrace();
        }
        if (records != null && records.length > 0) {
            return true;
        }
        try {
            lookup = new Lookup(domain, Type.A);
            lookup.setResolver(getConfiguredSimpleResolver(new SimpleResolver()));
            records = lookup.run();
        } catch (TextParseException | UnknownHostException e) {
            e.printStackTrace();
        }
        if (records != null && records.length > 0) {
            return true;
        }
        return false;
    }

    private static SimpleResolver getConfiguredSimpleResolver(final SimpleResolver simpleResolver) {
        simpleResolver.setTimeout(THIRTY_SECONDS);
        return simpleResolver;
    }
}
