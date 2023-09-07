package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.pml.model.expression.*;
import gov.nist.csd.pm.policy.pml.statement.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static gov.nist.csd.pm.pap.SuperUserBootstrapper.SUPER_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObligationTest {

    @Test
    void testObligation() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        String input = """
                create policy class 'pc1'
                create oa 'oa1' in ['pc1']
                create ua 'ua1' in ['pc1']
                create u 'u1' in ['ua1']
                create obligation 'obligation1' {
                    create rule 'rule1'
                    when any user
                    performs ['test_event']
                    on 'oa1'
                    do(evtCtx) {
                        create policy class evtCtx['eventName']
                        
                        delete rule 'rule1' from obligation 'obligation1'
                    }
                }
                """;
        pap.deserialize(new UserContext("u1"), input, new PMLDeserializer());

        Obligation obligation1 = pap.obligations().get("obligation1");
        assertEquals("obligation1", obligation1.getName());
        assertEquals(1, obligation1.getRules().size());
        assertEquals(new UserContext("u1"), obligation1.getAuthor());

        Rule rule = obligation1.getRules().get(0);
        assertEquals("rule1", rule.getName());
        assertEquals(new EventPattern(
                EventSubject.anyUser(),
                Performs.events("test_event"),
                Target.policyElement("oa1")
        ), rule.getEventPattern());
        assertEquals(2, rule.getResponse().getStatements().size());
    }

    @Test
    void testObligationComplex() throws PMException {
        String pml = """
                create policy class 'pc1'
                create oa 'oa1' in ['pc1']
                create ua 'ua1' in ['pc1']
                create u 'u1' in ['ua1']
                
                create obligation 'test' {
                    create rule 'rule1'
                    when any user
                    performs ['create_object_attribute']
                    on 'oa1'
                    do(evtCtx) {
                        create policy class evtCtx['eventName']
                        let target = evtCtx['target']
                        
                        let event = evtCtx['event']
                        create policy class concat([event['name'], '_test'])
                        set properties of event['name'] to {'key': target}
                        
                        create policy class concat([evtCtx['userCtx']['user'], '_test'])
                    }
                }
                """;

        UserContext userCtx = new UserContext("u1");
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(userCtx, pml, new PMLDeserializer());

        assertEquals(1, pap.obligations().getAll().size());
        Obligation actual = pap.obligations().get("test");
        assertEquals(1, actual.getRules().size());
        assertEquals("test", actual.getName());
        assertEquals(userCtx, actual.getAuthor());

        Rule rule = actual.getRules().get(0);
        assertEquals("rule1", rule.getName());

        EventPattern event = rule.getEventPattern();
        assertEquals(EventSubject.anyUser(), event.getSubject());
        assertTrue(event.getOperations().contains("create_object_attribute"));
        assertEquals(Target.policyElement("oa1"), event.getTarget());

        Response response = rule.getResponse();
        assertEquals("evtCtx", response.getEventCtxVariable());

        List<PMLStatement> statements = response.getStatements();
        assertEquals(6, statements.size());

        PMLStatement stmt = statements.get(0);
        Type evtCtxType = Type.map(Type.string(), Type.any());
        PMLStatement expected = new CreatePolicyStatement(
                new Expression(
                        new VariableReference(
                                new EntryReference(
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
                                new EntryReference(
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
                                new EntryReference(
                                        new VariableReference("evtCtx", evtCtxType), new Expression(new Literal("event"))
                                ),
                                Type.any()
                        )
                ),
                false);
        assertEquals(expected, stmt);

        stmt = statements.get(3);
        expected = new CreatePolicyStatement(
                new Expression(
                        new FunctionInvocationStatement(
                                "concat",
                                Arrays.asList(new Expression(new Literal(new ArrayLiteral(
                                        new Expression[]{
                                                new Expression(new VariableReference(new EntryReference(new VariableReference("event", Type.any()), new Expression(new Literal("name"))), Type.any())),
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
                new Expression(new VariableReference(new EntryReference(new VariableReference("event", Type.any()), new Expression(new Literal("name"))), Type.any())),
                new Expression(new Literal(new MapLiteral(exprMap, Type.string(), Type.any())))
        );
        assertEquals(expected, stmt);

        stmt = statements.get(5);
        expected = new CreatePolicyStatement(
                new Expression(
                        new FunctionInvocationStatement(
                                "concat",
                                Arrays.asList(new Expression(new Literal(new ArrayLiteral(
                                        new Expression[]{
                                                new Expression(
                                                        new VariableReference(
                                                                new EntryReference(
                                                                        new VariableReference(
                                                                                new EntryReference(
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
