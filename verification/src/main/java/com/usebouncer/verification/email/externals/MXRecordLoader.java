package com.usebouncer.verification.email.externals;

import org.springframework.util.StringUtils;
import org.xbill.DNS.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MXRecordLoader {

    private static final int THIRTY_SECONDS = 30;

    public static List<MXRecord> loadResults(final String domain) {
        Lookup lookup;
        MXRecord[] records = new MXRecord[]{};
        List<MXRecord> results = new ArrayList<>();
        if (StringUtils.isEmpty(domain)) {
            return results;
        }
        try {
            lookup = new Lookup(domain, Type.MX);
            lookup.setResolver(getConfiguredSimpleResolver(new SimpleResolver()));
            Record[] rar = lookup.run();
            for (Record r: rar) {
                MXRecord mxRecord = (MXRecord) r;
                results.add(mxRecord);
            }
        } catch (TextParseException | UnknownHostException e) {
            e.printStackTrace();
        }
        return results;
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
        return records != null && records.length > 0;
    }

    private static SimpleResolver getConfiguredSimpleResolver(final SimpleResolver simpleResolver) {
        simpleResolver.setTimeout(THIRTY_SECONDS);
        return simpleResolver;
    }
}
