package com.usebouncer.verification.email.externals;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.xbill.DNS.*;

import java.net.UnknownHostException;

import static java.util.Optional.ofNullable;

public class MXRecordLoader {

    private static final int THIRTY_SECONDS = 30;

    public static Tuple2<Record[], Integer> loadResults(final String domain) {
        Lookup lookup = null;
        Record[] records = new Record[]{};
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
        return Tuple.of(records, ofNullable(lookup).map(Lookup::getResult).orElse(-1));
    }

    private static SimpleResolver getConfiguredSimpleResolver(final SimpleResolver simpleResolver) {
        simpleResolver.setTimeout(THIRTY_SECONDS);
        return simpleResolver;
    }
}
