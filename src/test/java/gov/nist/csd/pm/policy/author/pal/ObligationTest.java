package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.policy.author.pal.model.expression.*;
import gov.nist.csd.pm.policy.author.pal.statement.*;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObligationTest {

    @Test
    void testObligation() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.graph().createPolicyClass("pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");

        String input = """
                create obligation obligation1 {
                    create rule rule1
                    when any user
                    performs 'test_event'
                    on oa1
                    do(evtCtx) {
                        create policy class evtCtx['eventName'];
                        
                        delete rule rule1 from obligation obligation1;
                    }
                }
                """;
        new PALExecutor(pap)
                .compileAndExecutePAL(new UserContext(SUPER_USER), input);
        Obligation obligation1 = pap.obligations().get("obligation1");
        assertEquals("obligation1", obligation1.getLabel());
        assertEquals(1, obligation1.getRules().size());
        assertEquals(new UserContext(SUPER_USER), obligation1.getAuthor());

        Rule rule = obligation1.getRules().get(0);
        assertEquals("rule1", rule.getLabel());
        assertEquals(new EventPattern(
                EventSubject.anyUser(),
                Performs.events("test_event"),
                Target.policyElement("oa1")
        ), rule.getEvent());
        assertEquals(2, rule.getResponse().getStatements().size());
    }

    @Test
    void testObligationComplex() throws PMException {
        String pal = """
                create obligation test {
                    create rule rule1
                    when any user
                    performs 'create_object_attribute'
                    on oa1
                    do(evtCtx) {
                        create policy class evtCtx['eventName'];
                        let target = evtCtx['target'];
                        
                        let event = evtCtx['event'];
                        create policy class concat([event['name'], '_test']);
                        set properties of event['name'] to {'key': target};
                        
                        create policy class concat([evtCtx['userCtx']['user'], '_test']);
                    }
                }
                """;
        UserContext userCtx = new UserContext(SUPER_USER);
        MemoryPAP pap = new MemoryPAP();
        pap.graph().createPolicyClass("pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");

        new PALExecutor(pap).compileAndExecutePAL(userCtx, pal);

        assertEquals(1, pap.obligations().getAll().size());
        Obligation actual = pap.obligations().get("test");
        assertEquals(1, actual.getRules().size());
        assertEquals("test", actual.getLabel());
        assertEquals(userCtx, actual.getAuthor());

        Rule rule = actual.getRules().get(0);
        assertEquals("rule1", rule.getLabel());

        EventPattern event = rule.getEvent();
        assertEquals(EventSubject.anyUser(), event.getSubject());
        assertTrue(event.getOperations().contains("create_object_attribute"));
        assertEquals(Target.policyElement("oa1"), event.getTarget());

        Response response = rule.getResponse();
        assertEquals("evtCtx", response.getEventCtxVariable());

        List<PALStatement> statements = response.getStatements();
        assertEquals(6, statements.size());

        PALStatement stmt = statements.get(0);
        Type evtCtxType = Type.map(Type.string(), Type.any());
        PALStatement expected = new CreatePolicyStatement(
                new NameExpression(
                        new VariableReference(
                                new MapEntryReference(
                                        new VariableReference("evtCtx", evtCtxType), new Expression(new Literal("eventName"))
                                ),
                                Type.any()
                        )
                )
        );
        assertEquals(expected, stmt);

        stmt = statements.get(1);
        expected = new VarStatement(
                "target",
                new Expression(
                        new VariableReference(
                                new MapEntryReference(
                                        new VariableReference("evtCtx", evtCtxType), new Expression(new Literal("target"))
                                ),
                                Type.any()
                        )
                ),
                false);
        assertEquals(expected, stmt);

        stmt = statements.get(2);
        expected = new VarStatement(
                "event",
                new Expression(
                        new VariableReference(
                                new MapEntryReference(
                                        new VariableReference("evtCtx", evtCtxType), new Expression(new Literal("event"))
                                ),
                                Type.any()
                        )
                ),
                false);
        assertEquals(expected, stmt);

        stmt = statements.get(3);
        expected = new CreatePolicyStatement(
                new NameExpression(
                        new FunctionStatement(
                                "concat",
                                Arrays.asList(new Expression(new Literal(new ArrayLiteral(
                                        new Expression[]{
                                                new Expression(new VariableReference(new MapEntryReference(new VariableReference("event", Type.any()), new Expression(new Literal("name"))), Type.any())),
                                                new Expression(new Literal("_test"))
                                        },
                                        Type.string()
                                )))))
                )
        );
        assertEquals(expected, stmt);

        stmt = statements.get(4);
        HashMap<Expression, Expression> exprMap = new HashMap<>();
        exprMap.put(new Expression(new Literal("key")), new Expression(new VariableReference("target", Type.any())));
        expected = new SetNodePropertiesStatement(
                new NameExpression(new VariableReference(new MapEntryReference(new VariableReference("event", Type.any()), new Expression(new Literal("name"))), Type.any())),
                new Expression(new Literal(new MapLiteral(exprMap, Type.string(), Type.any())))
        );
        assertEquals(expected, stmt);

        stmt = statements.get(5);
        expected = new CreatePolicyStatement(
                new NameExpression(
                        new FunctionStatement(
                                "concat",
                                Arrays.asList(new Expression(new Literal(new ArrayLiteral(
                                        new Expression[]{
                                                new Expression(
                                                        new VariableReference(
                                                                new MapEntryReference(
                                                                        new VariableReference(
                                                                                new MapEntryReference(
                                                                                        new VariableReference("evtCtx", Type.map(Type.string(), Type.any())),
                                                                                        new Expression(new Literal("userCtx"))
                                                                                ),
                                                                                Type.any()
                                                                        ),
                                                                        new Expression(new Literal("user"))
                                                                ),
                                                                Type.any()
                                                        )
                                                ),
                                                new Expression(new Literal("_test"))
                                        },
                                        Type.any()
                                )))))
                )
        );
        assertEquals(expected, stmt);
    }


}
