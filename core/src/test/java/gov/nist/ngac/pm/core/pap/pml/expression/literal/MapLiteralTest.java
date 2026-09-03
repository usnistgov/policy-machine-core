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

package gov.nist.ngac.pm.core.pap.pml.expression.literal;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.pml.TestPMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.ExpressionVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.scope.CompileScope;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MapLiteralTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.ExpressionContext ctx = TestPMLParser.parseExpression(
                """
                {
                    "a": ["1", "2"],
                    "b": "c"
                }
                """);

        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        Expression<Map<String, Object>> actual = ExpressionVisitor.compile(visitorContext, ctx, MapType.of(STRING_TYPE,
            ANY_TYPE));
        
        // Create a Map with Java data types
        Map<String, Object> values = new HashMap<>();
        values.put("a", List.of("1", "2"));
        values.put("b", "c");
        
        // Use the helper method to create a properly typed MapLiteralExpression
        Expression<Map<String, Object>> expected = new MapLiteralExpression<>(
            Map.of(
                new StringLiteralExpression("a"), new ArrayLiteralExpression<>(List.of(new StringLiteralExpression("1"), new StringLiteralExpression("2")), STRING_TYPE),
                new StringLiteralExpression("b"), new StringLiteralExpression("c")
            ),
            STRING_TYPE, ANY_TYPE
        );

        assertEquals(
                expected,
                actual
        );
        assertEquals(
                MapType.of(STRING_TYPE, ANY_TYPE),
                actual.getType()
        );

    }

}