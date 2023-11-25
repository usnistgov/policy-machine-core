package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.FunctionAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.Scope;
import gov.nist.csd.pm.policy.pml.statement.CreateObligationStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionReturnStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionReturnStatementVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.FunctionDefinitionStatementContext ctx1 = PMLContextVisitor.toCtx(
                """
                function func1(string a, bool b, []string c) string {
                    return "test"
                }
                """,
                PMLParser.FunctionDefinitionStatementContext.class);

        VisitorContext visitorCtx = new VisitorContext(
                GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                           .withPersistedFunctions(Map.of(
                                   "func1",
                                   new FunctionSignature(
                                           "func1",
                                           Type.string(),
                                           List.of(
                                                   new FormalArgument("a", Type.string()),
                                                   new FormalArgument("b", Type.bool()),
                                                   new FormalArgument("c", Type.array(Type.string()))))
                           ))
        );

        FunctionDefinitionStatement functionDefinitionStatement = (FunctionDefinitionStatement) new FunctionDefinitionVisitor(visitorCtx)
                .visitFunctionDefinitionStatement(ctx1);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(1, functionDefinitionStatement.getBody().size());
        assertEquals(
                new FunctionReturnStatement(new StringLiteral("test")),
                functionDefinitionStatement.getBody().get(0)
        );

        PMLParser.CreateObligationStatementContext ctx2 = PMLContextVisitor.toCtx(
                """
                        create obligation "test" {
                            create rule "test"
                            when users ["u1"]
                            performs ["e1"]
                            do(ctx) {
                                return
                            }
                        }
                        """,
                PMLParser.CreateObligationStatementContext.class
        );
        visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        CreateObligationStatement createObligationStatement = new CreateObligationStmtVisitor(visitorCtx)
                .visitCreateObligationStatement(ctx2);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new FunctionReturnStatement(),
                createObligationStatement.getRuleStmts().get(0).getResponse().getStatements().get(0)
        );
    }

    @Test
    void testReturnStatementNotInFunctionOrResponse() throws PMException {
        PMLParser.ReturnStatementContext ctx = PMLContextVisitor.toCtx(
                """
                        return
                        """,
                PMLParser.ReturnStatementContext.class
        );
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new FunctionReturnStmtVisitor(visitorCtx)
                .visitReturnStatement(ctx);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "return statement not in function definition or obligation response",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

    @Test
    void testReturnStatementWithValueInResponse() throws PMException {
        PMLParser.CreateObligationStatementContext ctx2 = PMLContextVisitor.toCtx(
                """
                        create obligation "test" {
                            create rule "test"
                            when users ["u1"]
                            performs ["e1"]
                            do(ctx) {
                                return "test"
                            }
                        }
                        """,
                PMLParser.CreateObligationStatementContext.class
        );
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        new CreateObligationStmtVisitor(visitorCtx)
                .visitCreateObligationStatement(ctx2);
        assertEquals(1, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "return statement in response cannot return a value",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
    }

}