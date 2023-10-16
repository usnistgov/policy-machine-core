package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.function.builtin.Equals;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.pml.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.statement.VariableAssignmentStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VarStmtVisitorTest {

    private static GlobalScope<Variable, FunctionSignature> testGlobalScope;

    @BeforeAll
    static void setup() throws PMException {
        testGlobalScope = GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore())
                                     .withPersistedFunctions(Map.of("equals", new Equals().getSignature()));
    }

    @Nested
    class ConstDeclarationTest {

        @Test
        void testSuccess() throws UnknownVariableInScopeException {
            PMLParser.ConstDeclarationContext ctx = PMLContextVisitor.toCtx(
                    "const x = \"a\"", PMLParser.ConstDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            new VarStmtVisitor(visitorCtx)
                    .visitConstDeclaration(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertTrue(visitorCtx.scope().getVariable("x").isConst());
        }

        @Test
        void testReassign() throws VariableAlreadyDefinedInScopeException {
            PMLParser.VariableAssignmentStatementContext ctx = PMLContextVisitor.toCtx(
                    "x = \"a\"", PMLParser.VariableAssignmentStatementContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            visitorCtx.scope().addVariable("x", new Variable("x", Type.string(), true));
            new VarStmtVisitor(visitorCtx)
                    .visitVariableAssignmentStatement(ctx);
            assertEquals(1, visitorCtx.errorLog().getErrors().size());
            assertEquals(
                    "cannot reassign const variable",
                    visitorCtx.errorLog().getErrors().get(0).errorMessage()
            );

            PMLParser.ConstDeclarationContext ctx2 = PMLContextVisitor.toCtx(
                    "const x = \"a\"", PMLParser.ConstDeclarationContext.class);
            visitorCtx = new VisitorContext(testGlobalScope);
            visitorCtx.scope().addVariable("x", new Variable("x", Type.string(), true));
            new VarStmtVisitor(visitorCtx)
                    .visitConstDeclaration(ctx2);
            assertEquals(1, visitorCtx.errorLog().getErrors().size());
            assertEquals(
                    "const 'x' already defined in scope",
                    visitorCtx.errorLog().getErrors().get(0).errorMessage()
            );
        }

        @Test
        void testReassignInBlock() {
            PMLParser.ConstDeclarationContext ctx = PMLContextVisitor.toCtx(
                    "const (\n" +
                            "                        x = \"a\"\n" +
                            "                        x = \"b\"\n" +
                            "                     )", PMLParser.ConstDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            new VarStmtVisitor(visitorCtx)
                    .visitConstDeclaration(ctx);
            assertEquals(1, visitorCtx.errorLog().getErrors().size());
            assertEquals(
                    "const 'x' already defined in scope",
                    visitorCtx.errorLog().getErrors().get(0).errorMessage()
            );
        }

    }

    @Nested
    class VarDeclarationTest {

        @Test
        void testSuccess() throws UnknownVariableInScopeException {
            PMLParser.VarDeclarationContext ctx = PMLContextVisitor.toCtx(
                    "var x = \"a\"", PMLParser.VarDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            new VarStmtVisitor(visitorCtx)
                    .visitVarDeclaration(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
        }

        @Test
        void testReassign() throws VariableAlreadyDefinedInScopeException {
            PMLParser.VarDeclarationContext ctx = PMLContextVisitor.toCtx(
                    "var x = \"a\"", PMLParser.VarDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            visitorCtx.scope().addVariable("x", new Variable("x", Type.string(), false));
            new VarStmtVisitor(visitorCtx)
                    .visitVarDeclaration(ctx);
            assertEquals(1, visitorCtx.errorLog().getErrors().size());
            assertEquals(
                    "variable 'x' already defined in scope",
                    visitorCtx.errorLog().getErrors().get(0).errorMessage()
            );
        }

        @Test
        void testReassignConstant() throws VariableAlreadyDefinedInScopeException {
            PMLParser.VarDeclarationContext ctx = PMLContextVisitor.toCtx(
                    "var x = \"a\"", PMLParser.VarDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            visitorCtx.scope().addVariable("x", new Variable("x", Type.string(), true));
            new VarStmtVisitor(visitorCtx)
                    .visitVarDeclaration(ctx);
            assertEquals(1, visitorCtx.errorLog().getErrors().size());
            assertEquals(
                    "variable 'x' already defined in scope",
                    visitorCtx.errorLog().getErrors().get(0).errorMessage()
            );
        }

        @Test
        void testReassignInBlock() throws VariableAlreadyDefinedInScopeException {
            PMLParser.VarDeclarationContext ctx = PMLContextVisitor.toCtx(
                    "var (\n" +
                            "                        x = \"a\"\n" +
                            "                        x = \"b\"\n" +
                            "                     )", PMLParser.VarDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            visitorCtx.scope().addVariable("x", new Variable("x", Type.string(), true));
            new VarStmtVisitor(visitorCtx)
                    .visitVarDeclaration(ctx);
            assertEquals(1, visitorCtx.errorLog().getErrors().size());
            assertEquals(
                    "variable 'x' already defined in scope",
                    visitorCtx.errorLog().getErrors().get(0).errorMessage()
            );
        }

    }

    @Nested
    class ShortDeclarationTest {
        @Test
        void testSuccess() throws UnknownVariableInScopeException {
            PMLParser.ShortDeclarationContext ctx = PMLContextVisitor.toCtx(
                    "x := \"a\"", PMLParser.ShortDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            new VarStmtVisitor(visitorCtx)
                    .visitShortDeclaration(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
        }

        @Test
        void testReassign() throws VariableAlreadyDefinedInScopeException {
            PMLParser.ShortDeclarationContext ctx = PMLContextVisitor.toCtx(
                    "x := \"a\"", PMLParser.ShortDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            visitorCtx.scope().addVariable("x", new Variable("x", Type.string(), true));
            new VarStmtVisitor(visitorCtx)
                    .visitShortDeclaration(ctx);
            assertEquals(1, visitorCtx.errorLog().getErrors().size());
            assertEquals(
                    "variable x already exists",
                    visitorCtx.errorLog().getErrors().get(0).errorMessage()
            );
        }
    }

    @Nested
    class VariableAssignmentTest {
        @Test
        void testSuccess() throws UnknownVariableInScopeException, VariableAlreadyDefinedInScopeException {
            PMLParser.VariableAssignmentStatementContext ctx = PMLContextVisitor.toCtx(
                    "x = \"a\"", PMLParser.VariableAssignmentStatementContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            visitorCtx.scope().addVariable("x", new Variable("x", Type.string(), false));
            VariableAssignmentStatement stmt =
                    (VariableAssignmentStatement) new VarStmtVisitor(visitorCtx)
                            .visitVariableAssignmentStatement(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
            assertFalse(stmt.isPlus());

            ctx = PMLContextVisitor.toCtx(
                    "x += \"a\"", PMLParser.VariableAssignmentStatementContext.class);
            visitorCtx = new VisitorContext(testGlobalScope);
            visitorCtx.scope().addVariable("x", new Variable("x", Type.string(), false));
            stmt = (VariableAssignmentStatement) new VarStmtVisitor(visitorCtx)
                    .visitVariableAssignmentStatement(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
            assertTrue(stmt.isPlus());
        }

        @Test
        void testVariableDoesNotExist() {
            PMLParser.VariableAssignmentStatementContext ctx = PMLContextVisitor.toCtx(
                    "x = \"a\"", PMLParser.VariableAssignmentStatementContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            new VarStmtVisitor(visitorCtx)
                    .visitVariableAssignmentStatement(ctx);
            assertEquals(1, visitorCtx.errorLog().getErrors().size());
            assertEquals(
                    "unknown variable 'x' in scope",
                    visitorCtx.errorLog().getErrors().get(0).errorMessage()
            );
        }

        @Test
        void testVariableIsConstant() throws VariableAlreadyDefinedInScopeException {
            PMLParser.VariableAssignmentStatementContext ctx = PMLContextVisitor.toCtx(
                    "x = \"a\"", PMLParser.VariableAssignmentStatementContext.class);
            VisitorContext visitorCtx = new VisitorContext(testGlobalScope);
            visitorCtx.scope().addVariable("x", new Variable("x", Type.string(), true));
            new VarStmtVisitor(visitorCtx)
                    .visitVariableAssignmentStatement(ctx);
            assertEquals(1, visitorCtx.errorLog().getErrors().size());
            assertEquals(
                    "cannot reassign const variable",
                    visitorCtx.errorLog().getErrors().get(0).errorMessage()
            );
        }
    }
}