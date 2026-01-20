package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.util.TestIdGenerator.id;
import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventContextUser;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EPP;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.pml.PMLCompiler;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLStmtsQueryOperation;
import gov.nist.csd.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CreateObligationStatementTest {

    @Test
    void testSuccess() throws PMException {
        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addVariable("opnd1", new Variable("opnd1", STRING_TYPE, false));
        EventPattern eventPattern = new EventPattern(
            new SubjectPattern(),
            new MatchesOperationPattern(
                "e1",
                Set.of("opnd1"),
                new PMLStmtsRoutine<>(
                    "",
                    BOOLEAN_TYPE,
                    List.of(new NodeNameFormalParameter("opnd1")),
                    new PMLStatementBlock(new PMLCompiler().compilePML(new MemoryPAP(), compileScope, """
                        return opnd1 == "oa1" || opnd1 == "oa2"
                        """))
                )
            )
        );

        CreateObligationStatement stmt = new CreateObligationStatement(
            new StringLiteralExpression("o1"),
            eventPattern,
            new ObligationResponse(
                "evtCtx", List.of(
                new CreatePolicyClassStatement(new StringLiteralExpression("pc2"))
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
        assertEquals(id("u2"), actual.getAuthorId());
        assertEquals(eventPattern, actual.getEventPattern());
    }

    @Test
    void testToFormattedString() throws PMException {
        CompileScope compileScope = new CompileScope(new MemoryPAP());
        compileScope.addVariable("opnd1", new Variable("opnd1", STRING_TYPE, false));

        EventPattern eventPattern1 = new EventPattern(
            new SubjectPattern(),
            new MatchesOperationPattern(
                "e1",
                Set.of("opnd1"),
                new PMLStmtsRoutine<>(
                    "",
                    BOOLEAN_TYPE,
                    List.of(new NodeNameFormalParameter("opnd1")),
                    new PMLStatementBlock(new PMLCompiler().compilePML(new MemoryPAP(), compileScope, """
                        return opnd1 == "oa1" || opnd1 == "oa2"
                        """))
                )
            )
        );

        CreateObligationStatement stmt1 = new CreateObligationStatement(
            new StringLiteralExpression("obl1"),
            eventPattern1,
            new ObligationResponse(
                "evtCtx",
                List.of(new CreatePolicyClassStatement(new StringLiteralExpression("pc2")))
            )
        );

        CreateObligationStatement stmt2 = new CreateObligationStatement(
            new StringLiteralExpression("obl2"),
            eventPattern1,
            new ObligationResponse(
                "evtCtx",
                List.of(new CreatePolicyClassStatement(new StringLiteralExpression("pc3")))
            )
        );

        assertEquals(
            """
                    create obligation "obl1"
                    when any user
                    performs e1 on (opnd1) {
                        return opnd1 == "oa1" || opnd1 == "oa2"
                    }
                    do (evtCtx) {
                        create PC "pc2"
                    }""",
            stmt1.toFormattedString(0)
        );

        assertEquals(
            """
                    create obligation "obl2"
                    when any user
                    performs e1 on (opnd1) {
                        return opnd1 == "oa1" || opnd1 == "oa2"
                    }
                    do (evtCtx) {
                        create PC "pc3"
                    }""",
            stmt2.toFormattedString(0)
        );
    }

    @Test
    void testCreateRuleInResponse() throws PMException {
        String pml = """
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create u "u1" in ["ua1"]
            
            associate "ua1" and PM_ADMIN_BASE_OA with ["*"]
            
            resourceop op1()
            resourceop op2()
            
            create obligation "o1"
                when any user
                performs op1
                do(ctx) {
                    create obligation "rule2"
                    when any user
                    performs op2
                    do(ctx2) {
                        create pc "pc2"
                    }
                }
            """;
        MemoryPAP pap = new TestPAP();
        pap.executePML(new UserContext(id("u1")), pml);

        PDP pdp = new PDP(pap);
        EPP epp = new EPP(pdp, pap);
        epp.processEvent(new EventContext(
            new EventContextUser("u1"),
            "op1",
            new HashMap<>()
        ));
        epp.processEvent(new EventContext(
            new EventContextUser("u1"),
            "op2",
            new HashMap<>()
        ));
        assertTrue(pap.query().graph().nodeExists("pc2"));
    }

}