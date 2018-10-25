package com.usebouncer.verification.email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.usebouncer.verification.email.verificators.EmailFormatVerificator.validate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(Parameterized.class)
public class EmailFormatVerificatorTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {null, false},
                {"", false},
                {"someemailgmail.com", false},
                {"somemeail@gmail.c", false},
                {".someemail@gmail.com", false},
                {"someemail()@gmail.com", false},
                {"some..email@gmail.com", false},
                {"someemail.@gmail.com", false},
                {"someemail@.gmail.com", false},
                {"someemail", false},
                {"some@email@gmail.com", false},
                {"someemail@gmail.co", true},
                {"some_email@gmail.com", true},
                {"some-email@gmail.com", true},
                {"someemail123@gmail.com", true},
                {"someemail.123@gmail.com", true},
                {"someemail+123@gmail.com", true},
                {"someemail@gmail.com.pl", true},
                {"someemail@server-gmail.com", true},
                {"someemail@1.com", true},
                {"someemail@gmail.com", true}
        });
    }

    private String email;

    private boolean isValid;

    public EmailFormatVerificatorTest(String email, boolean isValid) {
        this.email = email;
        this.isValid = isValid;
    }

    @Test
    public void test() {
        assertThat(isValid).isEqualTo(validate(email));
    }
}
