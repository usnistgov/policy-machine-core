package gov.nist.csd.pm.policy.author.pal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PALFormatterTest {

    private static final String expected = """
            set resource access rights read, write;
            create policy class pc1;
            create oa oa1 in pc1;
            create ua ua1 in pc1;
            associate ua1 and oa1 with read, write;
            create obligation obl1 {
                create rule rule1
                when any user
                performs event1, event2
                do {
                    function testFunc(string t) {
                        create policy class t;
                    }
                    event event1 as e1 {
                        create policy class e1;
                    }
                    event event2 as e2 {
                        create policy class e1;
                    }
                }
            }
            """;
    private static final String input =
            "set resource access rights read, write;" +
                    "create policy class pc1;" +
                    "create oa oa1 in pc1;" +
                    "create ua ua1 in pc1;" +
                    "associate ua1 and oa1 with read, write;" +
                    "create obligation obl1 {" +
                    "create rule rule1" +
                    " when any user" +
                    " performs" +
                    " event1," +
                    " event2" +
                    " do {" +
                    " function testFunc(string t) {" +
                    " create policy class t;" +
                    " }" +

                    "event event1 as e1 {" +
                    "create policy class e1;" +
                    "}" +

                    "event event2 as e2 {" +
                    "create policy class e1;" +
                    "}" +
                    "}" +
                    "}";

    @Test
    void testFormatWithObligation() {
        String format = PALFormatter.format(input);
        System.out.println(format);

        assertEquals(expected, format);
    }

}