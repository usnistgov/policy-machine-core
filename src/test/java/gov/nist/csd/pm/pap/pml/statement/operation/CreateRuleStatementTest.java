package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.AnyOperandPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.InOperandPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.NodeOperandPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateRuleStatementTest {

    @Test
    void testFormattedString() {
        CreateRuleStatement createRuleStatement = new CreateRuleStatement(
                new StringLiteral("rule1"),
                new SubjectPattern(),
                new OperationPattern(),
                Map.of(
                        "a", List.of(new AnyOperandPattern()),
                        "b", List.of(new InOperandPattern("test"), new NodeOperandPattern("test"))
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