package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionReturnStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement.*;
import static org.junit.jupiter.api.Assertions.*;

class FunctionDefinitionVisitorTest {

    @Test
    void testSuccess() {
        PMLParser.FunctionDefinitionStatementContext ctx = PMLContextVisitor.toCtx(
                """
                function func1(string a, bool b, []string c) string {
                    return "test"
                }
                """,
                PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        PMLStatement stmt = new FunctionDefinitionVisitor(visitorCtx)
                .visitFunctionDefinitionStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());

        FunctionDefinitionStatement expected = new Builder("func1")
                .returns(Type.string())
                .args(
                        new FormalArgument("a", Type.string()),
                        new FormalArgument("b", Type.bool()),
                        new FormalArgument("c", Type.array(Type.string()))
                )
                .body(
                        new FunctionReturnStatement(new StringLiteral("test"))
                )
                .build();
        assertEquals(expected, stmt);


        ctx = PMLContextVisitor.toCtx(
                """
                function func1(string a) { 
                    
                }
                """,
                PMLParser.FunctionDefinitionStatementContext.class);
        visitorCtx = new VisitorContext();
        stmt = new FunctionDefinitionVisitor(visitorCtx)
                .visitFunctionDefinitionStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size(), visitorCtx.errorLog().getErrors().toString());
        assertEquals(
                new FunctionDefinitionStatement.Builder("func1")
                        .returns(Type.voidType())
                        .args(
                                new FormalArgument("a", Type.string())
                        )
                        .body()
                        .build(),
                stmt
        );
    }

    @Test
    void testNotAllPathsReturn() {
        PMLParser.FunctionDefinitionStatementContext ctx = PMLContextVisitor.toCtx(
                """
                function func1(string a, bool b, []string c) string {
                    if true {
                        return "test"
                    } else {
                    
                    }
                }
                """,
                PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new FunctionDefinitionVisitor(visitorCtx)
                .visitFunctionDefinitionStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "not all conditional paths return",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                function func1(string a, bool b, []string c) string {
                    foreach x in c {
                        return
                    }
                }
                """,
                PMLParser.FunctionDefinitionStatementContext.class);
        visitorCtx = new VisitorContext();
        new FunctionDefinitionVisitor(visitorCtx)
                .visitFunctionDefinitionStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "not all conditional paths return",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );

        ctx = PMLContextVisitor.toCtx(
                """
                function func1(string a, bool b, []string c) string {
                    
                }
                """,
                PMLParser.FunctionDefinitionStatementContext.class);
        visitorCtx = new VisitorContext();
        new FunctionDefinitionVisitor(visitorCtx)
                .visitFunctionDefinitionStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "not all conditional paths return",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testFormalArgClashesWithVariable() throws VariableAlreadyDefinedInScopeException {
        PMLParser.FunctionDefinitionStatementContext ctx = PMLContextVisitor.toCtx(
                """
                function func1(string a, bool b, []string c) string {
                    
                }
                """,
                PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        visitorCtx.scope().addVariable("a", Type.string(), false);
        new FunctionDefinitionVisitor(visitorCtx)
                .visitFunctionDefinitionStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "formal arg 'a' already defined in scope",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testFunctionInScope() throws FunctionAlreadyDefinedInScopeException {
        PMLParser.FunctionDefinitionStatementContext ctx = PMLContextVisitor.toCtx(
                """
                function func1(string a, bool b, []string c) string {
                    return ""
                }
                """,
                PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        visitorCtx.scope().addFunction(new FunctionDefinitionStatement.Builder("func1").build());
        new FunctionDefinitionVisitor(visitorCtx)
                .visitFunctionDefinitionStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "function 'func1' already defined in scope",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testReturnVoidWhenReturnValueIsString() {
        PMLParser.FunctionDefinitionStatementContext ctx = PMLContextVisitor.toCtx(
                """
                function func1(string a, bool b, []string c) string {
                    return
                }
                """,
                PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new FunctionDefinitionVisitor(visitorCtx)
                .visitFunctionDefinitionStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "return statement \"return\" does not match return type string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testWrongTypeOfReturnValue() {
        PMLParser.FunctionDefinitionStatementContext ctx = PMLContextVisitor.toCtx(
                """
                function func1(string a, bool b, []string c) string {
                    return false
                }
                """,
                PMLParser.FunctionDefinitionStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext();
        new FunctionDefinitionVisitor(visitorCtx)
                .visitFunctionDefinitionStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "return statement \"return false\" does not match return type string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }
}