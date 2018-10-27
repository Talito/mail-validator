package com.usebouncer.verification.email.externals;

import com.usebouncer.verification.email.model.SMTPCommands;
import lombok.extern.slf4j.Slf4j;
import org.xbill.DNS.MXRecord;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

@Slf4j
public class SMTPLowLevelClient {

    private static final int WELCOME_CODE = 220;
    private static final int OK_CODE = 250;
    private static final int INVALID_CODES = 500;

    public static boolean smtpServersHaveEmail(final List<MXRecord> mxRecords, final String domain, final String email) {
        int responseCode;
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        for (MXRecord mxRecord: mxRecords) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(mxRecord.getAdditionalName().toString(), 25), 30000);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                responseCode = hear(reader);
                if (responseCode != WELCOME_CODE) {
                    log.warn("Invalid header");
                    continue;
                }
                say(writer, SMTPCommands.HELLO.apply(domain));
                responseCode = hear(reader);
                if (responseCode != OK_CODE) {
                    log.warn("Not STMP");
                    continue;
                }
                say(writer, SMTPCommands.SENDER.apply());
                responseCode = hear( reader );
                if (responseCode != OK_CODE) {
                    log.warn("Rejected sender. Sender command: " + SMTPCommands.SENDER.apply());
                    continue;
                }
                say(writer, SMTPCommands.RCPT_TO.apply(email));
                responseCode = hear(reader);
                say(writer, SMTPCommands.RESET.apply());
                hear(reader);
                say(writer, SMTPCommands.QUIT.apply());
                hear(reader);
                if (responseCode >= INVALID_CODES) { // TODO: Not exactly true and imho should be redefined
                    log.info("Invalid email. Exact response code: {} from SMTP server {} (domain: {}) for email {}",
                            responseCode, mxRecord.getAdditionalName().toString(), domain, email);
                    return false;
                }
                return true;
            } catch (Exception e) {
                log.error("Something went wrong. Socket timeout?", e);
            } finally {
                try {
                    if (reader != null) { reader.close(); }
                    if (writer != null) { writer.close(); }
                    if (socket != null) { socket.close(); }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private static void say(final BufferedWriter writer, final String command) {
        try {
            writer.write( command + "\r\n" );
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int hear(final BufferedReader reader) throws IOException {
        String line;
        int responseCode = 0;
        while ((line = reader.readLine()) != null) {
            String pfx = line.substring( 0, 3 );
            try {
                responseCode = Integer.parseInt( pfx );
            }
            catch (Exception ex) {
                responseCode = -1;
            }
            if (line.charAt(3) != '-') break;
        }
        return responseCode;
    }

}
