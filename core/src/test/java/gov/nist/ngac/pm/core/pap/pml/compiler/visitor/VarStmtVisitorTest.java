/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.ngac.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.VariableAssignmentStatement;
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
                    """, visitorCtx, 2,
                    "variable 'x' already defined in scope",
                    "variable 'x' already defined in scope"
            );
        }

        @Test
        void testMultipleVarSpecErrors() throws PMException {
            VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
            testCompilationError(
                """
                var (
                    x = unknownVar1
                    y = unknownVar2
                )
                """, visitorCtx, 2,
                "unknown variable 'unknownVar1' in scope",
                "unknown variable 'unknownVar2' in scope"
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