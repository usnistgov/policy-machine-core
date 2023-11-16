package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.PMLConstantNotDefinedException;
import gov.nist.csd.pm.policy.exceptions.PMLFunctionNotDefinedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.subject.AnyUserSubject;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.Value;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeleteStatementTest {

    @Test
    void testSuccess() throws PMException {
        DeleteStatement stmt1 = new DeleteStatement(DeleteStatement.Type.OBJECT_ATTRIBUTE, new StringLiteral("oa1"));
        DeleteStatement stmt2 = new DeleteStatement(DeleteStatement.Type.PROHIBITION, new StringLiteral("p1"));
        DeleteStatement stmt3 = new DeleteStatement(DeleteStatement.Type.OBLIGATION, new StringLiteral("o1"));
        DeleteStatement stmt4 = new DeleteStatement(DeleteStatement.Type.FUNCTION, new StringLiteral("testFunc"));
        DeleteStatement stmt5 = new DeleteStatement(DeleteStatement.Type.CONST, new StringLiteral("testConst"));

        MemoryPolicyStore store = new MemoryPolicyStore();
        store.graph().setResourceAccessRights(new AccessRightSet("read"));
        store.graph().createPolicyClass("pc1");
        store.graph().createUserAttribute("ua1", "pc1");
        store.graph().createUser("u1", "ua1");
        store.graph().createObjectAttribute("oa1", "pc1");
        store.graph().createObjectAttribute("oa2", "pc1");
        UserContext userContext = new UserContext("u1");
        store.obligations().create(userContext, "o1", new Rule(
                "rule1",
                new EventPattern(new AnyUserSubject(), new Performs("e1")),
                new Response("e", List.of())
        ));
        store.prohibitions().create("p1",
                                    new ProhibitionSubject("ua1", ProhibitionSubject.Type.USER_ATTRIBUTE),
                                    new AccessRightSet("read"),
                                    true,
                                    new ContainerCondition("oa1", true)
        );
        store.userDefinedPML().createFunction(new FunctionDefinitionStatement.Builder("testFunc").build());
        store.userDefinedPML().createConstant("testConst", new StringValue("test"));

        GlobalScope<Value, FunctionDefinitionStatement> globalScope = GlobalScope.withValuesAndDefinitions(store);

        stmt2.execute(new ExecutionContext(userContext, globalScope), store);
        stmt3.execute(new ExecutionContext(userContext, globalScope), store);
        stmt1.execute(new ExecutionContext(userContext, globalScope), store);
        stmt4.execute(new ExecutionContext(userContext, globalScope), store);
        stmt5.execute(new ExecutionContext(userContext, globalScope), store);

        assertFalse(store.graph().nodeExists("oa1"));
        assertFalse(store.prohibitions().exists("p1"));
        assertFalse(store.obligations().exists("o1"));
        assertThrows(PMLFunctionNotDefinedException.class, () -> store.userDefinedPML().getFunction("testFunc"));
        assertThrows(PMLConstantNotDefinedException.class, () -> store.userDefinedPML().getConstant("testConst"));
    }

    @Test
    void testToFormattedString() {
        DeleteStatement stmt = new DeleteStatement(DeleteStatement.Type.OBJECT_ATTRIBUTE, new StringLiteral("test"));
        DeleteStatement stmt1 = new DeleteStatement(DeleteStatement.Type.OBLIGATION, new StringLiteral("test"));
        DeleteStatement stmt2 = new DeleteStatement(DeleteStatement.Type.PROHIBITION, new StringLiteral("test"));
        DeleteStatement stmt3 = new DeleteStatement(DeleteStatement.Type.OBJECT, new StringLiteral("test"));
        DeleteStatement stmt4 = new DeleteStatement(DeleteStatement.Type.POLICY_CLASS, new StringLiteral("test"));
        DeleteStatement stmt5 = new DeleteStatement(DeleteStatement.Type.USER, new StringLiteral("test"));
        DeleteStatement stmt6 = new DeleteStatement(DeleteStatement.Type.USER_ATTRIBUTE, new StringLiteral("test"));

        assertEquals("delete OA \"test\"", stmt.toFormattedString(0));
        assertEquals("delete obligation \"test\"", stmt1.toFormattedString(0));
        assertEquals("delete prohibition \"test\"", stmt2.toFormattedString(0));
        assertEquals("delete O \"test\"", stmt3.toFormattedString(0));
        assertEquals("delete PC \"test\"", stmt4.toFormattedString(0));
        assertEquals("delete U \"test\"", stmt5.toFormattedString(0));
        assertEquals("delete UA \"test\"", stmt6.toFormattedString(0));
        assertEquals(
                """
                            delete OA "test"
                        """,
                stmt.toFormattedString(1) + "\n"
        );
    }

}