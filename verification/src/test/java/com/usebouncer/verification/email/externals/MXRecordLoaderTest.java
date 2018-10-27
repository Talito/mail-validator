package com.usebouncer.verification.email.externals;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.junit.Test;
import org.xbill.DNS.Record;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MXRecordLoaderTest {

//    @Test
//    public void givenNullDomainShouldReturnEmptyRecordsAndFailedResult() {
//        final String domain = null;
//        Tuple2<Record[], Integer> recordsAndResult = MXRecordLoader.loadResults(domain);
//        assertThat(recordsAndResult).isEqualTo(Tuple.of(new Record[]{}, -1));
//    }
//
//    @Test
//    public void givenDomainShouldReturnRecords() {
//        final String domain = "gmail.com";
//        Tuple2<Record[], Integer> recordsAndResult = MXRecordLoader.loadResults(domain);
//        assertThat(recordsAndResult._1()).isNotEmpty();
//    }
//
//    @Test
//    public void givenDomainShouldReturnSuccessfulResult() {
//        final String domain = "gmail.com";
//        Tuple2<Record[], Integer> recordsAndResult = MXRecordLoader.loadResults(domain);
//        assertThat(recordsAndResult._2()).isEqualTo(0);
//    }
//
//    @Test
//    public void givenInvalidDomainShouldReturnNoRecordsAndNXDomainResult() {
//        final String domain = "fawkefowefkpawefjweofweofew.com";
//        Tuple2<Record[], Integer> recordsAndResult = MXRecordLoader.loadResults(domain);
//        assertThat(recordsAndResult).isEqualTo(Tuple.of(new Record[]{}, 3));
//    }
//
//    @Test
//    public void givenInvalidDomain2ShouldReturnNoRecordsAndNXDomainResult() {
//        final String domain = "gmail.a";
//        Tuple2<Record[], Integer> recordsAndResult = MXRecordLoader.loadResults(domain);
//        assertThat(recordsAndResult).isEqualTo(Tuple.of(new Record[]{}, 3));
//    }
}
