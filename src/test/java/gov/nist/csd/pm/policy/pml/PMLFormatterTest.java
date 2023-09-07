package gov.nist.csd.pm.policy.pml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PMLFormatterTest {

    private static final String expected = """
            set resource access rights ['read', 'write']
            create policy class 'pc1'
            create oa 'oa1' in ['pc1']
            create ua 'ua1' in ['pc1']
            associate 'ua1' and 'oa1' with ['read', 'write']
            create obligation 'obl1' {
                create rule 'rule1'
                when any user
                performs ['event1', 'event2']
                do(evtCtx) {
                    let event = evtCtx['event']
                    if equals(event, 'event1') {
                        create policy class 'e1'
                    } else if equals(event, 'event2') {
                        create policy class 'e2'
                    }
                }
            }
            """;
    private static final String input =
            "set resource access rights ['read', 'write']" +
                    "create policy class 'pc1'" +
                    "create oa 'oa1' in ['pc1']" +
                    "create ua 'ua1' in ['pc1']" +
                    "associate 'ua1' and 'oa1' with ['read', 'write']" +
                    "create obligation 'obl1' {" +
                    "create rule 'rule1'" +
                    " when any user" +
                    " performs" +
                    " ['event1'," +
                    " 'event2']" +
                    " do(evtCtx) {" +
                    " let event = evtCtx['event']" +
                    " if equals(event, 'event1') {" +
                    " create policy class 'e1'" +
                    " } else if equals(event, 'event2') {" +
                    " create policy class 'e2'" +
                    "}" +
                    "}" +
                    "}";

    @Test
    void testFormatWithObligation() {
        String format = PMLFormatter.format(input);
        assertEquals(expected, format);
    }

    @Test
    void testFormatWithProhibition() {
        String pml = """
               create prohibition 'p1' deny user attribute 'ua1' access rights ['read'] on union of [!'oa1']
               """;
        String format = PMLFormatter.format(pml);

        String expected = """
                create prohibition 'p1'
                deny user attribute 'ua1'
                access rights ['read']
                on union of [!'oa1']
                """;

        assertEquals(expected, format);

        pml = """
               if equals('a', 'a') {
                   create prohibition 'p1' deny user attribute 'ua1' access rights ['read'] on union of [!'oa1']
               }
               """;
        format = PMLFormatter.format(pml);

        expected = """
                if equals('a', 'a') {
                    create prohibition 'p1'
                    deny user attribute 'ua1'
                    access rights ['read']
                    on union of [!'oa1']
                }
                """;

        assertEquals(expected, format);
    }

}