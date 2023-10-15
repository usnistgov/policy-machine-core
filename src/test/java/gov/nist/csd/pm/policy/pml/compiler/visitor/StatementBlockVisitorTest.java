package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLContextVisitor.toCtx;
import static gov.nist.csd.pm.policy.pml.PMLContextVisitor.toStatementBlockCtx;
import static org.junit.jupiter.api.Assertions.*;

class StatementBlockVisitorTest {

    @Test
    void testAllPathsReturned() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                """
                {
                    var a = "a"
                    if equals(a, "a") {
                        return "a"
                    } else if equals(a, "b") {
                        return "b"
                    } else if equals(a, "c") {
                        return "c"
                    } else if equals(a, "d") {
                        return "d"
                    } else {
                        return "e"
                    }
                }
                """
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext();
        StatementBlockVisitor.Result result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertTrue(result.allPathsReturned());

        ctx = toStatementBlockCtx(
                """
                {
                    var a = "a"
                    if equals(a, "a") {
                        if true {
                            return "a"
                        } else {
                            return "b"
                        }
                    } else if equals(a, "b") {
                        return "b"
                    } else if equals(a, "c") {
                        return "c"
                    } else if equals(a, "d") {
                        return "d"
                    } else {
                        return "e"
                    }
                }
                """
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        visitorContext = new VisitorContext();
        result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertTrue(result.allPathsReturned());

        ctx = toStatementBlockCtx(
                """
                {
                    var a = "a"
                    if equals(a, "a") {
                        return "a"
                    } else if equals(a, "b") {
                        return "b"
                    } else if equals(a, "c") {
                    } else if equals(a, "d") {
                    } else {
                    }
                    
                    return "e"
                }
                """
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        visitorContext = new VisitorContext();
        result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertTrue(result.allPathsReturned());

        ctx = toStatementBlockCtx(
                """
                {
                    var a = "a"
                    if equals(a, "a") {
                        return "a"
                    } else if equals(a, "b") {
                        if true {
                            return "a"
                        }
                        
                        return "a"
                    } else if equals(a, "c") {
                    } else if equals(a, "d") {
                    } else {
                    }
                    
                    return "e"
                }
                """
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        visitorContext = new VisitorContext();
        result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertTrue(result.allPathsReturned());
    }

    @Test
    void testReturnNotLastStatementInBlockThrowsException() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                """
                {
                    return "a"
                    var a = "a"
                    if equals(a, "a") {
                        return "a"
                    } else if equals(a, "b") {
                        return "b"
                    } else if equals(a, "c") {
                        return "c"
                    } else if equals(a, "d") {
                        return "d"
                    }
                }
                """
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext();
        new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(1, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertEquals(
                "function return should be last statement in block",
                visitorContext.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = toStatementBlockCtx(
                """
                {
                    return "a"
                    var a = "a"
                    if equals(a, "a") {
                        return "a"
                        
                        if true {
                            return "a"
                        }
                    } else if equals(a, "b") {
                        return "b"
                    } else if equals(a, "c") {
                        return "c"
                    } else {
                        return "d"
                    }
                }
                """
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        visitorContext = new VisitorContext();
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
                """
                {
                    return true
                }
                """
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext();
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
                """
                {
                    var a = "a"
                    if equals(a, "a") {
                        return "a"
                    } else if equals(a, "b") {
                        return "b"
                    } else if equals(a, "c") {
                        
                    } else if equals(a, "d") {
                        
                    }
                }
                """
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext();
        StatementBlockVisitor.Result result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertFalse(result.allPathsReturned());
    }

    @Test
    void testMultipleIfStatementsNotAllPathsReturned() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                """
                {
                    var a = "a"
                    if equals(a, "a") {
                        return "a"
                    } else if equals(a, "b") {
                        return "b"
                    } else {
                        return "c"
                    }
                     
                    if equals(a, "c") {
                        
                    } else if equals(a, "d") {
                        
                    }
                }
                """
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext();
        StatementBlockVisitor.Result result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertFalse(result.allPathsReturned());
    }

    @Test
    void testMultipleIfStatementsAllPathsReturned() {
        PMLParser.StatementBlockContext ctx = toStatementBlockCtx(
                """
                {
                    var a = "a"
                    if equals(a, "a") {
                        return "a"
                    } else if equals(a, "b") {
                        return "b"
                    } else {
                        return "c"
                    }
                     
                    if equals(a, "c") {
                        return "a"
                    } else if equals(a, "d") {
                        return "a"
                    }
                    
                    return "b"
                }
                """
        );
        ctx.parent = toCtx("function f1() {}", PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorContext = new VisitorContext();
        StatementBlockVisitor.Result result = new StatementBlockVisitor(visitorContext, Type.string())
                .visitStatementBlock(ctx);
        assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
        assertTrue(result.allPathsReturned());
    }


}