package com.usebouncer.verification.email.verificators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
//
//import static com.usebouncer.verification.email.verificators.EmailFormatVerificator.isInvalid;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@RunWith(Parameterized.class)
//public class EmailFormatVerificatorTest {
//
//    @Parameterized.Parameters
//    public static Collection<Object[]> data() {
//        return Arrays.asList(new Object[][] {
//                {null, true},
//                {"", true},
//                {"someemailgmail.com", true},
//                {"somemeail@gmail.c", true},
//                {".someemail@gmail.com", true},
//                {"someemail()@gmail.com", true},
//                {"some..email@gmail.com", true},
//                {"someemail.@gmail.com", true},
//                {"someemail@.gmail.com", true},
//                {"someemail", true},
//                {"some@email@gmail.com", true},
//                {"someemail@gmail.co", false},
//                {"some_email@gmail.com", false},
//                {"some-email@gmail.com", false},
//                {"someemail123@gmail.com", false},
//                {"someemail.123@gmail.com", false},
//                {"someemail+123@gmail.com", false},
//                {"someemail@gmail.com.pl", false},
//                {"someemail@server-gmail.com", false},
//                {"someemail@1.com", false},
//                {"someemail@gmail.com", false}
//        });
//    }
//
//    private String email;
//
//    private boolean isValid;
//
//    public EmailFormatVerificatorTest(String email, boolean isValid) {
//        this.email = email;
//        this.isValid = isValid;
//    }
//
//    @Test
//    public void test() {
//        assertThat(isValid).isEqualTo(isInvalid(email));
//    }
//}
