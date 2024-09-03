package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.LogicalOperandPatternExpression;
import gov.nist.csd.pm.pap.pml.pattern.operand.NodeOperandPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.UsernamePattern;
import gov.nist.csd.pm.pap.query.UserContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateObligationStatementTest {

    @Test
    void testSuccess() throws PMException, PMException {
        CreateObligationStatement stmt = new CreateObligationStatement(new StringLiteral("o1"), List.of(
                new CreateRuleStatement(
                        new StringLiteral("rule1"),
                        new SubjectPattern(),
                        new OperationPattern("e1"),
                        Map.of(
                                "opnd1", List.of(new LogicalOperandPatternExpression(
                                        new NodeOperandPattern("oa1"),
                                        new NodeOperandPattern("oa2"),
                                        false
                                ))
                        ),
                        new CreateRuleStatement.ResponseBlock("evtCtx", List.of(
                                new CreatePolicyStatement(new StringLiteral("pc2"))
                        ))
                )
        ));

        MemoryPAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua2", List.of("pc1"));
        pap.modify().graph().createUser("u2", List.of("ua2"));
        pap.modify().graph().createObjectAttribute("oa1", List.of("pc1"));
        pap.modify().graph().createObjectAttribute("oa2", List.of("pc1"));
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u2"), pap);

        stmt.execute(execCtx, pap);

        assertTrue(pap.query().obligations().obligationExists("o1"));

        Obligation actual = pap.query().obligations().getObligation("o1");
        assertEquals(1, actual.getRules().size());
        assertEquals("u2", actual.getAuthor());
        Rule rule = actual.getRules().get(0);
        assertEquals("rule1", rule.getName());
        assertEquals(new EventPattern(
                new SubjectPattern(),
                new OperationPattern("e1"),
                Map.of(
                        "opnd1", List.of(new LogicalOperandPatternExpression(
                                new NodeOperandPattern("oa1"),
                                new NodeOperandPattern("oa2"),
                                false
                        ))
                )
        ), rule.getEventPattern());
    }

    @Test
    void testToFormattedString() {
        CreateObligationStatement stmt = new CreateObligationStatement(
                new StringLiteral("obl1"),
                List.of(
                        new CreateRuleStatement(
                                new StringLiteral("rule1"),
                                new SubjectPattern(),
                                new OperationPattern("e1"),
                                Map.of(
                                        "opnd1", List.of(new LogicalOperandPatternExpression(
                                                new NodeOperandPattern("oa1"),
                                                new NodeOperandPattern("oa2"),
                                                false
                                        ))
                                ),
                                new CreateRuleStatement.ResponseBlock("evtCtx", List.of(
                                        new CreatePolicyStatement(new StringLiteral("pc2"))
                                ))
                        ),
                        new CreateRuleStatement(
                                new StringLiteral("rule2"),
                                new SubjectPattern(new UsernamePattern("u1")),
                                new OperationPattern("e3"),
                                Map.of(
                                        "opnd1", List.of(new LogicalOperandPatternExpression(
                                                new NodeOperandPattern("oa1"),
                                                new NodeOperandPattern("oa2"),
                                                false
                                        ))
                                ),
                                new CreateRuleStatement.ResponseBlock("evtCtx", List.of(
                                        new CreatePolicyStatement(new StringLiteral("pc3"))
                                ))
                        )
                )

        );
        assertEquals(
                "create obligation \"obl1\" {\n" +
                        "    create rule \"rule1\"\n" +
                        "    when any user\n" +
                        "    performs \"e1\"\n" +
                        "    on {\n" +
                        "        opnd1: \"oa1\" || \"oa2\"\n" +
                        "    }\n" +
                        "    do (evtCtx) {\n" +
                        "        create PC \"pc2\"\n" +
                        "    }\n" +
                        "\n" +
                        "    create rule \"rule2\"\n" +
                        "    when user \"u1\"\n" +
                        "    performs \"e3\"\n" +
                        "    on {\n" +
                        "        opnd1: \"oa1\" || \"oa2\"\n" +
                        "    }\n" +
                        "    do (evtCtx) {\n" +
                        "        create PC \"pc3\"\n" +
                        "    }\n" +
                        "\n" +
                        "}",
                stmt.toFormattedString(0)
        );
    }

}