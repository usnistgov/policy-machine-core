package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.statement.SetResourceAccessRightsStatement;
import gov.nist.csd.pm.policy.pml.statement.VariableAssignmentStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.policy.pml.PMLUtil.buildArrayLiteral;
import static org.junit.jupiter.api.Assertions.*;

class VarStmtVisitorTest {

    @Nested
    class ConstDeclarationTest {

        @Test
        void testSuccess() throws UnknownVariableInScopeException {
            PMLParser.ConstDeclarationContext ctx = PMLContextVisitor.toCtx(
                    """
                     const x = "a"
                     """, PMLParser.ConstDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext();
            new VarStmtVisitor(visitorCtx)
                    .visitConstDeclaration(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertTrue(visitorCtx.scope().getVariable("x").isConst());
        }

        @Test
        void testReassign() throws VariableAlreadyDefinedInScopeException {
            PMLParser.ConstDeclarationContext ctx = PMLContextVisitor.toCtx(
                    """
                     const x = "a"
                     """, PMLParser.ConstDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext();
            visitorCtx.scope().addVariable("x", Type.string(), true);
            new VarStmtVisitor(visitorCtx)
                    .visitConstDeclaration(ctx);
            assertEquals(1, visitorCtx.errorLog().getErrors().size());
            assertEquals(
                    "cannot reassign const variable",
                    visitorCtx.errorLog().getErrors().get(0).errorMessage()
            );
        }

        @Test
        void testReassignInBlock() {
            PMLParser.ConstDeclarationContext ctx = PMLContextVisitor.toCtx(
                    """
                     const (
                        x = "a"
                        x = "b"
                     )
                     """, PMLParser.ConstDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext();
            new VarStmtVisitor(visitorCtx)
                    .visitConstDeclaration(ctx);
            assertEquals(1, visitorCtx.errorLog().getErrors().size());
            assertEquals(
                    "cannot reassign const variable",
                    visitorCtx.errorLog().getErrors().get(0).errorMessage()
            );
        }

    }

    @Nested
    class VarDeclarationTest {

        @Test
        void testSuccess() throws UnknownVariableInScopeException {
            PMLParser.VarDeclarationContext ctx = PMLContextVisitor.toCtx(
                    """
                     var x = "a"
                     """, PMLParser.VarDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext();
            new VarStmtVisitor(visitorCtx)
                    .visitVarDeclaration(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
        }

        @Test
        void testReassign() throws VariableAlreadyDefinedInScopeException {
            PMLParser.VarDeclarationContext ctx = PMLContextVisitor.toCtx(
                    """
                     var x = "a"
                     """, PMLParser.VarDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext();
            visitorCtx.scope().addVariable("x", Type.string(), false);
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
                    """
                     var x = "a"
                     """, PMLParser.VarDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext();
            visitorCtx.scope().addVariable("x", Type.string(), true);
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
                    """
                     var (
                        x = "a"
                        x = "b"
                     )
                     """, PMLParser.VarDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext();
            visitorCtx.scope().addVariable("x", Type.string(), true);
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
                    """
                     x := "a"
                     """, PMLParser.ShortDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext();
            new VarStmtVisitor(visitorCtx)
                    .visitShortDeclaration(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
        }

        @Test
        void testReassign() throws VariableAlreadyDefinedInScopeException {
            PMLParser.ShortDeclarationContext ctx = PMLContextVisitor.toCtx(
                    """
                     x := "a"
                     """, PMLParser.ShortDeclarationContext.class);
            VisitorContext visitorCtx = new VisitorContext();
            visitorCtx.scope().addVariable("x", Type.string(), true);
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
                    """
                     x = "a"
                     """, PMLParser.VariableAssignmentStatementContext.class);
            VisitorContext visitorCtx = new VisitorContext();
            visitorCtx.scope().addVariable("x", Type.string(), false);
            VariableAssignmentStatement stmt =
                    (VariableAssignmentStatement) new VarStmtVisitor(visitorCtx)
                            .visitVariableAssignmentStatement(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
            assertFalse(stmt.isPlus());

            ctx = PMLContextVisitor.toCtx(
                    """
                     x += "a"
                     """, PMLParser.VariableAssignmentStatementContext.class);
            visitorCtx = new VisitorContext();
            visitorCtx.scope().addVariable("x", Type.string(), false);
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
                    """
                     x = "a"
                     """, PMLParser.VariableAssignmentStatementContext.class);
            VisitorContext visitorCtx = new VisitorContext();
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
                    """
                     x = "a"
                     """, PMLParser.VariableAssignmentStatementContext.class);
            VisitorContext visitorCtx = new VisitorContext();
            visitorCtx.scope().addVariable("x", Type.string(), true);
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