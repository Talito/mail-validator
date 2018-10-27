package com.usebouncer.verification.email.externals;

import org.junit.Test;
import org.xbill.DNS.MXRecord;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class MXRecordLoaderTest {

    @Test
    public void givenNullDomainShouldReturnEmptyRecords() {
        final String domain = null;
        List<MXRecord> recordsAndResult = MXRecordLoader.loadResults(domain);
        assertThat(recordsAndResult).isEmpty();
    }

    @Test
    public void givenEmptyDomainShouldReturnEmptyRecords() {
        final String domain = "";
        List<MXRecord> recordsAndResult = MXRecordLoader.loadResults(domain);
        assertThat(recordsAndResult).isEmpty();
    }

    @Test
    public void givenDomainShouldReturnRecords() {
        final String domain = "gmail.com";
        List<MXRecord> recordsAndResult = MXRecordLoader.loadResults(domain);
        assertThat(recordsAndResult).isNotEmpty();
    }

    @Test
    public void givenInvalidDomainShouldReturnNoRecordsAndNXDomainResult() {
        final String domain = "fawkefowefkpawefjweofweofew.com";
        List<MXRecord> recordsAndResult = MXRecordLoader.loadResults(domain);
        assertThat(recordsAndResult).isEmpty();
    }

    @Test
    public void givenInvalidDomain2ShouldReturnNoRecordsAndNXDomainResult() {
        final String domain = "gmail.a";
        List<MXRecord> recordsAndResult = MXRecordLoader.loadResults(domain);
        assertThat(recordsAndResult).isEmpty();
    }
}
