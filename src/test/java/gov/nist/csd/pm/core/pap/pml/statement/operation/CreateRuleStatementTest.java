package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.AnyArgPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.InArgPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.NodeNameArgPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.SubjectPattern;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateRuleStatementTest {

    @Test
    void testFormattedString() {
        CreateRuleStatement createRuleStatement = new CreateRuleStatement(
                new StringLiteralExpression("rule1"),
                new SubjectPattern(),
                new OperationPattern(),
                Map.of(
                        "a", List.of(new AnyArgPatternExpression()),
                        "b", List.of(new InArgPatternExpression("test"), new NodeNameArgPatternExpression("test"))
                ),
                new CreateRuleStatement.ResponseBlock("", List.of())
        );

        String actual = createRuleStatement.toFormattedString(0);
        assertTrue(actual.equals("""
                create rule "rule1"
                when any user
                performs any operation
                on {
                    a: any,
                    b: [in "test", "test"]
                }
                do () {
                }""".trim()) || actual.equals("""
                create rule "rule1"
                when any user
                performs any operation
                on {
                    b: [in "test", "test"],
                    a: any
                }
                do () {
                }"""));
    }

}