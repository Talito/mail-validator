package com.usebouncer.verification.email.verificators;

import com.usebouncer.verification.email.EmailVerification;
import com.usebouncer.verification.email.externals.MXRecordLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.smtp.SMTPClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.xbill.DNS.MXRecord;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
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
        final List<MXRecord> mxRecords = MXRecordLoader.loadResults(domain).stream()
                .sorted(Comparator.comparing(MXRecord::getPriority))
                .collect(Collectors.toList());;

        int res = 0;
        Socket skt = null;
        BufferedReader rdr = null;
        BufferedWriter wtr = null;
        for (MXRecord r: mxRecords) {
            try {
                skt = new Socket();
                skt.connect(new InetSocketAddress(r.getAdditionalName().toString(), 25), 30000);
                rdr = new BufferedReader
                        ( new InputStreamReader( skt.getInputStream() ) );
                wtr = new BufferedWriter
                        ( new OutputStreamWriter( skt.getOutputStream() ) );
                res = hear( rdr );
                if ( res != 220 ) {
                    log.warn("Invalid header");
                    continue;
                }
                say( wtr, "HELO " + domain );
                res = hear( rdr );
                if ( res != 250 ) {
                    log.warn("Not STMP");
                    continue;
                }
                say( wtr, "MAIL FROM: <josetalito@gmail.com>" );
                res = hear( rdr );
                if ( res != 250 ) {
                    log.warn("Rejected sender");
                    continue;
                }
                say( wtr, "RCPT TO: <" + email + ">" );
                res = hear( rdr );
                say( wtr, "RSET" );
                hear( rdr );
                say( wtr, "QUIT" );
                hear( rdr );
                if ( res >= 500 ) { // TODO: Not exactly true and imho should be redefined
                    log.info("Invalid email. Request result: {}", res);
                    return true;
                }
                return false;
            } catch (Exception e) {
                log.error("Something went wrong. Probably Socket timeout.", e);
            } finally {
                try {
                    if (rdr != null) {
                        rdr.close();
                    }
                    if (wtr != null) {
                        wtr.close();
                    }
                    if (skt != null) {
                        skt.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private static void say( BufferedWriter wr, String text ) {
        try {
            wr.write( text + "\r\n" );
            wr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int hear( BufferedReader in ) throws IOException {
        String line = null;
        int res = 0;

        while ( (line = in.readLine()) != null ) {
            String pfx = line.substring( 0, 3 );
            try {
                res = Integer.parseInt( pfx );
            }
            catch (Exception ex) {
                res = -1;
            }
            if ( line.charAt( 3 ) != '-' ) break;
        }

        return res;
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
