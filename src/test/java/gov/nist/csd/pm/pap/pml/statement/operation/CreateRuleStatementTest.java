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

import static org.junit.jupiter.api.Assertions.*;

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
        assertTrue(actual.equals("create rule \"rule1\"\n" +
                "when any user\n" +
                "performs any operation\n" +
                "on {\n" +
                "    a: any,\n" +
                "    b: [in \"test\", \"test\"]\n" +
                "}\n" +
                "do () {\n" +
                "}".trim()) || actual.equals("create rule \"rule1\"\n" +
                "when any user\n" +
                "performs any operation\n" +
                "on {\n" +
                "    b: [in \"test\", \"test\"],\n" +
                "    a: any\n" +
                "}\n" +
                "do () {\n" +
                "}"));

    }

}