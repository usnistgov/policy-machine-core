package gov.nist.csd.pm.core.pap.pml.expression.reference;


import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.compiler.visitor.ExpressionVisitor.compile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
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