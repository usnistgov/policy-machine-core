package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.pap.pml.TestPMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import org.antlr.v4.runtime.ParserRuleContext;

public class CompilerTestUtil {

    public static void testCompilationError(String input, VisitorContext visitorContext,
                                                int numExpectedErrors, String ... expectedError) {
        ParserRuleContext ctx = TestPMLParser.parseStatement(input);
        StatementVisitor statementVisitor = new StatementVisitor(visitorContext);

        PMLCompilationRuntimeException e = assertThrows(
                PMLCompilationRuntimeException.class,
                () -> statementVisitor.visit(ctx)
        );

        assertEquals(numExpectedErrors, e.getErrors().size());

        for (int i = 0; i < expectedError.length; i++) {
            assertEquals(expectedError[i], e.getErrors().get(i).errorMessage());
        }
    }

}
