package com.usebouncer.verification.email.model;

import io.vavr.Function0;
import io.vavr.Function1;

public class SMTPCommands {

    public static final Function1<String, String> HELLO = t -> "HELO " + t;
    public static final Function0<String> SENDER = () -> "MAIL FROM: <some-email@gmail.com>";
    public static final Function1<String, String> RCPT_TO = t -> "RCPT TO: <" + t + ">";
    public static final Function0<String> RESET = () -> "RSET";
    public static final Function0<String> QUIT = () -> "QUIT";

}
