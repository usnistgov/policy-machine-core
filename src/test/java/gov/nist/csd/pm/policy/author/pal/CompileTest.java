package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.exception.PALCompilationException;
import gov.nist.csd.pm.policy.author.pal.model.expression.*;
import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.author.pal.statement.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static org.junit.jupiter.api.Assertions.*;

public class CompileTest {

    @Test
    void testCompileLet() throws PMException {
        String pal = """
                let a = 'hello world';
                let b = ['hello', 'world'];
                let c = {
                            '1': '2',
                            '3': '4'
                        };
                let d = {'1': '2', a: b};
                let e = true;
                let f = c['1'];
                let g = concat([f, ' ', a]);
                """;
        List<PALStatement> stmts = test(pal, new MemoryPAP());
        VarStatement stmt = (VarStatement) stmts.get(0);
        assertEquals("a", stmt.getVarName());
        assertEquals("hello world", stmt.getExpression().getLiteral().getStringLiteral());

        stmt = (VarStatement) stmts.get(1);
        assertEquals("b", stmt.getVarName());
        assertEquals(Type.array(Type.string()), stmt.getExpression().getLiteral().getArrayLiteral().getType());
        assertArrayEquals(new Expression[]{
                new Expression(new Literal("hello")),
                new Expression(new Literal("world"))
        }, stmt.getExpression().getLiteral().getArrayLiteral().getArray());

        stmt = (VarStatement) stmts.get(2);
        assertEquals("c", stmt.getVarName());
        MapLiteral mapLiteral = stmt.getExpression().getLiteral().getMapLiteral();
        assertEquals(Type.map(Type.string(), Type.string()), mapLiteral.getType());
        Map<Expression, Expression> actual = mapLiteral.getMap();

        Expression k1 = new Expression(new Literal("1"));
        Expression v1 = new Expression(new Literal("2"));
        Expression k2 = new Expression(new Literal("3"));
        Expression v2 = new Expression(new Literal("4"));

        Map<Expression, Expression> expected = Map.of(
                k1, v1,
                k2, v2
        );
        assertEquals(2, actual.size());
        assertEquals(expected.get(k1), v1);
        assertEquals(expected.get(k2), v2);
        assertEquals(Type.map(Type.string(), Type.string()), mapLiteral.getType());

        stmt = (VarStatement) stmts.get(3);
        assertEquals("d", stmt.getVarName());
        mapLiteral = stmt.getExpression().getLiteral().getMapLiteral();
        assertEquals(Type.map(Type.string(), Type.any()), mapLiteral.getType());
        actual = mapLiteral.getMap();

        k1 = new Expression(new Literal("1"));
        v1 = new Expression(new Literal("2"));
        k2 = new Expression(new VariableReference("a", Type.string()));
        v2 = new Expression(new VariableReference("b", Type.array(Type.string())));

        expected = Map.of(
                k1, v1,
                k2, v2
        );
        assertEquals(2, actual.size());
        assertEquals(expected.get(k1), v1);
        assertEquals(expected.get(k2), v2);
        assertEquals(Type.map(Type.string(), Type.any()), mapLiteral.getType());

        stmt = (VarStatement) stmts.get(4);
        assertEquals("e", stmt.getVarName());
        assertTrue(stmt.getExpression().getLiteral().getBooleanLiteral());

        stmt = (VarStatement) stmts.get(5);
        assertEquals("f", stmt.getVarName());
        assertEquals("c", stmt.getExpression().getVariableReference().getMapEntryReference().getMap().getID());
        assertEquals(k1, stmt.getExpression().getVariableReference().getMapEntryReference().getKey());

        stmt = (VarStatement) stmts.get(6);
        assertEquals("g", stmt.getVarName());
        Expression expectedExpression = new Expression(new FunctionStatement("concat", Arrays.asList(
                new Expression(new Literal(new ArrayLiteral(
                        new Expression[]{
                                new Expression(new VariableReference("f", Type.string())),
                                new Expression(new Literal(" ")),
                                new Expression(new VariableReference("a", Type.string()))
                        },
                        Type.string()
                )))
        )));
        assertEquals(expectedExpression.toString(), stmt.getExpression().toString());
    }

    @Test
    void testCompileErrors() {
        String pal = """
                let a = concat('1');
                let b = a['a'];
                let c = {'a': 'b'};
                let d = c[true];
                let e = {'': '', ['']: {'': ''}, true: ''};
                let a = b;
                a = b;
                """;
        PALCompilationException ex = assertThrows(PALCompilationException.class, () -> test(pal, new MemoryPAP()));
        assertEquals(5, ex.getErrors().size(), ex.getErrors().toString());
        assertTrue(ex.getErrors().get(0).errorMessage().contains("expected []string, got string"));
        assertTrue(ex.getErrors().get(1).errorMessage().contains("expected map type"));
        assertTrue(ex.getErrors().get(2).errorMessage().contains("expression type boolean not allowed, only: [string]"));
        assertTrue(ex.getErrors().get(3).errorMessage().contains("expected map keys to be of the same type but found"));
        assertTrue(ex.getErrors().get(4).errorMessage().contains("expected map keys to be of the same type but found"));
    }

    @Test
    void testConstErrors() {
        String pal = """
                const a = 'hello world';
                const a = 'test';
                a = 'test';
                """;
        PALCompilationException ex = assertThrows(PALCompilationException.class, () -> test(pal, new MemoryPAP()));
        assertEquals(2, ex.getErrors().size(), ex.getErrors().toString());
        assertTrue(ex.getErrors().get(0).errorMessage().contains("already defined"));
        assertTrue(ex.getErrors().get(1).errorMessage().contains("cannot reassign"));
    }

    @Test
    void testForeachCompileErrors() {
        String pal = """
                foreach x, y in ['', ''] {
                
                }
                """;
        PALCompilationException ex = assertThrows(PALCompilationException.class, () -> test(pal, new MemoryPAP()));
        assertEquals(1, ex.getErrors().size());

        String pal1 = """
                foreach x, y in {'k': 'v', 'k1': 'v1'} {
                
                }
                """;
        assertDoesNotThrow(() -> test(pal1, new MemoryPAP()));
    }

    @Test
    void testForRangeCompileErrors() throws PMException {
        String pal = """
                for i in range [1, 5] {
                    create policy class i;
                }
                """;
        PALCompilationException ex = assertThrows(PALCompilationException.class, () -> test(pal, new MemoryPAP()));
        assertEquals(1, ex.getErrors().size());
        assertTrue(ex.getErrors().get(0).errorMessage().contains("number not allowed, only string"));
    }

    private List<PALStatement> test(String pal, PolicyAuthor policyAuthor) throws PMException {
        return new PALExecutor(policyAuthor).compilePAL(pal);
    }

    @Test
    void testMaps() {
        String pal = """
                let m = {'k1': {'k2': 'v1'}};
                let x = m['k1']['k2']['k3'];
                """;
        PALCompilationException ex = assertThrows(PALCompilationException.class, () -> test(pal, new MemoryPAP()));
        assertEquals(1, ex.getErrors().size());
        assertTrue(ex.getErrors().get(0).errorMessage().contains("expected map type"));

        String pal1 = """
                let m = {'k1': {'k2': 'v1'}};
                create policy class m['k1'];
                """;
        ex = assertThrows(PALCompilationException.class, () -> test(pal1, new MemoryPAP()));
        assertEquals(1, ex.getErrors().size());
        assertTrue(ex.getErrors().get(0).errorMessage().contains("name type map[string]string not allowed, only string"));
    }

    @Test
    void testMapsSuccess() throws PMException, UnknownVariableInScopeException {
        String pal = """
                let m = {'k1': {'k1-1': {'k1-1-1': 'v1'}}};
                let x = m['k1']['k1-1']['k1-1-1'];
                create policy class x;
                """;
        MemoryPAP pap = new MemoryPAP();
        List<PALStatement> test = test(pal, new MemoryPAP());

        ExecutionContext ctx = new ExecutionContext(new UserContext(SUPER_USER));
        PALStatement stmt = test.get(0);
        stmt.execute(ctx, pap);
        Value m = ctx.scope().getValue("m");
        assertTrue(m.isMap());
        assertEquals(Type.string(), m.getType().getMapKeyType());
        assertEquals(Type.map(Type.string(), Type.map(Type.string(), Type.map(Type.string(), Type.string()))), m.getType());

        stmt = test.get(1);
        stmt.execute(ctx, pap);
        Value x = ctx.scope().getValue("x");
        assertEquals(Type.string(), x.getType());
        assertEquals("v1", x.getStringValue());

        stmt = test.get(2);
        stmt.execute(ctx, pap);
    }

    @Test
    void testCompileObligation() throws PMException {
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
        ExecutionContext ctx = new ExecutionContext(userCtx);
        pap.graph().createPolicyClass("pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");
        List<PALStatement> test = test(pal, pap);
        assertEquals(1, test.size());

        PALStatement stmt = test.get(0);
        stmt.execute(ctx, pap);

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

        stmt = statements.get(0);

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

    @Test
    void testScopeOrder() {
        String pal = """
                let x = 'hello';
                create policy class concat([x, ' ', y]);
                let y = 'world';
                """;
        assertThrows(PALCompilationException.class, () -> new MemoryPAP().compilePAL(pal));
    }
}
