package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.TestPMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.*;
import gov.nist.csd.pm.pap.pml.statement.basic.VariableAssignmentStatement;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.*;

class VarStmtVisitorTest {

    @Nested
    class VarDeclarationTest {

        @Test
        void testSuccess() throws UnknownVariableInScopeException {
            PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                    """
                     var x = "a"
                     """);
            VisitorContext visitorCtx = new VisitorContext(new CompileScope());
            new VarStmtVisitor(visitorCtx)
                    .visit(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
        }

        @Test
        void testReassign() throws VariableAlreadyDefinedInScopeException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope());
            visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, false));
            testCompilationError(
                    """
                     var x = "a"
                    """, visitorCtx, 1,
                    "variable 'x' already defined in scope"
            );
        }

        @Test
        void testReassignConstant() throws VariableAlreadyDefinedInScopeException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope());
            visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, true));

            testCompilationError(
                    """
                     var x = "a"
                    """, visitorCtx, 1,
                    "variable 'x' already defined in scope"
            );
        }

        @Test
        void testReassignInBlock() throws VariableAlreadyDefinedInScopeException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope());
            visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, true));

            testCompilationError(
                    """
                    var (
                        x = "a"
                        x = "b"
                     )
                    """, visitorCtx, 1,
                    "variable 'x' already defined in scope"
            );
        }

    }

    @Nested
    class ShortDeclarationTest {
        @Test
        void testSuccess() throws UnknownVariableInScopeException {
            PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                    """
                     x := "a"
                     """);
            VisitorContext visitorCtx = new VisitorContext(new CompileScope());
            new VarStmtVisitor(visitorCtx)
                    .visit(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
        }

        @Test
        void testReassign() throws VariableAlreadyDefinedInScopeException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope());
            visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, true));
            testCompilationError(
                    """
                     x := "a"
                    """, visitorCtx, 1,
                    "variable x already exists"
            );
        }
    }

    @Nested
    class VariableAssignmentTest {
        @Test
        void testSuccess() throws UnknownVariableInScopeException, VariableAlreadyDefinedInScopeException {
            PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                    """
                     x = "a"
                     """);
            VisitorContext visitorCtx = new VisitorContext(new CompileScope());
            visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, false));
            VariableAssignmentStatement stmt =
                    (VariableAssignmentStatement) new VarStmtVisitor(visitorCtx)
                            .visit(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
            assertFalse(stmt.isPlus());

            ctx = TestPMLParser.parseStatement(
                    """
                     x += "a"
                     """);
            visitorCtx = new VisitorContext(new CompileScope());
            visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, false));
            stmt = (VariableAssignmentStatement) new VarStmtVisitor(visitorCtx)
                    .visit(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
            assertTrue(stmt.isPlus());
        }

        @Test
        void testVariableDoesNotExist() {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope());

            testCompilationError(
                    """
                     x = "a"
                    """, visitorCtx, 1,
                    "unknown variable 'x' in scope"
            );
        }

        @Test
        void testVariableIsConstant() throws VariableAlreadyDefinedInScopeException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope());
            visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, true));

            testCompilationError(
                    """
                     x = "a"
                    """, visitorCtx, 1,
                    "cannot reassign const variable"
            );
        }
    }
}