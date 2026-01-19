package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import gov.nist.csd.pm.core.pap.pml.statement.basic.VariableAssignmentStatement;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VarStmtVisitorTest {

    @Nested
    class VarDeclarationTest {

        @Test
        void testSuccess() throws PMException {
            PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                    """
                     var x = "a"
                     """);
            VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
            new VarStmtVisitor(visitorCtx)
                    .visit(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
        }

        @Test
        void testReassign() throws PMException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
            visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, false));
            testCompilationError(
                    """
                     var x = "a"
                    """, visitorCtx, 1,
                    "variable 'x' already defined in scope"
            );
        }

        @Test
        void testReassignConstant() throws PMException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
            visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, true));

            testCompilationError(
                    """
                     var x = "a"
                    """, visitorCtx, 1,
                    "variable 'x' already defined in scope"
            );
        }

        @Test
        void testReassignInBlock() throws PMException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
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
        void testSuccess() throws PMException {
            PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                    """
                     x := "a"
                     """);
            VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
            new VarStmtVisitor(visitorCtx)
                    .visit(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
        }

        @Test
        void testReassign() throws PMException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
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
        void testSuccess() throws PMException {
            PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                    """
                     x = "a"
                     """);
            VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
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
            visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
            visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, false));
            stmt = (VariableAssignmentStatement) new VarStmtVisitor(visitorCtx)
                    .visit(ctx);
            assertEquals(0, visitorCtx.errorLog().getErrors().size());
            assertTrue(visitorCtx.scope().variableExists("x"));
            assertFalse(visitorCtx.scope().getVariable("x").isConst());
            assertTrue(stmt.isPlus());
        }

        @Test
        void testVariableDoesNotExist() throws PMException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));

            testCompilationError(
                    """
                     x = "a"
                    """, visitorCtx, 1,
                    "unknown variable 'x' in scope"
            );
        }

        @Test
        void testVariableIsConstant() throws PMException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
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