package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.pml.model.expression.*;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.pml.statement.*;
import gov.nist.csd.pm.policy.serializer.PMLDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_USER;
import static org.junit.jupiter.api.Assertions.*;

class CompileTest {

    @Test
    void testCompileLet() throws PMException {
        String pml = """
                let a = 'hello world'
                let b = ['hello', 'world']
                let c = {
                            '1': '2',
                            '3': '4'
                        }
                let d = {'1': '2', a: b}
                let e = true
                let f = c['1']
                let g = concat([f, ' ', a])
                let h = []
                """;
        List<PMLStatement> stmts = test(pml, new PAP(new MemoryPolicyStore()));
        VarStatement stmt = (VarStatement) stmts.get(0);
        assertEquals("a", stmt.getVarName());
        Assertions.assertEquals("hello world", stmt.getExpression().getLiteral().getStringLiteral());

        stmt = (VarStatement) stmts.get(1);
        assertEquals("b", stmt.getVarName());
        Assertions.assertEquals(Type.array(Type.string()), stmt.getExpression().getLiteral().getArrayLiteral().getType());
        Assertions.assertArrayEquals(new Expression[]{
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
        Assertions.assertTrue(stmt.getExpression().getLiteral().getBooleanLiteral());

        stmt = (VarStatement) stmts.get(5);
        assertEquals("f", stmt.getVarName());
        Assertions.assertEquals("c", stmt.getExpression().getVariableReference().getEntryReference().getVarRef().getID());
        Assertions.assertEquals(k1, stmt.getExpression().getVariableReference().getEntryReference().getKey());

        stmt = (VarStatement) stmts.get(6);
        assertEquals("g", stmt.getVarName());
        Expression expectedExpression = new Expression(new FunctionInvocationStatement("concat", Arrays.asList(
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


        stmt = (VarStatement) stmts.get(7);
        assertEquals("h", stmt.getVarName());
        expectedExpression = new Expression(new Literal(new ArrayLiteral(Type.array(Type.any()))));
        assertEquals(expectedExpression.toString(), stmt.getExpression().toString());
        Assertions.assertEquals(Type.any(), stmt.getExpression().getLiteral().getArrayLiteral().getType());
    }

    @Test
    void testCompileErrors() {
        String pml = """
                let a = concat('1')
                let b = a['a']
                let c = {'a': 'b'}
                let d = c[true]
                let e = {'': '', ['']: {'': ''}, true: ''}
                let a = b
                a = b
                """;
        PMLCompilationException ex = assertThrows(PMLCompilationException.class, () -> test(pml, new PAP(new MemoryPolicyStore())));
        assertEquals(5, ex.getErrors().size(), ex.getErrors().toString());
        Assertions.assertTrue(ex.getErrors().get(0).errorMessage().contains("expected []string, got string"));
        Assertions.assertTrue(ex.getErrors().get(1).errorMessage().contains("expected map or array type"));
        Assertions.assertTrue(ex.getErrors().get(2).errorMessage().contains("expression type boolean not allowed, only: [string]"));
        Assertions.assertTrue(ex.getErrors().get(3).errorMessage().contains("expected map keys to be of the same type but found"));
        Assertions.assertTrue(ex.getErrors().get(4).errorMessage().contains("expected map keys to be of the same type but found"));
    }

    @Test
    void testConstErrors() {
        String pml = """
                const a = 'hello world'
                const a = 'test'
                a = 'test'
                """;
        PMLCompilationException ex = assertThrows(PMLCompilationException.class, () -> test(pml, new PAP(new MemoryPolicyStore())));
        assertEquals(2, ex.getErrors().size(), ex.getErrors().toString());
        Assertions.assertTrue(ex.getErrors().get(0).errorMessage().contains("already defined"));
        Assertions.assertTrue(ex.getErrors().get(1).errorMessage().contains("cannot reassign"));
    }

    @Test
    void testForeachCompileErrors() {
        String pml = """
                foreach x, y in ['', ''] {
                
                }
                """;
        PMLCompilationException ex = assertThrows(PMLCompilationException.class, () -> test(pml, new PAP(new MemoryPolicyStore())));
        assertEquals(1, ex.getErrors().size());

        String pml1 = """
                foreach x, y in {'k': 'v', 'k1': 'v1'} {
                
                }
                """;
        assertDoesNotThrow(() -> test(pml1, new PAP(new MemoryPolicyStore())));
    }

    @Test
    void testForRangeCompileErrors() throws PMException {
        String pml = """
                for i in range [1, 5] {
                    create policy class i
                }
                """;
        PMLCompilationException ex = assertThrows(PMLCompilationException.class, () -> test(pml, new PAP(new MemoryPolicyStore())));
        assertEquals(1, ex.getErrors().size());
        Assertions.assertTrue(ex.getErrors().get(0).errorMessage().contains("number not allowed, only: [string]"));
    }

    private List<PMLStatement> test(String pml, Policy policyAuthor) throws PMException {
        return PMLCompiler.compilePML(policyAuthor, pml);
    }

    @Test
    void testMaps() {
        String pml = """
                let m = {'k1': {'k2': 'v1'}}
                let x = m['k1']['k2']['k3']
                """;
        PMLCompilationException ex = assertThrows(PMLCompilationException.class, () -> test(pml, new PAP(new MemoryPolicyStore())));
        assertEquals(1, ex.getErrors().size());
        Assertions.assertTrue(ex.getErrors().get(0).errorMessage().contains("expected map or array type"));

        String pml1 = """
                let m = {'k1': {'k2': 'v1'}}
                create policy class m['k1']
                """;
        ex = assertThrows(PMLCompilationException.class, () -> test(pml1, new PAP(new MemoryPolicyStore())));
        assertEquals(1, ex.getErrors().size());
        Assertions.assertTrue(ex.getErrors().get(0).errorMessage().contains("expression type map[string]string not allowed, only: [string]"));
    }

    @Test
    void testMapsSuccess() throws PMException, UnknownVariableInScopeException {
        String pml = """
                let m = {'k1': {'k1-1': {'k1-1-1': 'v1'}}}
                let x = m['k1']['k1-1']['k1-1-1']
                create policy class x
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        List<PMLStatement> test = test(pml, new PAP(new MemoryPolicyStore()));

        ExecutionContext ctx = new ExecutionContext(new UserContext(SUPER_USER));
        PMLStatement stmt = test.get(0);
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
        String pml = """
                create obligation 'test' {
                    create rule 'rule1'
                    when any user
                    performs ['create_object_attribute']
                    on 'oa1'
                    do(event) {
                        create policy class event['eventName']
                        let target = event['target']
                        
                        create policy class concat([event['name'], '_test'])
                        set properties of event['event']['name'] to {'key': target}
                        
                        create policy class concat([event['userCtx']['user'], '_test'])
                    }
                }
                """;

        UserContext userCtx = new UserContext(SUPER_USER);
        PAP pap = new PAP(new MemoryPolicyStore());
        ExecutionContext ctx = new ExecutionContext(userCtx);
        pap.graph().createPolicyClass("pc1");
        pap.graph().createObjectAttribute("oa1", "pc1");
        List<PMLStatement> test = test(pml, pap);
        assertEquals(1, test.size());

        PMLStatement stmt = test.get(0);
        stmt.execute(ctx, pap);

        assertEquals(1, pap.obligations().getObligations().size());
        Obligation actual = pap.obligations().getObligation("test");
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

        List<PMLStatement> statements = response.getStatements();
        assertEquals(5, statements.size());

        stmt = statements.get(0);

        Type evtCtxType = Type.map(Type.string(), Type.any());
        PMLStatement expected = new CreatePolicyStatement(
                new Expression(
                        new VariableReference(
                                new EntryReference(
                                        new VariableReference("event", evtCtxType), new Expression(new Literal("eventName"))
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
                                        new VariableReference("event", evtCtxType), new Expression(new Literal("target"))
                                ),
                                Type.any()
                        )
                ),
                false);
        assertEquals(expected, stmt);

        stmt = statements.get(2);
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

        stmt = statements.get(3);
        HashMap<Expression, Expression> exprMap = new HashMap<>();
        exprMap.put(new Expression(new Literal("key")), new Expression(new VariableReference("target", Type.any())));
        expected = new SetNodePropertiesStatement(
                new Expression(
                        new VariableReference(
                                new EntryReference(
                                        new VariableReference(
                                                new EntryReference(
                                                        new VariableReference("event", Type.any()),
                                                        new Expression(new Literal("event"))
                                                ),
                                                Type.any()
                                        ),
                                        new Expression(new Literal("name"))
                                ),
                                Type.any()
                        )
                ),
                new Expression(new Literal(new MapLiteral(exprMap, Type.string(), Type.any())))
        );
        assertEquals(expected, stmt);

        stmt = statements.get(4);
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
                                                                                        new VariableReference("event", Type.any()),
                                                                                        new Expression(new Literal("userCtx"))
                                                                                ),
                                                                                Type.any()
                                                                        ),
                                                                        new Expression(new Literal("user"))
                                                                ),
                                                                Type.any()
                                                        )
                                                ),
                                                new Expression(new Literal("_test")),
                                        },
                                        Type.any()
                                )))))
                )
        );
        assertEquals(expected, stmt);
    }

    @Test
    void testScopeOrder() {
        String pml = """
                let x = 'hello'
                create policy class concat([x, ' ', y])
                let y = 'world'
                """;
        assertThrows(PMLCompilationException.class, () -> PMLCompiler.compilePML(new PAP(new MemoryPolicyStore()), pml));
    }

    @Test
    void testEmptyMap() {
        String pml = """
                function testFunc(map[string]string m) {
                
                }
                
                testFunc({})
                """;
        assertDoesNotThrow(() -> PMLCompiler.compilePML(new PAP(new MemoryPolicyStore()), pml));
    }

    @Test
    void testForLoopLocalVar() throws PMException {
        String pml = """
                for i in range [1, 100] {
                    let x = i
                    create pc numToStr(x)
                }
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.fromString(pml, new PMLDeserializer(new UserContext(SUPER_USER)));
        assertEquals(101, pap.graph().getPolicyClasses().size());

        String pml2 = """
                for i in range [1, 100] {
                    let x = i
                    create pc numToStr(x)
                }
                
                create oa 'oa1' in x
                """;
        PAP pap2 = new PAP(new MemoryPolicyStore());
        assertThrows(PMLCompilationException.class, () -> pap2.fromString(pml2, new PMLDeserializer(new UserContext(SUPER_USER))));

        pml = """
                create pc 'pc1'
                create oa 'oa1' in ['pc1']
                foreach child in getChildren('pc1') {
                    let x = 'pc2'
                    create pc x
                }
                """;
        pap = new PAP(new MemoryPolicyStore());
        pap.fromString(pml, new PMLDeserializer(new UserContext(SUPER_USER)));
        assertTrue(pap.graph().nodeExists("pc2"));

        String pml3 = """
                foreach child in getChildren('pc1') {
                    let x = 'pc2'
                    create pc x
                }
                
                create oa 'oa1' in x
                """;
        PAP pap3 = new PAP(new MemoryPolicyStore());
        assertThrows(PMLCompilationException.class, () -> pap3.fromString(pml3, new PMLDeserializer(new UserContext(SUPER_USER))));

    }
}
