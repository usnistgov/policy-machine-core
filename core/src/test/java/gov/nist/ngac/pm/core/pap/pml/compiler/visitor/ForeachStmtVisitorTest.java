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
import static gov.nist.ngac.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.ngac.pm.core.pap.pml.PMLUtil.buildMapLiteral;
import static gov.nist.ngac.pm.core.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import gov.nist.ngac.pm.core.pap.pml.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.ForeachStatement;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ForeachStmtVisitorTest {

    private static CompileScope testScope;

    @BeforeAll
    static void setup() throws PMException {
        testScope = new CompileScope(new MemoryPAP());
    }

    @Test
    void testSuccess() {
        PMLParser.StatementContext ctx = TestPMLParser.parseStatement(
                """
                foreach x in ["a", "b"] {}
                """);
        VisitorContext visitorCtx = new VisitorContext(testScope);
        PMLStatement<?> stmt = new ForeachStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new ForeachStatement("x", null, buildArrayLiteral("a", "b"), List.of()).toString(),
                stmt.toString()
        );

        ctx = TestPMLParser.parseStatement(
                """
                foreach x, y in {"a": "b"} {}
                """);
        visitorCtx = new VisitorContext(testScope);
        stmt = new ForeachStmtVisitor(visitorCtx).visit(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new ForeachStatement("x", "y", buildMapLiteral("a", "b"), List.of()).toString(),
                stmt.toString()
        );
    }

    @Test
    void testInvalidExpressions() {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x in "a" {}
                """, visitorCtx, 1,
                "expected expression type []any, got string"

        );

        testCompilationError(
                """
                foreach x in {"a": "b"} {}
                """, visitorCtx, 1,
                "expected expression type []any, got map[string]string"

        );
    }

    @Test
    void testKeyValueOnArray() {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x, y in ["a"] {}
                """, visitorCtx, 1,
                "expected expression type map[any]any, got []string"

        );
    }

    @Test
    void testIterVarDoesNotExists() throws VariableAlreadyDefinedInScopeException {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x in arr {}
                """, visitorCtx, 1,
                "unknown variable 'arr' in scope"

        );
    }

    @Test
    void testKeyValueVarsAlreadyExist() throws VariableAlreadyDefinedInScopeException {
        VisitorContext visitorCtx = new VisitorContext(testScope.copy());
        visitorCtx.scope().addVariable("x", new Variable("x", STRING_TYPE, false));

        testCompilationError(
                """
                foreach x in ["a"] {}
                """, visitorCtx, 1,
                "variable 'x' already defined in scope"

        );

        visitorCtx = new VisitorContext(testScope.copy());
        visitorCtx.scope().addVariable("y", new Variable("y", STRING_TYPE, false));

        testCompilationError(
                """
                foreach x, y in {"a": "b"} {}
                """, visitorCtx, 1,
                "variable 'y' already defined in scope"

        );
    }

    @Test
    void testKeyOnlyOnMapReturnsError() throws VariableAlreadyDefinedInScopeException {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x in {"a": "b"} {}
                """, visitorCtx, 1,
                "expected expression type []any, got map[string]string"

        );
    }

    @Test
    void testMultipleBodyStmtErrors() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileScope(new MemoryPAP()));
        testCompilationError(
            """
            foreach x in ["a", "b"] {
                badOp1()
                badOp2()
            }
            """, visitorCtx, 2,
            "unknown operation 'badOp1' in scope",
            "unknown operation 'badOp2' in scope"
        );
    }

    @Test
    void testKeyValueOnArrayReturnsError() throws VariableAlreadyDefinedInScopeException {
        VisitorContext visitorCtx = new VisitorContext(testScope);

        testCompilationError(
                """
                foreach x, y in ["a", "b"] {}
                """, visitorCtx, 1,
                "expected expression type map[any]any, got []string"

        );
    }

}