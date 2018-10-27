package com.usebouncer.verification.email.verificators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.usebouncer.verification.email.EmailVerification.Status.INVALID_FORMAT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(Parameterized.class)
public class EmailVerificatorFormatCaseTest {

    private EmailVerificator emailVerificator;
    
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {null},
                {""},
                {"someemailgmail.com"},
                {"somemeail@gmail.c"},
                {".someemail@gmail.com"},
                {"someemail()@gmail.com"},
                {"some..email@gmail.com"},
                {"someemail.@gmail.com"},
                {"someemail@.gmail.com"},
                {"someemail"},
                {"some@email@gmail.com"}
        });
    }

    private String email;

    public EmailVerificatorFormatCaseTest(String email) {
        this.emailVerificator = new EmailVerificator();
        this.email = email;
    }

    @Test
    public void test() {
        assertThat(INVALID_FORMAT).isEqualTo(emailVerificator.verifyEmail(email));
    }
}
