package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.arg.LogicalArgPatternExpression;
import gov.nist.csd.pm.pap.pml.pattern.arg.NodeArgPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.UsernamePattern;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.util.TestIdGenerator.id;
import static gov.nist.csd.pm.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateObligationStatementTest {

    @Test
    void testSuccess() throws PMException {
        CreateObligationStatement stmt = new CreateObligationStatement(new StringLiteral("o1"), List.of(
                new CreateRuleStatement(
                        new StringLiteral("rule1"),
                        new SubjectPattern(),
                        new OperationPattern("e1"),
                        Map.of(
                                "opnd1", List.of(new LogicalArgPatternExpression(
                                        new NodeArgPattern("oa1"),
                                        new NodeArgPattern("oa2"),
                                        false
                                ))
                        ),
                        new CreateRuleStatement.ResponseBlock("evtCtx", List.of(
                                new CreatePolicyClassStatement(new StringLiteral("pc2"))
                        ))
                )
        ));

        MemoryPAP pap = new TestPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua2", ids("pc1"));
        pap.modify().graph().createUser("u2", ids("ua2"));
        pap.modify().graph().createObjectAttribute("oa1", ids("pc1"));
        pap.modify().graph().createObjectAttribute("oa2", ids("pc1"));
        ExecutionContext execCtx = new ExecutionContext(new UserContext(id("u2")), pap);

        stmt.execute(execCtx, pap);

        assertTrue(pap.query().obligations().obligationExists("o1"));

        Obligation actual = pap.query().obligations().getObligation("o1");
        assertEquals(1, actual.getRules().size());
        assertEquals(id("u2"), actual.getAuthorId());
        Rule rule = actual.getRules().get(0);
        assertEquals("rule1", rule.getName());
        assertEquals(new EventPattern(
                new SubjectPattern(),
                new OperationPattern("e1"),
                Map.of(
                        "opnd1", List.of(new LogicalArgPatternExpression(
                                new NodeArgPattern("oa1"),
                                new NodeArgPattern("oa2"),
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
                                        "opnd1", List.of(new LogicalArgPatternExpression(
                                                new NodeArgPattern("oa1"),
                                                new NodeArgPattern("oa2"),
                                                false
                                        ))
                                ),
                                new CreateRuleStatement.ResponseBlock("evtCtx", List.of(
                                        new CreatePolicyClassStatement(new StringLiteral("pc2"))
                                ))
                        ),
                        new CreateRuleStatement(
                                new StringLiteral("rule2"),
                                new SubjectPattern(new UsernamePattern("u1")),
                                new OperationPattern("e3"),
                                Map.of(
                                        "opnd1", List.of(new LogicalArgPatternExpression(
                                                new NodeArgPattern("oa1"),
                                                new NodeArgPattern("oa2"),
                                                false
                                        ))
                                ),
                                new CreateRuleStatement.ResponseBlock("evtCtx", List.of(
                                        new CreatePolicyClassStatement(new StringLiteral("pc3"))
                                ))
                        )
                )

        );
        assertEquals(
                """
                        create obligation "obl1" {
                            create rule "rule1"
                            when any user
                            performs "e1"
                            on {
                                opnd1: "oa1" || "oa2"
                            }
                            do (evtCtx) {
                                create PC "pc2"
                            }
                            
                            create rule "rule2"
                            when user "u1"
                            performs "e3"
                            on {
                                opnd1: "oa1" || "oa2"
                            }
                            do (evtCtx) {
                                create PC "pc3"
                            }
                            
                        }""",
                stmt.toFormattedString(0)
        );
    }

}