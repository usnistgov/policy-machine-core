package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.function.builtin.Equals;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.Scope;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static gov.nist.csd.pm.policy.pml.PMLContextVisitor.toCtx;
import static gov.nist.csd.pm.policy.pml.PMLContextVisitor.toStatementBlockCtx;
import static org.junit.jupiter.api.Assertions.*;

class StatementBlockVisitorTest {

    private static GlobalScope<Variable, FunctionSignature> testGlobalScope;

    @BeforeAll
    static void setup() throws PMException {
        testGlobalScope = GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                   .withPersistedFunctions(Map.of("equals", new Equals().getSignature()));
    }

    @Test
    void testAllPathsReturned() throws PMException {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                "{\n" +
                        "                    var a = \"a\"\n" +
                        "                    if equals(a, \"a\") {\n" +
                        "                        return \"a\"\n" +
                        "                    } else if equals(a, \"b\") {\n" +
                        "                        return \"b\"\n" +
                        "                    } else if equals(a, \"c\") {\n" +
                        "                        return \"c\"\n" +
                        "                    } else if equals(a, \"d\") {\n" +
                        "                        return \"d\"\n" +
                        "                    } else {\n" +
                        "                        return \"e\"\n" +
                        "                    }\n" +
                        "                }"
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        StatementBlockVisitor.Result result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertTrue(result.allPathsReturned());

        ctx = toStatementBlockCtx(
                "{\n" +
                        "                    var a = \"a\"\n" +
                        "                    if equals(a, \"a\") {\n" +
                        "                        if true {\n" +
                        "                            return \"a\"\n" +
                        "                        } else {\n" +
                        "                            return \"b\"\n" +
                        "                        }\n" +
                        "                    } else if equals(a, \"b\") {\n" +
                        "                        return \"b\"\n" +
                        "                    } else if equals(a, \"c\") {\n" +
                        "                        return \"c\"\n" +
                        "                    } else if equals(a, \"d\") {\n" +
                        "                        return \"d\"\n" +
                        "                    } else {\n" +
                        "                        return \"e\"\n" +
                        "                    }\n" +
                        "                }"
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        visitorContext = new VisitorContext(testGlobalScope);
        result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertTrue(result.allPathsReturned());

        ctx = toStatementBlockCtx(
                "{\n" +
                        "                    var a = \"a\"\n" +
                        "                    if equals(a, \"a\") {\n" +
                        "                        return \"a\"\n" +
                        "                    } else if equals(a, \"b\") {\n" +
                        "                        return \"b\"\n" +
                        "                    } else if equals(a, \"c\") {\n" +
                        "                    } else if equals(a, \"d\") {\n" +
                        "                    } else {\n" +
                        "                    }\n" +
                        "                    \n" +
                        "                    return \"e\"\n" +
                        "                }"
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        visitorContext = new VisitorContext(testGlobalScope);
        result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertTrue(result.allPathsReturned());

        ctx = toStatementBlockCtx(
                "{\n" +
                        "                    var a = \"a\"\n" +
                        "                    if equals(a, \"a\") {\n" +
                        "                        return \"a\"\n" +
                        "                    } else if equals(a, \"b\") {\n" +
                        "                        if true {\n" +
                        "                            return \"a\"\n" +
                        "                        }\n" +
                        "                        \n" +
                        "                        return \"a\"\n" +
                        "                    } else if equals(a, \"c\") {\n" +
                        "                    } else if equals(a, \"d\") {\n" +
                        "                    } else {\n" +
                        "                    }\n" +
                        "                    \n" +
                        "                    return \"e\"\n" +
                        "                }"
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        visitorContext = new VisitorContext(testGlobalScope);
        result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertTrue(result.allPathsReturned());
    }

    @Test
    void testReturnNotLastStatementInBlockThrowsException() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                "{\n" +
                        "                    return \"a\"\n" +
                        "                    var a = \"a\"\n" +
                        "                    if equals(a, \"a\") {\n" +
                        "                        return \"a\"\n" +
                        "                    } else if equals(a, \"b\") {\n" +
                        "                        return \"b\"\n" +
                        "                    } else if equals(a, \"c\") {\n" +
                        "                        return \"c\"\n" +
                        "                    } else if equals(a, \"d\") {\n" +
                        "                        return \"d\"\n" +
                        "                    }\n" +
                        "                }"
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(1, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertEquals(
                "function return should be last statement in block",
                visitorContext.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = toStatementBlockCtx(
                "{\n" +
                        "                    return \"a\"\n" +
                        "                    var a = \"a\"\n" +
                        "                    if equals(a, \"a\") {\n" +
                        "                        return \"a\"\n" +
                        "                        \n" +
                        "                        if true {\n" +
                        "                            return \"a\"\n" +
                        "                        }\n" +
                        "                    } else if equals(a, \"b\") {\n" +
                        "                        return \"b\"\n" +
                        "                    } else if equals(a, \"c\") {\n" +
                        "                        return \"c\"\n" +
                        "                    } else {\n" +
                        "                        return \"d\"\n" +
                        "                    }\n" +
                        "                }"
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        visitorContext = new VisitorContext(testGlobalScope);
        new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(1, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertEquals(
                "function return should be last statement in block",
                visitorContext.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testReturnTypeDoesNotMatchThrowsException() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                "{\n" +
                        "                    return true\n" +
                        "                }"
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(1, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertEquals(
                "return statement \"return true\" does not match return type string",
                visitorContext.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testIfStatementNotAllPathsReturned() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                "{\n" +
                        "                    var a = \"a\"\n" +
                        "                    if equals(a, \"a\") {\n" +
                        "                        return \"a\"\n" +
                        "                    } else if equals(a, \"b\") {\n" +
                        "                        return \"b\"\n" +
                        "                    } else if equals(a, \"c\") {\n" +
                        "                        \n" +
                        "                    } else if equals(a, \"d\") {\n" +
                        "                        \n" +
                        "                    }\n" +
                        "                }"
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        StatementBlockVisitor.Result result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertFalse(result.allPathsReturned());
    }

    @Test
    void testMultipleIfStatementsNotAllPathsReturned() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                "{\n" +
                        "                    var a = \"a\"\n" +
                        "                    if equals(a, \"a\") {\n" +
                        "                        return \"a\"\n" +
                        "                    } else if equals(a, \"b\") {\n" +
                        "                        return \"b\"\n" +
                        "                    } else {\n" +
                        "                        return \"c\"\n" +
                        "                    }\n" +
                        "                     \n" +
                        "                    if equals(a, \"c\") {\n" +
                        "                        \n" +
                        "                    } else if equals(a, \"d\") {\n" +
                        "                        \n" +
                        "                    }\n" +
                        "                }"
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        StatementBlockVisitor.Result result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertFalse(result.allPathsReturned());
    }

    @Test
    void testMultipleIfStatementsAllPathsReturned() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                "{\n" +
                        "                    var a = \"a\"\n" +
                        "                    if equals(a, \"a\") {\n" +
                        "                        return \"a\"\n" +
                        "                    } else if equals(a, \"b\") {\n" +
                        "                        return \"b\"\n" +
                        "                    } else {\n" +
                        "                        return \"c\"\n" +
                        "                    }\n" +
                        "                     \n" +
                        "                    if equals(a, \"c\") {\n" +
                        "                        return \"a\"\n" +
                        "                    } else if equals(a, \"d\") {\n" +
                        "                        return \"a\"\n" +
                        "                    }\n" +
                        "                    \n" +
                        "                    return \"b\"\n" +
                        "                }"
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        StatementBlockVisitor.Result result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertTrue(result.allPathsReturned());
    }

    @Test
    void testFunctionInBlock() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                "{\n" +
                        "    function f1() {}\n" +
                        "}"
        );
        VisitorContext visitorContext = new VisitorContext(testGlobalScope);
        StatementBlockVisitor.Result result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(1, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertEquals("functions are not allowed inside statement blocks",
                     visitorContext.errorLog().getErrors().get(0).errorMessage());
    }


}