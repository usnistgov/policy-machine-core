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

package gov.nist.ngac.pm.core.pap.pml.expression.reference;


import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.ngac.pm.core.pap.pml.compiler.visitor.ExpressionVisitor.compile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.Variable;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class VariableReferenceTest {

    @Nested
    class ReferenceByIDTest {
        @Test
        void testReferenceById() throws PMException {
            PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                a
                """);
            VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
            visitorContext.scope().addVariable("a", new Variable("a", STRING_TYPE, false));
            Expression<String> actual = compile(visitorContext, ctx, STRING_TYPE);
            assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
            assertEquals(
                new VariableReferenceExpression<>("a", STRING_TYPE),
                actual
            );
        }

        @Test
        void testUnknownVariable() throws PMException {
            PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                a
                """);
            VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
            PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> compile(visitorContext, ctx, STRING_TYPE)
            );
            assertEquals(1, e.getErrors().size());
            assertEquals(
                "unknown variable 'a' in scope",
                e.getErrors().get(0).errorMessage()
            );
        }
    }

    @Nested
    class ReferenceByIndexTest {
        @Test
        void testSuccess() throws PMException {
            PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                a.b.c
                """);
            VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
            MapType<String, Map<String, String>> mapType = MapType.of(STRING_TYPE, MapType.of(STRING_TYPE, STRING_TYPE));
            visitorContext.scope().addVariable("a", new Variable("a", mapType, false));
            Expression<String> actual = compile(visitorContext, ctx, STRING_TYPE);
            assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
            assertEquals(
                new DotIndexExpression<>(
                    new DotIndexExpression<>(
                        new VariableReferenceExpression<>("a", mapType),
                        "b",
                        MapType.of(STRING_TYPE, STRING_TYPE)
                    ),
                    "c",
                    STRING_TYPE
                ),
                actual
            );

            ctx = TestPMLParser.parseExpression(
                """
                a["b"]["c"]
                """);
            visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
            visitorContext.scope().addVariable("a", new Variable("a", mapType, false));
            actual = compile(visitorContext, ctx, STRING_TYPE);
            assertEquals(0, visitorContext.errorLog().getErrors().size(), visitorContext.errorLog().toString());
            assertEquals(
                new BracketIndexExpression<>(
                    new BracketIndexExpression<>(
                        new VariableReferenceExpression<>("a", mapType),
                        new StringLiteralExpression("b"),
                        MapType.of(STRING_TYPE, STRING_TYPE)
                    ),
                    new StringLiteralExpression("c"),
                    STRING_TYPE
                ),
                actual
            );
        }

        @Test
        void testUnknownVariable() throws PMException {
            PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                a.b.c
                """);
            VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
            PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> compile(visitorContext, ctx, ANY_TYPE)
            );
            assertEquals(1, e.getErrors().size());
            assertEquals(
                "unknown variable 'a' in scope",
                e.getErrors().get(0).errorMessage()
            );
        }

        @Test
        void testVarRefNotAMap() throws PMException {
            PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                a.b.c
                """);
            VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
            visitorContext.scope().addVariable("a", new Variable("a", MapType.of(STRING_TYPE, STRING_TYPE), false));
            PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> compile(visitorContext, ctx, ANY_TYPE)
            );
            assertEquals(1, e.getErrors().size(), visitorContext.errorLog().toString());
            assertEquals(
                "Type mismatch: Cannot apply indexing to type string. Expected Map.",
                e.getErrors().get(0).errorMessage()
            );
        }
    }


}