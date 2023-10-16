package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.subject.AnyUserSubject;
import gov.nist.csd.pm.policy.model.obligation.event.target.AnyInUnionTarget;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class CreateObligationStatementTest {

    @Test
    void testSuccess() throws PMException {
        CreateObligationStatement stmt = new CreateObligationStatement(new StringLiteral("o1"), List.of(
                new CreateRuleStatement(
                        new StringLiteral("rule1"),
                        new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.ANY_USER, null),
                        new CreateRuleStatement.PerformsClause(buildArrayLiteral("e1", "e2")),
                        new CreateRuleStatement.OnClause(
                                buildArrayLiteral("oa1", "oa2"), CreateRuleStatement.TargetType.ANY_IN_UNION),
                        new CreateRuleStatement.ResponseBlock("evtCtx", List.of(
                                new CreatePolicyStatement(new StringLiteral("pc2"))
                        ))
                )
        ));

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua2", "pc1");
        store.graph().createUser("u2", "ua2");
        store.graph().createObjectAttribute("oa1", "pc1");
        store.graph().createObjectAttribute("oa2", "pc1");
        ExecutionContext execCtx = new ExecutionContext(new UserContext("u2"), GlobalScope.withValuesAndDefinitions(store));

        stmt.execute(execCtx, store);

        assertTrue(store.obligations().exists("o1"));

        Obligation actual = store.obligations().get("o1");
        assertEquals(1, actual.getRules().size());
        assertEquals("u2", actual.getAuthor().getUser());
        Rule rule = actual.getRules().get(0);
        assertEquals("rule1", rule.getName());
        assertEquals(new EventPattern(
                new AnyUserSubject(),
                new Performs("e1", "e2"),
                new AnyInUnionTarget("oa1", "oa2")
        ), rule.getEventPattern());
    }

    @Test
    void testToFormattedString() {
        CreateObligationStatement stmt = new CreateObligationStatement(
                new StringLiteral("obl1"),
                List.of(
                        new CreateRuleStatement(
                                new StringLiteral("rule1"),
                                new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.ANY_USER, null),
                                new CreateRuleStatement.PerformsClause(buildArrayLiteral("e1", "e2")),
                                new CreateRuleStatement.OnClause(
                                        buildArrayLiteral("oa1", "oa2"), CreateRuleStatement.TargetType.ANY_IN_UNION),
                                new CreateRuleStatement.ResponseBlock("evtCtx", List.of(
                                        new CreatePolicyStatement(new StringLiteral("pc2"))
                                ))
                        ),
                        new CreateRuleStatement(
                                new StringLiteral("rule2"),
                                new CreateRuleStatement.SubjectClause(CreateRuleStatement.SubjectType.USERS, buildArrayLiteral("u1")),
                                new CreateRuleStatement.PerformsClause(buildArrayLiteral("e3")),
                                new CreateRuleStatement.OnClause(
                                        buildArrayLiteral("oa1", "oa2"), CreateRuleStatement.TargetType.ANY_IN_UNION),
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
                        "    performs [\"e1\", \"e2\"]\n" +
                        "    on union of [\"oa1\", \"oa2\"]\n" +
                        "    do (evtCtx) {\n" +
                        "        create PC \"pc2\"\n" +
                        "    }\n" +
                        "    create rule \"rule2\"\n" +
                        "    when users [\"u1\"]\n" +
                        "    performs [\"e3\"]\n" +
                        "    on union of [\"oa1\", \"oa2\"]\n" +
                        "    do (evtCtx) {\n" +
                        "        create PC \"pc3\"\n" +
                        "    }\n" +
                        "}",
                stmt.toFormattedString(0)
        );
        assertEquals(
                "create obligation \"obl1\" {\n" +
                        "    create rule \"rule1\"\n" +
                        "    when any user\n" +
                        "    performs [\"e1\", \"e2\"]\n" +
                        "    on union of [\"oa1\", \"oa2\"]\n" +
                        "    do (evtCtx) {\n" +
                        "        create PC \"pc2\"\n" +
                        "    }\n" +
                        "    create rule \"rule2\"\n" +
                        "    when users [\"u1\"]\n" +
                        "    performs [\"e3\"]\n" +
                        "    on union of [\"oa1\", \"oa2\"]\n" +
                        "    do (evtCtx) {\n" +
                        "        create PC \"pc3\"\n" +
                        "    }\n" +
                        "}",
                stmt.toFormattedString(0)
        );
    }

}